package ar.edu.fintech.cuenta.domain;
import ar.edu.fintech.usuario.domain.Usuario;
import jakarta.persistence.*;
@Entity @Table(name="cuentas_ahorro") public class CuentaAhorro extends Cuenta {
 protected CuentaAhorro(){} public CuentaAhorro(String numero,String alias,String moneda,Usuario titular){super(numero,alias,moneda,titular);}
}
