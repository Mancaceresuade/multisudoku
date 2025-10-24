package com.multisudoku.multisudoku.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Partida {
    private String id;
    private Tablero tablero;
    private Map<String, Jugador> jugadores;
    private Map<String, Integer> puntuaciones;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean activa;
    private String estado; // "ESPERANDO", "EN_JUEGO", "COMPLETADA", "CANCELADA"
    private int maxJugadores;
    private String ganador;

    public Partida() {
        this.jugadores = new ConcurrentHashMap<>();
        this.puntuaciones = new ConcurrentHashMap<>();
        this.fechaInicio = LocalDateTime.now();
        this.activa = true;
        this.estado = "ESPERANDO";
        this.maxJugadores = 4; // Máximo 4 jugadores por partida
    }

    public Partida(String id, Tablero tablero) {
        this();
        this.id = id;
        this.tablero = tablero;
    }

    // Métodos para manejar jugadores
    public boolean agregarJugador(Jugador jugador) {
        if (jugadores.size() < maxJugadores && !jugadores.containsKey(jugador.getId())) {
            jugadores.put(jugador.getId(), jugador);
            puntuaciones.put(jugador.getId(), 0);
            
            // Si es el primer jugador, iniciar la partida
            if (jugadores.size() == 1) {
                estado = "EN_JUEGO";
            }
            return true;
        }
        return false;
    }

    public boolean removerJugador(String jugadorId) {
        if (jugadores.containsKey(jugadorId)) {
            jugadores.remove(jugadorId);
            puntuaciones.remove(jugadorId);
            
            // Si no quedan jugadores, cancelar partida
            if (jugadores.isEmpty()) {
                estado = "CANCELADA";
                activa = false;
                fechaFin = LocalDateTime.now();
            }
            return true;
        }
        return false;
    }

    public boolean procesarMovimiento(Movimiento movimiento) {
        if (!activa || !estado.equals("EN_JUEGO")) {
            return false;
        }

        // Validar el movimiento
        if (tablero.esMovimientoValido(movimiento.getFila(), movimiento.getColumna(), movimiento.getValor())) {
            // Aplicar el movimiento
            tablero.setValor(movimiento.getFila(), movimiento.getColumna(), movimiento.getValor());
            
            // Calcular puntos
            int puntos = calcularPuntos(movimiento);
            movimiento.setPuntosOtorgados(puntos);
            movimiento.setEsValido(true);
            
            // Actualizar puntuación del jugador
            Jugador jugador = jugadores.get(movimiento.getJugadorId());
            if (jugador != null) {
                jugador.incrementarPuntuacion(puntos);
                puntuaciones.put(movimiento.getJugadorId(), jugador.getPuntuacion());
            }
            
            // Agregar al historial
            tablero.agregarMovimiento(movimiento);
            
            // Verificar si la partida está completa
            if (tablero.isCompletado()) {
                finalizarPartida();
            }
            
            return true;
        }
        
        movimiento.setEsValido(false);
        movimiento.setPuntosOtorgados(0);
        return false;
    }

    private int calcularPuntos(Movimiento movimiento) {
        // Sistema de puntuación simple:
        // - Movimiento correcto: 10 puntos base
        // - Bonus por completar fila/columna/subgrid: 5 puntos adicionales
        
        int puntos = 10;
        
        // Verificar si se completó una fila
        if (esFilaCompleta(movimiento.getFila())) {
            puntos += 5;
        }
        
        // Verificar si se completó una columna
        if (esColumnaCompleta(movimiento.getColumna())) {
            puntos += 5;
        }
        
        // Verificar si se completó un subgrid
        if (esSubgridCompleto(movimiento.getFila(), movimiento.getColumna())) {
            puntos += 5;
        }
        
        return puntos;
    }

    private boolean esFilaCompleta(int fila) {
        for (int col = 0; col < Tablero.TAMANO; col++) {
            if (tablero.getValor(fila, col) == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean esColumnaCompleta(int columna) {
        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            if (tablero.getValor(fila, columna) == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean esSubgridCompleto(int fila, int columna) {
        int inicioFila = (fila / Tablero.SUBGRID_TAMANO) * Tablero.SUBGRID_TAMANO;
        int inicioColumna = (columna / Tablero.SUBGRID_TAMANO) * Tablero.SUBGRID_TAMANO;
        
        for (int f = inicioFila; f < inicioFila + Tablero.SUBGRID_TAMANO; f++) {
            for (int c = inicioColumna; c < inicioColumna + Tablero.SUBGRID_TAMANO; c++) {
                if (tablero.getValor(f, c) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void finalizarPartida() {
        estado = "COMPLETADA";
        activa = false;
        fechaFin = LocalDateTime.now();
        
        // Determinar ganador
        ganador = puntuaciones.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Map<String, Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(Map<String, Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Map<String, Integer> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(Map<String, Integer> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public int getNumeroJugadores() {
        return jugadores.size();
    }

    public boolean tieneEspacio() {
        return jugadores.size() < maxJugadores;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id='" + id + '\'' +
                ", estado='" + estado + '\'' +
                ", jugadores=" + jugadores.size() + "/" + maxJugadores +
                ", activa=" + activa +
                ", ganador='" + ganador + '\'' +
                '}';
    }
}
