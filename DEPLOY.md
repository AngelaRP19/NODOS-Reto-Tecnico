# Despliegue en Render

Este repo incluye todo lo necesario para desplegar en Render como **Web Service** manual
(Docker, a partir del `Dockerfile` existente) conectado a una base de datos Postgres ya
creada. También existe un [`render.yaml`](./render.yaml) por si en algún momento preferís
el flujo de Blueprint (ver el apéndice al final), pero **no hace falta usarlo** — Render
solo lo lee si elegís explícitamente "New > Blueprint"; con "New > Web Service" lo ignora
por completo, así que podés dejarlo en el repo sin que interfiera.

## Qué se preparó

- **`server.port`** ahora respeta la variable `PORT` que inyecta Render (con fallback a
  `SERVER_PORT` para uso local), en [`application.yml`](src/main/resources/application.yml).
- **`server.forward-headers-strategy: framework`**: necesario para que Spring genere bien
  las URLs absolutas (`{baseUrl}`) detrás del proxy de Render — importante para que el
  callback de OAuth2 use `https://` en vez de `http://`.
- **CORS ya no está hardcodeado a `localhost:5173`**: ahora lee `cors.allowed-origins`
  (env var `CORS_ALLOWED_ORIGINS`, admite varios orígenes separados por coma), con
  fallback a `frontend.url` (`FRONTEND_URL`) — ver
  [`SecurityConfig.java`](src/main/java/com/nodo/retotecnico/config/SecurityConfig.java).
- **Health check**: se agregó `spring-boot-starter-actuator` con `/actuator/health`
  expuesto y público (agregado explícitamente a `SecurityConfig`), usado por Render
  para verificar que el servicio está vivo.
- **`.dockerignore`**: evita copiar `target/`, `.git`, `.env`, logs, etc. al build de
  Docker.
- **`render.yaml`** (opcional, no usado en el flujo principal de este doc — ver
  apéndice): define la base de datos `nodos-db` (Postgres) y el servicio web
  `reto-tecnico-api` (Docker), conectando las credenciales de la DB automáticamente
  mediante `fromDatabase`.
- **JVM afinada para el plan free (512MB RAM / 0.1 CPU)** en el `ENTRYPOINT` del
  [`Dockerfile`](Dockerfile): heap acotado a `MaxRAMPercentage=70`, metaspace y code
  cache limitados, GC serial (menos overhead que G1 con heaps chicos) y JIT en modo
  `TieredStopAtLevel=1` para arrancar más rápido con poca CPU. También se redujo el
  pool de hilos de Tomcat (`server.tomcat.threads.max: 20`) y el pool de conexiones de
  Hikari (`spring.datasource.hikari.maximum-pool-size: 5`) en `application.yml`, ya que
  los valores por defecto (200 hilos, etc.) están pensados para instancias mucho más
  grandes.

## Pasos para desplegar (Web Service manual)

### 1. Base de datos Postgres (si todavía no tenés una)

En Render: **New > PostgreSQL**. Cualquier nombre/región/plan sirve (`free` alcanza para
demo). Una vez creada, andá a su página de detalle y anotá, de la sección **Connections**:
`Hostname` (o `Internal Database URL` si tu web service va a estar en la misma región de
Render — más rápido y sin costo de salida), `Port`, `Database`, `Username`, `Password`.

### 2. Web Service

1. Sube estos cambios a GitHub (rama `develop` o `main`, según cuál conectes en Render).
2. En Render: **New > Web Service**, seleccioná este repositorio.
3. **Runtime**: `Docker` (Render detecta el `Dockerfile` en la raíz automáticamente; si
   pregunta por el path, es `./Dockerfile`).
4. **Branch**: la que subiste en el paso 1. **Plan**: `Free` alcanza para demo.
5. **Health Check Path**: `/actuator/health` (campo propio en la configuración del
   servicio, no depende de `render.yaml`).
6. **Environment** → agregá **todas** estas variables a mano (acá no hay Blueprint que
   las autocomplete ni que genere secretos por vos):

   | Variable | Valor |
   |---|---|
   | `PORT` | `8081` (para que coincida con el `EXPOSE 8081` del `Dockerfile`) |
   | `DB_HOST` | Hostname de la DB del paso 1 |
   | `DB_PORT` | Normalmente `5432` |
   | `DB_NAME` | `Database` de la DB del paso 1 |
   | `DB_USERNAME` | `Username` de la DB del paso 1 |
   | `DB_PASSWORD` | `Password` de la DB del paso 1 |
   | `JWT_SECRET` | Generalo vos (ej. `openssl rand -base64 32`) — sin Blueprint no se autogenera |
   | `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | De Google Cloud Console |
   | `META_CLIENT_ID` / `META_CLIENT_SECRET` | De Meta for Developers |
   | `RESEND_API_KEY` | Para los correos de bienvenida/recuperación/compra |
   | `CRYPTO_SECRET_KEY` | Clave AES-256 en base64. **Tiene que ser exactamente la misma que `VITE_CRYPTO_SECRET_KEY` en el frontend**, si no el descifrado de `/auth/login` y `/auth/register` falla siempre |
   | `FRONTEND_URL` | URL de tu frontend en producción (ej. `https://mi-app.vercel.app`) |
   | `CORS_ALLOWED_ORIGINS` | Opcional; si no lo ponés, cae en `FRONTEND_URL` |

7. Actualiza los **Redirect URIs** en Google Cloud Console y en Meta for Developers para
   que apunten al dominio real que Render le asigne al servicio, por ejemplo:
   - `https://<tu-servicio>.onrender.com/login/oauth2/code/google`
   - `https://<tu-servicio>.onrender.com/login/oauth2/code/meta`
8. **Create Web Service** para disparar el primer deploy. Verifica que
   `https://<tu-servicio>.onrender.com/actuator/health` responde `{"status":"UP"}`.

## Cosas a tener en cuenta

- **Plan free de Postgres en Render expira a los 30 días** (con 14 días de gracia antes
  de borrar los datos). Sirve para demo/entrega del reto, pero si necesitas algo
  persistente elegí un plan pago (ej. `Basic-256mb`) al crear la base de datos.
- **Plan free del web service duerme tras inactividad** y tarda unos segundos en
  responder en el primer request ("cold start"). Normal en el free tier.
- **Esquema `reto`**: la app usa `hibernate.default_schema=reto` con `ddl-auto: update`.
  Se agregó `hibernate.hbm2ddl.create_namespaces: true` en `application.yml` para que
  Hibernate cree el esquema automáticamente si no existe (si no, falla con
  `schema "reto" does not exist` en bases nuevas). El usuario que Render crea como
  owner de la base tiene los permisos necesarios.
- Nunca subas tu `.env` real (ya está en `.gitignore`); usa `.env.example` como
  referencia de qué variables se necesitan.
- **Si el deploy muere con `Exited with status 137`**: es un OOM-kill (el contenedor
  se quedó sin memoria). El plan free de Render solo da 512MB, y un Spring Boot con
  JPA + Security + OAuth2 + Actuator puede llegar a necesitar más que eso en el pico de
  arranque si no se acota la JVM (ya se hizo, ver arriba). Si igual vuelve a pasar (por
  ejemplo, tras agregar más dependencias), la salida más simple es subir el servicio
  web a plan `Starter` (mismo RAM pero 5x más CPU, arranca más rápido) o `Standard`
  (2GB RAM) desde la configuración del servicio en Render.

## Apéndice: alternativa con Blueprint

Si en algún momento preferís que Render provisione la base de datos y complete las
variables `DB_*`/`JWT_SECRET` automáticamente, [`render.yaml`](./render.yaml) ya está
listo para eso: **New > Blueprint**, seleccioná este repo, y Render arma tanto la DB
como el web service en un solo paso, dejando solo los secretos (`GOOGLE_CLIENT_ID`,
`RESEND_API_KEY`, `CRYPTO_SECRET_KEY`, `FRONTEND_URL`, etc.) para completar a mano. Es
el mismo `Dockerfile` y la misma app en ambos casos — la única diferencia es quién arma
la base de datos y quién completa las variables de entorno.
