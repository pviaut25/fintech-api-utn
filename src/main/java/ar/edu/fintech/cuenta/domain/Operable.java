package ar.edu.fintech.cuenta.domain;
import java.math.BigDecimal;
public interface Operable { void depositar(BigDecimal monto); void extraer(BigDecimal monto); }
