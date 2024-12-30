package br.com.firstingressos.dashboard.sdkdemo.activities.novoProduto;

public class Produto {

    String nome = "";
    String capa = "";
    String categoria = "";
    String descricao = "";
    String valor = "";
    String evento = "";

    public Produto(String categoria, String capa, String descricao, String nome, String valor, String evento) {
        this.nome = nome;
        this.capa = capa;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.evento = evento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }
}

