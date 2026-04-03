package luizsantos.com.sistemaecomerce.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "TBL_PRODUTOS")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private Integer estoque;

    // Relacionamento 1:N com ItemPedido
    // Mapeado pelo atributo "produto" na entidade associativa ItemPedido
    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itens = new ArrayList<>();

    // Construtores
    public Produto() {
    }

    public Produto(Long id, String nome, Double preco, Integer estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}
