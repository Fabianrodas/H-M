# Seguridad

H-M es una aplicación de demostración para ejecutarse localmente. No introduzcas datos personales, contraseñas que uses en otros servicios ni información real de clientes.

## Reportar un problema

Si encuentras una vulnerabilidad, avisa al responsable del proyecto de forma privada antes de compartirla públicamente. No publiques detalles explotables en un issue público. Si el repositorio ofrece avisos de seguridad privados, puedes usarlos.

Incluye, si es posible, la versión o el commit afectado, los pasos mínimos para reproducir el problema y el impacto observado. No adjuntes contraseñas, tokens, archivos .env ni datos personales.

## Cuidar tu copia local

- Guarda los datos de conexión solo en .env; Git ignora ese archivo.
- No compartas ni subas .env, copias de bases de datos locales ni credenciales.
- Usa únicamente datos ficticios al probar la aplicación.
- El conjunto incluido en database/postgresql/seed.sql es de demostración.