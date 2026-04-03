package luizsantos.com.sistemaecomerce.service;

import luizsantos.com.sistemaecomerce.entity.*;
import luizsantos.com.sistemaecomerce.repository.*;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Injeção de dependência por construtor (Preferencial) [cite: 136]
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvar(Cliente cliente) {
        // Regra 1: Não permitir cadastro de clientes com emails duplicados [cite: 78, 79]
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Erro: Já existe um cliente cadastrado com este e-mail.");
        }
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarPorId(id);

        // Validação de e-mail na atualização (se o e-mail mudou, verifica se o novo já existe)
        if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail()) &&
                clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
            throw new RuntimeException("Erro: O novo e-mail já está em uso por outro cliente.");
        }

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());

        return clienteRepository.save(clienteExistente);
    }

    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
