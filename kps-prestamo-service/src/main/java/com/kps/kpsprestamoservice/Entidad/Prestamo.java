package com.kps.kpsprestamoservice.Entidad; // Paquete donde se encuentra la clase

import jakarta.persistence.*; // Importa anotaciones necesarias para JPA (Hibernate)

import java.time.LocalDate; // Importa tipo de dato para fechas sin tiempo (día, mes, año)
import java.util.Map; // Importa el tipo de colección Map para almacenar pares clave-valor

@Entity // Indica que esta clase es una entidad JPA, es decir, será mapeada a una tabla en la base de datos
public class Prestamo {

    // ---------- GETTERS Y SETTERS (acceso y modificación de atributos) ----------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public Map<Integer, Integer> getLibrosPrestados() { return librosPrestados; }
    public void setLibrosPrestados(Map<Integer, Integer> librosPrestados) { this.librosPrestados = librosPrestados; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public LocalDate getFechaDevuelto() { return fechaDevuelto; }
    public void setFechaDevuelto(LocalDate fechaDevuelto) { this.fechaDevuelto = fechaDevuelto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // ---------- CONSTRUCTORES ----------

    // Constructor con parámetros
    public Prestamo(Integer id, Integer usuarioId, String usuarioNombre, Map<Integer, Integer> librosPrestados,
                    LocalDate fechaPrestamo, LocalDate fechaDevolucion, LocalDate fechaDevuelto, String estado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.librosPrestados = librosPrestados;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaDevuelto = fechaDevuelto;
        this.estado = estado;
    }

    // Constructor por defecto (necesario para JPA)
    public Prestamo() {
    }

    // ---------- MÉTODO TO STRING (para imprimir datos de la clase) ----------
    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", librosPrestados=" + librosPrestados +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", fechaDevuelto=" + fechaDevuelto +
                ", estado='" + estado + '\'' +
                '}';
    }

    // ---------- ATRIBUTOS (CAMPOS DE LA ENTIDAD) ----------

    @Id // Indica que este atributo es la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor será autogenerado por la BD (auto-incremental)
    private Integer id;

    @Column(nullable = false) // Este campo no puede ser nulo (obligatorio)
    private Integer usuarioId; // ID del usuario que realiza el préstamo

    private String usuarioNombre; // Nombre del usuario al momento del préstamo (no obligatorio, puede ser útil para mostrar info directamente)

    @ElementCollection // Indica que es una colección simple embebida en otra tabla
    @CollectionTable(name = "prestamo_libros", joinColumns = @JoinColumn(name = "prestamo_id"))
    // Define el nombre de la tabla que almacenará el Map, y la columna que relaciona con la entidad principal
    @MapKeyColumn(name = "libro_id") // Define el nombre de la columna que representará la clave del mapa (ID del libro)
    @Column(name = "cantidad") // Define el nombre de la columna para los valores del mapa (cantidad de libros)
    private Map<Integer, Integer> librosPrestados; // Mapa de ID de libro -> cantidad prestada

    @Column(nullable = false) // Campo obligatorio
    private LocalDate fechaPrestamo; // Fecha en la que se realiza el préstamo

    private LocalDate fechaDevolucion; // Fecha esperada para devolver el préstamo (puede ser null si no se fija)

    private LocalDate fechaDevuelto; // Fecha en que efectivamente se devolvió (null si aún no se devolvió)

    private String estado; // Estado del préstamo (ej. "pendiente", "devuelto", "vencido", etc.)
}
