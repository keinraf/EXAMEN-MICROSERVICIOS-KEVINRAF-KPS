package com.kps.kpsprestamoservice.Feing;
// Define el paquete donde se encuentra este cliente Feign que se comunica con el microservicio de usuarios

import com.kps.kpsprestamoservice.Dto.UsuarioDto;
// Importa la clase DTO que representa los datos del usuario que serán recibidos del microservicio

import org.springframework.cloud.openfeign.FeignClient;
// Anotación que indica que esta interfaz es un cliente Feign, que permite hacer llamadas HTTP declarativas

import org.springframework.http.ResponseEntity;
// Permite definir métodos que devuelven respuestas HTTP completas (estado, headers, cuerpo)

import org.springframework.web.bind.annotation.GetMapping;
// Anotación para declarar métodos que hacen peticiones HTTP GET

import org.springframework.web.bind.annotation.PathVariable;
// Anotación para extraer valores desde el path de la URL (por ejemplo: /usuarios/{id})

@FeignClient(name = "kps-usuario-service", path = "/usuarios")
// Declara que esta interfaz se comunica con el microservicio llamado "kps-usuario-service"
// Todas las rutas declaradas en esta interfaz usarán el prefijo "/usuarios"
public interface UsuarioFeign {

    @GetMapping("/{id}/estado")
        // Método que hace una petición GET a /usuarios/{id}/estado
        // Obtiene el estado actual del usuario con el ID especificado (por ejemplo, "ACTIVO", "INACTIVO")
    ResponseEntity<String> obtenerEstadoUsuario(@PathVariable Integer id);

    @GetMapping("/{id}")
        // Método que hace una petición GET a /usuarios/{id}
        // Obtiene toda la información del usuario con ese ID en forma de UsuarioDto
    ResponseEntity<UsuarioDto> obtenerUsuarioPorId(@PathVariable Integer id);
}
