
package ar.edu.fintech.usuario.api;

import ar.edu.fintech.usuario.application.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ProblemDetail;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

        @ApiResponses({
                @ApiResponse(
                        responseCode = "400",
                        description = "Datos de entrada inválidos",
                        content = @Content(
                                schema = @Schema(implementation = ProblemDetail.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content = @Content(
                                schema = @Schema(implementation = ProblemDetail.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflicto por datos duplicados",
                        content = @Content(
                                schema = @Schema(implementation = ProblemDetail.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Error inesperado del servidor",
                        content = @Content(
                                schema = @Schema(implementation = ProblemDetail.class)
                        )
                )
        })
@RestController
@RequestMapping("/usuarios")
@Tag(
        name = "Usuarios",
        description = "Gestión de usuarios titulares de cuentas bancarias"
)
public class UsuarioController {

 private final UsuarioService service;

 public UsuarioController(UsuarioService service) {
  this.service = service;
 }

 @PostMapping
 @Operation(
         summary = "Crear usuario",
         description = "Registra un nuevo usuario que podrá ser titular de una o más cuentas."
 )
 @ApiResponse(
         responseCode = "201",
         description = "Usuario creado correctamente"
 )
 public ResponseEntity<UsuarioResponse> crear(
         @Valid @RequestBody CrearUsuarioRequest req) {

  UsuarioResponse respuesta = service.crear(req);

  return ResponseEntity
          .created(URI.create("/usuarios/" + respuesta.id()))
          .body(respuesta);
 }

 @GetMapping("/{id}")
 @Operation(
         summary = "Consultar usuario por ID",
         description = "Obtiene los datos de un usuario registrado."
 )
 @ApiResponse(
         responseCode = "200",
         description = "Usuario encontrado correctamente"
 )
 public UsuarioResponse buscar(
         @Parameter(
                 description = "Identificador único del usuario",
                 example = "1"
         )
         @PathVariable Long id) {

  return service.buscar(id);
 }
}
