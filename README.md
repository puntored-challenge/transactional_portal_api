# Puntored challenge

## Challenge
Para ampliar nuestro portafolio, buscamos desarrollar un portal transaccional integral
que ofrezca recargas a operadores móviles, pagos de servicios, compra de pines de
contenido y transferencias bancarias. Tu desafío inicial será crear el primer módulo de
recargas móviles, consumiendo los servicios API de Puntored. Para lograr esto, se
requiere la construcción de una API en Spring Boot y un proyecto web en React, que
consuman los servicios de Puntored descritos en este documento.

### Instalación y ejecución proyecto Spring boot.

#### Requisitos

Java 17 JDK
Maven 3.xz
PostgreSQL

##### Opcional

Docker

### Ejecución

Clonar el repository 
```
  git clone https://github.com/puntored-challenge/transactional_portal_api.git
  cd transactional_portal_api
```

Renombrar el archivo .env.example a .env y modificar las variables
```
SERVER_PORT=18081

PUNTORED_API_URL=https://example.com/api
PUNTORED_API_USER=testuser
PUNTORED_API_PASSWORD=TestPassword123
PUNTORED_API_KEY=DummyApiKey1234567890

JWT_CONFIGURATION_SECRET=tr4ns4cti0n4lP0rt4l4p1punt0r3dch4ll3nge
JWT_CONFIGURATION_EXPIRATION=600000

SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=1234

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
```

Ejecución
```
  mvn clean install
```

#### Ejecucion con docker 
```
  docker-compose build
  docker-compose up -d 
```

Esta installación contiene un imagen de posgreSQL por lo cual no seria necesario contar con otra instacia.


Una vez levantada la imagen si ejecutamos este endpoint se puede validar el estado del servicio
```
  /api/actuator/health
```

### Dependencias del projecto y uso

Dependencias principales de Spring

```
spring-boot-starter-actuator # métricas y monitoreo

spring-boot-starter-data-jpa # acceso a base de datos con JPA/Hibernate

spring-boot-starter-security # autenticación y autorización

spring-boot-starter-web # controlador REST y servidor embebido

spring-cloud-starter-openfeign – cliente HTTP

spring-boot-devtools – recarga automática en desarrollo

spring-dotenv – soporte para archivos .env

spring-boot-starter-test – pruebas unitarias e integración

spring-security-test – pruebas de seguridad

```

Dependencias de terceros

```
hibernate-validator – implementación de Bean Validation

jakarta.validation-api – API estándar de validación

postgresql – driver JDBC para PostgreSQL

mockito-core – mocks para pruebas

mockito-junit-jupiter – integración con JUnit 5

lombok – generación automática de getters/setters

springdoc-openapi-starter-webmvc-ui – Swagger UI para documentación OpenAPI

jjwt-api – API principal para JWT

jjwt-impl – implementación de JWT

jjwt-jackson – soporte para serialización JSON con Jackson
```

### Solucion del challenge

Para este desarrollo se uso la arquitectura en capas con el objetivo de separar las responsabilidades para facilitar el desarrollo, mantenimiento y escalabilidad, con capas comunes como presentación, lógica de negocio y acceso a datos.  

Las capas se y su relacion se realizaron de la siguiente forma

```
  Presentacion: 
      controller 
      dtos

  Logica de negocio
      service
      model

  Datos:
      Entities
      Repository
```

Donde cada capa respeta su comunicación 

```
  datos --> negocio --> presentacion
```

transformarcion datos entre diferentes capas o modelos de la aplicación atravez de mappers.


#### Integracion

Se integraron los endpoints dados en la documentacion del challenge permitiendo la creación de recargas, la consulta de proveedores y la autenticacion en la api de punto red para estableccer una comunicacion exitosa entre estos servicios

Los endpoint de la integracion se consumen el proyecto de la siguiente manera

<img width="927" height="235" alt="image" src="https://github.com/user-attachments/assets/f1baf1ba-ff13-4e60-9091-c8a6f2c24dee" />



El servicio mencionado a continuación aplica las siguientes reglas de negocio.

* Valor mínimo de transacción: 1,000
* Valor máximo de transacción: 100,000
* Número de teléfono: Debe iniciar en “3”, tener longitud de 10 caracteres y
solo aceptar valores numéricos.


```
   /v1/transaction/buy
```


Adicionalmente, se crea un servicio de autenticación que permite el registro y acceso de usuarios. Este se realiza mediante tokens JWT, los cuales son necesarios para interactuar con la aplicación, ya que los endpoints que no deben ser públicos están protegidos por este mecanismo.

<img width="931" height="182" alt="image" src="https://github.com/user-attachments/assets/538369ee-da35-4e65-85d5-c21753541908" />


Las imagenes anteriores provienen se encuentran en el endpoint de acontinuacion y estara disponible una vez el servicio se encuentre arriba

```
   /v1/swagger-ui/index.html
```

### Despliegue

La aplicacion se encuentra disponible en una instancia de ec2 la cual podra se consultado en el link:
```
http://ec2-13-222-134-242.compute-1.amazonaws.com:18081/api/swagger-ui/index.html
```

