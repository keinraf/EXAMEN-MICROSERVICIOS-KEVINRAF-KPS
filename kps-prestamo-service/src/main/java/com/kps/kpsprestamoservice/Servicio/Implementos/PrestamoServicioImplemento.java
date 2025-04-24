package com.kps.kpsprestamoservice.Servicio.Implementos;

import com.kps.kpsprestamoservice.Dto.UsuarioDto;
import com.kps.kpsprestamoservice.Entidad.Prestamo;
import com.kps.kpsprestamoservice.Feing.LibroFeign;
import com.kps.kpsprestamoservice.Feing.UsuarioFeign;
import com.kps.kpsprestamoservice.Repositorio.PrestamoRepositorio;
import com.kps.kpsprestamoservice.Servicio.PrestamoServicio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrestamoServicioImplemento implements PrestamoServicio {


    @Autowired
    private PrestamoRepositorio prk;
    @Autowired
    private LibroFeign libroFeign;
    @Autowired
    private UsuarioFeign usuarioFeign;

    @Override
    public List<Prestamo> getAllPrestamos() {
        return prk.findAll().stream()
                .map(this::cargarInformacionAdicional)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Prestamo> getPrestamoById(Integer id) {
        return prk.findById(id)
                .map(this::cargarInformacionAdicional);
    }

    @Override
    public Prestamo savePrestamo(Prestamo prestamo) {
        // 1. Verificar el stock de los libros
        if (prestamo.getLibrosPrestados() != null && !prestamo.getLibrosPrestados().isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : prestamo.getLibrosPrestados().entrySet()) {
                Integer libroId = entry.getKey();
                Integer cantidadPrestada = entry.getValue();
                Integer stockActual = obtenerStockLibro(libroId); // Asumiendo que tienes este método

                if (stockActual == null || stockActual < cantidadPrestada) {
                    throw new RuntimeException("No hay suficiente stock para el libro con ID: " + libroId);
                }
            }
        }

        // 2. Verificar si el usuario está activo
        String estadoUsuario = obtenerEstadoUsuario(prestamo.getUsuarioId()); // Asumiendo que tienes este método
        if (!"activo".equalsIgnoreCase(estadoUsuario)) {
            throw new RuntimeException("El usuario con ID: " + prestamo.getUsuarioId() + " no está activo y no puede realizar préstamos.");
        }

        // 3. Verificar el límite de préstamos activos del usuario
        List<Prestamo> prestamosActivosUsuario = prk.findByUsuarioIdAndEstado(prestamo.getUsuarioId(), "activo");
        if (prestamosActivosUsuario.size() >= 5) {
            throw new RuntimeException("El usuario con ID: " + prestamo.getUsuarioId() + " ha alcanzado el límite de 5 préstamos activos.");
        }

        // Si todas las verificaciones pasan, guardar el préstamo
        return prk.save(prestamo);
    }

    @Override
    public void deletePrestamo(Integer id) {
        prk.deleteById(id);
    }

    @Override
    public Prestamo actualizar(Prestamo prestamo) {
        Prestamo productoActualizado = null; // Inicializa para evitar el error de compilación
        Optional<Prestamo> prestamoExistenteOptional = prk.findById(prestamo.getId());
        if (prestamoExistenteOptional.isPresent()) {
            Prestamo prestamoExistente = prestamoExistenteOptional.get();
            // Actualiza los campos necesarios con la información de 'prestamo'
            prestamoExistente.setUsuarioId(prestamo.getUsuarioId());
            // No actualizamos usuarioNombre aquí, se obtiene dinámicamente
            prestamoExistente.setLibrosPrestados(prestamo.getLibrosPrestados());
            prestamoExistente.setFechaPrestamo(prestamo.getFechaPrestamo());
            prestamoExistente.setFechaDevolucion(prestamo.getFechaDevolucion());
            prestamoExistente.setFechaDevuelto(prestamo.getFechaDevuelto());
            prestamoExistente.setEstado(prestamo.getEstado());
            productoActualizado = prk.save(prestamoExistente);
        }
        return productoActualizado;
    }

    @Override
    public String obtenerTituloLibro(Integer libroId) {
        try {
            var response = libroFeign.obtenerLibroPorId(libroId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getTitulo();
            }
        } catch (Exception e) {
            return "Libro no encontrado o error al obtener el título";
        }
        return null;
    }

    @Override
    public String obtenerNombreUsuario(Integer usuarioId) {
        try {
            var responseUsuario = usuarioFeign.obtenerUsuarioPorId(usuarioId);
            if (responseUsuario.getStatusCode().is2xxSuccessful() && responseUsuario.getBody() != null) {
                return responseUsuario.getBody().getNombre();
            }
        } catch (Exception e) {
            return "Usuario no encontrado o error al obtener el nombre";
        }
        return null;
    }

    @Override
    public String obtenerEstadoUsuario(Integer usuarioId) {
        try {
            var responseUsuario = usuarioFeign.obtenerUsuarioPorId(usuarioId);
            if (responseUsuario.getStatusCode().is2xxSuccessful() && responseUsuario.getBody() != null) {
                return responseUsuario.getBody().getEstado();
            }
        } catch (Exception e) {
            return "Usuario no encontrado o error al obtener el nombre";
        }
        return null;
    }


    @Override
    public Optional<Prestamo> obtenerPrestamoConUsuarioPorId(Integer id) {
        return prk.findById(id)
                .map(prestamo -> {
                    try {
                        var responseUsuario = usuarioFeign.obtenerUsuarioPorId(prestamo.getUsuarioId());
                        if (responseUsuario.getStatusCode().is2xxSuccessful() && responseUsuario.getBody() != null) {
                            UsuarioDto usuarioDto = responseUsuario.getBody();
                            prestamo.setUsuarioNombre(usuarioDto.getNombre());
                        } else {
                            prestamo.setUsuarioNombre("Usuario no encontrado");
                        }
                    } catch (Exception e) {
                        prestamo.setUsuarioNombre("Error al obtener el usuario");
                    }
                    return prestamo;
                });
    }

    @Transactional
    @Override
    public void procesarPrestamo(Prestamo prestamo) {
        try {
            var responseUsuario = usuarioFeign.obtenerUsuarioPorId(prestamo.getUsuarioId());
            if (responseUsuario.getStatusCode().is2xxSuccessful() && responseUsuario.getBody() != null) {
                prestamo.setUsuarioNombre(responseUsuario.getBody().getNombre());
                prestamo.setFechaPrestamo(LocalDate.now());
                prestamo.setEstado("activo");
                Prestamo nuevoPrestamo = prk.save(prestamo);

                if (prestamo.getLibrosPrestados() != null && !prestamo.getLibrosPrestados().isEmpty()) {
                    for (Map.Entry<Integer, Integer> entry : prestamo.getLibrosPrestados().entrySet()) {
                        Integer libroId = entry.getKey();
                        Integer cantidad = entry.getValue();
                        try {
                            ResponseEntity<Void> responseStock = libroFeign.decrementarStockLibro(libroId, cantidad);
                            if (!responseStock.getStatusCode().is2xxSuccessful()) {
                                throw new RuntimeException("Error al decrementar stock del libro con ID: " + libroId + ". Código: " + responseStock.getStatusCode());
                            }
                        } catch (Exception e) {
                            System.err.println("Error al decrementar stock del libro ID: " + libroId + " - " + e.getMessage());
                            throw new RuntimeException("Error al procesar préstamo por problemas de stock.");
                            // Considerar rollback o compensación
                        }
                    }
                }
            } else {
                throw new RuntimeException("No se pudo obtener información del usuario ID: " + prestamo.getUsuarioId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar préstamo: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void procesarDevolucion(Integer prestamoId) {
        Optional<Prestamo> prestamoOptional = prk.findById(prestamoId);
        prestamoOptional.ifPresent(prestamo -> {
            if (prestamo.getFechaDevuelto() == null) {
                prestamo.setFechaDevuelto(LocalDate.now());
                prestamo.setEstado("devuelto");
                prk.save(prestamo);

                if (prestamo.getLibrosPrestados() != null && !prestamo.getLibrosPrestados().isEmpty()) {
                    for (Map.Entry<Integer, Integer> entry : prestamo.getLibrosPrestados().entrySet()) {
                        Integer libroId = entry.getKey();
                        Integer cantidad = entry.getValue();
                        try {
                            // Suponiendo endpoint para incrementar stock en el microservicio de libros
                            // ResponseEntity<Void> responseStock = libroFeign.incrementarStockLibro(libroId, cantidad);
                            System.out.println("Simulando incremento de stock para libro ID: " + libroId + ", Cantidad: " + cantidad);
                        } catch (Exception e) {
                            System.err.println("Error al incrementar stock del libro ID: " + libroId + " - " + e.getMessage());
                        }
                    }
                }
            }
        });
    }

    @Override
    public Optional<Prestamo> obtenerPrestamoPorId(Integer id) {
        return prk.findById(id)
                .map(this::cargarInformacionAdicional);
    }

    @Override
    public Integer obtenerStockLibro(Integer libroId) {
        try {
            ResponseEntity<Integer> response = libroFeign.obtenerStockLibro(libroId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el stock del libro con ID: " + libroId + " - " + e.getMessage());
            return null; // O manejar el error de otra manera
        }
        return null;
    }

    @Override
    public void decrementarStockLibro(Integer libroId, Integer cantidad) {
        try {
            ResponseEntity<Void> response = libroFeign.decrementarStockLibro(libroId, cantidad);
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("Error al decrementar el stock del libro con ID: " + libroId + ". Código: " + response.getStatusCode());
                // Puedes lanzar una excepción aquí si quieres que la operación falle
            }
        } catch (Exception e) {
            System.err.println("Error al decrementar el stock del libro con ID: " + libroId + " - " + e.getMessage());
            // Puedes lanzar una excepción aquí si quieres que la operación falle
        }
    }

    @Override
    public List<Prestamo> getPrestamosByUsuarioId(Integer usuarioId) {
        return prk.findByUsuarioId(usuarioId).stream()
                .map(this::cargarInformacionAdicional)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prestamo> getPrestamosByEstado(String estado) {
        return prk.findByEstado(estado).stream()
                .map(this::cargarInformacionAdicional)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prestamo> getPrestamosVencidos() {
        return prk.findByFechaDevolucionBeforeAndFechaDevueltoIsNull(LocalDate.now()).stream()
                .map(this::cargarInformacionAdicional)
                .collect(Collectors.toList());
    }

    private Prestamo cargarInformacionAdicional(Prestamo prestamo) {
        String usuarioNombre = obtenerNombreUsuarioDesdeFeign(prestamo.getUsuarioId());
        prestamo.setUsuarioNombre(usuarioNombre);
        return prestamo;
    }

    private String obtenerNombreUsuarioDesdeFeign(Integer usuarioId) {
        try {
            var responseUsuario = usuarioFeign.obtenerUsuarioPorId(usuarioId);
            if (responseUsuario.getStatusCode().is2xxSuccessful() && responseUsuario.getBody() != null) {
                return responseUsuario.getBody().getNombre();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el nombre del usuario con ID: " + usuarioId);
            return "Nombre de usuario no disponible"; // O algún otro valor por defecto
        }
        return null;
    }
}