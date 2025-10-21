# CaloriasAI (Android)

App Android profesional con Kotlin + Jetpack Compose para tracking de calorías con IA, monetización y sincronización en la nube.

## Stack
- **Frontend**: Compose, Navigation, Hilt
- **Cámara y ML**: CameraX, ML Kit (barcode + OCR)
- **Salud**: Health Connect
- **Backend**: Firebase (Auth, Firestore, Storage, Analytics, Crashlytics, Remote Config)
- **Base de datos local**: Room (offline-first con sync)
- **APIs externas**: Gemini AI, OpenFoodFacts
- **Monetización**: Google Play Billing v6, AdMob

## Requisitos
- Android Studio Jellyfish o superior
- JDK 17
- Cuenta de Firebase (gratuita)
- Cuenta de Google AI Studio (para Gemini API key)

## Setup

### 1. Configurar Firebase
1. Ir a [Firebase Console](https://console.firebase.google.com)
2. Crear un nuevo proyecto (o usar uno existente)
3. Añadir una app Android:
   - Package name: `com.calorias.ai`
   - Download `google-services.json` y colocarlo en `app/`
4. Habilitar servicios en la consola:
   - **Authentication**: Email/Password y Google Sign-In
   - **Firestore Database**: modo "producción" (luego configurar reglas)
   - **Storage**: modo "producción"
   - **Analytics**: habilitado por defecto
   - **Crashlytics**: seguir pasos de integración

### 2. Configurar API Keys
Crear `secrets.properties` en la raíz del proyecto (ignorado por Git):
```properties
GEMINI_API_KEY=tu_api_key_de_google_ai_studio
```
Para obtener la key: [Google AI Studio](https://makersuite.google.com/app/apikey)

### 3. Sincronizar proyecto
1. Abrir en Android Studio
2. Sync Gradle
3. Esperar a que descargue todas las dependencias

### 4. Reglas de Firestore
En Firebase Console → Firestore → Reglas, pegar:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{uid} {
      allow read, write: if request.auth.uid == uid;
      
      match /entries/{entryId} {
        allow read, write: if request.auth.uid == uid;
      }
      match /goals/{goalId} {
        allow read, write: if request.auth.uid == uid;
      }
      match /customFoods/{foodId} {
        allow read, write: if request.auth.uid == uid;
      }
    }
    
    match /subscriptions/{uid} {
      allow read: if request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

### 5. Reglas de Storage
En Firebase Console → Storage → Reglas:
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/{allPaths=**} {
      allow read, write: if request.auth.uid == userId;
    }
  }
}
```

## Arquitectura

- **Local-first**: Room como fuente de verdad, Firestore como backup en la nube
- **Offline**: Toda la funcionalidad core funciona sin conexión
- **Sync**: WorkManager sincroniza cambios en background
- **Auth**: Firebase Auth con soporte email/password, Google, y anónimo
- **Monetización**: Plan free (5 escaneos/día + ads) y Premium (ilimitado, sin ads)

## Estructura de datos

### Room (local)
- `users`: perfil del usuario y estado de suscripción
- `entries`: registros de comidas con kcal y macros
- `custom_foods`: alimentos creados por el usuario
- `goals`: objetivos diarios de calorías

### Firestore (nube)
```
users/{uid}
  - email, displayName, isPremium, dailyScanLimit
  /entries/{entryId}
  /goals/{goalId}
  /customFoods/{foodId}
subscriptions/{uid}
  - productId, purchaseToken, expiryTime, status
```

## Funcionalidades implementadas

- ✅ Escaneo de códigos de barras con CameraX + ML Kit
- ✅ Consulta de productos en OpenFoodFacts
- ✅ Integración con Health Connect (permisos y disponibilidad)
- ✅ Base de datos local con Room
- ✅ Firebase Auth (email/password, anónimo)
- ✅ Sincronización con Firestore
- ✅ Analytics y Crashlytics

## Roadmap

- 🔲 Análisis de fotos con Gemini Vision (estimación de kcal por foto)
- 🔲 Google Play Billing (suscripciones premium)
- 🔲 AdMob (banners y intersticiales para usuarios free)
- 🔲 Paywall y límites por plan
- 🔲 Sync automático con WorkManager
- 🔲 Pantallas: Historial, Objetivos, Settings, Premium
- 🔲 Escritura/lectura de NutritionRecord a Health Connect
- 🔲 Exportación de datos (CSV/PDF)
- 🔲 Cloud Functions para validación de compras

## Monetización

### Plan Free
- 5 escaneos por día
- Análisis IA limitado (1 foto/día)
- Ads (banner + intersticiales)
- Health Connect básico

### Plan Premium ($4.99/mes)
- Escaneos ilimitados
- Análisis IA ilimitado
- Sin publicidad
- Exportación de datos
- Estadísticas avanzadas
- Sincronización completa

## Testing
```bash
# Ejecutar en dispositivo físico (recomendado para cámara y Health Connect)
./gradlew installDebug
```

## Contribuir
Este es un proyecto profesional con potencial comercial. Para colaborar, contactar al autor.
