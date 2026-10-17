package ar.edu.fintech.cuenta;
import ar.edu.fintech.cuenta.domain.CuentaAhorro;
import ar.edu.fintech.usuario.domain.Usuario;
import ar.edu.fintech.shared.exception.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class CuentaAhorroTest {
 private CuentaAhorro nueva(){return new CuentaAhorro("CTA-1","principal","ARS",new Usuario("Ana","ana@example.com"));}
 @Test void depositarYExtraer(){CuentaAhorro c=nueva();c.depositar(new BigDecimal("100.00"));c.extraer(new BigDecimal("30.00"));assertEquals(0,new BigDecimal("70.00").compareTo(c.getSaldo()));}
 @Test void saldoInsuficiente(){CuentaAhorro c=nueva();assertThrows(SaldoInsuficienteException.class,()->c.extraer(new BigDecimal("1.00")));}
 @Test void rechazaMontosInvalidos(){CuentaAhorro c=nueva();assertThrows(IllegalArgumentException.class,()->c.depositar(new BigDecimal("-1")));assertThrows(IllegalArgumentException.class,()->c.depositar(new BigDecimal("1.234")));}
 @Test void cierreLogico(){CuentaAhorro c=nueva();c.cerrar();assertFalse(c.isActiva());assertThrows(CuentaInactivaException.class,()->c.depositar(BigDecimal.ONE));}
 @Test void noCerrarConSaldo(){CuentaAhorro c=nueva();c.depositar(BigDecimal.TEN);assertThrows(CuentaConSaldoException.class,c::cerrar);}
}
