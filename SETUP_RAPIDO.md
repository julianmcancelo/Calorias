# 🚀 Setup Rápido - CaloriasAI

## ✅ Archivos de configuración ya creados

He generado los archivos necesarios para que el proyecto compile localmente:

- ✅ `app/google-services.json` (versión de desarrollo temporal)
- ✅ `secrets.properties` (con key placeholder para Gemini)
- ✅ `local.properties` (configuración de SDK)
- ✅ `gradle/wrapper/` (Gradle 8.9)

## 📱 Abrir el proyecto ahora mismo

### 1. Abrir en Android Studio
1. **Android Studio** → **File** → **Open**
2. Seleccionar la carpeta: `C:\Users\Julian Cancelo\CascadeProjects\windsurf-project`
3. Click **OK**
4. Esperar a que Gradle sincronice (puede tardar 3-5 min la primera vez)

### 2. Si Gradle falla con SDK
Si Android Studio no encuentra el SDK automáticamente:
1. Editar `local.properties`
2. Agregar (reemplazar con tu ruta real):
   ```
   sdk.dir=C\:\\Users\\Julian Cancelo\\AppData\\Local\\Android\\Sdk
   ```
3. Sync Gradle de nuevo

### 3. Sincronizar proyecto
- **File** → **Sync Project with Gradle Files**
- O click en el ícono del elefante de Gradle en la toolbar

## 🔑 Obtener API Keys reales (opcional por ahora)

El proyecto ya compila con keys de desarrollo, pero para usar todas las features necesitás:

### Google Gemini API (para análisis de IA)
1. Ir a https://makersuite.google.com/app/apikey
2. Crear API key
3. Editar `secrets.properties`:
   ```properties
   GEMINI_API_KEY=TU_KEY_REAL_AQUI
   ```

### Firebase (para auth y base de datos en la nube)
Por ahora el proyecto usa un archivo temporal. Para configurar Firebase real:
1. Seguir **FIREBASE_SETUP.md** completo
2. Descargar `google-services.json` desde Firebase Console
3. Reemplazar el archivo en `app/google-services.json`

## 🏃 Ejecutar la app

### En emulador
1. **Tools** → **Device Manager**
2. Crear un emulador (Pixel 6, API 34 recomendado)
3. Click en **Run** (triángulo verde) o `Shift+F10`

### En dispositivo físico (RECOMENDADO para cámara)
1. Habilitar **Opciones de desarrollador** en tu Android
2. Habilitar **Depuración USB**
3. Conectar con cable USB
4. Click en **Run**

## ✨ Lo que ya funciona

Podés probar inmediatamente:
- ✅ **Navegación**: Home → Scanner → Volver
- ✅ **Escaneo de códigos**: Apuntá a un código de barras EAN/UPC
- ✅ **Consulta OpenFoodFacts**: Ver info nutricional del producto
- ✅ **Permisos de cámara**: Se solicitan automáticamente
- ✅ **Health Connect**: Detecta disponibilidad y permisos

## ⚠️ Limitaciones actuales (versión de desarrollo)

Como estamos usando keys temporales:
- 🔶 Firebase Auth NO funcionará (usar sign-in anónimo da error)
- 🔶 Firestore sync NO funcionará
- 🔶 Gemini AI NO funcionará con la key placeholder
- ✅ Escaneo de códigos SÍ funciona (usa OpenFoodFacts, no requiere auth)
- ✅ Health Connect SÍ funciona (local)

## 🔧 Solución de problemas

### Error: "SDK location not found"
- Editar `local.properties` con la ruta correcta de tu Android SDK

### Error: "google-services.json missing"
- Ya está creado, verificar que exista en `app/google-services.json`

### Error al compilar Kotlin
- Verificar que tenés JDK 17 instalado
- **File** → **Project Structure** → **SDK Location** → verificar JDK

### Gradle muy lento
- Primera sincronización tarda. Es normal.
- Descargar todas las dependencias puede tomar 5-10 minutos

### Cámara no funciona en emulador
- Normal. Usar dispositivo físico para escaneo de códigos.

## 📱 Testing rápido

1. **Ejecutar app**
2. En **HomeScreen**: ver estado de Health Connect
3. Click **"Abrir escáner"**
4. Aceptar permiso de cámara
5. Apuntar a un código de barras de un producto
6. Ver resultado con nombre y kcal (si está en OpenFoodFacts)

## 🎯 Próximos pasos

Una vez que verifiques que compila y ejecuta:

1. **Configurar Firebase real** (ver FIREBASE_SETUP.md)
2. **Obtener Gemini API key** (para análisis de IA)
3. **Implementar guardado de entradas** (Room + Firestore)
4. **Crear pantalla de Historial**

## 📚 Documentación adicional

- **FIREBASE_SETUP.md**: Configuración completa de Firebase paso a paso
- **README.md**: Descripción general del proyecto
- **IMPLEMENTATION_STATUS.md**: Estado y roadmap completo

---

**¿Dudas?** Revisá los archivos de documentación o consultame.

**Estado**: ✅ Proyecto listo para abrir en Android Studio y ejecutar
