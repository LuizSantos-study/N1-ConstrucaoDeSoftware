package luizsantos.com.sistemaecomerce.controller;

import luizsantos.com.sistemaecomerce.entity.*;
import luizsantos.com.sistemaecomerce.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    // Injeção de dependência por construtor conforme as boas práticas
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // POST /produtos: Cadastro de novos produtos
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvar(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    // GET /produtos: Listagem de todos os produtos (Catálogo)
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    // GET /produtos/{id}: Consulta de um produto específico
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    // PUT /produtos/{id}: Atualização de dados e estoque
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        // No Service, o método salvar/atualizar tratará a persistência
        // Aqui garantimos que o ID do caminho seja o mesmo do objeto
        produto.setId(id);
        Produto produtoAtualizado = produtoService.salvar(produto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    // DELETE /produtos/{id}: Remoção de produto do catálogo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
