package com.example.apiestoque.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Schema(description = "Representa um produto no sistema")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //falando para gerar o id altomaticamente
    @Schema(description = "ÍD único do produto", example = "1234")
    private int id;

    @NotNull(message = "O nome não pode ser null")
    @Size(min = 2, message = "O id nome ter pelo menos 2 caracteres")
    @Schema(description = "Nome do produto", example = "Peito de frango")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Peito de frango congelado de 500g")
    private String descricao;

    @NotNull(message = "O preço não pode ser null")
    @Min(value = 0, message = "O preço deve ser pelo menos 0")
    @Schema(description = "Preço do produto", example = "1999.99")
    private double preco;

    @NotNull(message = "A quantidade não pode ser null")
    @Min(value = 0, message = "A quantidade deve ser pelo menos 0")
    @Column(name = "quantidadeestoque")
    @Schema(description = "Estoque do produto", example = "120")
    private int qtdEstoque;

    public int getId() {
        return this.id;
    }
    public String getNome() {
        return this.nome;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public double getPreco() {
        return this.preco;
    }
    public int getQtdEstoque() {
        return this.qtdEstoque;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }


    public Produto(int id, String nome, String descricao, double preco, int qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }

    public Produto(){

    }


    public String toString() {
        return "\nProduto " +
                "\n| ID: " + this.id +
                "\n| Nome: " + this.nome +
                "\n| Descricao: " + this.descricao +
                "\n| Preco: " + this.preco +
                "\n| Quantidade estoque: " + this.qtdEstoque;
    }
}
