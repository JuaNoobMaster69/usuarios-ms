/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.config;

import com.pragma.usuarios.application.usecase.UsuarioUseCase;
import com.pragma.usuarios.domain.api.UsuarioServicePort;
import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.PasswordEncoderPort;
import com.pragma.usuarios.domain.spi.UsuarioPersistencePort;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
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

    @Bean
    public CommandLineRunner crearAdministrador(UsuarioPersistencePort persistencePort,
                                                PasswordEncoderPort passwordEncoderPort) {
        return args -> {
            String correoAdmin = "admin@plazoleta.com";
            if (!persistencePort.existePorCorreo(correoAdmin)) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Plazoleta");
                admin.setDocumento("1000000000");
                admin.setCelular("+573000000000");
                admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
                admin.setCorreo(correoAdmin);
                admin.setClave(passwordEncoderPort.encriptar("Admin123*"));
                admin.setRol(Rol.ADMINISTRADOR);
                persistencePort.guardar(admin);
                System.out.println(">>> Administrador creado: " + correoAdmin);
            } else {
                System.out.println(">>> Administrador ya existe: " + correoAdmin);
            }
        };
    }
}