# Estado de implementación - CaloriasAI

Fecha: 21 de octubre, 2025

## ✅ Completado

### Infraestructura base
- [x] Proyecto Android con Gradle configurado (Kotlin 2.0.20, Compose 1.6.10, Java 17)
- [x] Hilt DI configurado y funcionando
- [x] Navigation Compose con navegación básica
- [x] Material 3 y tema base

### Escaneo y detección
- [x] CameraX integrado con preview funcional
- [x] ML Kit Barcode Scanning (códigos EAN, UPC, QR)
- [x] Permisos de cámara con Accompanist
- [x] Integración OpenFoodFacts (Retrofit + Moshi)
- [x] ViewModel para Scanner con estados (loading, success, error)
- [x] UI de Scanner con detección en tiempo real

### Health Connect
- [x] Dependencia de Health Connect agregada
- [x] HealthConnectRepository para disponibilidad y permisos
- [x] HealthViewModel con Hilt
- [x] UI en HomeScreen para solicitar permisos
- [x] Desugaring habilitado para java.time APIs

### Firebase (Backend)
- [x] Firebase BOM y dependencias (Auth, Firestore, Storage, Analytics, Crashlytics, Remote Config)
- [x] Plugins de Gradle configurados
- [x] Módulos de DI para Firebase
- [x] AuthRepository con sign-in anónimo, email/password
- [x] Sincronización básica User a Firestore
- [x] google-services.json template
- [x] Documentación completa de setup (FIREBASE_SETUP.md)
- [x] Reglas de seguridad (Firestore + Storage) documentadas

### Base de datos local (Room)
- [x] Entidades: User, Entry, Food, Goal
- [x] DAOs con Flows y operaciones CRUD
- [x] CaloriasDatabase con Room
- [x] DatabaseModule de DI
- [x] Campos `pendingSync` para sincronización

### Monetización preparada
- [x] Google Play Billing dependency agregada
- [x] WorkManager dependency agregada
- [x] Estructura de User con flags `isPremium`, `dailyScanLimit`
- [x] Schema de Firestore para subscriptions

## 🔄 En progreso

### Repositorios y sincronización
- [ ] FoodRepository completo (actualmente solo OpenFoodFacts)
- [ ] Sync bidireccional Room ↔ Firestore
- [ ] WorkManager para sync en background
- [ ] Manejo de conflictos (last-write-wins)

## 📋 Pendiente (Roadmap)

### Features core
- [ ] Guardar Entry al escanear producto (Room + Firestore)
- [ ] Pantalla Historial con lista de entradas
- [ ] Pantalla Detalle de entrada (editar/eliminar)
- [ ] Pantalla Objetivos (crear/editar metas diarias)
- [ ] Dashboard con resumen del día (kcal consumidas vs objetivo)
- [ ] Escribir/leer NutritionRecord a Health Connect

### IA con Gemini
- [ ] Pantalla "Analizar plato" con foto
- [ ] Llamada a Gemini Vision para estimar ingredientes y kcal
- [ ] UI para confirmar/ajustar resultado de IA
- [ ] OCR de etiquetas nutricionales con ML Kit Text

### Monetización
- [ ] BillingRepository con BillingClient
- [ ] Productos de suscripción en Play Console (mensual, anual)
- [ ] Pantalla Premium con features y precios
- [ ] Paywall: limitar escaneos diarios para usuarios free
- [ ] Restaurar compras
- [ ] Validación server-side (Cloud Function)
- [ ] AdMob SDK y banner en HomeScreen
- [ ] Intersticiales condicionales (usuarios free)

### Sync y persistencia
- [ ] Worker de sync automático cada X horas
- [ ] Retry logic con exponential backoff
- [ ] Indicador de sincronización en UI
- [ ] Exportación CSV/PDF

### UX y calidad
- [ ] Onboarding inicial (explicar app y permisos)
- [ ] Pantalla Settings (logout, tema, unidades, idioma)
- [ ] Manejo de errores global
- [ ] Loading states consistentes
- [ ] Empty states con ilustraciones
- [ ] Modo oscuro/claro automático

### Backend y Cloud Functions
- [ ] Proyecto de Functions (Node.js o TypeScript)
- [ ] Function para validar compras de Play Billing
- [ ] Function para actualizar `isPremium` al confirmar suscripción
- [ ] Webhook de Play Billing para renovaciones/cancelaciones
- [ ] Trigger Firestore para auditoría de cambios

### Testing
- [ ] Unit tests para ViewModels
- [ ] Tests de Room DAOs
- [ ] Tests de repositorios (mock de Firebase)
- [ ] UI tests con Compose Testing

## 📊 Métricas objetivo (Firebase Analytics)

Eventos custom a trackear:
- `scan_barcode`: cada escaneo exitoso
- `scan_failed`: código no encontrado
- `ai_analysis_started`: usuario inicia análisis de foto
- `ai_analysis_completed`: resultado de IA mostrado
- `entry_created`: nueva entrada guardada
- `upgrade_to_premium`: usuario ve pantalla premium
- `purchase_started`: inicia flujo de compra
- `purchase_completed`: suscripción exitosa
- `daily_active`: usuario abre app cada día

## 📦 Estructura de archivos actual

```
app/
├── src/main/
│   ├── java/com/calorias/ai/
│   │   ├── CaloriasApp.kt (Application con Hilt)
│   │   ├── MainActivity.kt
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── entity/ (User, Entry, Food, Goal)
│   │   │   │   ├── dao/ (UserDao, EntryDao, FoodDao, GoalDao)
│   │   │   │   └── CaloriasDatabase.kt
│   │   │   ├── remote/
│   │   │   │   ├── OpenFoodFactsService.kt
│   │   │   │   └── model/OffModels.kt
│   │   │   └── repository/
│   │   │       ├── AuthRepository.kt
│   │   │       └── FoodRepository.kt (OpenFoodFacts)
│   │   ├── di/
│   │   │   ├── DatabaseModule.kt
│   │   │   ├── FirebaseModule.kt
│   │   │   ├── HealthModule.kt
│   │   │   └── NetworkModule.kt
│   │   ├── feature/
│   │   │   └── scanner/
│   │   │       ├── BarcodeAnalyzer.kt
│   │   │       └── ScannerViewModel.kt
│   │   ├── health/
│   │   │   ├── HealthConnectRepository.kt
│   │   │   └── HealthViewModel.kt
│   │   ├── navigation/
│   │   │   └── NavGraph.kt
│   │   └── ui/
│   │       ├── HomeScreen.kt
│   │       └── ScannerScreen.kt
│   ├── res/ (strings, themes, colors, drawables)
│   └── AndroidManifest.xml
├── build.gradle.kts
└── google-services.json (REEMPLAZAR CON TU ARCHIVO DE FIREBASE)
```

## 🚀 Próximos pasos inmediatos (recomendados)

1. **Configurar Firebase Console** (ver FIREBASE_SETUP.md):
   - Crear proyecto
   - Descargar google-services.json
   - Habilitar Auth, Firestore, Storage
   - Configurar reglas de seguridad

2. **Implementar guardado de Entry**:
   - Al detectar código, crear Entry con datos de OpenFoodFacts
   - Guardar en Room
   - Subir a Firestore
   - Opcional: escribir a Health Connect

3. **Pantalla de Historial**:
   - Lista de entradas del día/semana
   - Total de kcal
   - Pull-to-refresh

4. **Dashboard con objetivo**:
   - Mostrar kcal consumidas/objetivo
   - Barra de progreso
   - Macros (proteína, carbos, grasas)

5. **Sync automático**:
   - WorkManager cada 2 horas
   - Subir entradas pendientes a Firestore
   - Descargar cambios remotos

6. **Preparar monetización**:
   - Crear productos en Play Console
   - Implementar BillingRepository
   - Pantalla Premium
   - Limitar escaneos para usuarios free

## 📝 Notas importantes

- **google-services.json**: DEBE ser descargado desde Firebase Console y colocado en `app/`. El proyecto no compilará sin él.
- **secrets.properties**: Crear con `GEMINI_API_KEY=tu_key` en la raíz.
- **Health Connect**: Requiere dispositivo físico con Health Connect instalado (Android 14+).
- **Offline-first**: Room es la fuente de verdad. Firestore es backup/sync.
- **Privacidad**: Datos de salud deben manejarse con cuidado. Políticas claras de privacidad.

## 🔗 Enlaces útiles

- [Firebase Console](https://console.firebase.google.com)
- [Play Console](https://play.google.com/console)
- [Google AI Studio (Gemini API)](https://makersuite.google.com/app/apikey)
- [Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect)
- [Play Billing](https://developer.android.com/google/play/billing)
- [AdMob](https://admob.google.com/)

---

**Versión**: 1.0.0-alpha  
**Target SDK**: 35 (Android 15)  
**Min SDK**: 24 (Android 7.0)
