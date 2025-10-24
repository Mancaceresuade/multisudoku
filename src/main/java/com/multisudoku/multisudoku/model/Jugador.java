package com.multisudoku.multisudoku.model;

import java.time.LocalDateTime;

public class Jugador {
    private String id;
    private String nombre;
    private int puntuacion;
    private LocalDateTime fechaIngreso;
    private boolean conectado;

    public Jugador() {}

    public Jugador(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.puntuacion = 0;
        this.fechaIngreso = LocalDateTime.now();
        this.conectado = true;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public void incrementarPuntuacion(int puntos) {
        this.puntuacion += puntos;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", puntuacion=" + puntuacion +
                ", conectado=" + conectado +
                '}';
    }
}
