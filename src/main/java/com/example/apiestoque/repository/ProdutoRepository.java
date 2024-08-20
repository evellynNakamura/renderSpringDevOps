package com.example.apiestoque.repository;

import com.example.apiestoque.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
//    @Modifying
//    @Query("DELETE FROM Produto prod where :id = prod.id")
//    void deleteById(Long id);

    List<Produto> findByNomeLikeIgnoreCase(String nome);

    int countByQtdEstoqueIsLessThanEqual(int quant);

    void deleteByQtdEstoqueIsLessThanEqual(int quant);

    List <Produto> findByNomeLikeIgnoreCaseAndPrecoLessThan(String nome, double preco);
}
