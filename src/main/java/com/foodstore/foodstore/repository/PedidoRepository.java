package com.foodstore.foodstore.repository;

import com.foodstore.foodstore.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Esto busca pedidos cuyo campo 'cliente' contenga la cadena (ignorando mayúsculas/minúsculas)
    List<Pedido> findByClienteContainingIgnoreCase(String cliente);
}
