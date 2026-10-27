# Plan de migración a PostgreSQL y corrección de H-M

> **Para agentes de implementación:** usar `superpowers:executing-plans` y trabajar las tareas una por una. Cada tarea termina con verificación y un commit.

**Goal:** Migrar H-M a PostgreSQL y corregir los problemas reportados, conservando el diseño y los datos de los INSERT originales; terminar con README, capturas, licencia MIT y guía de seguridad.

**Arquitectura:** H-M es la única copia de trabajo y se mantiene sobre main. PostgreSQL será el único motor de la app. `dump.sql` permanece byte por byte intacto; un esquema y seed PostgreSQL separados conservan los mismos valores y orden de los seis INSERT. Se mantienen JavaFX, la presentación, recursos y flujos existentes mientras se corrigen credenciales, contraseñas, carrito, pedidos y borrado de cuentas.

**Tech Stack:** Java 21, Maven, JavaFX 21, PostgreSQL 18, pgJDBC 42.7.14, JUnit 5 y PBKDF2WithHmacSHA256 del JDK.

**Spec:** `docs/superpowers/specs/2026-10-27-postgresql-migration-design.md` (decisión de base) y `docs/superpowers/specs/2026-10-26-hm-corrections-design.md` (correcciones y límites de producto).

## Global Constraints

- Trabajar solo en H-M; todos los commits de implementación van en main.
- Preservar layout, colores, imágenes, navegación e INSERT existentes de dump.sql. Solo actualizar menciones técnicas del motor MySQL a PostgreSQL y los avisos de conexión.
- Verificar los seis INSERT actuales contra SHA-256 A6E739D9885CBEC22754FB0E02503010C3E2E7CC55B901A071B4F67D8819DA05.
- No copiar .git, target ni mysql_login.txt desde H-M-old.
- `.env` permanece local e ignorado; la conexión PostgreSQL usa `HM_DB_HOST`, `HM_DB_PORT`, `HM_DB_NAME`, `HM_DB_USER` y `HM_DB_PASSWORD`. `ADMIN_EMAIL` es opcional y, si falta, el acceso a la pantalla BD permanece cerrado. Usar `HM_ENV_FILE` como override opcional para pruebas temporales.
- Antes del primer commit ya se quitaron del código los datos MySQL incrustados y se excluyeron `mysql_login.txt` y credenciales; preservar el historial limpio existente.
- PostgreSQL 18 es el único motor soportado; usar pgJDBC 42.7.14, la versión estable actual consultada en [la página oficial](https://jdbc.postgresql.org/download/).
- No editar `dump.sql`; generar schema, seed y migraciones nuevas en `database/postgresql/`. Los valores y el orden de sus seis INSERT deben coincidir con los originales.
- La aplicación es una demo local con datos ficticios, no un servicio público multiusuario.
- Usar PBKDF2-HMAC-SHA-256, 600.000 iteraciones, sal aleatoria y verificación en tiempo constante; el valor codificado debe caber en la columna existente.
- No permitir que la app inicie/aplique migraciones automáticamente. La configuración normal permanece en `.env`; el esquema y seed se importan con pasos documentados.
- Las pruebas de integración usan solo un clúster PostgreSQL temporal bajo `target/`, con contraseña SCRAM y escucha en loopback; no alterar servicios ni bases locales preexistentes.
- Cada tarea termina con verificación y su propio commit local sobre main; no hacer push.

## Review Focus

- PostgreSQL convierte identificadores sin comillas a minúsculas: schema, consultas y etiquetas `ResultSet` deben coincidir; smoke test de importación en Tarea 4.
- Importación de mock data debe conservar los seis INSERT y la secuencia de IDs después de insertarlos; script de equivalencia y smoke test en Tarea 4.
- PostgreSQL distingue mayúsculas en búsquedas que MySQL ejecutaba sin distinguirlas; correo y búsqueda de producto deben conservar ese comportamiento; integración en Tarea 4.
- Mismo producto en dos tallas frente a misma combinación producto/talla repetida; integración en Tarea 5.
- Error en checkout o borrado debe revertir sus transacciones, conservar el carrito/sesión y no informar éxito; pruebas en Tareas 6 y 7.

---

### Task 1: Preparar `.env` y sembrar el proyecto sin credenciales

**Archivos:**
- Crear primero en H-M: `.env` local ignorado, `.env.example` sin secretos y `.gitignore`.
- Crear en H-M: src/, user_images/, pom.xml, nbactions.xml y dump.sql.
- Crear: scripts/verify-dump-inserts.ps1.
- Excluir: .git/, target/, mysql_login.txt.
- Añadir el test de `DatabaseConfig` y la implementación mínima necesaria para cargar `.env`; hacer que `DbConnection`, `AccountController` y `TiendaController` consuman esa configuración para que ningún secreto de conexión llegue al primer commit.

- [x] Consultar origin/main con git ls-remote origin refs/heads/main. Si existe, traer y conservar esa historia; si no existe, mantener la rama local main sin commits.
- [x] Antes de copiar código, crear `.env` local con la configuración actual de MySQL y comprobar que Git lo ignora.
- [x] Copiar el proyecto desde H-M-old excluyendo metadatos Git, artefactos generados y `mysql_login.txt`. No cambiar FXML, estilos ni dump.sql.
- [x] Escribir primero `DatabaseConfigTest` para carga y validación de `.env`; ejecutar y observar los fallos esperados.
- [x] Implementar `DatabaseConfig` y reemplazar los valores de conexión incrustados antes de preparar el primer commit. Mantener los valores locales fuera de Git y añadir `.env.example`.
- [x] Crear verify-dump-inserts.ps1: filtra líneas que empiezan con INSERT INTO, las une con LF, calcula SHA-256 y falla si no coincide con el hash global.
- [x] Ejecutar las pruebas de configuración, el verificador y comparar el inventario de código/recursos.
- [x] Revisar el contenido staged: `.env` y `mysql_login.txt` ausentes, sin credenciales incrustadas en fuentes versionadas.
- [x] Ejecutar mvn -B test y confirmar compilación/pruebas.
- [x] Commit en main: chore: seed app with local environment configuration

### Task 2: Manejar fallos de configuración y conexión DB

**Archivos:**
- Crear: DbConnectionTest.java.
- Modificar: .gitignore, modelo/DbConnection.java, controladores DB, AccountController.java, TiendaController.java y LoadingController.java.
- Pruebas: DatabaseConfigTest y DbConnectionTest.

**Interfaces:**
- DatabaseConfig.load(Path) devuelve URL, usuario, contraseña y email de admin validados, o un error descriptivo; la tarea 1 introduce la interfaz.
- DbConnection.conectar() devuelve Connection activa o lanza SQLException; nunca devuelve false ni expone una conexión nula.

- [x] Escribir DbConnectionTest.unavailableServerReportsConnectionError.
- [x] Ejecutar las pruebas y confirmar que fallan con el comportamiento actual.
- [x] Cargar configuración una vez, reportar errores de archivo/controlador/SQL claramente y hacer que desconectar sea seguro si nunca se abrió una conexión.
- [x] Capturar el fallo al iniciar y mostrar alerta clara en español con reintentar/cerrar, sin dejar la pantalla de carga colgada ni imprimir el error de FXMLLoader.
- [x] Ejecutar pruebas enfocadas, mvn -B test y lanzar con MySQL apagado; no debe producir NullPointerException.
- [x] Commit en main: fix: handle database configuration and connection failures

### Task 3: Proteger contraseñas y limitar BD (completa, commit 3218907)

**Archivos:**
- Crear: modelo/PasswordService.java, modelo/AdminAccess.java, PasswordServiceTest.java y AdminAccessTest.java.
- Modificar: RegistroController.java, AccountController.java, TiendaController.java, ClienteController.java, `.env.example`, accountsettings.fxml y FXML de BD solo si hace falta.
- Pruebas: migración de contraseñas, acceso de admin y enmascaramiento.

- [x] Escribir pruebas para hash aleatorio, clave incorrecta, límite de columna, migración legacy, acceso admin y máscara de contraseña.
- [x] Ejecutar las pruebas antes de implementar y confirmar el fallo de compilación por las clases aún inexistentes.
- [x] Implementar PBKDF2WithHmacSHA256, 600.000 iteraciones, sal aleatoria, comparación en tiempo constante y formato que quepa en la columna existente.
- [x] Las cuentas nuevas y cambios de contraseña guardan hashes. Un inicio válido legacy actualiza esa misma fila sin cambiar el INSERT fuente. El admin con hash legacy debe cambiar contraseña antes de entrar.
- [x] Leer admin.email de la configuración local. Ocultar menú/enlace BD a no admins y bloquear también la navegación directa.
- [x] Mantener la columna y dimensiones de contraseña en la tabla, pero enviar un texto fijo enmascarado a la vista, nunca el hash.
- [x] Cambiar el control de nueva contraseña por uno enmascarado de apariencia equivalente; no reorganizar la pantalla.
- [x] Ejecutar integración PostgreSQL temporal para subir una contraseña legacy de cliente y marcar la contraseña legacy de administrador para cambio obligatorio; las pruebas unitarias verifican bloqueo por admin y enmascaramiento. La interacción visual queda pendiente por bloqueo del control de ventanas.
- [x] Commit en main: fix: hash passwords and restrict database screen

### Task 4: Migrar la capa de datos a PostgreSQL

**Archivos:**
- Modificar: `pom.xml`, `.env.example`, `DatabaseConfig.java`, `DatabaseConfigTest.java`, `LoadingController.java`, controladores DAO y etiquetas de resultados PostgreSQL, `about.fxml` y `.env` local ignorado.
- Crear: `database/postgresql/schema.sql`, `database/postgresql/seed.sql`, `scripts/verify-postgresql-seed.ps1`, `scripts/test-postgresql.ps1` y `PostgreSqlIntegrationTest.java`.

**Interfaces:**
- `DatabaseConfig.load(Path)` requiere las cinco variables `HM_DB_*` y construye la URL PostgreSQL; `DatabaseConfig.loadDefault()` usa `.env` o el archivo indicado por `HM_ENV_FILE`.
- El test runner inicia PostgreSQL 18 en loopback/puerto temporal y prepara su propia configuración mediante `HM_ENV_FILE`.
- `PostgreSqlIntegrationTest.seedSequencesAdvanceAfterImport`, `findsCustomerByEmailIgnoringCase` y `findsProductByNameIgnoringCase` prueban IDs/consultas reales contra el clúster temporal.

- [x] Agregar pruebas del formato `HM_DB_*`, validación de puerto, override `HM_ENV_FILE` y comparación/orden del seed frente a los seis INSERT originales; observar RED antes de implementar.
- [x] Reemplazar el driver Maven por `org.postgresql:postgresql:42.7.14` y actualizar la configuración de conexión, mensajes y la mención técnica de About sin mover controles.
- [x] Crear el esquema de las diez tablas con columnas en minúsculas, claves foráneas e identidades `GENERATED BY DEFAULT`; no cambiar `dump.sql`.
- [x] Crear el seed PostgreSQL conservando valores, filas y orden; verificar igualdad y SHA-256 aprobado.
- [x] Alinear etiquetas JDBC con PostgreSQL y mantener las consultas de correo/producto sin distinción de mayúsculas y parametrizadas.
- [x] Conservar el `.env` local completado por el usuario; comprobar que se ignora y no imprimir ni versionar sus valores.
- [x] Ejecutar el runner en clúster SCRAM temporal: importación, recuentos, columnas minúsculas, secuencias, búsquedas y `mvn -B -ntp test` (24/24); el runner detiene y elimina solo su clúster.
- [x] Verificar que `DbConnection` reporta fallo de conexión PostgreSQL cuando el servidor no responde; no se pudo observar la ventana JavaFX porque la herramienta de control bloqueó abrir una consola interactiva.
- [x] Verificar la conexión de solo lectura a `hm-db`, su motor PostgreSQL y nombre de catálogo, sin modificar datos ni consumir secuencias.
- [x] Verificar identidad/valores originales de `dump.sql`.
- [x] Commit en main: `feat: migrate database layer to PostgreSQL` (`a1383ab`)

- [x] Corregir UTF-8 y la conversión SHA-256 del verificador para Windows PowerShell clásico; comprobarlo también en PowerShell 7 y registrar el commit `197d79b`.

### Task 5: Incluir talla en la identidad del carrito

**Archivos:**
- Modificar: `controller/CarritoController.java`, `TiendaController.java` y `CompraController.java`.
- Prueba: CarritoControllerTest.java contra una DB de prueba descartable con datos mock.

**Interfaz:**
- Sustituir `ReadByCliePro(int clienteId, int productoId)` por `ReadByCliePro(int clienteId, int productoId, String talla)`; listar las líneas exactas con `ReadByCliente(int clienteId)`.
- Las pantallas de carrito y checkout recorren líneas `Carrito`; cada fila se enlaza a su `idCarrito`, talla y producto.

- [x] Agregar CarritoControllerTest.allowsSameProductInDifferentSizes y CarritoControllerTest.rejectsDuplicateProductAndSameSize.
- [x] Ejecutar la prueba y confirmar que falta la consulta que acepta talla.
- [x] Incluir talla en consulta y propagar la línea exacta al agregar, mostrar y comprar; eliminar por `idCarrito` para no borrar otra talla.
- [x] Mantener layout y mensaje actual para duplicados.
- [x] Ejecutar las 26 pruebas en PostgreSQL descartable, verificar INSERTs y probar añadir dos tallas, rechazar el duplicado y quitar solo una talla.
- [x] Commit en main: `fix: distinguish cart items by size` (`386f4fa`)

### Task 6: Guardar líneas del pedido con una transacción

**Archivos:**
- Crear: `database/postgresql/migrations/001_order_details.sql`, `modelo/PedidoDetalle.java` y `PedidoCheckoutTest.java`.
- Modificar: `controller/PedidoController.java`, `CompraController.java`, modelo `Pedido` y lectura/visualización de pedidos.
- Prueba: PedidoCheckoutTest contra PostgreSQL descartable.

**Interfaz:**
- `PedidoController.crearConDetalle(int clienteId, LocalDate fecha, List<Carrito> lineas)` devuelve el ID generado como `long`.
- `modelo.Pedido.idPedido` y su getter/setter usan `long` para alinear la columna PostgreSQL `BIGINT`.

- [x] Agregar PedidoCheckoutTest.successStoresEveryProductSizeAndQuantity y PedidoCheckoutTest.failureRollsBackOrderDetailsAndCart.
- [x] Ejecutar las pruebas y confirmar que faltan la interfaz transaccional y el guardado de detalles.
- [x] Crear `pedido_detalle` en migración PostgreSQL aparte con pedido/producto, talla, cantidad, precio unitario y subtotal de línea. No editar dump.sql ni INSERTs existentes.
- [x] Guardar cabecera, líneas y vaciado de carrito en la misma transacción; revertir ante cualquier fallo y usar el ID generado.
- [x] Mantener pantalla y layout; conservar el IVA y los importes usando el precio guardado en cada línea del carrito.
- [x] Aplicar la migración a una DB PostgreSQL temporal limpia; ejecutar 28 pruebas, el verificador de INSERT y los caminos de checkout exitoso/fallido.
- [x] Commit en main: `fix: persist order lines atomically`

### Task 7: Corregir borrado de cuenta y sesiones

**Archivos:**
- Modificar: `controller/ClienteController.java`, `controller/SesionController.java`, `AccountController.java` y lógica PostgreSQL de eliminación.
- Pruebas: AccountDeletionTest.java y SesionControllerTest.java con DB PostgreSQL descartable.

**Interfaces:**
- `ClienteController.DeleteAccount(int clienteId)` devuelve `true` solo si borra filas relacionadas y cliente dentro de una transacción confirmada.
- `SesionController.Delete()` elimina la sesión sin limitar el ID; `DeleteByClient(int clienteId)` elimina la sesión del cliente dentro del borrado de cuenta.

- [x] Agregar AccountDeletionTest.deletesRelatedMockRowsAndReportsSuccess, AccountDeletionTest.reportsFailureWithoutSigningOut y SesionControllerTest.deletesSessionAboveId500.
- [x] Ejecutar pruebas y confirmar que falla el límite de 500 y falta el resultado de borrado verificable.
- [x] Borrar filas relacionadas y cuenta en una transacción; mostrar éxito solo si confirma.
- [x] Borrar la sesión dentro de la transacción de cuenta; quitar el rango ID 1–500.
- [x] Preservar confirmación y layout actuales; un borrado fallido mantiene la sesión activa.
- [x] Ejecutar 31 pruebas en PostgreSQL temporal, verificar INSERTs y probar borrado de cuenta con pedido/carrito/favoritos y sesión >500.
- [x] Commit en main: `fix: delete accounts and sessions consistently`

### Task 8: Renombrar el proyecto H-M, alinear Maven y excluir artefactos generados

**Archivos:**
- Modificar: pom.xml y .gitignore.
- Establecer `artifactId` y `name` como `H-M` para que NetBeans muestre el nombre pedido; conservar los paquetes Java actuales.
- Mantener `target/` local e ignorado; confirmar si ya hay algún archivo generado versionado antes de quitarlo del índice sin borrarlo.

- [x] Renombrar el artefacto y el nombre visible de Maven a `H-M` sin renombrar paquetes Java.
- [x] Unificar release a Java 21; el driver PostgreSQL ya se añade en Tarea 4.
- [x] Confirmar `/target/` ignorado; no había archivos generados versionados que retirar.
- [x] Ejecutar `mvn -B -ntp test` y `mvn -B -ntp package`; ambos builds pasan y `target/` queda ignorado.
- [x] Verificar nombre del artefacto, estado Git y digest de INSERTs.
- [x] Commit en main: `chore: name project H-M and align Maven`

### Task 9: Añadir licencia, SECURITY.md, capturas y README

**Archivos:**
- Crear: LICENSE, SECURITY.md, README.md, docs/assets/hm-logo-white.png, docs/assets/readme-header.png y docs/screenshots/.
- Preservar: recursos y estilos originales de la aplicación.

- [ ] Recolorear una copia del logo transparente a blanco y ponerlo sobre un encabezado oscuro para README; revisar que se vea nítido. No reemplazar el logo de la app.
- [ ] Ejecutar con datos mock y capturar tienda, producto, carrito/checkout, cuenta y BD enmascarada. No capturar datos reales.
- [ ] Escribir README sencillo en español: explicación corta, capturas, requisitos, pasos copiables de Windows para crear PostgreSQL local, importar schema/seed/migraciones, completar `.env` desde plantilla y lanzar con `mvn javafx:run`.
- [ ] Añadir MIT con titular propuesto Fabian Rodas. SECURITY.md explica reporte privado y recuerda no versionar credenciales locales ni datos reales.
- [ ] Verificar links, comandos e imágenes; ejecutar `mvn -B -ntp test`, `mvn -B -ntp package` y los verificadores de INSERT.
- [ ] Commit en main: docs: add README screenshots and MIT license

## Verificación final

- Ejecutar `mvn -B -ntp test` y `mvn -B -ntp package`.
- Ejecutar toda integración contra un clúster PostgreSQL temporal; verificar `schema.sql`, `seed.sql`, migraciones, secuencias y recuentos.
- Lanzar con PostgreSQL detenido y confirmar el mensaje de reintentar/cerrar.
- Probar dinámicamente migración de contraseña, BD admin/no-admin, máscara, tallas, checkout normal y revertido, cierre de sesión y borrado de cuenta.
- Parsear todos los FXML; comparar el digest original y el contenido del seed; comprobar ausencia de `.env` y credenciales reales en Git y que `target/` esté ignorado.
- Revisar historia: todos los commits de implementación están en main y cada tarea tiene su commit.
- Detener y borrar únicamente el clúster PostgreSQL de prueba creado en `target/`.

