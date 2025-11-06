package com.foodstore.foodstore.impl;

import com.foodstore.foodstore.entity.Pedido;
import com.foodstore.foodstore.entity.Product;
import com.foodstore.foodstore.repository.PedidoRepository;
import com.foodstore.foodstore.repository.ProductRepository;
import com.foodstore.foodstore.service.PedidoService;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProductRepository productRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Pedido crearPedido(Pedido pedido) throws Exception {
        if (pedido.getItemsJson() == null || pedido.getItemsJson().isBlank()) {
            throw new Exception("El pedido debe contener items");
        }

        List<Map<String,Object>> items = objectMapper.readValue(pedido.getItemsJson(), List.class);

        for (Map<String,Object> it : items) {
            Integer prodId = (Integer) (it.get("productId") instanceof Integer ? it.get("productId") : ((Number)it.get("productId")).intValue());
            Integer qty = (Integer) (it.get("qty") instanceof Integer ? it.get("qty") : ((Number)it.get("qty")).intValue());

            Optional<Product> pOpt = productRepository.findById(Long.valueOf(prodId));
            if (pOpt.isEmpty()) throw new Exception("Producto no encontrado: " + prodId);
            Product p = pOpt.get();
            if (p.getStock() < qty) throw new Exception("Stock insuficiente para: " + p.getName());

            p.setStock(p.getStock() - qty);
            productRepository.save(p);
        }

        pedido.setEstado("recibido");
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
        return pedidoRepository.findByCliente(cliente);
    }
}
