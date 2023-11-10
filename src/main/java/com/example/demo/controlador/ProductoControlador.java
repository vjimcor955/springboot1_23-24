package com.example.demo.controlador;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.repos.ProductoRepositorio;
import com.example.demo.repos.UsuarioRepositorio;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.modelo.Producto;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/producto")
public class ProductoControlador {
    private final ProductoRepositorio productoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public ProductoControlador(ProductoRepositorio productoRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.productoRepositorio = productoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductoDTO>> getProductos() {
        List<ProductoDTO> resultado = new ArrayList<>();
        for (Producto producto : productoRepositorio.findAll()) {
            resultado.add(new ProductoDTO(producto));
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
    ResponseEntity<String> customHeader() {
        return ResponseEntity.ok()
                .header("Custom-Header", "foo")
                .body("Custom header set");
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<Producto>> getProductoById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productoRepositorio.findById(id));
    }
    @PostMapping("/")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto) {
        try {
            productoRepositorio.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado exitosamente");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Producto ya existente");
        }
    }

    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoRepositorio.findById(id)
            .map(existingProducto -> {
                existingProducto.setName(producto.getName());
                existingProducto.setPrice(producto.getPrice());
                return ResponseEntity.status(HttpStatus.OK).body(productoRepositorio.save(existingProducto));
            })
            .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id " + id)).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        return productoRepositorio.findById(id)
            .map(producto -> {
                productoRepositorio.delete(producto);
                return ResponseEntity.ok().build();
            })
            .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id " + id));
    }

    // Crear productos asociados a cliente
    @PostMapping("/{id}/productos")
    public Producto addProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoRepositorio.findById(id)
            .map(usuario -> {
                producto.setUsuario(usuario.getUsuario());
                return ResponseEntity.status(HttpStatus.OK).body(productoRepositorio.save(producto));
            })
            .orElseThrow(() -> new ResourceNotFoundException("Usuario not found with id " + id)).getBody();
    }

    @PutMapping("/{id}/productos/{productoId}")
    public Producto updateProducto(@PathVariable Long id, @PathVariable Long productoId, @Valid @RequestBody Producto productoRequest) {
        if(!usuarioRepositorio.existsById(id)) {
            throw new ResourceNotFoundException("Usuario not found with id " + id);
        }

        return productoRepositorio.findById(productoId)
            .map(producto -> {
                producto.setName(productoRequest.getName());
                producto.setPrice(productoRequest.getPrice());
                return ResponseEntity.status(HttpStatus.OK).body(productoRepositorio.save(producto));
            })
            .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id " + productoId)).getBody();
    }
    @GetMapping("/usuario/{usuarioId}")
    public List<Producto> getProductosByUsuario(@PathVariable Long usuarioId) {
        return productoRepositorio.findByUsuarioId(usuarioId);
    }

}
