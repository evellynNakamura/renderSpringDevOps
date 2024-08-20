package com.example.apiestoque.controllers;

import com.example.apiestoque.models.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.example.apiestoque.services.ProdutoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //Rest: sinaliza que vai ser um retorno JSON | Controller: sinalizar que é um controler
@RequestMapping("/api/produtos") //indicando qual é a url padrão do nosso Controller
public class ProdutoController {

    private final ProdutoService produtoService;

    private final Validator validador;

    public ProdutoController(ProdutoService produtoService, Validator validador) {
        this.produtoService = produtoService;
        this.validador = validador;
    }

    @GetMapping("/selecionar") //indicando qual é a url padrão do nosso Controller
    @Operation(summary = "Lista todos os produtos",
            description = "Retorna uma lista de todos os produtos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content)
    })
    public List<Produto> listarProdutos() {
        return produtoService.buscarTodosProdutos();
    }


    @GetMapping("/buscarPorNome/{nome}") //indicando qual é a url padrão do nosso Controller
    @Operation(summary = "Lista todos os produtos que tenham o mesmo nome solicitado",
            description = "Retorna uma lista de todos os produtos que tenham o mesmo nome solicitado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public List<Produto> buscarPorNome(@Parameter(description = "ID do produto a ser inserido") @PathVariable String nome) {
        return produtoService.buscarPorNome(nome);
    }


    @GetMapping("/buscarPorNomePreco/{nome}/{preco}") //indicando qual é a url padrão do nosso Controller
    @Operation(summary = "Lista todos os produtos que tenham o mesmo nome e preço solicitado",
            description = "Retorna uma lista de todos os produtos que tenham o mesmo nome e preço solicitado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<?> buscarPorNomePreco(
            @Parameter(description = "ID e preço do produto a ser inserido")
            @PathVariable String nome, @PathVariable double preco) {

        List<Produto> produto = produtoService.buscarPorNomePreco(nome, preco);

        if(!produto.isEmpty()){
            return new ResponseEntity<>(produto, HttpStatus.OK);
        } else {
            return ResponseEntity.ok("Nenhum produto encontrado");
        }
    }


    @PostMapping("/inserir")
    @Operation(summary = "Insere um produto",
            description = "Insere um produto no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto inserido com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao inserir o produto",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody Produto produto,
                                                 BindingResult resultado) {
        if(resultado.hasErrors()){
            Map<String, String> erros = new HashMap<>();
            for (FieldError erro : resultado.getFieldErrors()) {
                // Coloque o nome do campo e a mensagem de erro no mapa
                erros.put(erro.getField(), erro.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros.toString());

        } else {
            produtoService.salvarProduto(produto);
            return ResponseEntity.ok("Produto inserido com sucesso");
        }
    }

    @DeleteMapping("/excluir/{id}")
    @Operation(summary = "Exclui um produto",
            description = "Exclui um produto no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto excluido com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir o produto",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<String> excluirProduto(
            @Parameter(description = "ID a ser inserido") @PathVariable long id) {

        if (produtoService.buscarProdutoPorId(id) != null) {
            produtoService.excluirProduto(id);
            return ResponseEntity.ok("Produto excluido com sucesso");

        } else {
            return ResponseEntity.ok("Produto não existe");
        }
    }


    @DeleteMapping("/excluirPorQuant/{quant}")
    @Operation(summary = "Exclui um produto de acondo com a quantidade solicitada",
            description = "Excui um produto no banco de acordo com a quantidade solicitado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto excluido com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir o produto",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<String> excluirPorQuant(
            @Parameter(description = "Quantidade a ser inserida") @PathVariable int quant) {
        int excluidos = produtoService.excluirPorQuant(quant);

        if(excluidos > 0){
            return ResponseEntity.ok("Produto excluido");
        } else {
            return ResponseEntity.ok("Não possui nenhum produto com esse estoque!");
        }
    }


    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualiza um produto de acordo com o id",
            description = "Atualiza um produto no banco de acordo com o id solicitado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto inserido com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar o produto",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<String> atualizarProduto(@Parameter(description = "ID a ser inserido")
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody Produto produtoAtualizado,
                                                   BindingResult resultado) {
        if (resultado.hasErrors()){
            Map<String, String> erros = new HashMap<>();
            for (FieldError erro : resultado.getFieldErrors()) {
                // Coloque o nome do campo e a mensagem de erro no mapa
                erros.put(erro.getField(), erro.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros.toString());

        } else {
            Produto produtoExistente = produtoService.buscarProdutoPorId(id);

            Produto produto = produtoExistente;
            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQtdEstoque(produtoAtualizado.getQtdEstoque());
            produtoService.salvarProduto(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso");
        }
    }


    @PatchMapping("atualizarParcial/{id}")
    @Operation(summary = "Atualiza um produto de acordo com o id",
            description = "Atualiza um produto no banco de acordo com o id solicitado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar o produto",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<String> atualizarProdutoParcial(@Parameter(description = "ID a ser inserido")
                                                          @PathVariable Long id,
                                                          @RequestBody Map<String, Object> updates) {
        Produto produtoExistente = produtoService.buscarProdutoPorId(id);

        try {
            Produto produto = produtoExistente;

            //Atualiza apenas os campos que foram passados no corpo da requisição
            if (updates.containsKey("nome")) {
                produto.setNome((String) updates.get("nome"));
            }
            if (updates.containsKey("descricao")) {
                produto.setDescricao((String) updates.get("descricao"));
            }

            if (updates.containsKey("preco")) {
                produto.setPreco((Double) updates.get("preco"));
            }

            if (updates.containsKey("quantidadeEstoque")) {
                produto.setQtdEstoque((Integer) updates.get("quantidadeEstoque"));
            }

            //Validando os dados
            DataBinder binder =  new DataBinder(produto); //vincula o DataBinder ao produto
            binder.setValidator(validador); //configura o validador
            binder.validate(); // executa o validado no objeto vinculado
            BindingResult resultado = binder.getBindingResult();
            if (resultado.hasErrors()){
                Map<String, String> erros = new HashMap<>();
                for (FieldError erro : resultado.getFieldErrors()) {
                    // Coloque o nome do campo e a mensagem de erro no mapa
                    erros.put(erro.getField(), erro.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros.toString());
            }
            Produto produtoSalvo = produtoService.salvarProduto(produto);
            return ResponseEntity.ok(String.valueOf(produtoSalvo));

        } catch (RuntimeException re){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
    }

}