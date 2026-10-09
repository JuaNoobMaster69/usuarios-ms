/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.input.rest.mapper;

import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.infrastructure.input.rest.dto.EmpleadoRequestDto;
import com.pragma.usuarios.infrastructure.input.rest.dto.UsuarioRequestDto;
import com.pragma.usuarios.infrastructure.input.rest.dto.UsuarioResponseDto;
import org.springframework.stereotype.Component;

/**
 *
 * @author Juan Camilo
 */
@Component
public class UsuarioDtoMapper {

    public Usuario toDomain(UsuarioRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDocumento(dto.getDocumento());
        usuario.setCelular(dto.getCelular());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(dto.getClave());
        return usuario;
    }

    public Usuario toDomain(EmpleadoRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setDocumento(dto.documento());
        usuario.setCelular(dto.celular());
        usuario.setFechaNacimiento(dto.fechaNacimiento());
        usuario.setCorreo(dto.correo());
        usuario.setClave(dto.clave());
        return usuario;
    }

    public UsuarioResponseDto toResponse(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setDocumento(usuario.getDocumento());
        dto.setCelular(usuario.getCelular());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getRol());
        return dto;
    }
}