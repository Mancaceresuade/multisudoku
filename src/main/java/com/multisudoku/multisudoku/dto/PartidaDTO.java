package com.multisudoku.multisudoku.dto;

import java.util.Map;

public class PartidaDTO {
    private String id;
    private int[][] tablero;
    private int[][] celdasIniciales;
    private Map<String, String> jugadores; // jugadorId -> nombre
    private Map<String, Integer> puntuaciones;
    private String estado;
    private boolean activa;
    private int numeroJugadores;
    private int maxJugadores;
    private String ganador;

    public PartidaDTO() {}

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public void setTablero(int[][] tablero) {
        this.tablero = tablero;
    }

    public int[][] getCeldasIniciales() {
        return celdasIniciales;
    }

    public void setCeldasIniciales(int[][] celdasIniciales) {
        this.celdasIniciales = celdasIniciales;
    }

    public Map<String, String> getJugadores() {
        return jugadores;
    }

    public void setJugadores(Map<String, String> jugadores) {
        this.jugadores = jugadores;
    }

    public Map<String, Integer> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(Map<String, Integer> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public int getNumeroJugadores() {
        return numeroJugadores;
    }

    public void setNumeroJugadores(int numeroJugadores) {
        this.numeroJugadores = numeroJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    @Override
    public String toString() {
        return "PartidaDTO{" +
                "id='" + id + '\'' +
                ", estado='" + estado + '\'' +
                ", numeroJugadores=" + numeroJugadores +
                ", maxJugadores=" + maxJugadores +
                ", activa=" + activa +
                ", ganador='" + ganador + '\'' +
                '}';
    }
}
