# Bitacora DOSW - Corte 2 - Sushi Craft

API REST del restaurante **Sushi Craft** (app *Kaze & Nori*), construida
siguiendo la Guia Turtwig (arquitectura por capas: Dominio -> DTO ->
Mapper -> Service -> Controller) y las diapositivas de la Semana 8 de
DOSW 1.

**Estado actual: sin persistencia.** Los Services guardan todo en
memoria (`Map`/`AtomicLong`) mientras no se agregue base de datos - asi
lo pide explicitamente la clase.

## Como se ejecuta
```
mvn spring-boot:run
```
- API: http://localhost:8080/api/v1/...
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## Pruebas
```
mvn test
```

## Avance (hasta diapositiva 87 del deck de la S08)

Dominio completo (7 clases + enums) segun el diagrama de clases del
restaurante. Capa completa (DTO/Mapper/Service/Controller) implementada
por ahora solo para **Plato** (con dos Controllers que lo exponen
distinto, ver diapositiva 24: Menu = lo que ve el cliente, Plato = lo
que administra el gerente - mismo Service).

- [x] `model/domain/` - las 7 clases de dominio + enums (`EstadoMesa`, `EstadoPedido`, `EstadoCuenta`)
- [x] `model/dto/request/PlatoRequestDTO.java` + `response/PlatoResponseDTO.java` - con Bean Validation
- [x] `mapper/PlatoMapper.java` - MapStruct (`@Mapper(componentModel="spring")`)
- [x] `service/IPlatoService.java` + `service/impl/PlatoServiceImpl.java` - en memoria, con Streams
- [x] `controller/PlatoController.java` (`/api/v1/platos`, CRUD completo - administracion)
- [x] `controller/MenuController.java` (`/api/v1/menu`, solo lectura - lo que ve el cliente)
- [x] Excepciones personalizadas: `RecursoNoEncontradoException`, `ConflictoException`, `EstadoInvalidoException`, `ReglaDeNegocioException`
- [x] `ErrorResponseDTO` + `GlobalExceptionHandler` (`@RestControllerAdvice`, respuestas uniformes, handlers mas especificos primero)
- [x] Swagger / OpenAPI (`springdoc-openapi`, `OpenApiConfig`, `@Operation`/`@ApiResponse` en los controllers)
- [x] `@Slf4j` y logs en controllers y service
- [x] Versionamiento de rutas (`/api/v1/...`)
- [x] Pruebas unitarias del Service con JUnit 5 (`PlatoServiceImplTest`, 11 casos: happy path, recurso no encontrado, lista vacia, filtros)
- [x] Diagrama de secuencia de "Crear un Plato" (happy path + error) - [`docs/diagramas/secuencia-crear-plato.md`](docs/diagramas/secuencia-crear-plato.md)

## Pendiente

- [ ] Validaciones de negocio con `IPlatoValidator` (nombre unico, etc.) - opcional segun el deck
- [ ] `application.yml` con perfiles por ambiente (no es obligatorio: la config actual es simple)
- [ ] Pruebas del Controller con `@Mock`/`@InjectMocks` (mockeando `IPlatoService` y `PlatoMapper`)
- [ ] Resto de los dominios de Sushi Craft con capa completa: Mesa, Pedido, ItemPedido, Cuenta, Reserva, RegistroVehiculo (por ahora solo tienen el dominio)
- [ ] Diagrama de clases actualizado con los cambios de hoy
- [ ] Jacoco (cobertura) y Sonar (analisis estatico) - no estan en el deck de la S08, pendientes del rubric general del corte

## Estructura
```
src/main/java/com/restaurante/
  RestauranteApplication.java
  config/OpenApiConfig.java
  model/domain/          Plato, Mesa, Pedido, ItemPedido, Cuenta, Reserva, RegistroVehiculo + enums
  model/dto/request/PlatoRequestDTO.java
  model/dto/response/PlatoResponseDTO.java
  model/dto/ErrorResponseDTO.java
  mapper/PlatoMapper.java
  service/IPlatoService.java
  service/impl/PlatoServiceImpl.java
  controller/PlatoController.java
  controller/MenuController.java
  controller/GlobalExceptionHandler.java
  exception/                RecursoNoEncontradoException, ConflictoException, EstadoInvalidoException, ReglaDeNegocioException
src/test/java/com/restaurante/
  service/impl/PlatoServiceImplTest.java
docs/diagramas/
  secuencia-crear-plato.md
```
