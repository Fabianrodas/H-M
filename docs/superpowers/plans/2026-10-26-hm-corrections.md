# Plan de corrección y documentación de H-M

> **Para agentes de implementación:** usar superpowers:subagent-driven-development (recomendado) o superpowers:executing-plans. Ejecutar las tareas una por una. Cada tarea termina con verificación y un commit.

**Meta:** Corregir los problemas reportados de la aplicación JavaFX/MySQL de H-M, mantener el diseño y los INSERT existentes, y añadir README, capturas, licencia MIT y guía de seguridad.

**Arquitectura:** Inicializar H-M con el proyecto de H-M-old sin reemplazar sus metadatos Git. Mantener JavaFX y MySQL, centralizar la configuración local y la gestión de contraseñas, restringir la pantalla BD y añadir solo una migración auxiliar para el detalle de pedidos. Conservar dump.sql y los estilos actuales.

**Tecnologías:** Java 21, Maven, JavaFX 21, MySQL, JUnit 5 y PBKDF2WithHmacSHA256 del JDK.

**Diseño:** docs/superpowers/specs/2026-10-26-hm-corrections-design.md

## Restricciones globales

- Trabajar solo en H-M; todos los commits de implementación van en main.
- Preservar layout, colores, imágenes, textos, navegación e INSERT existentes de dump.sql.
- Verificar los seis INSERT actuales contra SHA-256 A6E739D9885CBEC22754FB0E02503010C3E2E7CC55B901A071B4F67D8819DA05.
- No copiar .git, target ni mysql_login.txt desde H-M-old.
- Antes de copiar el código, crear `.env` local con la configuración MySQL actual y añadirlo a `.gitignore`; versionar solo `.env.example` con valores ficticios.
- Antes del primer commit, eliminar del código los datos de conexión incrustados y excluir `mysql_login.txt`; revisar el contenido staged para evitar contraseñas.
- Mantener MySQL en esta fase; PostgreSQL queda expresamente fuera de alcance.
- La aplicación es una demo local con datos ficticios, no un servicio público multiusuario.
- Usar PBKDF2-HMAC-SHA-256, 600.000 iteraciones, sal aleatoria y verificación en tiempo constante; el valor codificado debe caber en la columna existente.
- No editar dump.sql; agregar cualquier esquema nuevo en otro archivo.
- Cada tarea termina con verificación y su propio commit.

## Casos de revisión

- Configuración DB ausente/malformada o MySQL apagado: mostrar error útil sin NullPointerException ni error de FXMLLoader. Probar en Tarea 2.
- Contraseña legacy hex: migrarla al iniciar sesión y obligar al admin legacy a cambiarla antes de continuar. Probar en Tarea 3.
- Usuario no admin intenta abrir BD directamente; el valor de contraseña nunca debe llegar a la tabla visible. Probar en Tarea 3.
- Mismo producto en dos tallas frente a la misma combinación producto/talla repetida. Probar en Tarea 4.
- Falla el guardado del pedido o el vaciado del carrito: transacción revierte y no pierde el carrito ni deja pedido parcial. Probar en Tarea 5.
- Cuenta con registros relacionados y sesión con ID >500: borrado honesto y sin sesión antigua. Probar en Tarea 6.

---

### Tarea 1: Preparar `.env` y sembrar el proyecto sin credenciales

**Archivos:**
- Crear primero en H-M: `.env` local ignorado, `.env.example` sin secretos y `.gitignore`.
- Crear en H-M: src/, user_images/, pom.xml, nbactions.xml y dump.sql.
- Crear: scripts/verify-dump-inserts.ps1.
- Excluir: .git/, target/, mysql_login.txt.
- Añadir el test de `DatabaseConfig` y la implementación mínima necesaria para cargar `.env`; hacer que `DbConnection`, `AccountController` y `TiendaController` consuman esa configuración para que ningún secreto de conexión llegue al primer commit.

- [ ] Consultar origin/main con git ls-remote origin refs/heads/main. Si existe, traer y conservar esa historia; si no existe, mantener la rama local main sin commits.
- [ ] Antes de copiar código, crear `.env` local con la configuración actual de MySQL y comprobar que Git lo ignora.
- [ ] Copiar el proyecto desde H-M-old excluyendo metadatos Git, artefactos generados y `mysql_login.txt`. No cambiar FXML, estilos ni dump.sql.
- [ ] Escribir primero `DatabaseConfigTest` para carga y validación de `.env`; ejecutar y observar los fallos esperados.
- [ ] Implementar `DatabaseConfig` y reemplazar los valores de conexión incrustados antes de preparar el primer commit. Mantener los valores locales fuera de Git y añadir `.env.example`.
- [ ] Crear verify-dump-inserts.ps1: filtra líneas que empiezan con INSERT INTO, las une con LF, calcula SHA-256 y falla si no coincide con el hash global.
- [ ] Ejecutar las pruebas de configuración, el verificador y comparar el inventario de código/recursos.
- [ ] Revisar el contenido staged: `.env` y `mysql_login.txt` ausentes, sin credenciales incrustadas en fuentes versionadas.
- [ ] Ejecutar mvn -B test y confirmar compilación/pruebas.
- [ ] Commit en main: chore: seed app with local environment configuration

### Tarea 2: Manejar fallos de configuración y conexión DB

**Archivos:**
- Crear: DbConnectionTest.java.
- Modificar: .gitignore, modelo/DbConnection.java, controladores DB, AccountController.java, TiendaController.java y LoadingController.java.
- Pruebas: DatabaseConfigTest y DbConnectionTest.

**Interfaces:**
- DatabaseConfig.load(Path) devuelve URL, usuario, contraseña y email de admin validados, o un error descriptivo; la tarea 1 introduce la interfaz.
- DbConnection.conectar() devuelve Connection activa o lanza SQLException; nunca devuelve false ni expone una conexión nula.

- [ ] Escribir DbConnectionTest.unavailableServerReportsConnectionError.
- [ ] Ejecutar las pruebas y confirmar que fallan con el comportamiento actual.
- [ ] Cargar configuración una vez, reportar errores de archivo/controlador/SQL claramente y hacer que desconectar sea seguro si nunca se abrió una conexión.
- [ ] Capturar el fallo al iniciar y mostrar alerta clara en español con reintentar/cerrar, sin dejar la pantalla de carga colgada ni imprimir el error de FXMLLoader.
- [ ] Ejecutar pruebas enfocadas, mvn -B test y lanzar con MySQL apagado; no debe producir NullPointerException.
- [ ] Commit en main: fix: handle database configuration and connection failures

### Tarea 3: Proteger contraseñas y limitar BD

**Archivos:**
- Crear: modelo/PasswordService.java, modelo/AdminAccess.java, PasswordServiceTest.java y AdminAccessTest.java.
- Modificar: RegistroController.java, AccountController.java, TiendaController.java, ClienteController.java, `.env.example`, accountsettings.fxml y FXML de BD solo si hace falta.
- Pruebas: migración de contraseñas, acceso de admin y enmascaramiento.

- [ ] Escribir PasswordServiceTest.hashesVerifyWithRandomSalt, PasswordServiceTest.rejectsWrongPassword, PasswordServiceTest.encodedValueFitsExistingColumn, PasswordServiceTest.verifiesLegacyHexAndFlagsUpgrade, AdminAccessTest.deniesNonAdminMenuAndDirectRoute y AdminAccessTest.neverExposesStoredPassword.
- [ ] Ejecutar las pruebas y confirmar que fallan.
- [ ] Implementar PBKDF2WithHmacSHA256, 600.000 iteraciones, sal aleatoria, comparación en tiempo constante y formato que quepa en la columna existente.
- [ ] Las cuentas nuevas y cambios de contraseña guardan hashes. Un inicio válido legacy actualiza esa misma fila sin cambiar el INSERT fuente. El admin con hash legacy debe cambiar contraseña antes de entrar.
- [ ] Leer admin.email de la configuración local. Ocultar menú/enlace BD a no admins y bloquear también la navegación directa.
- [ ] Mantener la columna y dimensiones de contraseña en la tabla, pero enviar un texto fijo enmascarado a la vista, nunca el hash.
- [ ] Cambiar el control de nueva contraseña por uno enmascarado de apariencia equivalente; no reorganizar la pantalla.
- [ ] Ejecutar pruebas, mvn -B test, digest de INSERTs y pruebas dinámicas de legacy, cambio, admin/no admin y tabla enmascarada.
- [ ] Commit en main: fix: hash passwords and restrict database screen

### Tarea 4: Incluir talla en la identidad del carrito

**Archivos:**
- Modificar: controller/CarritoController.java y cada llamada a ReadByCliePro en TiendaController.java y CompraController.java.
- Prueba: CarritoControllerTest.java contra una DB de prueba descartable con datos mock.

**Interfaz:**
- Sustituir ReadByCliePro(int clienteId, int productoId) por ReadByCliePro(int clienteId, int productoId, String talla).

- [ ] Agregar CarritoControllerTest.allowsSameProductInDifferentSizes y CarritoControllerTest.rejectsDuplicateProductAndSameSize.
- [ ] Ejecutar pruebas y confirmar que falla la consulta actual que no considera talla.
- [ ] Incluir talla en consulta y propagar selección a agregar, mostrar, borrar y consultar al comprar.
- [ ] Mantener layout y mensaje actual para duplicados.
- [ ] Ejecutar pruebas, mvn -B test, digest de INSERT y prueba dinámica de añadir/quitar dos tallas del mismo producto.
- [ ] Commit en main: fix: distinguish cart items by size

### Tarea 5: Guardar líneas del pedido con una transacción

**Archivos:**
- Crear: database/migrations/001_order_details.sql, modelo/PedidoDetalle.java y PedidoCheckoutTest.java.
- Modificar: controller/PedidoController.java, CompraController.java y lectura/visualización de pedidos.
- Prueba: PedidoCheckoutTest contra MySQL descartable.

**Interfaz:**
- PedidoController.crearConDetalle(int clienteId, LocalDate fecha, List<Carrito> lineas) devuelve el ID generado.

- [ ] Agregar PedidoCheckoutTest.successStoresEveryProductSizeAndQuantity y PedidoCheckoutTest.failureRollsBackOrderDetailsAndCart.
- [ ] Ejecutar pruebas y confirmar que faltan persistencia de detalle y rollback.
- [ ] Crear pedido_detalle en migración aparte con pedido/producto, talla, cantidad, precio unitario y subtotal de línea. No editar dump.sql ni INSERTs existentes.
- [ ] Guardar cabecera, líneas y vaciado de carrito en la misma transacción. Revertir ante cualquier fallo. Usar ID generado en lugar de consultar MAX(idPedido).
- [ ] Mantener pantalla, layout e importes/impuesto actuales.
- [ ] Aplicar migración a DB de prueba limpia; ejecutar pruebas, mvn -B test, digest y checkout normal/fallido.
- [ ] Commit en main: fix: persist order lines atomically

### Tarea 6: Corregir borrado de cuenta y sesiones

**Archivos:**
- Modificar: controller/ClienteController.java, controller/SesionController.java, AccountController.java y lógica DB de eliminación.
- Pruebas: AccountDeletionTest.java y SesionControllerTest.java con DB descartable.

- [ ] Agregar AccountDeletionTest.deletesRelatedMockRowsAndReportsSuccess, AccountDeletionTest.reportsFailureWithoutSigningOut y SesionControllerTest.deletesSessionAboveId500.
- [ ] Ejecutar pruebas y confirmar que falla el límite de 500 y el resultado no verificado.
- [ ] Borrar filas mock relacionadas y cuenta en una transacción; mostrar éxito solo si confirma.
- [ ] Borrar sesión persistida al eliminar cuenta. Quitar rango ID 1–500 y manejar cualquier valor autoincremental.
- [ ] Preservar confirmación y layout actuales.
- [ ] Ejecutar pruebas, mvn -B test, digest y prueba dinámica con cuenta que tiene pedido/carrito/favoritos y sesión >500.
- [ ] Commit en main: fix: delete accounts and sessions consistently

### Tarea 7: Alinear Maven y excluir artefactos generados

**Archivos:**
- Modificar: pom.xml y .gitignore.
- Dejar de versionar: archivos existentes en target/; conservarlos localmente e ignorados.

- [ ] Unificar release a Java 21 y cambiar la coordenada MySQL obsoleta a com.mysql:mysql-connector-j, manteniendo la versión 8.0.31 salvo que una revisión de seguridad separada justifique actualizarla.
- [ ] Ignorar /target/ y quitar de Git los 220 archivos target existentes sin borrarlos del disco.
- [ ] Ejecutar mvn -B test y mvn -B package; esperar build correcto, sin aviso de coordenada relocalizada y target ignorado.
- [ ] Verificar estado Git y digest de INSERTs.
- [ ] Commit en main: chore: align Maven and ignore build output

### Tarea 8: Añadir licencia, SECURITY.md, capturas y README

**Archivos:**
- Crear: LICENSE, SECURITY.md, README.md, docs/assets/hm-logo-white.png, docs/assets/readme-header.png y docs/screenshots/.
- Preservar: recursos y estilos originales de la aplicación.

- [ ] Recolorear una copia del logo transparente a blanco y ponerlo sobre un encabezado oscuro para README; revisar que se vea nítido. No reemplazar el logo de la app.
- [ ] Ejecutar con datos mock y capturar tienda, producto, carrito/checkout, cuenta y BD enmascarada. No capturar datos reales.
- [ ] Escribir README sencillo en español: explicación corta, capturas, requisitos, pasos copiables de Windows para importar dump.sql en DB local nueva, crear config desde plantilla y lanzar con mvn javafx:run.
- [ ] Añadir MIT con titular propuesto Fabian Rodas. SECURITY.md explica reporte privado y recuerda no versionar credenciales locales ni datos reales.
- [ ] Verificar links, comandos e imágenes; ejecutar mvn -B test y digest de INSERTs.
- [ ] Commit en main: docs: add README screenshots and MIT license

## Verificación final

- Ejecutar mvn -B test y mvn -B package.
- Ejecutar integración solo contra una DB de prueba descartable.
- Lanzar con MySQL apagado y confirmar el mensaje de reintentar/cerrar.
- Probar dinámicamente migración de contraseña, BD admin/no-admin, tallas, checkout normal y revertido, cierre de sesión y borrado de cuenta.
- Parsear todo FXML; comprobar digest de INSERTs, ausencia de credenciales versionadas y target ignorado.
- Revisar historia: todos los commits de implementación están en main y cada tarea tiene un commit.

