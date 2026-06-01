package com.example.atividaden2.database;

public class Trilha {
    private int id;
    private String nome;
    private String dataInicio;
    private String dataFim;
    private double velocidadeMaxima;
    private double velocidadeMedia;
    private double distanciaTotal;

    public Trilha() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
    public String getDataFim() { return dataFim; }
    public void setDataFim(String dataFim) { this.dataFim = dataFim; }
    public double getVelocidadeMaxima() { return velocidadeMaxima; }
    public void setVelocidadeMaxima(double velocidadeMaxima) { this.velocidadeMaxima = velocidadeMaxima; }
    public double getVelocidadeMedia() { return velocidadeMedia; }
    public void setVelocidadeMedia(double velocidadeMedia) { this.velocidadeMedia = velocidadeMedia; }
    public double getDistanciaTotal() { return distanciaTotal; }
    public void setDistanciaTotal(double distanciaTotal) { this.distanciaTotal = distanciaTotal; }
}