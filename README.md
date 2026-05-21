# PersonApp - Arquitectura Hexagonal con Spring Boot

Aplicacion de ejemplo para el laboratorio de Arquitectura de Software usando el patron Hexagonal (Puertos y Adaptadores).

## Stack tecnologico

- JDK 11
- Spring Boot 2.7.11
- MariaDB 10.8
- MongoDB 6.0
- Swagger 3 (OpenAPI) via Springdoc
- Maven (multi-modulo)

## Prerrequisitos

- JDK 11 instalado
- Maven 3.8+ instalado
- Docker y Docker Compose (opcional, para entorno local)
- MariaDB (puerto 3307) y MongoDB (puerto 27017) si no usa Docker

## Configuracion del entorno

### Con Docker Compose (recomendado)

```bash
# 1. Iniciar bases de datos
docker compose up -d

# 2. Verificar que los contenedores esten corriendo
docker compose ps

# 3. Construir la imagen de la REST API
docker build -t personapp-rest .

# 4. Iniciar la aplicacion (puerto 3000)
docker run -d --network host --name personapp-rest personapp-rest

# 5. Verificar los logs
docker logs personapp-rest
```

### Sin Docker

1. Instalar MariaDB en puerto 3307 y MongoDB en puerto 27017
2. Ejecutar los scripts SQL:
   ```bash
   mysql -u root -p < scripts/ddl.sql
   mysql -u root -p < scripts/dml.sql
   ```
3. Configurar MongoDB con usuario `persona_db` / `persona_db`

## Compilacion

```bash
mvn clean package -DskipTests
```

## Ejecucion

La aplicacion tiene dos adaptadores de entrada (dos SpringApplication independientes):

### REST API (puerto 3000)

**Con Docker:**
```bash
# Iniciar (si ya construyo la imagen)
docker run -d --network host --name personapp-rest personapp-rest

# Ver logs en vivo
docker logs -f personapp-rest

# Detener
docker stop personapp-rest && docker rm personapp-rest
```

**Sin Docker (via Maven):**
```bash
mvn spring-boot:run -pl rest-input-adapter
```

**Sin Docker (via JAR):**
```bash
java -jar rest-input-adapter/target/rest-input-adapter-*.jar
```

### CLI (linea de comandos)

**Sin Docker:**
```bash
mvn spring-boot:run -pl cli-input-adapter
```

> Nota: La CLI requiere una terminal interactiva; no se ejecuta dentro del contenedor Docker.

## Probando la API

### Con curl (funciona con o sin Docker)

```bash
# Obtener todas las personas (MariaDB)
curl http://localhost:3000/api/v1/persona/MARIA

# Obtener todas las personas (MongoDB)
curl http://localhost:3000/api/v1/persona/MONGO

# Obtener persona por CC
curl http://localhost:3000/api/v1/persona/MARIA/1001

# Crear persona
curl -X POST http://localhost:3000/api/v1/persona \
  -H "Content-Type: application/json" \
  -d '{"dni":"2001","firstName":"Ana","lastName":"Lopez","age":"28","sex":"F","database":"MARIA"}'

# Actualizar persona
curl -X PUT http://localhost:3000/api/v1/persona/MARIA/2001 \
  -H "Content-Type: application/json" \
  -d '{"dni":"2001","firstName":"Ana","lastName":"Perez","age":"29","sex":"F","database":"MARIA"}'

# Eliminar persona
curl -X DELETE http://localhost:3000/api/v1/persona/MARIA/2001

# Profesiones
curl http://localhost:3000/api/v1/profesion
curl -X POST http://localhost:3000/api/v1/profesion \
  -H "Content-Type: application/json" \
  -d '{"identification":99,"name":"Data Scientist","description":"IA"}' 

# Telefonos
curl http://localhost:3000/api/v1/telefono
curl -X POST http://localhost:3000/api/v1/telefono \
  -H "Content-Type: application/json" \
  -d '{"number":"3998887766","company":"Claro"}'

# Estudios
curl http://localhost:3000/api/v1/estudios
curl -X POST http://localhost:3000/api/v1/estudios \
  -H "Content-Type: application/json" \
  -d '{"personIdentification":1001,"professionIdentification":1,"graduationDate":"2020-06-15","universityName":"UNAL"}'
```

### Con docker exec (solo con Docker)

```bash
# Ejecutar curl dentro del contenedor
docker exec personapp-rest curl -s http://localhost:3000/api/v1/persona/MARIA
docker exec personapp-rest curl -s http://localhost:3000/api/v1/profesion

# Conectarse a la base de datos MariaDB
docker exec -it persona-mariadb mysql -u root -p

# Conectarse a la base de datos MongoDB
docker exec -it persona-mongodb mongosh -u admin -p admin
```

## Swagger UI

Una vez corriendo la REST API, acceder a:

- Swagger UI: http://localhost:3000/swagger-ui.html
- OpenAPI docs: http://localhost:3000/api-docs

## Endpoints REST

| Endpoint | Metodo | Descripcion |
|---|---|---|
| `/api/v1/persona/{database}` | GET | Lista todas las personas |
| `/api/v1/persona` | POST | Crea una nueva persona |
| `/api/v1/persona/{database}/{cc}` | GET | Busca persona por cc |
| `/api/v1/persona/{database}/{cc}` | PUT | Actualiza una persona |
| `/api/v1/persona/{database}/{cc}` | DELETE | Elimina una persona |
| `/api/v1/profesion` | GET | Lista todas las profesiones |
| `/api/v1/profesion` | POST | Crea una nueva profesion |
| `/api/v1/profesion/{id}` | GET | Busca profesion por id |
| `/api/v1/profesion/{id}` | PUT | Actualiza una profesion |
| `/api/v1/profesion/{id}` | DELETE | Elimina una profesion |
| `/api/v1/telefono` | GET | Lista todos los telefonos |
| `/api/v1/telefono` | POST | Crea un nuevo telefono |
| `/api/v1/telefono/{number}` | GET | Busca telefono por numero |
| `/api/v1/telefono/{number}` | PUT | Actualiza un telefono |
| `/api/v1/telefono/{number}` | DELETE | Elimina un telefono |
| `/api/v1/estudios` | GET | Lista todos los estudios |
| `/api/v1/estudios` | POST | Crea un nuevo estudio |
| `/api/v1/estudios/{idProf}/{ccPer}` | GET | Busca estudio por id |
| `/api/v1/estudios/{idProf}/{ccPer}` | PUT | Actualiza un estudio |
| `/api/v1/estudios/{idProf}/{ccPer}` | DELETE | Elimina un estudio |

## Estructura del proyecto

```
personapp-hexa-spring-boot/
├── domain/                  # Entidades del dominio (Person, Profession, Study, Phone)
├── common/                  # Anotaciones y utilidades compartidas
├── application/             # Casos de uso (puertos de entrada/salida)
├── maria-output-adapter/    # Adaptador de salida MariaDB (JPA)
├── mongo-output-adapter/    # Adaptador de salida MongoDB
├── rest-input-adapter/      # Adaptador de entrada REST
├── cli-input-adapter/       # Adaptador de entrada CLI
├── scripts/                 # Scripts SQL
│   ├── ddl.sql              # Esquema de base de datos
│   └── dml.sql              # Datos de prueba
├── docker-compose.yml       # Entorno local con Docker
└── Dockerfile               # Imagen Docker para REST API
```

## Notas

- Para usar Lombok en el IDE, instalar el plugin correspondiente
- `persona.cc` no es auto-generado; el usuario debe proveerlo
- `profesion.id` es auto-generado (AUTO_INCREMENT)
- Las claves foraneas usan ON DELETE RESTRICT (sin cascade)
- Solo el modulo `persona` soporta MariaDB y MongoDB como persistencia intercambiable
