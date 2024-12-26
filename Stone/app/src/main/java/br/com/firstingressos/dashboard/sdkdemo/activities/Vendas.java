package br.com.firstingressos.dashboard.sdkdemo.activities;

public class Vendas {
    String valor = "";
    String email = "";
    String data = "";
    String fluxoCaixa = "0";
    String tipo = "";
    String formaPagamento = "";
    String nome = "";

    public Vendas(String valor, String email, String data, String tipo, String formaPagamento, String fluxoCaixa, String nome) {
        this.valor = valor;
        this.email = email;
        this.data = data;
        this.tipo = tipo;
        this.formaPagamento = formaPagamento;
        this.fluxoCaixa = fluxoCaixa;
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFormaPagamento() {
        return formaPagamento;
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getFluxoCaixa() {
        return fluxoCaixa;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
}


