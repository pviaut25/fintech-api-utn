package ar.edu.fintech.cuenta;
import ar.edu.fintech.cuenta.api.*;
import ar.edu.fintech.cuenta.application.CuentaService;
import ar.edu.fintech.cuenta.domain.*;
import ar.edu.fintech.cuenta.infrastructure.CuentaRepository;
import ar.edu.fintech.usuario.domain.Usuario;
import ar.edu.fintech.usuario.infrastructure.UsuarioRepository;
import ar.edu.fintech.shared.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class) class CuentaServiceTest {
 @Mock CuentaRepository cuentas;@Mock UsuarioRepository usuarios;@InjectMocks CuentaService service;
 @Test void cuentaInexistente(){when(cuentas.findById(99L)).thenReturn(Optional.empty());assertThrows(CuentaNoEncontradaException.class,()->service.buscar(99L));}
 @Test void crearDuplicada(){when(cuentas.existsByNumero("CTA-1")).thenReturn(true);assertThrows(CuentaDuplicadaException.class,()->service.crear(new CrearCuentaRequest("CTA-1","alias","ARS",1L)));verify(cuentas,never()).save(any());}
 @Test void actualizarAlias(){CuentaAhorro c=new CuentaAhorro("CTA-1","viejo","ARS",new Usuario("Ana","ana@example.com"));when(cuentas.findById(1L)).thenReturn(Optional.of(c));CuentaResponse r=service.actualizar(1L,new ActualizarCuentaRequest("nuevo"));assertEquals("nuevo",r.alias());}
 @Test void eliminarCuentaVacia(){CuentaAhorro c=new CuentaAhorro("CTA-1","alias","ARS",new Usuario("Ana","ana@example.com"));when(cuentas.findById(1L)).thenReturn(Optional.of(c));service.eliminar(1L);assertFalse(c.isActiva());}
 @Test void eliminarCuentaInexistente(){when(cuentas.findById(3L)).thenReturn(Optional.empty());assertThrows(CuentaNoEncontradaException.class,()->service.eliminar(3L));}
 @Test void cerrarConSaldoDebeFallar(){CuentaAhorro c=new CuentaAhorro("CTA-1","alias","ARS",new Usuario("Ana","ana@example.com"));c.depositar(new java.math.BigDecimal("10.00"));when(cuentas.findById(1L)).thenReturn(Optional.of(c));assertThrows(CuentaConSaldoException.class,()->service.eliminar(1L));assertTrue(c.isActiva());}
 @Test void actualizarInexistente(){when(cuentas.findById(44L)).thenReturn(Optional.empty());assertThrows(CuentaNoEncontradaException.class,()->service.actualizar(44L,new ActualizarCuentaRequest("alias")));}
 @Test void cuentaCerradaNoSePuedeActualizar(){CuentaAhorro c=new CuentaAhorro("CTA-1","alias","ARS",new Usuario("Ana","ana@example.com"));c.cerrar();when(cuentas.findById(1L)).thenReturn(Optional.of(c));assertThrows(CuentaInactivaException.class,()->service.actualizar(1L,new ActualizarCuentaRequest("otro")));}
 @Test void depositoAumentaSaldo(){CuentaAhorro c=new CuentaAhorro("CTA-1","alias","ARS",new Usuario("Ana","ana@example.com"));when(cuentas.findById(1L)).thenReturn(Optional.of(c));CuentaResponse r=service.depositar(1L,new java.math.BigDecimal("25.50"));assertEquals(0,new java.math.BigDecimal("25.50").compareTo(r.saldo()));}
 @Test void extraccionSinFondosDebeFallar(){CuentaAhorro c=new CuentaAhorro("CTA-1","alias","ARS",new Usuario("Ana","ana@example.com"));when(cuentas.findById(1L)).thenReturn(Optional.of(c));assertThrows(SaldoInsuficienteException.class,()->service.extraer(1L,new java.math.BigDecimal("5.00")));}
}
