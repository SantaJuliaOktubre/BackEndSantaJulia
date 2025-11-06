package com.foodstore.foodstore.impl;

import com.foodstore.foodstore.entity.Pedido;
import com.foodstore.foodstore.entity.Product;
import com.foodstore.foodstore.repository.PedidoRepository;
import com.foodstore.foodstore.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodstore.foodstore.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProductRepository productRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productRepository = productRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public Pedido crearPedido(Pedido pedido) throws Exception {
        // Parsear itemsJson
        List<CartItem> items = objectMapper.readValue(pedido.getItemsJson(), new TypeReference<List<CartItem>>() {});

        // Validar stock
        for (CartItem item : items) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalStateException("Producto no encontrado: " + item.getProductId()));
            if (p.getStock() < item.getQty()) {
                throw new IllegalStateException("Stock insuficiente para: " + p.getName());
            }
        }

        // Reducir stock
        for (CartItem item : items) {
            Product p = productRepository.findById(item.getProductId()).get();
            p.setStock(p.getStock() - item.getQty());
            productRepository.save(p);
        }

        pedido.setEstado("recibido"); // Estado inicial
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> obtenerPedido(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public List<Pedido> obtenerPorCliente(String cliente) {
        if (cliente == null || cliente.isBlank()) return pedidoRepository.findAll();
        return pedidoRepository.findByClienteContainingIgnoreCase(cliente);
    }

    // Clase interna para mapear itemsJson
    public static class CartItem {
        private Long productId;
        private Integer qty;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQty() { return qty; }
        public void setQty(Integer qty) { this.qty = qty; }
    }
}
