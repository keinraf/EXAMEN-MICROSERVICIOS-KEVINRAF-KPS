package com.kps.kpslibroservice.Repositorio;

import com.kps.kpslibroservice.Entidad.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {
}
