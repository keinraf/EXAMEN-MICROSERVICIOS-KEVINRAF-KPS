package com.kps.kpsprestamoservice.Repositorio;
// Define el paquete del repositorio para la entidad Prestamo

import com.kps.kpsprestamoservice.Entidad.Prestamo;
// Importa la clase Prestamo que se usará como entidad en este repositorio

import org.springframework.data.jpa.repository.JpaRepository;
// Importa JpaRepository, que provee métodos CRUD listos para usar y permite definir consultas personalizadas

import java.time.LocalDate;
import java.util.List;

public interface PrestamoRepositorio extends JpaRepository<Prestamo, Integer> {
// Declara una interfaz que extiende JpaRepository para la entidad Prestamo con clave primaria de tipo Integer

    int countByUsuarioIdAndEstado(Integer usuarioId, String estado);
    // Cuenta la cantidad de préstamos que tiene un usuario con un estado específico
    // Ejemplo: cantidad de préstamos "ACTIVO" para un usuario determinado

    List<Prestamo> findByUsuarioId(Integer usuarioId);
    // Busca todos los préstamos realizados por un usuario específico

    List<Prestamo> findByEstado(String estado);
    // Busca todos los préstamos que tienen un estado específico
    // Ejemplo: todos los préstamos "VENCIDO" o "ACTIVO"

    List<Prestamo> findByFechaDevolucionBeforeAndFechaDevueltoIsNull(LocalDate fechaDevolucion);
    // Busca préstamos cuya fecha de devolución ya pasó y aún no han sido devueltos
    // Útil para detectar préstamos vencidos

    List<Prestamo> findByUsuarioIdAndEstado(Integer usuarioId, String estado);
    // Busca préstamos de un usuario específico que estén en un estado determinado
    // Ejemplo: préstamos "ACTIVO" del usuario con ID 5
}
