package ar.edu.fintech.shared.exception;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
@RestControllerAdvice public class GlobalExceptionHandler {
 private ResponseEntity<ProblemDetail> error(HttpStatus status,String mensaje){ProblemDetail p=ProblemDetail.forStatusAndDetail(status,mensaje);p.setTitle(status.getReasonPhrase());return ResponseEntity.status(status).body(p);}
 @ExceptionHandler({CuentaNoEncontradaException.class,UsuarioNoEncontradoException.class}) public ResponseEntity<ProblemDetail> noEncontrado(RuntimeException e){return error(HttpStatus.NOT_FOUND,e.getMessage());}
 @ExceptionHandler({CuentaDuplicadaException.class,SaldoInsuficienteException.class,CuentaConSaldoException.class,CuentaInactivaException.class,DataIntegrityViolationException.class,ObjectOptimisticLockingFailureException.class}) public ResponseEntity<ProblemDetail> conflicto(RuntimeException e){return error(HttpStatus.CONFLICT,"Operación incompatible con el estado actual o datos duplicados");}
 @ExceptionHandler({IllegalArgumentException.class,MethodArgumentNotValidException.class,org.springframework.http.converter.HttpMessageNotReadableException.class}) public ResponseEntity<ProblemDetail> invalido(Exception e){return error(HttpStatus.BAD_REQUEST,"Datos de entrada inválidos");}
 @ExceptionHandler(Exception.class) public ResponseEntity<ProblemDetail> interno(Exception e){return error(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del servidor");}
}
