package com.example.apiestoque.services;

import com.example.apiestoque.models.Produto;
import com.example.apiestoque.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> buscarTodosProdutos(){
        return produtoRepository.findAll();
    }

    @Transactional //Quando você vai fazer alguma alteração no banco
    public Produto salvarProduto(Produto produto){
        return produtoRepository.save(produto);
    }

    public Produto buscarProdutoPorId(Long id){
        return produtoRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Produto não encontrado"));
    }

    public List<Produto> buscarPorNome(String nome){
        return produtoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public List<Produto> buscarPorNomePreco(String nome, double preco){
        return produtoRepository.findByNomeLikeIgnoreCaseAndPrecoLessThan(nome, preco);
    }

    @Transactional
    public Produto excluirProduto(Long id){
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.deleteById(id);;
        return produto;
    }

    public int excluirPorQuant(int quant){
        int excluidos = produtoRepository.countByQtdEstoqueIsLessThanEqual(quant);
        if(excluidos > 0){
            produtoRepository.deleteByQtdEstoqueIsLessThanEqual(quant);
        }
        return excluidos;
    }

}
