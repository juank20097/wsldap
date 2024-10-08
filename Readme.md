# PROYECTO WS-LDAP

## Prerrequisitos
Asegúrate de tener instalados los siguientes programas en tu sistema:
- **Docker:**
  - Versión: `25.0.3`
- **Docker Compose:**
  - Versión: `v2.24.5`

## Instalación

### 1. Configura tus credenciales
Antes de iniciar los contenedores, edita el archivo `.env` en la raíz del proyecto con tus credenciales para la conexión con LDAP. El contenido del archivo debe ser:

  SPRING_LDAP_URLS=ldap://ldap:389
  SPRING_LDAP_BASE=dc=example,dc=com
  SPRING_LDAP_USERNAME=cn=admin,dc=example,dc=com
  SPRING_LDAP_PASSWORD=admin_password

### 2. Levanta los contenedores
    Navega a la carpeta del proyecto y abre un terminal. Ejecuta el siguiente comando para levantar los contenedores en segundo plano:

    docker compose up -d

### 3. Verifica que los contenedores estén en ejecución
    Asegúrate de que los contenedores se estén ejecutando correctamente con el siguiente comando:

    docker ps

### 4. Carga los datos preconfigurados en LDAP
    Para montar los datos de ejemplo en LDAP, ejecuta el siguiente comando. Esto accederá al contenedor de LDAP y cargará el archivo users.ldif:

    docker exec -it ldap /bin/bash -c "ldapadd -x -D 'cn=admin,dc=example,dc=com' -w admin_password -f /etc/ldap/users.ldif"

## Pruebas de Servicios
    Para probar los servicios, puedes acceder a la documentación generada por Swagger en el siguiente enlace:

    http://[ip_servidor]:8080/swagger-ui/index.html

    Reemplaza [ip_servidor] con la dirección IP de tu servidor donde esté corriendo la aplicación.