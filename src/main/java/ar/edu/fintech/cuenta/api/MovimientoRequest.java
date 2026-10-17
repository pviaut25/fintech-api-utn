package ar.edu.fintech.cuenta.api;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record MovimientoRequest(@NotNull @DecimalMin("0.01") @Digits(integer=17,fraction=2) BigDecimal monto){}
