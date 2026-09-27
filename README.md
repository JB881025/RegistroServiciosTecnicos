# Registro de Servicios Técnicos

Aplicación móvil Android para el registro y seguimiento de servicios técnicos de campo, diseñada para empresas de ingeniería.

---

## Descripción del proyecto

Esta aplicación permite a los técnicos de campo registrar los servicios que realizan en cada visita, adjuntar fotos como evidencia, y generar un reporte en PDF que se envía automáticamente al cliente por correo electrónico. El sistema centraliza la información, mejora la trazabilidad y profesionaliza la entrega de servicios.

---

## Exposición del problema

Actualmente, el registro de servicios técnicos se realiza de forma manual (papel o Excel), lo que genera:

- **Falta de evidencia visual** de los trabajos realizados.
- **Retrasos en la comunicación** con el cliente sobre los trabajos completados.
- **Reportes no estandarizados** que dificultan la facturación y el seguimiento.
- **Dificultad para demostrar** la calidad y el alcance del servicio.
- **Pérdida o extravío de información**.

Esta aplicación digitaliza y centraliza todo el proceso, resolviendo estos problemas y mejorando la eficiencia operativa.

---

## Contexto del ecosistema Android

Esta aplicación fue desarrollada como parte del curso de desarrollo de Android, aplicando los conceptos del ecosistema Android. Se eligió Android como plataforma por su amplia participación en el mercado móvil, su arquitectura abierta y la disponibilidad de herramientas como Android Studio y el SDK. Durante el módulo 1, se exploraron herramientas como MIT App Inventor para prototipado rápido y GitHub para control de versiones y colaboración. La app está dirigida a técnicos de campo y administradores de empresas de ingeniería, con el objetivo de digitalizar y centralizar el registro de servicios técnicos.

---

## Plataforma

- **Sistema Operativo:** Android (versión mínima 7.0 Nougat, API 24).
- **Lenguaje:** Kotlin.
- **IDE:** Android Studio.
- **Base de Datos:** SQLite (local).
- **Generación de PDF:** Librería iText 7.
- **Envío de correo:** JavaMail para Android.
- **Control de Versiones:** GitHub.

---

## Herramientas utilizadas

- **MIT App Inventor:** explorado para prototipado rápido y comprensión de la lógica de bloques.
- **Android Studio:** IDE principal para el desarrollo de la aplicación.
- **Android SDK:** kit de desarrollo de software para compilar y ejecutar la app.
- **GitHub:** control de versiones y publicación del código.
- **Figma:** diseño de wireframes y prototipos.
- **SQLite:** base de datos local.
- **iText 7:** generación de PDF.
- **JavaMail:** envío de correo electrónico.

---

## Interfaz de usuario e interfaz de administrador

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

## Funcionalidad

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

## Actividades e Intents 

La aplicación está compuesta por múltiples Activities que se comunican mediante Intents explícitos:

- **LoginActivity:** autentica al usuario y pasa el rol (Administrador/Técnico) a `MainActivity` mediante `Intent.putExtra`.
- **MainActivity:** panel principal que redirige a `ClientesActivity`, `NuevoServicioActivity`, `HistorialActivity` y `CrearTecnicoActivity`.
- **ClientesActivity:** muestra la lista de clientes y permite seleccionar uno para pasarlo a `NuevoServicioActivity` mediante `registerForActivityResult`.
- **NuevoServicioActivity:** registra el servicio, genera el PDF y envía el correo.
- **HistorialActivity:** muestra los servicios registrados.
- **DetalleServicioActivity:** muestra el detalle de un servicio.
- **CrearTecnicoActivity:** permite registrar nuevos técnicos.

Cada Activity gestiona su ciclo de vida (`onCreate`, `onResume`, etc.) y mantiene el estado necesario para la navegación.

---

## Estilos y temas 

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

Wireframe 

El diseño de la interfaz se planificó primero a mano y luego se digitalizó en Figma. El wireframe incluye las siguientes pantallas:

Login (Administrador / Técnico)
Panel principal
Clientes
Nuevo Servicio
Historial
Detalle del Servicio
Crear Técnico

El wireframe permitió definir la estructura, la navegación y la disposición de los elementos antes de programar, siguiendo los principios de claridad, simplicidad y coherencia.

Conceptos del módulo 3 aplicados

ViewGroups y Layouts: LinearLayout, MaterialCardView, ConstraintLayout.
RecyclerView: ClienteAdapter y ServicioAdapter para listas.
Spinner (PickerView): selección de tipo de servicio.
Views básicos: TextView, Button, EditText, ImageView, FloatingActionButton.
Escucha de eventos de UI: setOnClickListener, registerForActivityResult.
Material Design: TextInputLayout, MaterialCardView, tema personalizado.
UI por programación: generación dinámica de PDF con iText 7.

Imágenes

La aplicación usa ImageView para mostrar:

El logo de JB Ingenieria S.A.S en las pantallas de login y panel principal.
Las fotos adjuntas de cada servicio, que se muestran en el PDF generado y se pueden visualizar en el detalle del servicio.

Como mejora futura, se puede implementar un GridView o un RecyclerView con GridLayoutManager para mostrar las fotos del servicio en una cuadrícula, lo que facilita la visualización de múltiples imágenes. También se puede usar ImageSwitcher para transiciones suaves entre fotos.

Menús

Actualmente, la navegación se realiza mediante botones en el panel principal. Como mejora futura, se pueden implementar los tres tipos de menús de Android:

Menú de opciones: en la Toolbar de MainActivity, con acciones como "Cerrar sesión", "Configuración" o "Ayuda".
Menú contextual: en los ítems de la lista de clientes o servicios, con opciones como "Editar", "Eliminar" o "Ver detalle".
Menú popup: en un ícono de la interfaz, para mostrar opciones rápidas.

Estos menús mejorarían la asequibilidad de la app, ya que los usuarios encontrarían las acciones en el lugar esperado.

WebView.

Como mejora futura, se puede implementar un WebView para mostrar:

La política de privacidad de la empresa.
Términos y condiciones del servicio.
Una página de ayuda en línea.

Esto requiere el permiso INTERNET, que ya está declarado en el AndroidManifest.xml.

Persistencia de datos

La aplicación utiliza varios mecanismos de almacenamiento de datos en Android:

SQLite mediante SQLiteOpenHelper: se usa la clase DatabaseHelper para crear y gestionar la base de datos registro.db, con tablas para clientes, servicios y tecnicos.
SharedPreferences: se puede implementar para guardar el estado de la sesión (rol del usuario, si ya inició sesión) y evitar que tenga que loguearse cada vez.
Almacenamiento interno y externo: los PDFs generados se guardan temporalmente en la caché interna antes de enviarse por correo. Se puede extender para exportarlos al almacenamiento externo.
Proveedor de contenido (ContentProvider): no se implementa actualmente, pero se considera como mejora futura si se desea compartir datos de clientes o servicios con otras aplicaciones.
PreferenceActivity: se puede implementar para ofrecer una pantalla de configuración donde el usuario ajuste preferencias como el correo de la empresa o el tema de la app.

Seguridad y privacidad de datos
La aplicación almacena los datos localmente en SQLite (registro.db), lo que significa que la información no se comparte con servidores externos ni con otras aplicaciones. Esto es adecuado para un entorno empresarial donde la privacidad de los datos de los clientes es importante.
Sin embargo, es importante tener en cuenta que los datos almacenados en el dispositivo pueden ser accedidos mediante técnicas forenses como:

Extracción de datos del dispositivo.
Análisis de aplicaciones y bases de datos.
Recuperación de datos eliminados.
Ingeniería inversa.
Rooting y desbloqueo del gestor de arranque.
Por lo tanto, se recomienda:
No almacenar información sensible en texto plano.
Considerar el cifrado de la base de datos en versiones futuras.
Implementar autenticación más robusta en lugar de credenciales fijas.

CI/CD y DevOps

El proyecto se gestiona con GitHub para control de versiones. Como mejora futura, se puede implementar GitHub Actions para:

Compilar la app automáticamente en cada push.
Ejecutar pruebas unitarias e instrumentadas.
Generar APKs de prueba.

Esto se alinea con los principios de Integración Continua (CI) y Entrega Continua (CD), que permiten actualizaciones más rápidas y seguras.

Estructura del proyecto
text
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

Cómo ejecutar

Clonar el repositorio.
Abrir el proyecto en Android Studio.
Sincronizar Gradle.
Ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.
Credenciales de prueba:
Administrador: proyectos@jbingenieria.com.co / 12345
Técnico: tecnico@jbingenieria.com.co / 12345

Permisos requeridos

INTERNET (envío de correo)
CAMERA (tomar fotos del servicio)
READ_EXTERNAL_STORAGE / WRITE_EXTERNAL_STORAGE (adjuntar imágenes)

Changelog

[v0.4] - 2026-09-27 (Módulo 5)
Cambios actuales:

Documentación de la persistencia de datos con SQLite y SharedPreferences.
Documentación de las actividades e Intents usados en la navegación.
Documentación de estilos y temas personalizados.
Documentación de imágenes, menús y Material Design.
Documentación de seguridad y privacidad de datos.
Publicación del código y README en GitHub Classroom.

[v0.3]
Cambios pasados:

Implementación de DatabaseHelper con SQLite para clientes, servicios y técnicos.
Implementación de ClienteAdapter y ServicioAdapter con RecyclerView.
Implementación de Spinner para selección de tipo de servicio.
Implementación de generación de PDF con iText 7.
Implementación de envío de correo con JavaMail.
Documentación de la persistencia de datos.

[v0.2]
Cambios pasados:

Implementación de ImageView para el logo y las fotos del servicio.
Implementación de Material Design con TextInputLayout y MaterialCardView.
Documentación de menús y su posible implementación futura.
Documentación de imágenes y WebView.

[v0.1]
Cambios pasados:

Diseño del wireframe a mano y digitalización en Figma.
Implementación de LinearLayout y ConstraintLayout para las pantallas.
Implementación de RecyclerView para listas de clientes y servicios.
Implementación de Spinner para selección de tipo de servicio.
Documentación de los conceptos de UI y diseño.

[v0.0]

Creación del repositorio en GitHub.
Definición del problema empresarial (registro manual de servicios técnicos).
Definición del público objetivo (técnicos de campo y administradores).
Elección de la plataforma (Android, Kotlin, Android Studio).
Exploración de MIT App Inventor y GitHub.
Documentación del ecosistema Android.

Cambios futuros planificados

v0.5: Implementación de SharedPreferences para mantener la sesión activa, implementación de menús de opciones y contextuales, y mejora de la seguridad de la base de datos.
v0.6: Implementación de fragmentos para organizar la UI, pruebas unitarias e instrumentadas, y preparación para la entrega final.
v1.0 (Entrega final): Versión completa y estable de la aplicación, con documentación completa, pruebas y APK firmado.

Autor
[Julian Bedoya]

Referencias

Android Developers. (2024). Save data using SQLite. https://developer.android.com/training/data-storage/sqlite
Android Developers. (2024). Save key-value data. https://developer.android.com/training/data-storage/shared-preferences
Android Developers. (2024). Menus. https://developer.android.com/develop/ui/views/components/menus
AppMaster. (2023). Elija la base de datos correcta para su aplicación móvil sin código. https://appmaster.io/es/blog/elija-la-base-de-datos-correcta-para-su-aplicacion-movil-sin-codigo-puntos-a-tener-en-cuenta
Google. (2024). Material Design 3. https://m3.material.io/
Lucidchart. (s.f.). ¿Qué es un wireframe para un sitio web? https://www.lucidchart.com/pages/es/que-es-un-wireframe-para-un-sitio-web
