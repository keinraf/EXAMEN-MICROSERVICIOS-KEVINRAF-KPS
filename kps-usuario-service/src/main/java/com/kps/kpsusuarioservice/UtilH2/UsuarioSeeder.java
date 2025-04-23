package com.kps.kpsusuarioservice.UtilH2;

import com.kps.kpsusuarioservice.Entidad.Usuario;
import com.kps.kpsusuarioservice.Repositorio.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UsuarioSeeder implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioSeeder(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepositorio.count() == 0) {
            Usuario u1 = Usuario.builder()
                    .nombre("pedrito")
                    .correo("pedrito@example.com")
                    .tipoUsuario("estudiante")
                    .estadoUsuario("activo")
                    .carrera("contabilidad")
                    .build();

            Usuario u2 = Usuario.builder()
                    .nombre("elvis")
                    .correo("elvis@example.com")
                    .tipoUsuario("docente")
                    .estadoUsuario("activo")
                    .carrera("sistemas")
                    .build();

            usuarioRepositorio.save(u1);
            usuarioRepositorio.save(u2);

            System.out.println("Datos de Usuario insertados correctamente.");
        } else {
            System.out.println("Los Usuarios ya existen, no se insertaron datos.");
        }
    }
}