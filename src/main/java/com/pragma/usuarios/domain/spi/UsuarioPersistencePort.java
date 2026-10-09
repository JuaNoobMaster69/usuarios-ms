/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.domain.spi;


import java.util.Optional;
import com.pragma.usuarios.domain.model.Usuario;

/**
 *
 * @author Juan Camilo
 */
public interface UsuarioPersistencePort {

    Usuario guardar(Usuario usuario);

    boolean existePorCorreo(String correo);

    boolean existePorDocumento(String documento);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorCorreo(String correo);
}