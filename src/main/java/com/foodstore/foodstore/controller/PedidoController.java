package com.foodstore.foodstore.controller;

import com.foodstore.foodstore.entity.Pedido;
import com.foodstore.foodstore.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(
        origins = "http://localhost:5174",
        allowedHeaders = {"Content-Type", "Authorization"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "true"
)
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        try {
            // Validaciones básicas
            if (pedido == null) {
                return ResponseEntity.badRequest().body("Payload de pedido vacío");
            }
            if (pedido.getItemsJson() == null || pedido.getItemsJson().isBlank()) {
                return ResponseEntity.badRequest().body("El carrito está vacío");
            }
            if (pedido.getSubtotal() == null || pedido.getSubtotal() <= 0
                    || pedido.getTotalItems() == null || pedido.getTotalItems() <= 0) {
                return ResponseEntity.badRequest().body("Subtotal o totalItems inválidos");
            }

            // Estado inicial
            pedido.setEstado("recibido");

            Pedido saved = pedidoService.crearPedido(pedido);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(saved.getId())
                    .toUri();

            return ResponseEntity.created(location).body(saved);

        } catch (IllegalStateException e) {
            logger.warn("Stock insuficiente para el pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Stock insuficiente: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error creando pedido", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        return pedidoService.obtenerPedido(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        try {
            pedidoService.eliminarPedido(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error eliminando pedido id=" + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Pedido>> listarPorCliente(@RequestParam(value = "cliente", required = false) String cliente) {
        try {
            String clienteParam = (cliente == null) ? "" : cliente;
            List<Pedido> resultados = pedidoService.obtenerPorCliente(clienteParam);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            logger.error("Error listando pedidos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
