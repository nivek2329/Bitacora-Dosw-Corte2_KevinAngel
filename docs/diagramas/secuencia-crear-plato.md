# Diagrama de secuencia - Crear un Plato

`POST /api/v1/platos`. Incluye el flujo exitoso (happy path) y el flujo de
error (validacion de input fallida), usando un fragmento `alt` para
mostrar ambos caminos en un solo diagrama.

```mermaid
sequenceDiagram
    actor Cliente
    participant Controller as PlatoController
    participant Mapper as PlatoMapper
    participant Service as PlatoServiceImpl
    participant Handler as GlobalExceptionHandler

    Cliente ->> Controller: POST /api/v1/platos (PlatoRequestDTO)
    activate Controller

    alt Datos validos (happy path)
        Note over Controller: @Valid pasa la validacion de Bean Validation
        Controller ->> Mapper: toDomain(dto)
        activate Mapper
        Mapper -->> Controller: Plato (sin id, disponible=true)
        deactivate Mapper

        Controller ->> Service: crear(plato)
        activate Service
        Note over Service: asigna id (AtomicLong) y guarda en el mapa en memoria
        Service -->> Controller: Plato (con id)
        deactivate Service

        Controller ->> Mapper: toResponse(plato)
        activate Mapper
        Mapper -->> Controller: PlatoResponseDTO
        deactivate Mapper

        Controller -->> Cliente: 201 Created + PlatoResponseDTO
    else Datos invalidos (ej. nombre vacio, precio negativo)
        Note over Controller: @Valid detecta violaciones antes de ejecutar el metodo
        Controller ->> Handler: MethodArgumentNotValidException
        activate Handler
        Handler -->> Controller: ErrorResponseDTO (400)
        deactivate Handler
        Controller -->> Cliente: 400 Bad Request + ErrorResponseDTO
    end

    deactivate Controller
```
