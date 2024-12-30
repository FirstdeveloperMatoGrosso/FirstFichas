package br.com.firstingressos.dashboard.sdkdemo.activities.novo_promotor;

import android.widget.Switch;

public class Promotor {
    String nome = "";
    String telefone = "";
    String estado = "";
    String cidade = "";
    String bairro = "";
    String rua = "";
    String numero = "";
    String cep = "";
    String ipTef = "";
    String ipPrint = "";
    Boolean ingressos = false;
    Boolean pulseiras = false;
    Boolean fichas = false;

    public Promotor(String nome, String telefone, String estado, String cidade, String bairro, String rua, String numero, String cep, String ipTef, String ipPrint, Boolean ingressos, Boolean pulseiras, Boolean fichas) {
        this.nome = nome;
        this.telefone = telefone;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.ipTef = ipTef;
        this.ipPrint = ipPrint;
        this.ingressos = ingressos;
        this.pulseiras = pulseiras;
        this.fichas = fichas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getIpTef() {
        return ipTef;
    }

    public void setIpTef(String ipTef) {
        this.ipTef = ipTef;
    }

    public String getIpPrint() {
        return ipPrint;
    }

    public void setIpPrint(String ipPrint) {
        this.ipPrint = ipPrint;
    }

    public Boolean getIngressps() {
        return ingressos;
    }

    public void setIngressos(Boolean ingressos) {
        this.ingressos = ingressos;
    }

    public Boolean getPulseiras() {
        return pulseiras;
    }

    public void setPulseiras(Boolean pulseiras) {
        this.pulseiras = pulseiras;
    }

    public Boolean getFichas() {
        return fichas;
    }

    public void setFichas(Boolean fichas) {
        this.fichas = fichas;
    }
}
