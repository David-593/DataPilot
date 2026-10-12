# DataPilot — proyecto inicial

DataPilot es una plataforma inicial de analítica comercial para la empresa ficticia NovaCommerce.

## Tecnologías
- Java 21 + Spring Boot 3.5
- PostgreSQL 16
- Angular 19 + TypeScript
- Docker Compose (para ejecutar PostgreSQL localmente)

## Incluye
- Modelo SQL: categorías, productos, sucursales, inventario, clientes, compras y detalle de compra.
- Restricción `UNIQUE (sucursal_id, producto_id)` para evitar duplicados de inventario.
- Precio histórico por unidad en `detalle_compra`.
- API REST con `/api/health` y `/api/analytics/sales`.
- Dashboard Angular conectado a la API.
- Datos de prueba: las ventas de octubre de 2026 deben sumar $110 en 2 compras.

Esta es una base de desarrollo funcional, no una aplicación lista para producción. Aún faltan autenticación, autorización, pruebas de integración, migraciones versionadas, observabilidad y despliegue.

## Requisitos
- JDK 21
- Maven 3.9+ (o abrir `backend/pom.xml` en IntelliJ IDEA y dejar que importe el proyecto)
- Node.js 20.19+ o 22.12+ y npm
- Docker Desktop, recomendado para PostgreSQL
- Git

## 1. Iniciar la base de datos
En una terminal, desde la carpeta raíz `DataPilot`:

```bash
docker compose up -d
```

PostgreSQL queda en `localhost:5432`, base `datapilot`, usuario `datapilot` y contraseña `datapilot_dev`.

El script `database/init/01-schema-and-seed.sql` se ejecuta cuando PostgreSQL inicializa por primera vez el volumen. Si necesitas reiniciar por completo los datos locales, ejecuta `docker compose down -v` y después `docker compose up -d`. **Eso borra los datos guardados en el volumen local.**

Si ya tienes PostgreSQL instalado, puedes ejecutar el script SQL en una base vacía llamada `datapilot` y configurar las credenciales en `backend/src/main/resources/application.properties` o mediante variables de entorno.

## 2. Ejecutar el backend
Abre la carpeta `backend` como proyecto Maven en IntelliJ IDEA. Espera a que termine la importación y ejecuta la clase `DataPilotApplication`.

También puedes usar una terminal:

```bash
cd backend
mvn spring-boot:run
```

API: `http://localhost:8080`
- `GET http://localhost:8080/api/health`
- `GET http://localhost:8080/api/analytics/sales?from=2026-10-01&to=2026-11-01`

El parámetro `to` es exclusivo: octubre incluye desde el 1 de octubre hasta antes del 1 de noviembre.

Variables opcionales: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SERVER_PORT`.

## 3. Ejecutar el frontend
Abre otra terminal:

```bash
cd frontend
npm install
npm start
```

Abre `http://localhost:4200`. El dashboard consulta la API de Spring Boot. Si no ves datos, comprueba que PostgreSQL y el backend estén activos.

## Resultado esperado para octubre de 2026
- Compra 101: 2 audífonos a $25 + 1 teclado a $30 = $80.
- Compra 102: 3 unidades a $10 = $30.
- Total vendido: $110.
- Compras distintas: 2.
- Unidades vendidas: 6.

La compra 103 pertenece a noviembre y no se incluye en el informe de octubre.

## Estructura
```text
DataPilot/
├── backend/                 # API Spring Boot
├── database/init/           # Esquema y datos de prueba
├── frontend/                # Dashboard Angular
├── docker-compose.yml
└── README.md
```

## Primer ticket: DEV-008 — Ejecutar el proyecto
1. Levanta PostgreSQL.
2. Inicia Spring Boot.
3. Consulta el endpoint de ventas.
4. Inicia Angular.
5. Comprueba que el dashboard muestra $110, 2 compras y 6 unidades.

No agregaremos IA ni autenticación hasta que este flujo básico funcione y podamos probarlo.
