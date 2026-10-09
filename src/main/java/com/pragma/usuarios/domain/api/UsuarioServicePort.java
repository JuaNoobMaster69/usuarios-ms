/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;

/**
 *
 * @author Juan Camilo
 */
public interface UsuarioServicePort {

    Usuario crearPropietario(Usuario usuario);

    Rol obtenerRol(Long id);

    Usuario login(String correo, String clave);
}
