/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.config;

import com.pragma.usuarios.application.usecase.UsuarioUseCase;
import com.pragma.usuarios.domain.api.UsuarioServicePort;
import com.pragma.usuarios.domain.spi.PasswordEncoderPort;
import com.pragma.usuarios.domain.spi.UsuarioPersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Juan Camilo
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public UsuarioServicePort usuarioServicePort(UsuarioPersistencePort persistencePort,
            PasswordEncoderPort passwordEncoderPort) {
        return new UsuarioUseCase(persistencePort, passwordEncoderPort);
    }
}
