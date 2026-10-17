package ar.edu.fintech.usuario.infrastructure;
import ar.edu.fintech.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{boolean existsByEmail(String email);}
