/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pragma.usuarios.infrastructure.output.jpa.adapter;
import com.pragma.usuarios.domain.model.Usuario;
import com.pragma.usuarios.domain.spi.UsuarioPersistencePort;
import com.pragma.usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import com.pragma.usuarios.infrastructure.output.jpa.mapper.UsuarioEntityMapper;
import com.pragma.usuarios.infrastructure.output.jpa.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 *
 * @author Juan Camilo
 */
@Component
public class UsuarioJpaAdapter implements UsuarioPersistencePort {

    private final UsuarioRepository repository;
    private final UsuarioEntityMapper mapper;

    public UsuarioJpaAdapter(UsuarioRepository repository, UsuarioEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity guardada = repository.save(mapper.toEntity(usuario));
        return mapper.toDomain(guardada);
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return repository.existsByCorreo(correo);
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}