package com.depcue.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.depcue.model.Usuario;
import com.depcue.repository.IUsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin()
@RequestMapping("/usuarios")
public class RestUsuarioController {

    private final IUsuarioRepository repUsuario;

    @Autowired
    public RestUsuarioController(IUsuarioRepository repUsuario) {
        this.repUsuario = repUsuario;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = repUsuario.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(value = "/activos")
    public ResponseEntity<List<Usuario>> listarUsuariosActivos() {
        List<Usuario> usuarios = repUsuario.findByEstado("A");
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> recuperaUsuario(@PathVariable("id") Long id) {

        Optional<Usuario> usuarioOpt = repUsuario.findById(id);

        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Usuario> insertarUsuario(@RequestBody Usuario usuario) {

        Usuario usuarioNew = repUsuario.save(usuario);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuarioNew.getId())
                .toUri();

        return ResponseEntity.created(location).body(usuarioNew);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuario) {

        Optional<Usuario> usuarioOpt = repUsuario.findById(id);
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        usuario.setId(usuarioOpt.get().getId());
        repUsuario.save(usuario);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "duser/{id}")
    public ResponseEntity<Usuario> desabilitarUsuario(@PathVariable("id") Long id) {

        Optional<Usuario> usuarioOpt = repUsuario.findById(id);
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
//		Usuario useusuarioOpt.get().
        usuarioOpt.get().setEstado("D");
        repUsuario.save(usuarioOpt.get());

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "auser/{id}")
    public ResponseEntity<Usuario> habilitarUsuario(@PathVariable("id") Long id) {

        Optional<Usuario> usuarioOpt = repUsuario.findById(id);
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        usuarioOpt.get().setEstado("A");
        repUsuario.save(usuarioOpt.get());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("id") Long id) {
        repUsuario.deleteById(id);
        return new ResponseEntity<>("Usuario con ID :" + id + " eliminado correctamente", HttpStatus.OK);
    }

}
