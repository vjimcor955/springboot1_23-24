package com.example.demo.controlador;

import com.example.demo.repos.ProductoRepositorio;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.modelo.Producto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/producto")
public class ProductoControlador {
    private final ProductoRepositorio productoRepositorio;

    public ProductoControlador(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    @GetMapping("/")
    List<Producto> getProductos()
    {
        return productoRepositorio.findAll();
    }

    @GetMapping("/{id}")
    Producto getProductoById(@PathVariable Long id)
    {
        return productoRepositorio.findById(id).orElse(null);
    }
    @PostMapping("/")
    public Producto createProducto(@Valid @RequestBody Producto producto) {
        return productoRepositorio.save(producto);
    }

    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoRepositorio.findById(id)
                .map(existingProducto -> {
                    existingProducto.setName(producto.getName());
                    existingProducto.setPrice(producto.getPrice());
                    return productoRepositorio.save(existingProducto);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Producto not found with id " + id));
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
}
