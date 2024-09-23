
package main.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import main.anotacao.ColunaTabela;
import main.anotacao.Tabela;
import main.anotacao.TipoChave;
import main.dao.Persistente;


@Tabela("TB_VENDA")
public class Venda implements Persistente {

    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;

        public static Status getByName(String value) {
            for (Status status : Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @ColunaTabela(dbName = "id", setJavaName = "setId")
    private Long id;

    @TipoChave("getCodigo")
    @ColunaTabela(dbName = "codigo", setJavaName = "setCodigo")
    private String codigo;

    @ColunaTabela(dbName = "id_cliente_fk", setJavaName = "setIdClienteFk")
    private Cliente cliente;

    //@ColunaTabela(dbName = "id", setJavaName = "setId")
    private Set<ProdutoQuantidade> produtos;

    @ColunaTabela(dbName = "valor_total", setJavaName = "setValorTotal")
    private BigDecimal valorTotal;

    @ColunaTabela(dbName = "data_venda", setJavaName = "setDataVenda")
    private Instant dataVenda;

    @ColunaTabela(dbName = "status_venda", setJavaName = "setStatus")
    private Status status;

    public Venda() {
        produtos = new HashSet<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<ProdutoQuantidade> getProdutos() {
        return produtos;
    }

    public void adicionarProduto(Produto produto, Integer quantidade) {
        validarStatus();
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getCodigo().equals(produto.getCodigo())).findAny();
        if (op.isPresent()) {
            ProdutoQuantidade produtpQtd = op.get();
            produtpQtd.adicionar(quantidade);
        } else {
            // Criar fabrica para criar ProdutoQuantidade
            ProdutoQuantidade prod = new ProdutoQuantidade();
            prod.setProduto(produto);
            prod.adicionar(quantidade);
            produtos.add(prod);
        }
        recalcularValorTotalVenda();
    }

    private void validarStatus() {
        if (this.status == Status.CONCLUIDA) {
            throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA");
        }
    }

    public void removerProduto(Produto produto, Integer quantidade) {
        validarStatus();
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getCodigo().equals(produto.getCodigo())).findAny();

        if (op.isPresent()) {
            ProdutoQuantidade produtpQtd = op.get();
            if (produtpQtd.getQuantidade()>quantidade) {
                produtpQtd.remover(quantidade);
                recalcularValorTotalVenda();
            } else {
                produtos.remove(op.get());
                recalcularValorTotalVenda();
            }

        }
    }

    public void removerTodosProdutos() {
        validarStatus();
        produtos.clear();
        valorTotal = BigDecimal.ZERO;
    }

    public Integer getQuantidadeTotalProdutos() {
        // Soma a quantidade getQuantidade() de todos os objetos ProdutoQuantidade
        int result = produtos.stream()
                .reduce(0, (partialCountResult, prod) -> partialCountResult + prod.getQuantidade(), Integer::sum);
        return result;
    }

    public void recalcularValorTotalVenda() {
        //validarStatus();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ProdutoQuantidade prod : this.produtos) {
            valorTotal = valorTotal.add(prod.getValorTotal());
        }
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public Instant getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Instant dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setProdutos(Set<ProdutoQuantidade> produtos) {
        this.produtos = produtos;
    }



}

//Cliente
//
///**
// *
// */
//package main.domain;
//
//import main.anotacao.ColunaTabela;
//import main.anotacao.Tabela;
//import main.anotacao.TipoChave;
//import main.dao.Persistente;
//
//
//@Tabela("TB_CLIENTE")
//public class Cliente implements Persistente {
//
//    @ColunaTabela(dbName = "id", setJavaName = "setId")
//    private Long id;
//
//    @ColunaTabela(dbName = "nome", setJavaName = "setNome")
//    private String nome;
//
//    @TipoChave("getCpf")
//    @ColunaTabela(dbName = "cpf", setJavaName = "setCpf")
//    private Long cpf;
//
//    @ColunaTabela(dbName = "tel", setJavaName = "setTel")
//    private Long tel;
//
//    @ColunaTabela(dbName = "endereco", setJavaName = "setEnd")
//    private String end;
//
//    @ColunaTabela(dbName = "numero", setJavaName = "setNumero")
//    private Integer numero;
//
//    @ColunaTabela(dbName = "cidade", setJavaName = "setCidade")
//    private String cidade;
//
//    @ColunaTabela(dbName = "estado", setJavaName = "setEstado")
//    private String estado;
//
//    public String getNome() {
//        return nome;
//    }
//    public void setNome(String nome) {
//        this.nome = nome;
//    }
//    public Long getCpf() {
//        return cpf;
//    }
//    public void setCpf(Long cpf) {
//        this.cpf = cpf;
//    }
//    public Long getTel() {
//        return tel;
//    }
//    public void setTel(Long tel) {
//        this.tel = tel;
//    }
//    public String getEnd() {
//        return end;
//    }
//    public void setEnd(String end) {
//        this.end = end;
//    }
//    public Integer getNumero() {
//        return numero;
//    }
//    public void setNumero(Integer numero) {
//        this.numero = numero;
//    }
//    public String getCidade() {
//        return cidade;
//    }
//    public void setCidade(String cidade) {
//        this.cidade = cidade;
//    }
//    public String getEstado() {
//        return estado;
//    }
//    public void setEstado(String estado) {
//        this.estado = estado;
//    }
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//
//
//
//
//}
//
//
//Persistence:
//
//<?xml version="1.0" encoding="UTF-8"?>
//<persistence version="2.2" xmlns="http://xmlns.jcp.org/xml/ns/persistence"
//             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
//
//    <persistence-unit name="ExemploJPA">
//
//        <provider>org.hibernate.ejb.HibernatePersistence</provider>
//
//        <class>main.domain.jpa.ClienteJpa</class>
//        <!-- <class>br.com.rpires.domain.Pessoa</class>
//        <class>br.com.rpires.domain.Endereco</class>
//        <class>br.com.rpires.domain.Curso</class>
//        <class>br.com.rpires.domain.Matricula</class>
//        <class>br.com.rpires.domain.Empregado</class>
//        <class>br.com.rpires.domain.Projeto</class> -->
//
//        <properties>
//            <property name="javax.persistence.jdbc.driver" value="org.postgresql.Driver" /> <!-- DB Driver -->
//            <property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/vendas_online" /> <!-- BD Mane -->
//            <property name="javax.persistence.jdbc.user" value="postgres" /> <!-- DB User -->
//            <property name="javax.persistence.jdbc.password" value="Nascimento" /> <!-- DB Password -->
//
//            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/> <!-- DB Dialect -->
//            <property name="hibernate.hbm2ddl.auto" value="update" /> <!-- create / create-drop / update -->
//
//            <property name="hibernate.show_sql" value="true" /> <!-- Show SQL in console -->
//            <property name="hibernate.format_sql" value="true" /> <!-- Show SQL formatted -->
//        </properties>
//
//    </persistence-unit>
//
//</persistence>
//
//
//Script de criação sql
//
//CREATE TABLE TB_CLIENTE (
//    id BIGINT PRIMARY KEY DEFAULT nextval('cliente_seq'),
//    cpf BIGINT NOT NULL,
//    nome VARCHAR(255) NOT NULL,
//    cidade VARCHAR(255) NOT NULL,
//    "end" VARCHAR(255) NOT NULL,
//    estado VARCHAR(2) NOT NULL,
//    numero INT NOT NULL,
//    tel BIGINT NOT NULL
//);
