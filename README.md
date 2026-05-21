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
# 1. Construir la imagen e iniciar todo (DBs + app)
docker compose up -d --build

# 2. Verificar que los contenedores esten corriendo
docker compose ps

# 3. Verificar los logs de la app
docker compose logs app

# 4. Seguir logs en vivo
docker compose logs -f app

# 5. Detener todo
docker compose down
```

La app queda accesible en `http://localhost:3000` desde el navegador.

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

**Con Docker Compose (recomendado):**
```bash
# Construir e iniciar todo
docker compose up -d --build

# Ver logs
docker compose logs -f app
```

**Con Docker (solo la app, para Linux):**
```bash
# Requiere MariaDB y MongoDB accesibles en localhost:3307 y localhost:27017
docker build -t personapp-rest .
docker run -d --network host --name personapp-rest personapp-rest
docker logs -f personapp-rest
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

**Con Docker (recomendado si no tiene JDK 11 + Maven local):**
```bash
# Primera vez o cuando hay cambios en el codigo:
docker run --rm -v "$(pwd):/app" -w /app maven:3.8-eclipse-temurin-11 \
  sh -c "mvn package -pl cli-input-adapter -am -DskipTests -q -T 1C"

# Ejecutar CLI (segundo comando, en segundos):
docker run -it --rm -v "$(pwd):/app" -w /app --network personapp-hexa-spring-boot_default \
  -e SPRING_DATASOURCE_URL=jdbc:mariadb://mariadb:3306/persona_db \
  -e SPRING_DATA_MONGODB_HOST=mongodb \
  maven:3.8-eclipse-temurin-11 java -jar cli-input-adapter/target/cli-input-adapter-*.jar
```

> **Build vs Run**: Compilar (`mvn package`) es lento (~1-2 min) porque compila Java y empaqueta el JAR. Solo es necesario cuando cambia el codigo fuente. Ejecutar (`java -jar`) el JAR ya compilado es inmediato (~3 segundos). Por eso los comandos estan separados: build una sola vez, run cada vez que quieras usar el CLI.

**Sin Docker (si tiene JDK 11 + Maven local):**
```bash
# Usar el script automatico (reconstruye solo si hay cambios):
./run-cli.sh

# O manualmente: compilar una vez, ejecutar el JAR despues
# Build (solo cuando cambia el codigo):
mvn package -pl cli-input-adapter -am -DskipTests -q -T 1C
# Run (cada vez, rapido):
java -jar cli-input-adapter/target/cli-input-adapter-*.jar
```

> Nota: Las DBs deben estar accesibles en `localhost:3307` y `localhost:27017` (via `docker compose up -d` o instalacion local).

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
