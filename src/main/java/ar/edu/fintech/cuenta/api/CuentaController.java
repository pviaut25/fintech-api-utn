package ar.edu.fintech.cuenta.api;
import ar.edu.fintech.cuenta.application.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ProblemDetail;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetail.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Recurso no encontrado",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetail.class)
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Conflicto de negocio o concurrencia",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetail.class)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error inesperado",
                content = @Content(
                        schema = @Schema(implementation = ProblemDetail.class)
                )
        )
})
@RestController @RequestMapping("/cuentas") @Tag(name="Cuentas",description="Administración de cuentas FinTech")
public class CuentaController {
 private final CuentaService service;public CuentaController(CuentaService service){this.service=service;}
 @GetMapping @Operation(summary="Listar cuentas") @ApiResponse(responseCode="200",description="Listado de cuentas obtenido") public List<CuentaResponse> listar(){return service.listar();}
 @GetMapping("/{id}") @Operation(summary="Consultar cuenta por ID") @ApiResponse(responseCode="200",description="Cuenta encontrada") public CuentaResponse buscar(@Parameter(description="Identificador de la cuenta",example="1") @PathVariable Long id){return service.buscar(id);}
 @PostMapping @Operation(summary="Crear cuenta") @ApiResponse(responseCode="201",description="Creada") public ResponseEntity<CuentaResponse> crear(@Valid @RequestBody CrearCuentaRequest req){CuentaResponse r=service.crear(req);return ResponseEntity.created(URI.create("/cuentas/"+r.id())).body(r);}
 @PutMapping("/{id}") @Operation(summary="Actualizar alias de cuenta (bonus)") @ApiResponse(responseCode="200",description="Alias actualizado") public CuentaResponse actualizar(@Parameter(description="Identificador de la cuenta",example="1") @PathVariable Long id,@Valid @RequestBody ActualizarCuentaRequest req){return service.actualizar(id,req);}
 @DeleteMapping("/{id}") @Operation(summary="Cerrar cuenta sin eliminar historial (bonus)") @ApiResponse(responseCode="204",description="Cuenta cerrada") public ResponseEntity<Void> eliminar(@Parameter(description="Identificador de la cuenta",example="1") @PathVariable Long id){service.eliminar(id);return ResponseEntity.noContent().build();}
 @PostMapping("/{id}/depositos") @Operation(summary="Depositar fondos") @ApiResponse(responseCode="200",description="Depósito realizado") public CuentaResponse depositar(@Parameter(description="Identificador de la cuenta",example="1") @PathVariable Long id,@Valid @RequestBody MovimientoRequest req){return service.depositar(id,req.monto());}
 @PostMapping("/{id}/extracciones") @Operation(summary="Extraer fondos") @ApiResponse(responseCode="200",description="Extracción realizada") public CuentaResponse extraer(@Parameter(description="Identificador de la cuenta",example="1") @PathVariable Long id,@Valid @RequestBody MovimientoRequest req){return service.extraer(id,req.monto());}
}
