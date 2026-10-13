/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.application.usecase;

import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.model.Rol;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.PasswordEncoderPort;
import com.pragma.usuarios.domain.spi.UsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * @author Juan Camilo
 */
@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioPersistencePort persistencePort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private UsuarioUseCase useCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setDocumento("123456789");
        usuario.setCelular("+573005698325");
        usuario.setFechaNacimiento(LocalDate.of(1990, 5, 20));
        usuario.setCorreo("juan@correo.com");
        usuario.setClave("secreta123");
    }

    // =========================================================
    // HU1: crearPropietario
    // =========================================================

    @Test
    void crearPropietario_conDatosValidos_guardaConRolPropietarioYClaveCifrada() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(false);
        when(passwordEncoderPort.encriptar("secreta123")).thenReturn("hash-bcrypt");
        when(persistencePort.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = useCase.crearPropietario(usuario);

        assertEquals(Rol.PROPIETARIO, resultado.getRol());
        assertEquals("hash-bcrypt", resultado.getClave());
        verify(persistencePort).guardar(usuario);
    }

    @Test
    void crearPropietario_sinNombre_lanzaExcepcion() {
        usuario.setNombre(" ");

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearPropietario(usuario));

        assertEquals("El nombre es obligatorio", ex.getMessage());
        verify(persistencePort, never()).guardar(any());
    }

    @Test
    void crearPropietario_sinFechaNacimiento_lanzaExcepcion() {
        usuario.setFechaNacimiento(null);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearPropietario(usuario));

        assertEquals("La fecha de nacimiento es obligatoria", ex.getMessage());
    }

    @Test
    void crearPropietario_conCorreoInvalido_lanzaExcepcion() {
        usuario.setCorreo("correo-sin-arroba");

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearPropietario(usuario));

        assertEquals("El correo no tiene un formato válido", ex.getMessage());
    }

    @Test
    void crearPropietario_conCelularInvalido_lanzaExcepcion() {
        usuario.setCelular("12345678901234");

        assertThrows(DomainException.class, () -> useCase.crearPropietario(usuario));
    }

    @Test
    void crearPropietario_conCelularConLetras_lanzaExcepcion() {
        usuario.setCelular("300abc1234");

        assertThrows(DomainException.class, () -> useCase.crearPropietario(usuario));
    }

    @Test
    void crearPropietario_conDocumentoConLetras_lanzaExcepcion() {
        usuario.setDocumento("12345abc");

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearPropietario(usuario));

        assertEquals("El documento debe contener solo números", ex.getMessage());
    }

    @Test
    void crearPropietario_siendoMenorDeEdad_lanzaExcepcion() {
        usuario.setFechaNacimiento(LocalDate.now().minusYears(15));

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearPropietario(usuario));

        assertEquals("El usuario debe ser mayor de edad", ex.getMessage());
    }

    @Test
    void crearPropietario_conCorreoExistente_lanzaExcepcion() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearPropietario(usuario));

        assertEquals("Ya existe un usuario con ese correo", ex.getMessage());
        verify(persistencePort, never()).guardar(any());
    }

    @Test
    void crearPropietario_conDocumentoExistente_lanzaExcepcion() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearPropietario(usuario));

        assertEquals("Ya existe un usuario con ese documento", ex.getMessage());
    }

    // =========================================================
    // HU6: crearEmpleado
    // =========================================================

    @Test
    void crearEmpleado_conDatosValidos_guardaConRolEmpleadoYClaveCifrada() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(false);
        when(passwordEncoderPort.encriptar("secreta123")).thenReturn("hash-bcrypt");
        when(persistencePort.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = useCase.crearEmpleado(usuario);

        assertEquals(Rol.EMPLEADO, resultado.getRol());
        assertEquals("hash-bcrypt", resultado.getClave());
        verify(persistencePort).guardar(usuario);
    }

    @Test
    void crearEmpleado_conCorreoExistente_lanzaExcepcion() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearEmpleado(usuario));

        assertEquals("Ya existe un usuario con ese correo", ex.getMessage());
        verify(persistencePort, never()).guardar(any());
    }

    @Test
    void crearEmpleado_conDocumentoExistente_lanzaExcepcion() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearEmpleado(usuario));

        assertEquals("Ya existe un usuario con ese documento", ex.getMessage());
    }

    @Test
    void crearEmpleado_sinNombre_lanzaExcepcion() {
        usuario.setNombre("");

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearEmpleado(usuario));

        assertEquals("El nombre es obligatorio", ex.getMessage());
    }

    // =========================================================
    // HU8: crearCliente
    // =========================================================

    @Test
    void crearCliente_conDatosValidos_guardaConRolClienteYClaveCifrada() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(false);
        when(passwordEncoderPort.encriptar("secreta123")).thenReturn("hash-bcrypt");
        when(persistencePort.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = useCase.crearCliente(usuario);

        assertEquals(Rol.CLIENTE, resultado.getRol());
        assertEquals("hash-bcrypt", resultado.getClave());
        verify(persistencePort).guardar(usuario);
    }

    @Test
    void crearCliente_conCorreoExistente_lanzaExcepcion() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearCliente(usuario));

        assertEquals("Ya existe un usuario con ese correo", ex.getMessage());
        verify(persistencePort, never()).guardar(any());
    }

    @Test
    void crearCliente_conDocumentoExistente_lanzaExcepcion() {
        when(persistencePort.existePorCorreo("juan@correo.com")).thenReturn(false);
        when(persistencePort.existePorDocumento("123456789")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearCliente(usuario));

        assertEquals("Ya existe un usuario con ese documento", ex.getMessage());
    }

    @Test
    void crearCliente_siendoMenorDeEdad_lanzaExcepcion() {
        usuario.setFechaNacimiento(LocalDate.now().minusYears(15));

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.crearCliente(usuario));

        assertEquals("El usuario debe ser mayor de edad", ex.getMessage());
    }

    // =========================================================
    // HU5: login
    // =========================================================

    @Test
    void login_conCredencialesValidas_devuelveUsuario() {
        usuario.setRol(Rol.CLIENTE);
        usuario.setClave("hash-bcrypt");
        when(persistencePort.buscarPorCorreo("juan@correo.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.coinciden("secreta123", "hash-bcrypt")).thenReturn(true);

        Usuario resultado = useCase.login("juan@correo.com", "secreta123");

        assertEquals("juan@correo.com", resultado.getCorreo());
        assertEquals(Rol.CLIENTE, resultado.getRol());
    }

    @Test
    void login_conCorreoInexistente_lanzaExcepcion() {
        when(persistencePort.buscarPorCorreo("noexiste@correo.com")).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.login("noexiste@correo.com", "clave"));

        assertEquals("Correo o clave incorrectos", ex.getMessage());
    }

    @Test
    void login_conClaveIncorrecta_lanzaExcepcion() {
        usuario.setClave("hash-bcrypt");
        when(persistencePort.buscarPorCorreo("juan@correo.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.coinciden("claveMala", "hash-bcrypt")).thenReturn(false);

        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.login("juan@correo.com", "claveMala"));

        assertEquals("Correo o clave incorrectos", ex.getMessage());
    }

    @Test
    void login_conCamposVacios_lanzaExcepcion() {
        DomainException ex = assertThrows(DomainException.class,
                () -> useCase.login("", ""));

        assertEquals("El correo y la clave son obligatorios", ex.getMessage());
    }
}