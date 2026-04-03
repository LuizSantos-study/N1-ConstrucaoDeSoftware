package luizsantos.com.sistemaecomerce.service;

import luizsantos.com.sistemaecomerce.entity.*;
import luizsantos.com.sistemaecomerce.repository.*;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    // Injeção por construtor conforme solicitado
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // CRUD Completo
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    public void excluir(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }

    // Regra 2: Controle de estoque
    public void subtrairEstoque(Long produtoId, Integer quantidade) {
        Produto produto = buscarPorId(produtoId);

        // Validar disponibilidade
        if (produto.getEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        // Subtrair estoque
        produto.setEstoque(produto.getEstoque() - quantidade);
        produtoRepository.save(produto);
    }
}
