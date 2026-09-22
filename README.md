# Bitacora DOSW - Corte 2 - Sushi Craft

API REST del restaurante **Sushi Craft**, construida siguiendo la Guia
Turtwig (arquitectura por capas: Dominio -> DTO -> Mapper -> Service ->
Controller) y las diapositivas de la Semana 8 de DOSW 1.

**Estado actual: sin persistencia.** Los Services guardan todo en
memoria (`Map`/`AtomicLong`) mientras no se agregue base de datos - asi
lo pide explicitamente la clase de hoy.

## Como se ejecuta
```
mvn spring-boot:run
```
- API: http://localhost:8080/api/...

## Avance (hasta diapositiva 33 - Mappers)

Dominio implementado hasta el momento: **Plato** (con dos Controllers
que lo exponen distinto, ver diapositiva 24: Menu = lo que ve el
cliente, Plato = lo que administra el gerente - mismo Service).

- [x] `model/domain/Plato.java` - clase de dominio con Lombok
- [x] `model/dto/request/PlatoRequestDTO.java` + `response/PlatoResponseDTO.java` - con Bean Validation
- [x] `mapper/PlatoMapper.java` - MapStruct (`@Mapper(componentModel="spring")`)
- [x] `service/IPlatoService.java` + `service/impl/PlatoServiceImpl.java` - en memoria, con Streams
- [x] `controller/PlatoController.java` (`/api/v1/platos`, CRUD completo - administracion)
- [x] `controller/MenuController.java` (`/api/v1/menu`, solo lectura - lo que ve el cliente)
- [x] `exception/RecursoNoEncontradoException.java` - minima, para que `obtenerPorId` compile

## Pendiente (proximas clases, segun las diapositivas)

- [ ] Validaciones de negocio con `IPlatoValidator` (nombre unico, etc.)
- [ ] `ErrorResponseDTO` + `GlobalExceptionHandler` completo (`ConflictoException`, `EstadoInvalidoException`, `ReglaDeNegocioException`)
- [ ] Swagger / OpenAPI (`springdoc-openapi`, `@Operation`, `@ApiResponse`)
- [ ] `@Slf4j` y logs en el resto de la app / `application.yml` con perfiles
- [x] Versionamiento de rutas (`/api/v1/...`)
- [ ] Pruebas unitarias (Mockito para el Service, sin mocks para el Validator)
- [ ] Resto de los dominios de Sushi Craft: Mesa, Pedido, ItemPedido, Cuenta, Reserva, RegistroVehiculo
- [ ] Diagramas actualizados (clases, secuencia) en el repositorio

## Estructura
```
src/main/java/com/restaurante/
  RestauranteApplication.java
  model/domain/Plato.java
  model/dto/request/PlatoRequestDTO.java
  model/dto/response/PlatoResponseDTO.java
  mapper/PlatoMapper.java
  service/IPlatoService.java
  service/impl/PlatoServiceImpl.java
  controller/PlatoController.java
  controller/MenuController.java
  exception/RecursoNoEncontradoException.java
```
