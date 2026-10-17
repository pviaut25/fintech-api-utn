package ar.edu.fintech.cuenta.domain;
import ar.edu.fintech.usuario.domain.Usuario;
import ar.edu.fintech.shared.exception.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
@Entity @Table(name="cuentas",uniqueConstraints=@UniqueConstraint(columnNames="numero")) @Inheritance(strategy=InheritanceType.JOINED)
public abstract class Cuenta implements Operable {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=40) private String numero;
 @Column(nullable=false,length=80) private String alias;
 @Column(nullable=false,precision=19,scale=2) private BigDecimal saldo=BigDecimal.ZERO.setScale(2);
 @Column(nullable=false,length=3) private String moneda;
 @Column(nullable=false) private boolean activa=true;
 @Version private Long version;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="usuario_id",nullable=false) private Usuario titular;
 protected Cuenta(){}
 protected Cuenta(String numero,String alias,String moneda,Usuario titular){this.numero=validarTexto(numero,"Número");this.alias=validarTexto(alias,"Alias");this.moneda=validarTexto(moneda,"Moneda");this.titular=Objects.requireNonNull(titular);}
 private static String validarTexto(String valor,String campo){if(valor==null||valor.isBlank())throw new IllegalArgumentException(campo+" obligatorio");return valor.trim();}
 public void cambiarAlias(String nuevo){verificarActiva();this.alias=validarTexto(nuevo,"Alias");}
 public void cerrar(){verificarActiva();if(saldo.signum()!=0)throw new CuentaConSaldoException("No se puede cerrar una cuenta con saldo distinto de cero");activa=false;}
 private void verificarActiva(){if(!activa)throw new CuentaInactivaException("La cuenta está inactiva");}
 private void validarMonto(BigDecimal monto){verificarActiva();if(monto==null||monto.signum()<=0||monto.scale()>2)throw new IllegalArgumentException("Monto positivo con máximo dos decimales requerido");}
 @Override public void depositar(BigDecimal monto){validarMonto(monto);saldo=saldo.add(monto).setScale(2);}
 @Override public void extraer(BigDecimal monto){validarMonto(monto);if(saldo.compareTo(monto)<0)throw new SaldoInsuficienteException("Saldo insuficiente");saldo=saldo.subtract(monto).setScale(2);}
 public Long getId(){return id;} public String getNumero(){return numero;} public String getAlias(){return alias;} public BigDecimal getSaldo(){return saldo;} public String getMoneda(){return moneda;} public boolean isActiva(){return activa;} public Usuario getTitular(){return titular;}
}
