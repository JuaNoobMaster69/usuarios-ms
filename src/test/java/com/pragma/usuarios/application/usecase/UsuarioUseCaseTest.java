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
}