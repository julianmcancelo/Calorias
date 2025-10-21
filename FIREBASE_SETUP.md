# Configuración de Firebase - Paso a Paso

## 1. Crear proyecto en Firebase Console

1. Ir a https://console.firebase.google.com
2. Click en "Agregar proyecto" o "Crear un proyecto"
3. Nombre del proyecto: `CaloriasAI` (o el que prefieras)
4. Habilitar Google Analytics (recomendado)
5. Seleccionar cuenta de Analytics (usar existente o crear nueva)
6. Click "Crear proyecto" y esperar

## 2. Agregar app Android al proyecto

1. En la página de inicio del proyecto, click en el ícono de Android
2. Completar formulario:
   - **Nombre del paquete de Android**: `com.calorias.ai` (IMPORTANTE: debe coincidir exactamente)
   - **Sobrenombre de la app**: CaloriasAI
   - **Certificado de firma SHA-1** (opcional por ahora, necesario para Google Sign-In):
     - En terminal/PowerShell, ejecutar:
       ```bash
       # Windows
       keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
       
       # Copiar el SHA-1 que aparece
       ```
3. Click "Registrar app"
4. **Descargar `google-services.json`**
   - Click en "Descargar google-services.json"
   - Mover el archivo a la carpeta `app/` del proyecto (reemplazar el `.template` si existe)
5. Click "Siguiente" hasta completar

## 3. Habilitar Authentication

1. En el menú lateral de Firebase Console, ir a **Build → Authentication**
2. Click en "Comenzar"
3. En la pestaña **Sign-in method**:
   - Habilitar **Email/Password** (click, toggle ON, guardar)
   - Habilitar **Anónimo** (click, toggle ON, guardar)
   - (Opcional) Habilitar **Google**:
     - Click en Google
     - Toggle ON
     - Seleccionar email de soporte
     - Guardar

## 4. Crear Firestore Database

1. En el menú lateral, ir a **Build → Firestore Database**
2. Click "Crear base de datos"
3. Seleccionar ubicación (recomendado: `southamerica-east1` para Argentina, o la más cercana)
4. Modo de seguridad: **Modo de producción** (empezar con reglas restrictivas)
5. Click "Crear"
6. Una vez creada, ir a **Reglas** y reemplazar con:
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
         allow write: if false; // Solo Cloud Functions
       }
     }
   }
   ```
7. Click "Publicar"

## 5. Configurar Storage

1. En el menú lateral, ir a **Build → Storage**
2. Click "Comenzar"
3. Modo de seguridad: **Modo de producción**
4. Ubicación: igual que Firestore
5. Click "Listo"
6. Ir a **Reglas** y reemplazar con:
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
7. Click "Publicar"

## 6. Habilitar Analytics (ya incluido)

Analytics se habilita automáticamente al agregar la app. Verás eventos en **Analytics → Eventos** después de usar la app.

## 7. Habilitar Crashlytics

1. En el menú lateral, ir a **Build → Crashlytics**
2. Click "Habilitar Crashlytics"
3. La SDK ya está integrada en el proyecto (ver `app/build.gradle.kts`)
4. Los crashes aparecerán automáticamente al ejecutar la app

## 8. Remote Config (opcional, para feature flags)

1. En el menú lateral, ir a **Build → Remote Config**
2. Click "Crear configuración"
3. Agregar parámetros (ejemplos):
   - `free_daily_scan_limit`: 5 (number)
   - `premium_monthly_price`: 4.99 (number)
   - `enable_ai_analysis`: true (boolean)
4. Click "Publicar cambios"

## 9. Verificar instalación

1. Abrir el proyecto en Android Studio
2. Asegurarse de que `google-services.json` está en `app/`
3. Sync Gradle
4. Si hay errores, verificar que:
   - El package name en `google-services.json` coincide con `com.calorias.ai`
   - El archivo `google-services.json` no tiene errores de sintaxis
   - Los plugins de Firebase están aplicados en `app/build.gradle.kts`

## 10. Testing

1. Ejecutar la app en un dispositivo o emulador
2. Verificar en Firebase Console que:
   - **Analytics → Eventos**: aparecen eventos como `first_open`
   - **Authentication → Usuarios**: si te registrás/logueás, aparece tu usuario
   - **Firestore → Datos**: se crean documentos al guardar entradas

## Notas importantes

- **google-services.json NO se debe commitear a Git** (ya está en `.gitignore`)
- Para producción, generar un **certificado de firma release** y agregarlo a Firebase
- Si usás múltiples ambientes (dev/prod), podés tener múltiples apps en Firebase
- Para Google Sign-In, el SHA-1 del certificado de firma **es obligatorio**

## Próximos pasos opcionales

- Configurar **Cloud Functions** para validación server-side de compras
- Crear índices compuestos en Firestore si Firestore lo sugiere en logs
- Configurar **App Distribution** para testing interno
- Habilitar **Performance Monitoring**

## Soporte

Si tenés problemas:
1. Revisar logs en Android Studio (Logcat)
2. Verificar reglas de Firestore/Storage
3. Chequear que `google-services.json` existe y tiene el package correcto
4. Consultar [Documentación oficial de Firebase](https://firebase.google.com/docs/android/setup)
