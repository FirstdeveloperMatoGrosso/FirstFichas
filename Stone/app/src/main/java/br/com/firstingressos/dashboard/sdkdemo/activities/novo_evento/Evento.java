package br.com.firstingressos.dashboard.sdkdemo.activities.novo_evento;

public class Evento {
    String nome = "";
    String capa = "";
    String classificacao = "";
    String data = "";
    String descricao = "";
    String hora = "";
    String local = "";
    String status = "";
    String taxa = "";
    String valor = "";
    String email = "";
    String tipo = "";
    public Evento(String email, String capa, String classificacao, String data, String descricao, String hora, String local, String nome, String status, String taxa, String valor, String tipo) {
        this.nome = nome;
        this.capa = capa;
        this.classificacao = classificacao;
        this.data = data;
        this.descricao = descricao;
        this.hora = hora;
        this.local = local;
        this.status = status;
        this.taxa = taxa;
        this.valor = valor;
        this.email = email;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaxa() {
        return taxa;
    }

    public void setTaxa(String taxa) {
        this.taxa = taxa;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
