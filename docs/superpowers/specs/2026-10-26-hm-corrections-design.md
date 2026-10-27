# H-M fixes and documentation — design

> La decisión inicial de mantener MySQL quedó reemplazada por el diseño aprobado de PostgreSQL en `docs/superpowers/specs/2026-10-27-postgresql-migration-design.md`. Las demás restricciones de producto y seguridad de este documento siguen vigentes.

**Estado:** revisado según la aprobación y restricciones del usuario. No se ha cambiado código de la aplicación.

## Propósito

Llevar la aplicación JavaFX de H-M-old al repositorio vacío H-M, corregir los problemas reportados y documentar su uso local. Mantener el diseño actual de la aplicación y los inserts mock actuales de la base de datos.

## Objetivos

- Mostrar un error claro cuando falte la configuración o MySQL no esté disponible.
- Crear primero un `.env` local ignorado por Git para MySQL, sacar las credenciales del código y de Git, usar hashes seguros para contraseñas, limitar la pantalla BD al administrador local y enmascarar los valores de contraseña.
- Permitir el mismo producto en distintas tallas, guardar el detalle del pedido con atomicidad, informar correctamente si una cuenta no se pudo borrar y eliminar el límite de ID de sesiones.
- Alinear Java/Maven, dejar de versionar artefactos generados y agregar pruebas.
- Terminar con README en español sencillo, capturas de la aplicación, licencia MIT y SECURITY.md.

## Fuera de alcance y límites

- Sin rediseño, cambios de layout/colores, nuevas funciones de tienda ni cambio de JavaFX/MySQL. PostgreSQL queda fuera de esta fase.
- Sin editar dump.sql ni sus INSERT o valores existentes.
- Sin migración de clientes reales; el usuario confirmó que los datos actuales son ficticios.
- El control de admin es protección de interfaz para esta demo de escritorio local, no autorización de servidor para una app pública multiusuario.

## Enfoque elegido

Conservar MySQL y restringir la pantalla BD según `ADMIN_EMAIL` configurado localmente. Antes de copiar o versionar el proyecto, crear `.env` con la configuración MySQL local y añadirlo a `.gitignore`; versionar solo `.env.example`. Mantener dump.sql y todos sus inserts sin cambios. Agregar el detalle del pedido en una migración SQL aparte. Para las contraseñas legacy en hexadecimal, verificar una vez y actualizar el valor en la misma columna; obligar al administrador con contraseña legacy a cambiarla antes de continuar. Para nuevas contraseñas usar PBKDF2-HMAC-SHA-256 con 600.000 iteraciones, sal aleatoria por contraseña y verificación en tiempo constante, según la recomendación actual de OWASP: https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html.

La alternativa era quitar BD, lo que reduce trabajo pero pierde una función existente. El usuario eligió conservarla con acceso de administrador.

## Restricciones y aceptación

- Todos los commits de implementación van en main dentro de H-M. Confirmado: `origin/main` aún no existe.
- El primer paso operativo es crear `.env` local con los datos MySQL actuales y actualizar `.gitignore`; ambos ocurren antes de copiar/sembrar el código y del primer commit. El `.env` nunca se versiona.
- Antes del primer commit, retirar del código todo usuario/contraseña de conexión incrustado, excluir `mysql_login.txt`, revisar el contenido staged por secretos y comprobar que solo queda `.env.example` con valores ficticios.
- Copiar código, recursos, imágenes, user_images, pom.xml, nbactions.xml y dump.sql desde H-M-old. Excluir .git, target y mysql_login.txt.
- Los seis INSERT actuales de dump.sql deben conservar el SHA-256 canónico A6E739D9885CBEC22754FB0E02503010C3E2E7CC55B901A071B4F67D8819DA05.
- Mantener la presentación y textos existentes. Ocultar BD a no administradores y enmascarar contraseñas en las mismas celdas.
- No guardar credenciales reales en Git. Las capturas solo deben usar datos mock.
- Usar MIT; el titular propuesto es Fabian Rodas, basado en el dueño del repositorio.
- No implementar código hasta que el usuario apruebe el plan detallado.

