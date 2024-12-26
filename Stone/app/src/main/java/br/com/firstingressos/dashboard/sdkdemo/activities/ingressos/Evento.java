package br.com.firstingressos.dashboard.sdkdemo.activities.ingressos;

public class Evento {
    String Image = "";
    String Name = " - ";
    String Address = " - ";
    String Valor = "R$ 0,00";
    String Taxa = "R$ 0,00";
    String Id = "";
    String Data = "";
    String Tipo = "";
    String Qtd = "";
    String NEvento = "";
    String Descricao = "";
    String Classificacao = "";
    String Area = "";

    public Evento(String image, String name, String address, String valor, String taxa, String id, String data, String tipo, String qtd, String evento, String descricao, String classificacao, String area) {
        Image = image;
        Name = name;
        Address = address;
        Valor = valor;
        Taxa = taxa;
        Id = id;
        Data = data;
        Tipo = tipo;
        Qtd = qtd;
        NEvento = evento;
        Descricao = descricao;
        Classificacao = classificacao;
        Area = area;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getTaxa() {
        return Taxa;
    }

    public void setTaxa(String taxa) {
        Taxa = taxa;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getQtd() {
        return Qtd;
    }

    public void setQtd(String qtd) {
        Qtd = qtd;
    }

    public String getEvento() {
        return NEvento;
    }

    public void setEvento(String evento) {
        NEvento = evento;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getClassificacao() {
        return Classificacao;
    }

    public void setClassificacao(String classificacao) {
        Classificacao = classificacao;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }
}
