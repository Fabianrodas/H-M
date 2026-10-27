# Diseño: migrar H-M a PostgreSQL

**Estado:** aprobado por el usuario
**Fecha:** 2026-10-27

## Intención

H-M debe funcionar localmente con PostgreSQL porque MySQL no está disponible en el equipo. La tienda conserva su apariencia, navegación y comportamiento visible. El cambio se limita al motor, su configuración, el esquema/importación y el acceso a datos.

## Objetivos

- Usar PostgreSQL como único motor soportado por esta versión de H-M.
- Mantener `.env` local ignorado por Git; leer `HM_DB_HOST`, `HM_DB_PORT`, `HM_DB_NAME`, `HM_DB_USER` y `HM_DB_PASSWORD` desde esa configuración.
- Conservar `dump.sql` original sin modificarlo y preservar exactamente los valores y el orden de sus seis sentencias `INSERT` en un seed compatible con PostgreSQL.
- Crear en archivos separados el esquema PostgreSQL y las futuras migraciones de la aplicación.
- Probar la importación y los controladores contra una instancia PostgreSQL descartable, sin tocar datos reales.
- Mantener intactos el diseño, los recursos, los INSERT originales y las correcciones ya implementadas.

## Fuera de alcance y antiobjetivos

- No ofrecer soporte dual MySQL/PostgreSQL.
- No reescribir ni borrar el `dump.sql` de MySQL ni cambiar sus INSERT.
- No modificar FXML, estilos, imágenes, textos, navegación ni importes mostrados, excepto el texto de error que nombra al motor.
- No migrar datos de clientes reales: los datos actuales son ficticios.
- No introducir otro motor o framework de persistencia.

## Enfoques considerados

1. **Migración completa a PostgreSQL (seleccionado por el usuario).** Sustituir el driver y la configuración; agregar esquema y seed PostgreSQL; adaptar las consultas y claves generadas; validar el mismo contenido de datos. Esta ruta resuelve la indisponibilidad de MySQL y evita mantener dos dialectos.
2. **Soporte dual MySQL y PostgreSQL.** Mantener ambos drivers y mantener dos esquemas/semillas. Se descarta por duplicar configuración y trabajo de prueba cuando el proyecto solo necesita un motor disponible.
3. **Mantener MySQL y provisionar un servidor local.** Se descarta porque el usuario informó que MySQL no está disponible; Docker tampoco tiene un motor activo. Las herramientas de PostgreSQL 18 sí están instaladas, aunque no hay servidor escuchando actualmente.

## Decisiones de diseño

- Formar la URL PostgreSQL en `DatabaseConfig` a partir de las cinco variables `HM_DB_*`; `ADMIN_EMAIL` es opcional y, si falta, la pantalla BD queda restringida.
- Mantener `.env` como configuración normal sin sobrescribir el archivo local completado por el usuario; aceptar `HM_ENV_FILE` opcional solo para apuntar pruebas a un archivo temporal sin reemplazar sus credenciales.
- Reemplazar el conector Maven MySQL por el driver JDBC oficial de PostgreSQL 42.7.14, compatible con Java 21 y disponible en la caché local. Es la versión estable más reciente consultada en la página oficial el 2026-10-27 y contiene las correcciones de seguridad publicadas en octubre de 2026.
- No iniciar ni instalar servicios PostgreSQL automáticamente como parte de H-M. La persona que ejecute el proyecto inicia/instala PostgreSQL y crea la base siguiendo README; las pruebas del proyecto sí pueden inicializar y detener un clúster aislado y descartable.
- Añadir `database/postgresql/schema.sql` con las tablas actuales, relaciones, índices y columnas de identidad. Usar identificadores SQL sin comillas para que PostgreSQL los almacene en minúsculas y las consultas JDBC no dependan de identificadores camelCase entre comillas.
- Añadir `database/postgresql/seed.sql` derivado de las seis sentencias `INSERT` de `dump.sql`. Solo se adaptará la sintaxis que PostgreSQL requiere (identificador de tabla); los valores, el orden de filas y columnas y los tipos de literales se conservarán. El volcado MySQL original permanece byte por byte intacto.
- Añadir un verificador que extraiga los INSERT de ambas fuentes, normalice únicamente el identificador de tabla y compruebe igualdad de cada sentencia y su orden; también volverá a comprobar el SHA-256 original aprobado.
- Sincronizar las secuencias de identidad después de importar IDs explícitos para que nuevos registros no choquen con los IDs sembrados.
- Alinear las lecturas `ResultSet` con las etiquetas en minúscula de PostgreSQL. Las operaciones JDBC seguirán usando `PreparedStatement`.
- Preservar la comparación de textos que MySQL hacía sin distinguir mayúsculas: búsquedas de producto usarán `ILIKE` y el acceso por correo comparará el valor en minúsculas.
- Actualizar la mención técnica del motor en la pantalla About y los avisos de conexión a PostgreSQL sin cambiar el tamaño, posición, estilos ni estructura de sus controles.
- Escribir las migraciones nuevas, incluida la tabla de detalle de pedido, con sintaxis PostgreSQL bajo `database/postgresql/migrations/`; no cambiar `dump.sql`.
- Aplicar el esquema, seed y migraciones de forma explícita con los comandos/documentación de instalación local. La aplicación no ejecutará migraciones al iniciar y no se añadirá un gestor de migraciones para esta demo.
- Mantener cada componente de aplicación en su tarea/commit previsto: carrito por talla, checkout transaccional y borrado/sesión. Los cambios al plan previo solo reemplazarán sus pasos SQL/MySQL por equivalentes PostgreSQL.
- Actualizar el plan ya aprobado en `docs/superpowers/plans/2026-10-26-hm-corrections.md`; mantener la instrucción de un commit por tarea directamente en `main`.
- Ejecutar pruebas automatizadas y una base local aislada bajo artefactos ignorados; después detener y borrar únicamente el clúster de prueba creado para la validación. No usar otra base local existente.

## Verificación y límites

- Probar configuración incompleta/malformada, URL PostgreSQL, hashes de contraseña, acceso admin y reglas del carrito.
- Inicializar un clúster PostgreSQL temporal, crear una base de prueba, importar esquema y seed, comprobar recuentos y relaciones, y correr las pruebas de operaciones transaccionales y eliminación.
- Ejecutar la app contra esa base con datos ficticios y comprobar la tienda, carrito, checkout, cuenta, pantalla BD enmascarada y el error de conexión con PostgreSQL detenido.
- Repetir la comprobación SHA-256 de los seis INSERT de `dump.sql`, validar la equivalencia de los valores del seed, revisar que `.env` no esté staged y ejecutar `mvn -B test` y `mvn -B package`.
- El hash aprobado de referencia para los seis INSERT originales es `A6E739D9885CBEC22754FB0E02503010C3E2E7CC55B901A071B4F67D8819DA05`.
- PostgreSQL 18 se encuentra instalado en el equipo, pero no aparece en `PATH` y el puerto local 5432 no está escuchando. Si el binario local no puede iniciar el clúster temporal sin privilegios adicionales, se dejará documentada esa limitación y no se tocarán servicios del sistema.

## Pendiente para el implementador

- Actualizar la documentación/planning existentes y los pasos de ejecución local para PostgreSQL.
- Preservar el diseño y verificar todos los FXML después de cambiar solo los mensajes de conexión.
- Mantener commits separados por tarea y trabajar exclusivamente en `H-M` sobre `main`.

## Decisión posterior: namespace interno de Java

El usuario pidió retirar `tiendaproyecto` del nombre interno. El paquete y módulo Java se renombran a `proyecto.hm`, ya que `H-M` no es un identificador Java válido por el guion. Maven y NetBeans siguen mostrando `H-M`. Las vistas FXML se mueven junto al paquete para mantener sus rutas relativas; se actualizan solo las referencias de paquete. No se rediseñan pantallas ni se cambian imágenes, estilos, consultas o datos.
## Hallazgo durante la verificación de recursos FXML

`account.fxml` referencia `/imagen_usuario/default_user.jpg`, pero el proyecto solo tenía el archivo bajo `user_images/`, fuera de las ubicaciones empaquetadas por Maven. Se incluirá una copia byte por byte bajo `src/main/resources/imagen_usuario/` y se conservará el archivo original sin cambios.