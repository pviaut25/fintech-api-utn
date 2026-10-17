package ar.edu.fintech.cuenta.application;
import ar.edu.fintech.cuenta.api.*;
import ar.edu.fintech.cuenta.domain.*;
import ar.edu.fintech.cuenta.infrastructure.CuentaRepository;
import ar.edu.fintech.usuario.domain.Usuario;
import ar.edu.fintech.usuario.infrastructure.UsuarioRepository;
import ar.edu.fintech.shared.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
@Service public class CuentaService {
 private final CuentaRepository cuentas;private final UsuarioRepository usuarios;
 public CuentaService(CuentaRepository cuentas,UsuarioRepository usuarios){this.cuentas=cuentas;this.usuarios=usuarios;}
 @Transactional(readOnly=true) public List<CuentaResponse> listar(){return cuentas.findAll().stream().map(this::mapear).toList();}
 @Transactional(readOnly=true) public CuentaResponse buscar(Long id){return mapear(obtener(id));}
 @Transactional public CuentaResponse crear(CrearCuentaRequest req){if(cuentas.existsByNumero(req.numero()))throw new CuentaDuplicadaException("Número de cuenta ya registrado");Usuario titular=usuarios.findById(req.usuarioId()).orElseThrow(()->new UsuarioNoEncontradoException("Usuario no encontrado: "+req.usuarioId()));return mapear(cuentas.save(new CuentaAhorro(req.numero(),req.alias(),req.moneda(),titular)));}
 @Transactional public CuentaResponse actualizar(Long id,ActualizarCuentaRequest req){Cuenta cuenta=obtener(id);cuenta.cambiarAlias(req.alias());return mapear(cuenta);}
 @Transactional public void eliminar(Long id){obtener(id).cerrar();}
 @Transactional public CuentaResponse depositar(Long id,BigDecimal monto){Cuenta cuenta=obtener(id);cuenta.depositar(monto);return mapear(cuenta);}
 @Transactional public CuentaResponse extraer(Long id,BigDecimal monto){Cuenta cuenta=obtener(id);cuenta.extraer(monto);return mapear(cuenta);}
 private Cuenta obtener(Long id){return cuentas.findById(id).orElseThrow(()->new CuentaNoEncontradaException("Cuenta no encontrada: "+id));}
 private CuentaResponse mapear(Cuenta c){return new CuentaResponse(c.getId(),c.getNumero(),c.getAlias(),c.getSaldo(),c.getMoneda(),c.isActiva(),c.getTitular().getId());}
}
