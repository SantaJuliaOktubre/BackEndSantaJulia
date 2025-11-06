package com.foodstore.foodstore.service;

import com.foodstore.foodstore.entity.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido crearPedido(Pedido pedido) throws Exception;
    Optional<Pedido> obtenerPedido(Long id);
    void eliminarPedido(Long id);
    List<Pedido> obtenerPorCliente(String cliente);
}
