package com.multisudoku.multisudoku.dto;

public class MovimientoDTO {
    private String jugadorId;
    private String jugadorNombre;
    private int fila;
    private int columna;
    private int valor;
    private boolean esValido;
    private int puntosOtorgados;
    private PartidaDTO partidaDTO;

    public MovimientoDTO() {}

    public MovimientoDTO(String jugadorId, String jugadorNombre, int fila, int columna, int valor, boolean esValido, int puntosOtorgados) {
        this.jugadorId = jugadorId;
        this.jugadorNombre = jugadorNombre;
        this.fila = fila;
        this.columna = columna;
        this.valor = valor;
        this.esValido = esValido;
        this.puntosOtorgados = puntosOtorgados;
    }

    // Getters y Setters
    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getJugadorNombre() {
        return jugadorNombre;
    }

    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
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

    public PartidaDTO getPartidaDTO() {
        return partidaDTO;
    }

    public void setPartidaDTO(PartidaDTO partidaDTO) {
        this.partidaDTO = partidaDTO;
    }

    @Override
    public String toString() {
        return "MovimientoDTO{" +
                "jugadorId='" + jugadorId + '\'' +
                ", jugadorNombre='" + jugadorNombre + '\'' +
                ", fila=" + fila +
                ", columna=" + columna +
                ", valor=" + valor +
                ", esValido=" + esValido +
                ", puntosOtorgados=" + puntosOtorgados +
                '}';
    }
}
