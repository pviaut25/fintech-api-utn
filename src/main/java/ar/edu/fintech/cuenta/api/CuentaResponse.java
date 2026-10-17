package ar.edu.fintech.cuenta.api;
import java.math.BigDecimal;
public record CuentaResponse(Long id,String numero,String alias,BigDecimal saldo,String moneda,boolean activa,Long usuarioId){}
