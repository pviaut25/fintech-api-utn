package ar.edu.fintech.usuario.api;
import ar.edu.fintech.usuario.application.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
@ApiResponses({@ApiResponse(responseCode="400",description="Solicitud inválida"),@ApiResponse(responseCode="404",description="Usuario inexistente"),@ApiResponse(responseCode="409",description="Datos duplicados"),@ApiResponse(responseCode="500",description="Error interno")})
@RestController @RequestMapping("/usuarios") @Tag(name="Usuarios") public class UsuarioController {
 private final UsuarioService service;public UsuarioController(UsuarioService service){this.service=service;}
 @PostMapping @Operation(summary="Crear usuario") public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody CrearUsuarioRequest req){UsuarioResponse r=service.crear(req);return ResponseEntity.created(URI.create("/usuarios/"+r.id())).body(r);}
 @GetMapping("/{id}") @Operation(summary="Consultar usuario") public UsuarioResponse buscar(@PathVariable Long id){return service.buscar(id);}
}
