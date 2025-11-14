package com.foodstore.foodstore.service;

import com.foodstore.foodstore.entity.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {

    /**
     * Crea un nuevo pedido, actualizando el stock de productos.
     */
    Pedido crearPedido(Pedido pedido) throws Exception;

    /**
     * Obtiene un pedido por su ID.
     */
    Optional<Pedido> obtenerPedido(Long id);

    /**
     * Elimina un pedido por su ID.
     */
    void eliminarPedido(Long id);

    /**
     * Obtiene los pedidos filtrados por cliente.
     * Si el parámetro está vacío, devuelve todos los pedidos.
     */
    List<Pedido> obtenerPorCliente(String cliente);

    /**
     * Actualiza solo el estado del pedido (por ejemplo: recibido, enviado, entregado, cancelado).
     */
    void actualizarEstado(Long id, String status) throws Exception;
}
