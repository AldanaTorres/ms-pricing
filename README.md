# Pricing Service en Java


El motivo de este proyecto académico.

Es un microservicio que permite manejar el precio y descuento de artículos.


La documentación de las api se puede consultar desde el home del microservicio
que una vez levantado el servidor se puede navegar en [localhost:3005](http://localhost:3005/)

## Dependencias

### Auth

El servicio de pricing sólo puede usarse por usuario autenticados, algunas operaciones como agregar un precio o descuento nuevo requieren que el usuario sea "admin", ver la arquitectura de microservicios de [ecommerce](https://github.com/nmarsollier/ecommerce).

### MongoDb

Ver tutorial de instalación en [ecommerce](https://github.com/nmarsollier/ecommerce) en la raíz.

### RabbitMQ

La comunicación con Catalog y Auth es a través de rabbit.

Ver tutorial de instalación en [ecommerce](https://github.com/nmarsollier/ecommerce) en la raíz.

### Java

Java JDK 1.8  [oracle.com](http://www.oracle.com/technetwork/es/java/javase/downloads/index.html)

Gradle [gradle.org](https://gradle.org/install/)

Establecer las variables de entorno sujeridas en las instalaciones.
Tanto los ejecutables de java, como gradle deben poder encontrarse en el path.

## Ejecución del servidor

Se clona el repositorio en el directorio deseado.

Nos paramos en la carpeta donde se encuentra el archivo build.gradle y ejecutamos :
```bash
gradle run
```

La primera vez que ejecute descarga las dependencias, puede tardar un momento.

## Apidoc

Apidoc es una herramienta que genera documentación de apis para proyectos node (ver [Apidoc](http://apidocjs.com/)).

El microservicio muestra la documentación como archivos estáticos si se abre en un browser la raíz del servidor [localhost:3002](http://localhost:3002/)

Ademas se genera la documentación en formato markdown.

Para que funcione correctamente hay que instalarla globalmente con

```bash
npm install apidoc -g
npm install -g apidoc-markdown2
```

La documentación necesita ser generada manualmente ejecutando la siguiente linea en la carpeta raíz :

```bash
apidoc -o www
apidoc-markdown2 -p www -o README-API.md
```

Esto nos genera una carpeta www con la documentación, esta carpeta debe estar presente desde donde se ejecute el proyecto, aunque se puede configurar desde el archivo de properties.

## Configuración del servidor

Este servidor se configura con variables de entorno

SERVER_PORT = Puerto del servidor (3005)
AUTH_SERVICE_URL = Servidor Auth (http://localhost:3000)
RABBIT_URL = Rabbit (localhost)
MONGO_URL = Url de mongo (localhost)
WWW_PATH = Path documentación (www)

Este archivo permite configurar parámetros del servidor, ver ejemplos en config-example.json.
