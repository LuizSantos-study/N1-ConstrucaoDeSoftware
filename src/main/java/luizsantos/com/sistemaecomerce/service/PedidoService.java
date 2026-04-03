package luizsantos.com.sistemaecomerce.service;

import jakarta.transaction.*;
import luizsantos.com.sistemaecomerce.entity.*;
import luizsantos.com.sistemaecomerce.repository.*;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;

    // Injeção de dependência por construtor
    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
    }

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        pedido.setData(LocalDateTime.now());
        pedido.setStatus(StatusPedido.CRIADO);

        for (ItemPedido item : pedido.getItens()) {
            Produto produtoOriginal = produtoService.buscarPorId(item.getProduto().getId());
            item.setPrecoUnitario(produtoOriginal.getPreco());
            item.setPedido(pedido);
            produtoService.subtrairEstoque(produtoOriginal.getId(), item.getQuantidade());
        }

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
    }

    @Transactional
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);

        // Regra 5: Fluxo de status (CRIADO -> PAGO -> ENVIADO)
        validarTransicaoStatus(pedido.getStatus(), novoStatus);

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        // Regra 4: Apenas pedidos com status CRIADO podem ser cancelados
        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new RuntimeException("Apenas pedidos com status CRIADO podem ser cancelados.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private void validarTransicaoStatus(StatusPedido atual, StatusPedido novo) {
        // Regra 5: Restrições de não pular etapas ou voltar status
        if (atual == StatusPedido.CRIADO && novo != StatusPedido.PAGO) {
            throw new RuntimeException("De CRIADO o pedido só pode ir para PAGO.");
        }
        if (atual == StatusPedido.PAGO && novo != StatusPedido.ENVIADO) {
            throw new RuntimeException("De PAGO o pedido só pode ir para ENVIADO.");
        }
        if (atual == StatusPedido.ENVIADO) {
            throw new RuntimeException("Pedido já enviado não pode ter o status alterado.");
        }
        if (atual == StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido cancelado não pode mudar de status.");
        }
    }
}
