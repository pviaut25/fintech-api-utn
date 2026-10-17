# API FinTech UTN — Gestión de Usuarios y Cuentas Bancarias

**Proyecto:** `fintech-api`  
**Tecnologías:** Java 21 · Spring Boot 3 · Maven · MySQL · Spring Data JPA · Swagger/OpenAPI · JUnit/Mockito

Proyecto académico de la **Diplomatura en Desarrollo de Software FinTech: IA y Microservicios (UTN)**. Es una continuación del trabajo práctico de modelado de clientes bancarios (calificado 10/10). Se conserva el paquete `ar.edu.fintech`, el uso de herencia, las validaciones de dominio y `BigDecimal` para los importes monetarios.

## Requisitos previos

- JDK 21.
- IntelliJ IDEA.
- Maven 3.9+ (también se puede utilizar Maven integrado en IntelliJ).
- MySQL 8+.

## Instalación y ejecución

1. Clonar el repositorio y abrir la carpeta `fintech-api` desde **File > Open** en IntelliJ IDEA.
2. Configurar **JDK 21** en **File > Project Structure > Project SDK** y esperar a que Maven importe las dependencias.
3. Crear la base de datos en MySQL, si todavía no existe:

   ```sql
   CREATE DATABASE fintech_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

   Alternativamente, la URL JDBC del proyecto permite `createDatabaseIfNotExist=true` si el usuario de MySQL tiene los permisos correspondientes.
4. En **Run > Edit Configurations**, seleccionar `FintechApplication` y configurar las variables de entorno:

   ```text
   DB_USER=tu_usuario_mysql
   DB_PASSWORD=tu_contraseña_mysql
   ```

   Opcionalmente, configurar `DB_URL` para cambiar la conexión predeterminada.
5. Ejecutar la clase `ar.edu.fintech.FintechApplication`.
6. Abrir la documentación interactiva:

   - **Swagger UI:** http://localhost:8080/swagger-ui/index.html
   - **OpenAPI JSON:** http://localhost:8080/v3/api-docs

> **Seguridad:** no subir contraseñas reales, archivos `.env` con secretos ni credenciales al repositorio.

## Arquitectura

El código se organiza por funcionalidades (`cuenta` y `usuario`), cada una con las capas `domain`, `application`, `infrastructure` y `api`. El paquete `shared/exception` centraliza el manejo de errores.

- `Cuenta` es una clase abstracta y `CuentaAhorro` hereda de ella.
- `Operable` es una interfaz que representa operaciones sobre cuentas.
- La relación entre `Usuario` y `Cuenta` es **1:N**.
- Se utilizan DTOs `record` para no exponer directamente las entidades JPA.
- Las reglas de dominio y los montos monetarios se manejan con `BigDecimal`.

## Endpoints REST

| Método | Ruta | Respuestas esperadas |
|---|---|---|
| POST | `/usuarios` | 201 |
| GET | `/usuarios/{id}` | 200 / 404 |
| GET | `/cuentas` | 200 |
| POST | `/cuentas` | 201 |
| GET | `/cuentas/{id}` | 200 / 404 |
| PUT | `/cuentas/{id}` | 200 / 404  |
| DELETE | `/cuentas/{id}` | 204 / 404 / 409  |
| POST | `/cuentas/{id}/depositos` | 200 |
| POST | `/cuentas/{id}/extracciones` | 200 / 409 |

### Ejemplos de uso

**Crear un usuario** — `POST /usuarios`:

```json
{
  "nombre": "Ana García",
  "email": "ana@example.com"
}
```

**Crear una cuenta** — `POST /cuentas`:

```json
{
  "numero": "CTA-000001",
  "alias": "mi.cuenta",
  "moneda": "ARS",
  "usuarioId": 1
}
```

El valor de `usuarioId` debe corresponder a un usuario previamente creado. Si ya existe una cuenta con el mismo número, usar un número distinto para la prueba.

## Colección de Postman

La colección del proyecto se encuentra prevista en:

```text
postman/Fintech-API.postman_collection.json
```

Para utilizarla:

1. Abrir Postman y seleccionar **Import**.
2. Importar `postman/Fintech-API.postman_collection.json`.
3. Verificar que la variable `baseUrl` tenga el valor `http://localhost:8080`.
4. Ejecutar primero `POST /usuarios` y luego `POST /cuentas` con el ID del usuario creado.
5. Probar consultas, actualización de alias, depósitos, extracciones y cierre de cuenta.

**Nota:** antes de publicar el repositorio, comprobar que el archivo JSON de la colección exista realmente en esa ruta.

## Pruebas automatizadas

Desde la terminal, en la raíz del proyecto:

```bash
mvn clean test
```

Para generar el paquete ejecutable:

```bash
mvn package
```

En IntelliJ también se pueden ejecutar los objetivos desde el panel **Maven > Lifecycle**.

Durante la validación del trabajo práctico se ejecutaron **15 tests con 0 fallos y 0 errores**.

## Casos de prueba funcionales

1. Crear un titular con `POST /usuarios` y una cuenta asociada con `POST /cuentas`.
2. Consultar cuentas mediante `GET /cuentas` y `GET /cuentas/{id}`.
3. Actualizar el alias con `PUT /cuentas/{id}` sin modificar el saldo (**bonus**).
4. Cerrar una cuenta sin saldo mediante `DELETE /cuentas/{id}` y obtener **204 No Content** (**bonus**).
5. Consultar nuevamente la cuenta cerrada: debe permanecer registrada con `activa: false`.
6. Consultar un ID inexistente y comprobar la respuesta **404 Not Found**.
7. Depositar fondos e intentar cerrar la cuenta con saldo: la operación debe rechazarse con **409 Conflict**.
8. Mostrar los tests JUnit/Mockito y la documentación Swagger.

## Decisiones de diseño

- **PUT:** modifica únicamente el alias. El saldo se actualiza mediante operaciones específicas de depósito y extracción.
- **DELETE:** implementa un **cierre lógico**, conserva el registro en MySQL y rechaza cuentas con saldo pendiente.
- **Número de cuenta:** se protege mediante una restricción `UNIQUE` y una comprobación previa; los conflictos concurrentes se gestionan mediante errores de integridad.
- **Transacciones:** se delimitan en la capa `application` mediante `@Transactional`.
- **Concurrencia:** `@Version` permite el control optimista.
- **Errores HTTP:** se utiliza `ProblemDetail` para respuestas como 400 (entrada inválida), 404 (recurso inexistente), 409 (conflicto) y 500 (error inesperado).
- **Validaciones:** se aplican validaciones Jakarta y reglas de dominio.

## Persistencia y consideraciones de producción

La configuración `spring.jpa.hibernate.ddl-auto=update` está destinada **únicamente al desarrollo**. Para producción se requieren migraciones de base de datos versionadas, por ejemplo mediante Flyway.

Este proyecto es un **simulador académico**, no una plataforma apta para operar con dinero real. No incorpora autenticación/autorización, libro mayor contable de doble entrada (*ledger*), conciliación, idempotencia de operaciones, auditoría inmutable ni migraciones versionadas. Estos componentes serían necesarios para una solución FinTech productiva.

## Contenido requerido para la entrega

- Código fuente completo (`pom.xml`, `src/main/java` y `src/main/resources`).
- Este archivo `README.md` con instrucciones de ejecución.
- Colección de endpoints de Postman (o formato equivalente).
- Tests implementados en `src/test/java`.
- Repositorio Git con todos los archivos necesarios para reproducir el proyecto, sin secretos ni carpetas generadas como `target/`.
