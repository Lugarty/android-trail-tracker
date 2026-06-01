package com.example.atividaden2.database;

public class PontoTrilha {
    private int id;
    private int trilhaId;
    private double latitude;
    private double longitude;
    private String timestamp;

    public PontoTrilha() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTrilhaId() { return trilhaId; }
    public void setTrilhaId(int trilhaId) { this.trilhaId = trilhaId; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}