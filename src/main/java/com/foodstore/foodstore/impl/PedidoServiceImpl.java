package com.foodstore.foodstore.impl;

import com.foodstore.foodstore.entity.Pedido;
import com.foodstore.foodstore.repository.PedidoRepository;
import com.foodstore.foodstore.service.PedidoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
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
}
