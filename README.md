# Single-sing-on Google utilizando Spring-boot 
<div align="center">
  
![Badge en Desarollo](https://img.shields.io/badge/status-FINISH-690)
![Maven](https://img.shields.io/badge/maven_central-v4.0.0-d84933)
![Spring_boot](https://img.shields.io/badge/spring_boot-v3.2.5-6ab23c)
![GitHub Org's stars](https://img.shields.io/github/stars/mariocodev?style=social)

</div>

## Content

* [Description](#description)
* [Features](#features)
* [Installation](#installation)
  * [Despliegue local](#despliegue-local)
  * [Despliegue como servicio](#despliegue-como-servicio)
* [FAQ](#faq)
   
## Description

El proyecto `Spring-SSO-Google` es una aplicación de demostración que implementa la autenticación de un single-sign-on (SSO) con Google utilizando Spring Boot. Permite a los usuarios autenticarse utilizando sus cuentas de Google a través de `OAuth 2.0`. La configuración de seguridad está definida en la clase `SecurityConfig`, que configura el inicio de sesión de OAuth2 para autenticar solicitudes.

La aplicación está configurada para ser desplegada en diferentes ambientes mediante el uso de perfiles de Maven y propiedades de Spring Boot. Esto permite una fácil gestión de la configuración específica del entorno, como `id` y `secret` de cliente de `OAuth2`, que se deben proporcionar en el archivo `application.yml` correspondiente al entorno.

El punto de entrada principal de la aplicación es la clase `SpringSsoGoogleApplication`, que define dos endpoints: uno en la raíz que da la bienvenida al usuario autenticado mostrando su nombre, apellido y correo electrónico obtenidos de los atributos del token de autenticación de Google, y otro en /user que devuelve el objeto Principal del usuario autenticado.

## Features

El siguiente proyecto ha sido levantado en un entorno local con `Apache Maven 3.9.6`.

```
Apache Maven 3.9.6
Java version: 17, vendor: Oracle Corporation
Default locale: es_PY, platform encoding: Cp1252
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"

```

### Herramientas de desarrollo

* IDE: IntelliJ IDEA [v: 2024.1.1](https://www.jetbrains.com/idea/download/)
* Framework: [Spring Boot 3.2.5](https://start.spring.io/)
* Gestor y constructor de proyectos: [Maven](https://maven.apache.org/)

## Installation

### Despliegue local
***

#### Maven Profile & Spring Boot Properties

A continuación se muestra como desplegar la aplicación en un entorno definido a través de perfiles de configuración. Se definen 4 perfiles que hacen referencia al ambiente local `local`, desarrollo `dev`, pre-producción `uat`  y producción `prod` pudiendo existir otros más dependiendo de la necesidad.

Copiar el archivo denominado `application-example.yml` que se encuentra en el directorio `src/main/resources` y renombrarlo como el environment seleccionado a levantar, ejemplo `application-local.yml`. Modificar los valores de las propiedades por los del ambiente local.

Verificar que en el mismo directorio se encuentre el archivo `application.yml` cuyo contenido debe ser el siguiente:

```yml

spring:
    profiles:
        active: @activatedProperties@

```

##### Configuración de proveedor OAuth2 Google

 Definir los clientes OAuth2 en el archivo de configuración de Spring Boot:
 
* [OAuth 2.0]( https://cloud.google.com/docs/authentication?hl=es-419#oauth2)
* [Cómo usar OAuth 2.0 para acceder a las API de Google](https://developers.google.com/identity/protocols/oauth2)
* [Guide to using OAuth 2.0 to access Google APIs](https://medium.com/@tony.infisical/guide-to-using-oauth-2-0-to-access-google-apis-dead94d6866d) 

```yml
 spring:
    security:
        oauth2:
            client:
                registration:
                    google:
                        clientId: TU_CLIENT_ID
                        clientSecret: TU_CLIENT_SECRET
                        scope:
                            - email
                            - profile
                            - openid
```

#### Maven compile & run

El archivo `pom.xml` está preparado para desplegar la aplicación dependiendo de la configuración seleccionada a través del parámetro `-P <profile-id>`.

```cmd

# mvn clean package -P <profile-id>
# mvn spring-boot:run -P <profile-id>
# Example:

mvn spring-boot:run -P local

```

En el log debe mostrar un mensaje de nivel `INFO` donde se indica el perfil seleccionado para levantar el entorno, similar a:

```ini

2024-05-01 15:16:54.305  INFO 1476 --- [main] p.c.s.g.auth.SpringSsoGoogleApplication  : The following 1 profile is active: "local"


```


> **Nota:** Los archivos derivados de `application-example.yml` (`application-local.yml`, `application-dev.yml`, `application-uat.yml`, `application-prod.yml`, otros) no deben estar versionados y no serán reconocidos para ser versionados (`.gitignore`).


### Despliegue como servicio
***

A continuación los pasos para desplegar la [aplicación como servicio](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html#deployment.installing) según recomenienda la documentación de Spring en un entorno seleccionado, para éste ejemplo se utilizará el entorno `dev` junto con `java-11`

#### Instalar OpenJDK11

Verificar versión de Java

```cmd
$ java -version
```

Instalar la siguiente versión

```cmd
$ sudo yum install java-11-openjdk-devel
$ sudo yum install java-11-openjdk

```

#### Maven Profile & Spring Boot Properties & compile & run

Copiar el archivo denominado `application-example.yml` que se encuentra en el directorio `src/main/resources` y renombrarlo como el environment `dev` de la siguiente manera `application-dev.yml`. Modificar los valores de las propiedades por los del ambiente de desarrollo.

##### Generar .jar

Para crear un `jar` 'totalmente ejecutable' con Maven, use la siguiente configuración de complemento entre el tag `build` > `plugins` del `pom.xml`:

```ini
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <executable>true</executable>
    </configuration>
</plugin>
```

En ese mismo apartado de `build` se puede modificar el nombre por defecto del paquete generado agregando:

```ini
<finalName>[nombre-application]</finalName>
```

Generar el ejecutable en `target/`

```cmd
$ mvn clean package -P dev
```

Copiar el archivo `.jar` generado `[nombre-application].jar`. Se puede levantar la aplicación ejecutando el comando `java -jar [nombre-application].jar` pero se recomienda levantar como servicio del sistema operativo.

#### Levantar el ejecutable como servicio

Se asume que la aplicación fue copiada en el directorio `[/dir/proyecto]`. Para instalar una aplicación Spring Boot como un servicio, se debe crear un enlace simbólico de la siguiente manera:

```cmd

# modificar [/dir/proyecto]/ por el directorio donde se encuentra el archivo

$ sudo ln -s [/dir/proyecto]/[nombre-application].jar /etc/init.d/[nombre-vinculo]

```

Se visualiza un archivo de log desde el siguiente enlace `var/log/[nombre-vinculo].log`

Una vez instalado, se puede iniciar y detener el servicio de la forma habitual. Por ejemplo, en un sistema basado en Debian, podría iniciarlo con el siguiente comando:

```cmd

$ service [nombre-vinculo] start

# {start|stop|force-stop|restart|force-reload|status|run}

```

## FAQ

* [Downloading Apache Maven](https://maven.apache.org/download.cgi)
* [Instalar OpenJDK11 para CentOS 7.9](https://phoenixnap.com/kb/install-java-on-centos)
* [Maven Profile & Spring Boot Properties](https://medium.com/@derrya/maven-profile-spring-boot-properties-a34f2b2bb386)
* [Manual de instalación Spring | 2.1. Supported Operating Systems](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html#deployment.installing)
* [Spring boot change jar name](https://javadeveloperzone.com/spring-boot/spring-boot-change-jar-name/)
* [OAuth 2.0]( https://cloud.google.com/docs/authentication?hl=es-419#oauth2)
* [Cómo usar OAuth 2.0 para acceder a las API de Google](https://developers.google.com/identity/protocols/oauth2)
* [Guide to using OAuth 2.0 to access Google APIs](https://medium.com/@tony.infisical/guide-to-using-oauth-2-0-to-access-google-apis-dead94d6866d) 
