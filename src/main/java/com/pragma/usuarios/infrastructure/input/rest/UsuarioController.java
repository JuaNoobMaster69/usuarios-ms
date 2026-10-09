/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.domain.api.UsuarioServicePort;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.infrastructure.input.rest.dto.LoginRequestDto;
import com.pragma.usuarios.infrastructure.input.rest.dto.LoginResponseDto;
import com.pragma.usuarios.infrastructure.input.rest.dto.UsuarioRequestDto;
import com.pragma.usuarios.infrastructure.input.rest.dto.UsuarioResponseDto;
import com.pragma.usuarios.infrastructure.input.rest.mapper.UsuarioDtoMapper;
import com.pragma.usuarios.infrastructure.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juan Camilo
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioServicePort servicePort;
    private final UsuarioDtoMapper mapper;
    private final AuthService authService;

    public UsuarioController(UsuarioServicePort servicePort,
                             UsuarioDtoMapper mapper,
                             AuthService authService) {
        this.servicePort = servicePort;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Operation(summary = "Crear un propietario (solo el administrador)")
    @PostMapping("/propietario")
    public ResponseEntity<UsuarioResponseDto> crearPropietario(@RequestBody UsuarioRequestDto request) {
        Usuario creado = servicePort.crearPropietario(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }

    @Operation(summary = "Consultar el rol de un usuario")
    @GetMapping("/{id}/rol")
    public ResponseEntity<String> obtenerRol(@PathVariable Long id) {
        return ResponseEntity.ok(servicePort.obtenerRol(id).name());
    }

    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String token = authService.login(request.correo(), request.clave());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}