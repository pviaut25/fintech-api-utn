package ar.edu.fintech.usuario.application;
import ar.edu.fintech.usuario.api.*;
import ar.edu.fintech.usuario.domain.Usuario;
import ar.edu.fintech.usuario.infrastructure.UsuarioRepository;
import ar.edu.fintech.shared.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service public class UsuarioService {
 private final UsuarioRepository repo;public UsuarioService(UsuarioRepository repo){this.repo=repo;}
 @Transactional public UsuarioResponse crear(CrearUsuarioRequest req){if(repo.existsByEmail(req.email().trim().toLowerCase(java.util.Locale.ROOT)))throw new CuentaDuplicadaException("Email ya registrado");return mapear(repo.save(new Usuario(req.nombre(),req.email())));}
 @Transactional(readOnly=true) public UsuarioResponse buscar(Long id){return mapear(repo.findById(id).orElseThrow(()->new UsuarioNoEncontradoException("Usuario no encontrado: "+id)));}
 private UsuarioResponse mapear(Usuario u){return new UsuarioResponse(u.getId(),u.getNombre(),u.getEmail());}
}
