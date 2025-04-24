package com.kps.kpsprestamoservice.Feing; // Paquete donde se encuentra el cliente Feign

import com.kps.kpsprestamoservice.Dto.LibroDto; // Importa el DTO del libro que se usará para recibir datos del otro microservicio
import org.springframework.cloud.openfeign.FeignClient; // Anotación para declarar el cliente Feign
import org.springframework.http.ResponseEntity; // Permite manejar respuestas HTTP con cuerpo, headers y estado
import org.springframework.web.bind.annotation.GetMapping; // Anotación para peticiones GET
import org.springframework.web.bind.annotation.PathVariable; // Anotación para variables en la ruta (ej. /libros/{id})
import org.springframework.web.bind.annotation.PutMapping; // Anotación para peticiones PUT
import org.springframework.web.bind.annotation.RequestParam; // Anotación para parámetros en la URL (?cantidad=2, etc.)

@FeignClient(name = "kps-libro-service", path = "/libros")
// Declara que esta interfaz se conecta con el microservicio llamado "kps-libro-service"
// El prefijo común de todas las rutas será "/libros"
public interface LibroFeign {

    @GetMapping("/{id}/stock")
        // Llama al endpoint GET /libros/{id}/stock del microservicio de libros
        // Retorna el stock actual del libro con ID dado
    ResponseEntity<Integer> obtenerStockLibro(@PathVariable Integer id);

    @PutMapping("/{id}/decrementarStock")
        // Llama al endpoint PUT /libros/{id}/decrementarStock
        // Disminuye el stock del libro con ID dado según la cantidad indicada
    ResponseEntity<Void> decrementarStockLibro(@PathVariable Integer id, @RequestParam Integer cantidad);

    @GetMapping("/{id}")
        // Llama al endpoint GET /libros/{id}
        // Retorna la información detallada de un libro en forma de DTO
    ResponseEntity<LibroDto> obtenerLibroPorId(@PathVariable Integer id);
}
