package com.kps.kpsusuarioservice.Controlador;

import com.kps.kpsusuarioservice.Entidad.Usuario;
import com.kps.kpsusuarioservice.Servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio kr;

    // Obtener todas las Usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuario() {
        List<Usuario> usuarios = kr.Listar();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Obtener una Usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarios = kr.Buscar(id);
        return usuarios.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva Usuario
    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuarios) {
        Usuario nuevousuarios = kr.Guardar(usuarios);
        return new ResponseEntity<>(nuevousuarios, HttpStatus.CREATED);
    }


    // Actualizar o modificar una Usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuarios) {
        Usuario usuariosModificado = kr.Modificar(id, usuarios);
        return usuariosModificado != null ? new ResponseEntity<>(usuariosModificado, HttpStatus.OK)
                : ResponseEntity.notFound().build();
    }

    // Eliminar una Usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> eliminarUsuario(@PathVariable Long id) {
        kr.Eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
