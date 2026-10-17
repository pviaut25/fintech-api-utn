package ar.edu.fintech.usuario.domain;
import jakarta.persistence.*;
import java.util.*;
import ar.edu.fintech.cuenta.domain.Cuenta;
@Entity @Table(name="usuarios", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String nombre;
 @Column(nullable=false,length=180) private String email;
 @OneToMany(mappedBy="titular") private List<Cuenta> cuentas=new ArrayList<>();
 protected Usuario() {}
 public Usuario(String nombre,String email) { if(nombre==null||nombre.isBlank()||email==null||email.isBlank()) throw new IllegalArgumentException("Nombre y email obligatorios");this.nombre=nombre.trim();this.email=email.trim().toLowerCase(Locale.ROOT); }
 public Long getId(){return id;} public String getNombre(){return nombre;} public String getEmail(){return email;}
}
