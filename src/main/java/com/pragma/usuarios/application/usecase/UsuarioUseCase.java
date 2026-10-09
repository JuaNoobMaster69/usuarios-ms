/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.domain.api.UsuarioServicePort;
import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.exception.UsuarioNoEncontradoException;
import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.PasswordEncoderPort;
import com.pragma.usuarios.domain.spi.UsuarioPersistencePort;

import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Juan Camilo
 */
public class UsuarioUseCase implements UsuarioServicePort {

    private static final String REGEX_CORREO = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String REGEX_CELULAR = "^\\+?\\d{1,12}$";
    private static final String REGEX_DOCUMENTO = "^\\d+$";
    private static final int EDAD_MINIMA = 18;

    private final UsuarioPersistencePort persistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public UsuarioUseCase(UsuarioPersistencePort persistencePort,
            PasswordEncoderPort passwordEncoderPort) {
        this.persistencePort = persistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public Usuario crearPropietario(Usuario usuario) {
        validarCamposObligatorios(usuario);
        validarCorreo(usuario.getCorreo());
        validarCelular(usuario.getCelular());
        validarDocumento(usuario.getDocumento());
        validarMayorDeEdad(usuario.getFechaNacimiento());

        if (persistencePort.existePorCorreo(usuario.getCorreo())) {
            throw new DomainException("Ya existe un usuario con ese correo");
        }
        if (persistencePort.existePorDocumento(usuario.getDocumento())) {
            throw new DomainException("Ya existe un usuario con ese documento");
        }

        usuario.setRol(Rol.PROPIETARIO);
        usuario.setClave(passwordEncoderPort.encriptar(usuario.getClave()));

        return persistencePort.guardar(usuario);
    }

    @Override
    public Rol obtenerRol(Long id) {
        return persistencePort.buscarPorId(id)
                .map(Usuario::getRol)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No existe un usuario con ese id"));
    }

    @Override
    public Usuario login(String correo, String clave) {
        if (esVacio(correo) || esVacio(clave)) {
            throw new DomainException("El correo y la clave son obligatorios");
        }

        Usuario usuario = persistencePort.buscarPorCorreo(correo)
                .orElseThrow(() -> new DomainException("Correo o clave incorrectos"));

        if (!passwordEncoderPort.coinciden(clave, usuario.getClave())) {
            throw new DomainException("Correo o clave incorrectos");
        }

        return usuario;
    }

    private void validarCamposObligatorios(Usuario u) {
        if (esVacio(u.getNombre())) {
            throw new DomainException("El nombre es obligatorio");
        }
        if (esVacio(u.getApellido())) {
            throw new DomainException("El apellido es obligatorio");
        }
        if (esVacio(u.getDocumento())) {
            throw new DomainException("El documento es obligatorio");
        }
        if (esVacio(u.getCelular())) {
            throw new DomainException("El celular es obligatorio");
        }
        if (u.getFechaNacimiento() == null) {
            throw new DomainException("La fecha de nacimiento es obligatoria");
        }
        if (esVacio(u.getCorreo())) {
            throw new DomainException("El correo es obligatorio");
        }
        if (esVacio(u.getClave())) {
            throw new DomainException("La clave es obligatoria");
        }
    }

    private void validarCorreo(String correo) {
        if (!correo.matches(REGEX_CORREO)) {
            throw new DomainException("El correo no tiene un formato válido");
        }
    }

    private void validarCelular(String celular) {
        if (!celular.matches(REGEX_CELULAR)) {
            throw new DomainException("El celular debe tener máximo 13 caracteres y puede iniciar con +");
        }
    }

    private void validarDocumento(String documento) {
        if (!documento.matches(REGEX_DOCUMENTO)) {
            throw new DomainException("El documento debe contener solo números");
        }
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < EDAD_MINIMA) {
            throw new DomainException("El usuario debe ser mayor de edad");
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}