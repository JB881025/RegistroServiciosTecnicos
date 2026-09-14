# Registro de Servicios Técnicos

Aplicación móvil Android para el registro y seguimiento de servicios técnicos de campo, diseñada para empresas de ingeniería.

## Descripción del proyecto

Esta aplicación permite a los técnicos de campo registrar los servicios que realizan en cada visita, adjuntar fotos como evidencia, y generar un reporte en PDF que se envía automáticamente al cliente por correo electrónico. El sistema centraliza la información, mejora la trazabilidad y profesionaliza la entrega de servicios.

## Exposición del problema

Actualmente, el registro de servicios técnicos se realiza de forma manual (papel o Excel), lo que genera:

- Falta de evidencia visual de los trabajos realizados.
- Retrasos en la comunicación con el cliente sobre los trabajos completados.
- Reportes no estandarizados que dificultan la facturación y el seguimiento.
- Dificultad para demostrar la calidad y el alcance del servicio.
- Pérdida o extravío de información.

Esta aplicación digitaliza y centraliza todo el proceso, resolviendo estos problemas y mejorando la eficiencia operativa.

## Plataforma

- **Sistema Operativo:** Android (versión mínima 7.0 Nougat, API 24).
- **Lenguaje:** Kotlin.
- **IDE:** Android Studio.
- **Base de Datos:** SQLite (local).
- **Generación de PDF:** Librería iText 7.
- **Envío de correo:** JavaMail para Android.
- **Control de Versiones:** GitHub.

## Wireframe

El diseño inicial se realizó a mano y luego se digitalizó. Incluye las pantallas:

- Login (Administrador / Técnico)
- Panel principal
- Clientes
- Nuevo Servicio
- Historial
- Detalle del Servicio
- Crear Técnico

<img width="968" height="1300" alt="image" src="https://github.com/user-attachments/assets/c2a39e3a-f834-49d4-9218-d6c9afc0884a" />

## Interfaz de usuario e interfaz de administrador

### Interfaz de Usuario (Técnicos)

| Pantalla | Elementos |
|---|---|
| Login | Correo electrónico, contraseña, botón "Ingresar". |
| Panel Principal | Botones: "Clientes", "Nuevo Servicio", "Historial", "Cerrar Sesión". |
| Nuevo Servicio | Selección de cliente, tipo de servicio, materiales, observaciones, botones para tomar/adjuntar fotos, y "Guardar y Enviar". |
| Historial | Lista de servicios con cliente, fecha, tipo y estado (pendiente/enviado). |
| Detalle del Servicio | Muestra toda la información, fotos adjuntas y opción de reenviar el PDF. |

### Interfaz de Administrador

| Pantalla | Elementos |
|---|---|
| Panel de Control | Resumen de servicios por técnico, por mes, ingresos estimados. |
| Gestión de Usuarios | Agregar/eliminar técnicos, asignar roles. |
| Reportes | Generar y exportar reportes globales en PDF. |

## Funcionalidad

| # | Funcionalidad | Descripción |
|---|---|---|
| 1 | Autenticación | Inicio de sesión con credenciales fijas (Administrador / Técnico). |
| 2 | Gestión de Clientes | CRUD completo de clientes (nombre, email). |
| 3 | Registro de Servicio | Formulario con cliente, tipo de servicio, descripción y fotos. |
| 4 | Captura de Fotos | Integración con la cámara y galería para adjuntar evidencias. |
| 5 | Generación de PDF | Reporte automático con datos del cliente, servicio y fecha. |
| 6 | Envío de Correo | El PDF se envía al correo del cliente mediante JavaMail. |
| 7 | Historial | Lista de servicios completados con estado (pendiente/enviado). |
| 8 | Almacenamiento Local | Todos los datos se guardan en SQLite (sin conexión a internet). |


Este proyecto aplica los conceptos como:

- **ViewGroups y Layouts:** `LinearLayout`, `MaterialCardView` y `ConstraintLayout` para organizar las pantallas.
- **RecyclerView:** `ClienteAdapter` y `ServicioAdapter` para mostrar listas de clientes y servicios.
- **Spinner (PickerView):** selección del tipo de servicio (Servicio Técnico, Mantenimiento, Obra en Ejecución, Otro).
- **Views básicos:** `TextView`, `Button`, `EditText`, `ImageView`, `FloatingActionButton`.
- **Escucha de eventos de UI:** `setOnClickListener`, `registerForActivityResult` para cámara y galería.
- **Material Design:** `TextInputLayout`, `MaterialCardView`, tema personalizado con colores corporativos.
- **UI por programación:** generación dinámica de PDF con iText 7.
- **Adaptación de roles:** visibilidad condicional de botones según el perfil (Administrador / Técnico).

## Estructura del proyecto

## Cómo ejecutar

1. Clonar el repositorio.
2. Abrir el proyecto en Android Studio.
3. Sincronizar Gradle.
4. Ejecutar en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.
5. Credenciales de prueba:
   - **Administrador:** `proyectos@jbingenieria.com.co` / `12345`
   - **Técnico:** `tecnico@jbingenieria.com.co` / `12345`

## Permisos requeridos

- `INTERNET` (envío de correo)
- `CAMERA` (tomar fotos del servicio)
- `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` (adjuntar imágenes)

## Autor

[Julián Bedoya]

## Referencias

- Android Developers. (2024). *RecyclerView*. https://developer.android.com/guide/topics/ui/layout/recyclerview
- Google. (2024). *Material Design 3*. https://m3.material.io/
- iText Software. (2024). *iText 7 Core*. https://itextpdf.com/
