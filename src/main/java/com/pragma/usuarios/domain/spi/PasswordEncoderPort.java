/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.domain.spi;

/**
 *
 * @author Juan Camilo
 */
public interface PasswordEncoderPort {

    String encriptar(String claveSinCifrar);

    boolean coinciden(String claveSinCifrar, String claveEncriptada);
}