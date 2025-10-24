package com.multisudoku.multisudoku.model;

import java.time.LocalDateTime;

public class Movimiento {
    private String id;
    private String jugadorId;
    private int fila;
    private int columna;
    private int valor;
    private LocalDateTime timestamp;
    private boolean esValido;
    private int puntosOtorgados;

    public Movimiento() {}

    public Movimiento(String id, String jugadorId, int fila, int columna, int valor) {
        this.id = id;
        this.jugadorId = jugadorId;
        this.fila = fila;
        this.columna = columna;
        this.valor = valor;
        this.timestamp = LocalDateTime.now();
        this.esValido = false; // Se valida después
        this.puntosOtorgados = 0;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEsValido() {
        return esValido;
    }

    public void setEsValido(boolean esValido) {
        this.esValido = esValido;
    }

    public int getPuntosOtorgados() {
        return puntosOtorgados;
    }

    public void setPuntosOtorgados(int puntosOtorgados) {
        this.puntosOtorgados = puntosOtorgados;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id='" + id + '\'' +
                ", jugadorId='" + jugadorId + '\'' +
                ", fila=" + fila +
                ", columna=" + columna +
                ", valor=" + valor +
                ", esValido=" + esValido +
                ", puntosOtorgados=" + puntosOtorgados +
                '}';
    }
}
