package ar.edu.fintech.usuario.api;
import jakarta.validation.constraints.*;
public record CrearUsuarioRequest(@NotBlank String nombre,@NotBlank @Email String email){}
