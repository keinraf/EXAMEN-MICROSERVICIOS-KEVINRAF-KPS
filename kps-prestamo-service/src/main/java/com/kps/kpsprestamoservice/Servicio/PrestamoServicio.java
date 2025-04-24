package com.kps.kpsprestamoservice.Servicio;
// Paquete donde se define la interfaz de servicio de préstamos

import com.kps.kpsprestamoservice.Entidad.Prestamo;
// Se importa la entidad Prestamo que será usada en los métodos

import java.util.List;
import java.util.Optional;
// Se importan tipos genéricos para retornar listas y objetos opcionales

public interface PrestamoServicio {
    // Esta interfaz define todos los métodos que implementará el servicio de Prestamos

    List<Prestamo> getAllPrestamos();
    // Obtiene todos los préstamos registrados en el sistema

    Optional<Prestamo> getPrestamoById(Integer id);
    // Busca un préstamo por su ID y lo retorna en un Optional para evitar errores si no existe

    Prestamo savePrestamo(Prestamo prestamo);
    // Guarda un nuevo préstamo en la base de datos

    void deletePrestamo(Integer id);
    // Elimina un préstamo existente por su ID

    // --- Métodos para gestionar la lógica de negocio ---

    void procesarPrestamo(Prestamo prestamo);
    // Procesa un nuevo préstamo: valida stock, descuenta libros, registra préstamo

    void procesarDevolucion(Integer prestamoId);
    // Procesa la devolución de un préstamo: actualiza estado, fecha y stock

    Prestamo actualizar(Prestamo prestamo);
    // Actualiza un préstamo ya existente (por ejemplo, para modificar fechas o estado)

    // --- Métodos auxiliares para acceder a datos externos via Feign ---

    String obtenerTituloLibro(Integer libroId);
    // Consulta el título de un libro usando su ID (usando Feign al microservicio de libros)

    String obtenerNombreUsuario(Integer usuarioId);
    // Obtiene el nombre del usuario desde otro servicio

    String obtenerEstadoUsuario(Integer usuarioId);
    // Obtiene el estado del usuario (ej. ACTIVO, BLOQUEADO) desde el servicio de usuarios

    Optional<Prestamo> obtenerPrestamoConUsuarioPorId(Integer id);
    // Devuelve un préstamo junto con los datos del usuario (si se necesita enriquecer la info)

    Optional<Prestamo> obtenerPrestamoPorId(Integer id);
    // Método adicional que también busca préstamo por ID (posiblemente para otra lógica)

    Integer obtenerStockLibro(Integer libroId);
    // Obtiene el stock disponible de un libro específico

    void decrementarStockLibro(Integer libroId, Integer cantidad);
    // Resta una cantidad al stock del libro prestado

    // --- Métodos de filtrado y búsqueda por campos específicos ---

    List<Prestamo> getPrestamosByUsuarioId(Integer usuarioId);
    // Devuelve todos los préstamos realizados por un usuario específico

    List<Prestamo> getPrestamosByEstado(String estado);
    // Devuelve préstamos que tengan un estado específico (ej. ACTIVO, VENCIDO)

    List<Prestamo> getPrestamosVencidos();
    // Retorna préstamos cuya fecha de devolución ya pasó y aún no han sido devueltos
}
