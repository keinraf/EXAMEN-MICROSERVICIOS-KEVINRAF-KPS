package com.kps.kpsusuarioservice.Repositorio;

import com.kps.kpsusuarioservice.Entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}
