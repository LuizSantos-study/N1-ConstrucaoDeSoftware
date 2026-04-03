package luizsantos.com.sistemaecomerce.controller;

import luizsantos.com.sistemaecomerce.entity.*;
import luizsantos.com.sistemaecomerce.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos") // Rota no plural conforme sugerido
public class EnderecoController {

    private final EnderecoService enderecoService;

    // Injeção de dependência por construtor
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    // POST /enderecos: Cadastro de novo endereço
    @PostMapping
    public ResponseEntity<Endereco> cadastrar(@RequestBody Endereco endereco) {
        Endereco novoEndereco = enderecoService.salvar(endereco);
        return new ResponseEntity<>(novoEndereco, HttpStatus.CREATED);
    }

    // GET /enderecos: Listagem de todos os endereços
    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos() {
        List<Endereco> enderecos = enderecoService.listarTodos();
        return ResponseEntity.ok(enderecos);
    }

    // GET /enderecos/{id}: Busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id) {
        Endereco endereco = enderecoService.buscarPorId(id);
        return ResponseEntity.ok(endereco);
    }

    // PUT /enderecos/{id}: Atualização de endereço
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable Long id, @RequestBody Endereco endereco) {
        Endereco enderecoAtualizado = enderecoService.atualizar(id, endereco);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    // DELETE /enderecos/{id}: Exclusão de endereço
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        enderecoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}