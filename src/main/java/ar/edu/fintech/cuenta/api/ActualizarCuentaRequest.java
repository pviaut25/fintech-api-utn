package ar.edu.fintech.cuenta.api;
import jakarta.validation.constraints.NotBlank;
public record ActualizarCuentaRequest(@NotBlank String alias){}
