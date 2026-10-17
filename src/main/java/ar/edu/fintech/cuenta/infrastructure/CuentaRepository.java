package ar.edu.fintech.cuenta.infrastructure;
import ar.edu.fintech.cuenta.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CuentaRepository extends JpaRepository<Cuenta,Long>{boolean existsByNumero(String numero);}
