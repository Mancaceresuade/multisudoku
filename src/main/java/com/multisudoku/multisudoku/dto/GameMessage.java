package com.multisudoku.multisudoku.dto;

public class GameMessage {
    private String tipo; // "MOVIMIENTO", "UNIRSE", "CREAR_PARTIDA", "ESTADO_PARTIDA", "PUNTUACIONES", "MENSAJE"
    private String jugadorId;
    private String partidaId;
    private Object datos;
    private String mensaje;
    private long timestamp;

    public GameMessage() {
        this.timestamp = System.currentTimeMillis();
    }

    public GameMessage(String tipo, String jugadorId, String partidaId, Object datos) {
        this();
        this.tipo = tipo;
        this.jugadorId = jugadorId;
        this.partidaId = partidaId;
        this.datos = datos;
    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(String partidaId) {
        this.partidaId = partidaId;
    }

    public Object getDatos() {
        return datos;
    }

    public void setDatos(Object datos) {
        this.datos = datos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GameMessage{" +
                "tipo='" + tipo + '\'' +
                ", jugadorId='" + jugadorId + '\'' +
                ", partidaId='" + partidaId + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
