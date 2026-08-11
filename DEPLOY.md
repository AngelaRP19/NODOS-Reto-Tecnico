# Despliegue en Render

Este repo incluye todo lo necesario para desplegar en Render usando el Blueprint
[`render.yaml`](./render.yaml): crea automáticamente la base de datos Postgres y el
servicio web (a partir del `Dockerfile` existente).

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
- **`render.yaml`**: define la base de datos `nodos-db` (Postgres) y el servicio web
  `reto-tecnico-api` (Docker), conectando las credenciales de la DB automáticamente
  mediante `fromDatabase`. También fija `PORT=8081` explícitamente para que Render no
  tenga que auto-detectar el puerto y reiniciar el deploy la primera vez.
- **JVM afinada para el plan free (512MB RAM / 0.1 CPU)** en el `ENTRYPOINT` del
  [`Dockerfile`](Dockerfile): heap acotado a `MaxRAMPercentage=70`, metaspace y code
  cache limitados, GC serial (menos overhead que G1 con heaps chicos) y JIT en modo
  `TieredStopAtLevel=1` para arrancar más rápido con poca CPU. También se redujo el
  pool de hilos de Tomcat (`server.tomcat.threads.max: 20`) y el pool de conexiones de
  Hikari (`spring.datasource.hikari.maximum-pool-size: 5`) en `application.yml`, ya que
  los valores por defecto (200 hilos, etc.) están pensados para instancias mucho más
  grandes.

## Pasos para desplegar

1. Sube estos cambios a GitHub (rama `develop` o `main`, según cuál conectes en Render).
2. En Render: **New > Blueprint**, selecciona este repositorio. Render detectará
   `render.yaml` y mostrará el plan de recursos a crear (1 base de datos + 1 web
   service).
3. Antes de aplicar (o justo después, en la sección **Environment** del servicio),
   completa las variables marcadas como `sync: false` — Render las deja vacías a
   propósito porque son secretos que no deben vivir en el repo:
   - `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`
   - `META_CLIENT_ID`, `META_CLIENT_SECRET`
   - `RESEND_API_KEY` (envío de emails de bienvenida/recuperación de contraseña)
   - `CRYPTO_SECRET_KEY` — clave AES-256 (base64) usada para cifrar `/auth/login` y
     `/auth/register`. **Tiene que ser exactamente la misma que `VITE_CRYPTO_SECRET_KEY`
     en el frontend**, si no el descifrado del body falla en cada login/registro.
   - `FRONTEND_URL` (URL de tu frontend en producción, ej. `https://mi-app.vercel.app`)
   - `CORS_ALLOWED_ORIGINS` (opcional; si no lo pones, cae en `FRONTEND_URL`)

   `JWT_SECRET` se genera automáticamente por Render (`generateValue: true`), y las
   variables `DB_*` se completan solas desde la base de datos creada.
4. Actualiza los **Redirect URIs** en Google Cloud Console y en Meta for Developers para
   que apunten al dominio real de Render, por ejemplo:
   - `https://reto-tecnico-api.onrender.com/login/oauth2/code/google`
   - `https://reto-tecnico-api.onrender.com/login/oauth2/code/meta`

   (Sustituye por el nombre real que Render le asigne al servicio si lo cambiaste.)
5. Deploy. Verifica que `https://<tu-servicio>.onrender.com/actuator/health` responde
   `{"status":"UP"}`.

## Cosas a tener en cuenta

- **Plan free de Postgres en Render expira a los 30 días** (con 14 días de gracia antes
  de borrar los datos). Sirve para demo/entrega del reto, pero si necesitas algo
  persistente cambia `plan: free` por `basic-256mb` (u otro) en `render.yaml` bajo
  `databases`.
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
  web a plan `starter` (mismo RAM pero 5x más CPU, arranca más rápido) o `standard`
  (2GB RAM) en `render.yaml`.
