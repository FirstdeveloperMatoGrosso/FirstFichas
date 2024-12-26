package br.com.firstingressos.dashboard.sdkdemo.activities.novaCategoria;

public class Categoria {

    String nome = "";
    String capa = "";

    public Categoria(String capa, String nome) {
        this.nome = nome;
        this.capa = capa;
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
}

