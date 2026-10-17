package ar.edu.fintech.cuenta.api;
import jakarta.validation.constraints.*;
public record CrearCuentaRequest(@NotBlank String numero,@NotBlank String alias,@NotBlank @Pattern(regexp="[A-Z]{3}") String moneda,@NotNull @Positive Long usuarioId){}
