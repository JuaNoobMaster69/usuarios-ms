/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.input.rest.dto;

import java.time.LocalDate;

/**
 *
 * @author Juan Camilo
 */
public record ClienteRequestDto(
        String nombre,
        String apellido,
        String documento,
        String celular,
        LocalDate fechaNacimiento,
        String correo,
        String clave) {
}