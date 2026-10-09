/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.security;

import com.pragma.usuarios.domain.api.UsuarioServicePort;
import com.pragma.usuarios.domain.model.Usuario;
import org.springframework.stereotype.Service;
/**
 *
 * @author Juan Camilo
 */
@Service
public class AuthService {

    private final UsuarioServicePort usuarioServicePort;
    private final JwtService jwtService;

    public AuthService(UsuarioServicePort usuarioServicePort, JwtService jwtService) {
        this.usuarioServicePort = usuarioServicePort;
        this.jwtService = jwtService;
    }

    public String login(String correo, String clave) {
        Usuario usuario = usuarioServicePort.login(correo, clave);
        return jwtService.generarToken(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol().name());
    }
}