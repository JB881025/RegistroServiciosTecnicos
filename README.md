# Informe Final: Registro de Servicios Técnicos

**Aplicación móvil Android para el registro y seguimiento de servicios técnicos de campo, diseñada para empresas de ingeniería.**

---

## 1. Descripción del proyecto

Esta aplicación permite a los técnicos de campo registrar los servicios que realizan en cada visita, adjuntar fotos como evidencia, y generar un reporte en PDF que se envía automáticamente al cliente por correo electrónico. El sistema centraliza la información, mejora la trazabilidad y profesionaliza la entrega de servicios.

---

## 2. Exposición del problema

Actualmente, el registro de servicios técnicos se realiza de forma manual (papel o Excel), lo que genera:

- **Falta de evidencia visual** de los trabajos realizados.
- **Retrasos en la comunicación** con el cliente sobre los trabajos completados.
- **Reportes no estandarizados** que dificultan la facturación y el seguimiento.
- **Dificultad para demostrar** la calidad y el alcance del servicio.
- **Pérdida o extravío de información**.

Esta aplicación digitaliza y centraliza todo el proceso, resolviendo estos problemas y mejorando la eficiencia operativa.

---

## 3. Contexto del ecosistema Android

Esta aplicación fue desarrollada como parte del curso de desarrollo de Android, aplicando los conceptos del ecosistema Android. Se eligió Android como plataforma por su amplia participación en el mercado móvil, su arquitectura abierta y la disponibilidad de herramientas como Android Studio y el SDK. Durante el módulo 1, se exploraron herramientas como MIT App Inventor para prototipado rápido y GitHub para control de versiones y colaboración. La app está dirigida a técnicos de campo y administradores de empresas de ingeniería, con el objetivo de digitalizar y centralizar el registro de servicios técnicos.

---

## 4. Plataforma y Herramientas

- **Sistema Operativo:** Android (versión mínima 7.0 Nougat, API 24).
- **Lenguaje:** Kotlin.
- **IDE:** Android Studio.
- **Base de Datos:** SQLite (local).
- **Generación de PDF:** Librería iText 7.
- **Envío de correo:** JavaMail para Android.
- **Control de Versiones:** GitHub.

**Herramientas utilizadas:**
- **MIT App Inventor:** explorado para prototipado rápido.
- **Android SDK:** kit de desarrollo de software.
- **GitHub:** control de versiones y publicación del código.
- **Figma:** diseño de wireframes y prototipos.

---

## 5. Interfaz de usuario e interfaz de administrador

### Interfaz de Usuario (Técnicos)
| Pantalla | Elementos |
| :--- | :--- |
| **Login** | Correo electrónico, contraseña, botón "Ingresar". |
| **Panel Principal** | Botones: "Clientes", "Nuevo Servicio", "Historial", "Cerrar Sesión". |
| **Nuevo Servicio** | Selección de cliente, tipo de servicio, materiales, observaciones, botones para tomar/adjuntar fotos, y "Guardar y Enviar". |
| **Historial** | Lista de servicios con cliente, fecha, tipo y estado (pendiente/enviado). |
| **Detalle del Servicio** | Muestra toda la información, fotos adjuntas y opción de reenviar el PDF. |

### Interfaz de Administrador
| Pantalla | Elementos |
| :--- | :--- |
| **Panel de Control** | Resumen de servicios por técnico, por mes, ingresos estimados. |
| **Gestión de Usuarios** | Agregar/eliminar técnicos, asignar roles. |
| **Reportes** | Generar y exportar reportes globales en PDF. |

---

## 6. Funcionalidad

| # | Funcionalidad | Descripción |
| :--- | :--- | :--- |
| 1 | **Autenticación** | Inicio de sesión con credenciales fijas (Administrador / Técnico). |
| 2 | **Gestión de Clientes** | CRUD completo de clientes (nombre, email). |
| 3 | **Registro de Servicio** | Formulario con cliente, tipo de servicio, descripción y fotos. |
| 4 | **Captura de Fotos** | Integración con la cámara y galería para adjuntar evidencias. |
| 5 | **Generación de PDF** | Reporte automático con datos del cliente, servicio y fecha. |
| 6 | **Envío de Correo** | El PDF se envía al correo del cliente mediante JavaMail. |
| 7 | **Historial** | Lista de servicios completados con estado (pendiente/enviado). |
| 8 | **Almacenamiento Local** | Todos los datos se guardan en SQLite (sin conexión a internet). |

---

## 7. Diseño (Wireframes)

El diseño de la interfaz se planificó primero a mano y luego se digitalizó en Figma. El wireframe incluye las siguientes pantallas:
- Login (Administrador / Técnico)
- Panel principal
- Clientes
- Nuevo Servicio
- Historial
- Detalle del Servicio
- Crear Técnico

<img width="286" height="595" alt="Screenshot 2026-10-18 124057" src="https://github.com/user-attachments/assets/a84d9bbd-ccd5-4cef-8eae-3cdbc09b4a28" />
<img width="290" height="590" alt="Screenshot 2026-10-18 124047" src="https://github.com/user-attachments/assets/684e0268-d6c7-4f65-af46-4aa3de0fad23" />
<img width="291" height="597" alt="Screenshot 2026-10-18 124029" src="https://github.com/user-attachments/assets/86ead883-a731-489f-bd35-d06cd528e9fa" />
<img width="289" height="594" alt="Screenshot 2026-10-18 124013" src="https://github.com/user-attachments/assets/51e4a53c-4a8d-4fc7-9096-574bd548af0a" />
- 
El wireframe permitió definir la estructura, la navegación y la disposición de los elementos antes de programar, siguiendo los principios de claridad, simplicidad y coherencia.

---

## 8. Conceptos y Arquitectura Aplicada

**Actividades e Intents:** 
La aplicación está compuesta por múltiples Activities que se comunican mediante Intents explícitos. `LoginActivity` pasa el rol a `MainActivity` con `Intent.putExtra`, y `ClientesActivity` devuelve un cliente seleccionado mediante `registerForActivityResult`. Cada Activity gestiona su ciclo de vida (`onCreate`, `onResume`, etc.).

**Estilos y temas:**
La aplicación usa un tema personalizado definido en `themes.xml`:
```xml
<style name="Theme.RegistroServiciosTecnicos" parent="Theme.MaterialComponents.Light.DarkActionBar">
    <item name="colorPrimary">@color/primary</item>
    <item name="colorPrimaryVariant">@color/primaryDark</item>
    <item name="colorOnPrimary">@color/white</item>
    <item name="colorSecondary">@color/accent</item>
    <item name="colorOnSecondary">@color/white</item>
    <item name="android:windowBackground">@color/lightGray</item>
    <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
</style>

**Usuario y diseño:**

Uso de LinearLayout, MaterialCardView y ConstraintLayout.

Implementación de RecyclerView con ClienteAdapter y ServicioAdapter.

Uso de Spinner y Material Design (TextInputLayout).

Generación dinámica de PDF con iText 7.

**Imágenes y Menús:**

Uso de ImageView para el logo y fotos del servicio.

Proyección futura de menús de opciones, contextuales y popup, así como GridView para fotos y WebView para políticas de privacidad.

**Persistencia y Seguridad:**

Uso de SQLiteOpenHelper (DatabaseHelper) para la base de datos registro.db (clientes, servicios, técnicos).

Proyección futura de SharedPreferences para sesiones y ContentProvider para compartir datos.

Conciencia de seguridad: los datos locales pueden ser extraídos por técnicas forenses. Se recomienda cifrar la base de datos y no usar credenciales fijas.

**Servicios en segundo plano:**

Actualmente, el envío de correo en NuevoServicioActivity podría bloquear la interfaz si la red es lenta. Como mejora, se propone mover esta lógica a un Service o WorkManager para que se ejecute en segundo plano.

CI/CD:
El proyecto se gestiona con GitHub. Se proyecta implementar GitHub Actions para compilación y pruebas automáticas.

9. Estructura del proyecto

app/src/main/java/com/example/registroserviciostecnicos/
├── activities/
│   ├── LoginActivity.kt
│   ├── MainActivity.kt
│   ├── ClientesActivity.kt
│   ├── NuevoServicioActivity.kt
│   ├── HistorialActivity.kt
│   ├── DetalleServicioActivity.kt
│   └── CrearTecnicoActivity.kt
├── adapters/
│   ├── ClienteAdapter.kt
│   └── ServicioAdapter.kt
├── models/
│   ├── Cliente.kt
│   └── Servicio.kt
└── utils/
    ├── DatabaseHelper.kt
    └── MailSender.kt

10. Cómo ejecutar y Permisos

Clonar el repositorio.
Abrir el proyecto en Android Studio.
Sincronizar Gradle.
Ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.

Credenciales de prueba:
Administrador: proyectos@jbingenieria.com.co / 12345
Técnico: tecnico@jbingenieria.com.co / 12345

Permisos requeridos:
INTERNET (envío de correo)
CAMERA (tomar fotos del servicio)
READ_EXTERNAL_STORAGE / WRITE_EXTERNAL_STORAGE (adjuntar imágenes)

11. Registro de Cambios (Changelog)
[v1.0] - 2026-10-18 (Módulo 8 - Entrega Final)
Cambios actuales:

Consolidación del informe final en formato estructurado.
Documentación de la propuesta de implementación de servicios en segundo plano (Módulo 8).
Corrección de formato del README.
Preparación de los entregables finales (código y wiki).

[v0.4] - 2026-09-27 (Módulo 5)
Cambios pasados:

Documentación de la persistencia de datos con SQLite y SharedPreferences.
Documentación de actividades, Intents, estilos, seguridad y privacidad.

[v0.3]
Cambios pasados:

Implementación de DatabaseHelper con SQLite.
Implementación de ClienteAdapter y ServicioAdapter.
Implementación de generación de PDF con iText 7 y envío con JavaMail.

[v0.2]
Cambios pasados:

Implementación de ImageView y Material Design (TextInputLayout, MaterialCardView).

[v0.1]
Cambios pasados:

Diseño del wireframe y digitalización en Figma.
Implementación de layouts y documentación de UI.

[v0.0]
Cambios iniciales:
Creación del repositorio.
Definición del problema y plataforma (Android, Kotlin).

Cambios futuros planificados
v1.1: Implementación de SharedPreferences, menús y fragmentos.

v1.2: Pruebas unitarias, cifrado de base de datos y publicación en Google Play.

12. Autor
Julián Bedoya

13. Referencias
Android Developers. (2024). Save data using SQLite. https://developer.android.com/training/data-storage/sqlite
Android Developers. (2024). Save key-value data. https://developer.android.com/training/data-storage/shared-preferences
Android Developers. (2024). Descripción general de los servicios. https://developer.android.com/develop/background-work/services?hl=es-419
AppMaster. (2023). Elija la base de datos correcta para su aplicación móvil sin código. https://appmaster.io/es/blog/elija-la-base-de-datos-correcta-para-su-aplicacion-movil-sin-codigo-puntos-a-tener-en-cuenta
Google. (2024). Material Design 3. https://m3.material.io/
Lucidchart. (s.f.). ¿Qué es un wireframe para un sitio web? https://www.lucidchart.com/pages/es/que-es-un-wireframe-para-un-sitio-web
