/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.output.jpa.mapper;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author Juan Camilo
 */
@Component
public class UsuarioEntityMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setApellido(usuario.getApellido());
        entity.setDocumento(usuario.getDocumento());
        entity.setCelular(usuario.getCelular());
        entity.setFechaNacimiento(usuario.getFechaNacimiento());
        entity.setCorreo(usuario.getCorreo());
        entity.setClave(usuario.getClave());
        entity.setRol(usuario.getRol());
        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setNombre(entity.getNombre());
        usuario.setApellido(entity.getApellido());
        usuario.setDocumento(entity.getDocumento());
        usuario.setCelular(entity.getCelular());
        usuario.setFechaNacimiento(entity.getFechaNacimiento());
        usuario.setCorreo(entity.getCorreo());
        usuario.setClave(entity.getClave());
        usuario.setRol(entity.getRol());
        return usuario;
    }
}