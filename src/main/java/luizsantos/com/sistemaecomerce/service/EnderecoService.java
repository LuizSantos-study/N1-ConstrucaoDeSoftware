package luizsantos.com.sistemaecomerce.service;

import jakarta.transaction.*;
import luizsantos.com.sistemaecomerce.entity.*;
import luizsantos.com.sistemaecomerce.repository.*;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Endereco buscarPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com o ID: " + id));
    }

    @Transactional
    public Endereco atualizar(Long id, Endereco enderecoAtualizado) {
        Endereco enderecoExistente = buscarPorId(id);

        // Atualiza os campos conforme a modelagem do domínio
        enderecoExistente.setRua(enderecoAtualizado.getRua());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());
        enderecoExistente.setCep(enderecoAtualizado.getCep());

        return enderecoRepository.save(enderecoExistente);
    }

    @Transactional
    public void excluir(Long id) {
        Endereco endereco = buscarPorId(id);
        enderecoRepository.delete(endereco);
    }
}
