# Bitacora DOSW - Corte 2 - Sushi Craft

**Autor:** Kevin Angel

## Descripcion

API REST del restaurante **Sushi Craft** (app *Kaze & Nori*), el concepto de
restaurante escogido en el LAB03 de corte 1. Construida siguiendo la Guia
Turtwig (arquitectura por capas: Dominio -> DTO -> Mapper -> Service ->
Controller) y las diapositivas de la Semana 8 de DOSW 1.

Ademas del CRUD de platos, se implemento el CRUD completo de los otros 6
dominios del restaurante (Mesa, Pedido, ItemPedido, Cuenta, Reserva y
RegistroVehiculo), siguiendo la misma arquitectura por capas.

**Persistencia (en progreso, Semana 9):** ya existen la entidad JPA
(`persistence/entity/PlatoEntity`) y `PlatoJpaRepository`, listas para
conectar PostgreSQL. El `PlatoServiceImpl` todavia guarda en memoria
mientras se avanza con el resto de la clase.

## Funcionalidades

La API agrupa las funcionalidades en los 7 dominios del restaurante:

- **Platos** (`/api/v1/platos`): CRUD completo para que el gerente cree,
  edite, consulte, active/desactive disponibilidad y elimine platos del
  menu.
- **Menu publico** (`/api/v1/menu`): vista de solo lectura para el
  cliente, que solo muestra los platos disponibles en este momento, con
  filtro opcional por categoria (Roll, Nigiri, Sashimi, Temaki, Entrada,
  Bebida).
- **Mesas** (`/api/v1/mesas`): CRUD de las mesas del restaurante y cambio
  de estado (LIBRE, OCUPADA, RESERVADA).
- **Pedidos** (`/api/v1/pedidos`): CRUD de los pedidos de cada mesa, con
  cambio de estado (RECIBIDO, EN_PREPARACION, LISTO, ENTREGADO,
  CANCELADO) y calculo automatico del total a partir de sus items.
- **Items de pedido** (`/api/v1/items-pedido`): agregar/editar/eliminar
  items dentro de un pedido. El nombre y el precio del plato se "congelan"
  al momento de crear el item (no cambian si despues se edita el plato).
- **Cuentas** (`/api/v1/cuentas`): CRUD de las cuentas por mesa, con
  actualizacion del total y cierre de cuenta.
- **Reservas** (`/api/v1/reservas`): CRUD de las reservas de mesa, con
  cancelacion y reprogramacion.
- **Parqueadero** (`/api/v1/vehiculos`): registro de entrada y salida de
  vehiculos.
- **Manejo de errores uniforme**: cualquier endpoint responde con el mismo
  formato de error (`ErrorResponseDTO`) ante recurso no encontrado,
  datos invalidos o body malformado.

## Diagrama de clases

Dominio de Sushi Craft (7 clases + 3 enums) y sus relaciones:
- Archivo editable (abrir en [draw.io](https://app.diagrams.net) / diagrams.net): [`docs/diagramas/diagrama-clases-sushicraft.drawio`](docs/diagramas/diagrama-clases-sushicraft.drawio)
- Explicacion de las relaciones y respuesta a la diapositiva 9: [`docs/diagramas/diagrama-clases.md`](docs/diagramas/diagrama-clases.md)

## Tabla de endpoints

### Platos y menu

| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| GET | `/api/v1/platos` | Listar todos los platos (disponibles o no) | 200 |
| GET | `/api/v1/platos/{id}` | Obtener un plato por id | 200, 404 |
| POST | `/api/v1/platos` | Crear un plato nuevo (queda disponible por defecto) | 201, 400 |
| PUT | `/api/v1/platos/{id}` | Actualizar un plato existente | 200, 404, 400 |
| PATCH | `/api/v1/platos/{id}/disponible?disponible={true\|false}` | Cambiar la disponibilidad de un plato | 200, 404 |
| DELETE | `/api/v1/platos/{id}` | Eliminar un plato | 204, 404 |
| GET | `/api/v1/menu` | Ver el menu publico (solo platos disponibles) | 200 |
| GET | `/api/v1/menu/categoria/{categoria}` | Ver el menu filtrado por categoria | 200 |

### Mesas

| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| GET | `/api/v1/mesas` | Listar todas las mesas | 200 |
| GET | `/api/v1/mesas/{id}` | Obtener una mesa por id | 200, 404 |
| POST | `/api/v1/mesas` | Crear una mesa nueva (queda LIBRE por defecto) | 201, 400 |
| PUT | `/api/v1/mesas/{id}` | Actualizar una mesa existente | 200, 404, 400 |
| PATCH | `/api/v1/mesas/{id}/estado?estado={LIBRE\|OCUPADA\|RESERVADA}` | Cambiar el estado de una mesa | 200, 404 |
| DELETE | `/api/v1/mesas/{id}` | Eliminar una mesa | 204, 404 |

### Pedidos

| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| GET | `/api/v1/pedidos?idMesa=` | Listar todos los pedidos (o filtrar por mesa) | 200 |
| GET | `/api/v1/pedidos/{id}` | Obtener un pedido por id | 200, 404 |
| POST | `/api/v1/pedidos` | Crear un pedido nuevo (queda RECIBIDO y sin items) | 201, 400 |
| PUT | `/api/v1/pedidos/{id}` | Actualizar un pedido existente | 200, 404, 400 |
| PATCH | `/api/v1/pedidos/{id}/estado?estado=` | Cambiar el estado del pedido | 200, 404 |
| DELETE | `/api/v1/pedidos/{id}` | Eliminar un pedido | 204, 404 |

### Items de pedido

| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| GET | `/api/v1/items-pedido?idPedido=` | Listar todos los items (o filtrar por pedido) | 200 |
| GET | `/api/v1/items-pedido/{id}` | Obtener un item por id | 200, 404 |
| POST | `/api/v1/items-pedido` | Agregar un item a un pedido (congela nombre/precio del plato) | 201, 400, 404 |
| PATCH | `/api/v1/items-pedido/{id}/cantidad?cantidad=` | Cambiar la cantidad de un item | 200, 404 |
| DELETE | `/api/v1/items-pedido/{id}` | Eliminar un item | 204, 404 |

### Cuentas

| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| GET | `/api/v1/cuentas?idMesa=` | Listar todas las cuentas (o filtrar por mesa) | 200 |
| GET | `/api/v1/cuentas/{id}` | Obtener una cuenta por id | 200, 404 |
| POST | `/api/v1/cuentas` | Abrir una cuenta nueva (queda ABIERTA en 0) | 201, 400 |
| PATCH | `/api/v1/cuentas/{id}/total?total=` | Actualizar el total acumulado | 200, 404 |
| PATCH | `/api/v1/cuentas/{id}/cerrar` | Cerrar una cuenta | 200, 404 |
| DELETE | `/api/v1/cuentas/{id}` | Eliminar una cuenta | 204, 404 |

### Reservas

| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| GET | `/api/v1/reservas?idMesa=` | Listar todas las reservas (o filtrar por mesa) | 200 |
| GET | `/api/v1/reservas/{id}` | Obtener una reserva por id | 200, 404 |
| POST | `/api/v1/reservas` | Crear una reserva nueva | 201, 400 |
| PUT | `/api/v1/reservas/{id}` | Actualizar una reserva existente | 200, 404, 400 |
| PATCH | `/api/v1/reservas/{id}/cancelar` | Cancelar una reserva | 200, 404 |
| PATCH | `/api/v1/reservas/{id}/reprogramar?fechaHora=` | Reprogramar una reserva | 200, 404 |
| DELETE | `/api/v1/reservas/{id}` | Eliminar una reserva | 204, 404 |

### Parqueadero

| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| GET | `/api/v1/vehiculos?activos=` | Listar todos los registros (o solo los activos) | 200 |
| GET | `/api/v1/vehiculos/{id}` | Obtener un registro por id | 200, 404 |
| POST | `/api/v1/vehiculos` | Registrar el ingreso de un vehiculo | 201, 400 |
| PATCH | `/api/v1/vehiculos/{id}/salida` | Registrar la salida de un vehiculo | 200, 404 |
| DELETE | `/api/v1/vehiculos/{id}` | Eliminar un registro | 204, 404 |

## Como se ejecuta

1. Levanta PostgreSQL local con Docker (una sola vez; si ya existe el
   contenedor, solo hace falta arrancarlo desde Docker Desktop):

```
docker run --name sushicraft-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=sushicraft -p 5432:5432 -d postgres:16
```

2. Corre la aplicacion (Hibernate crea la tabla `platos` solo, gracias a
   `spring.jpa.hibernate.ddl-auto=update` en `application.properties`):

```
mvn spring-boot:run
```
- API: http://localhost:8080/api/v1/...
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## Pruebas y cobertura

```
mvn test
```
Corre las 95 pruebas unitarias (mapper + service de cada uno de los 7
dominios: Plato, Mesa, Pedido, ItemPedido, Cuenta, Reserva y
RegistroVehiculo) y genera el reporte de cobertura de Jacoco en
`target/site/jacoco/index.html` (abrelo en el navegador despues de correr
`mvn test`).

## Analisis estatico (SonarQube local)

SonarQube corre localmente en Docker (contenedor `sonarqube`, puerto 9000).

1. En Docker Desktop, inicia el contenedor `sonarqube` (boton Play) y
   espera ~1 minuto a que levante.
2. Abre http://localhost:9000 (usuario/clave que ya tengas configurados).
3. Crea el proyecto (o usa uno existente) y copia su **Project Key**.
4. En **My Account > Security**, genera un **token**.
5. En `pom.xml`, reemplaza `sonar.projectKey` por la key del paso 3.
6. Corre:

```
mvn verify sonar:sonar -Dsonar.token=TU_TOKEN
```

(o exporta `SONAR_TOKEN` como variable de entorno en vez de pasarlo por
linea de comandos). El reporte queda visible en http://localhost:9000
dentro del proyecto.

## Evidencias

> Pega aqui las capturas de pantalla antes de entregar. Guardalas en una
> carpeta `docs/evidencias/` y enlazalas con `![descripcion](docs/evidencias/archivo.png)`.

### Swagger UI

![Swagger UI](docs/evidencias/swagger-ui.png)

### Cobertura de pruebas (Jacoco)

![Cobertura Jacoco](docs/evidencias/jacoco-cobertura.png)

Cobertura total del proyecto: 23% (559/734 instrucciones). El paquete
`service.impl` (que es el que tiene pruebas unitarias, `PlatoServiceImpl`)
llega al 98% de cobertura de instrucciones.

### Analisis estatico (SonarQube local)

![SonarQube Overview](docs/evidencias/sonar-overview.png)

### Ejecucion

![Ejecucion de la app](docs/evidencias/ejecucion-consola.png)

### Pruebas por funcionalidad (Postman)

Se probaron los 8 endpoints de platos y menu, cada uno con al menos un
caso exitoso y un caso de error:

| Endpoint | Caso exitoso | Caso de error |
|---|---|---|
| GET `/platos` | 200, lista completa | - |
| GET `/platos/{id}` | 200, plato encontrado | 404, id inexistente |
| POST `/platos` | 201, plato creado | 400, body invalido/vacio |
| PUT `/platos/{id}` | 200, plato actualizado | 404, id inexistente |
| PATCH `/platos/{id}/disponible` | 200, disponibilidad cambiada | 404, id inexistente |
| DELETE `/platos/{id}` | 204, plato eliminado | 404, id inexistente |
| GET `/menu` | 200, solo platos disponibles | - |
| GET `/menu/categoria/{categoria}` | 200, filtrado por categoria | - |

Capturas representativas (creacion, actualizacion, eliminacion y los
errores 404 al usar un id que ya no existe):

![POST exitoso](docs/evidencias/postman-post-exito.png)

![PUT exitoso](docs/evidencias/postman-put-exito.png)

![GET con id inexistente - 404](docs/evidencias/postman-get-404.png)

![PATCH con id inexistente - 404](docs/evidencias/postman-patch-404.png)

![DELETE exitoso](docs/evidencias/postman-delete-exito.png)
