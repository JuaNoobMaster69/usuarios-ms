/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.security;
import com.pragma.usuarios.domain.spi.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
/**
 *
 * @author Juan Camilo
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encriptar(String claveSinCifrar) {
        return encoder.encode(claveSinCifrar);
    }

    @Override
    public boolean coinciden(String claveSinCifrar, String claveEncriptada) {
        return encoder.matches(claveSinCifrar, claveEncriptada);
    }
}