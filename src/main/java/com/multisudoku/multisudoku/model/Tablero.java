package com.multisudoku.multisudoku.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private String id;
    private int[][] celdas;
    private int[][] celdasIniciales; // Para mantener las pistas originales
    private LocalDateTime fechaCreacion;
    private boolean completado;
    private List<Movimiento> historialMovimientos;

    // Constantes del Sudoku
    public static final int TAMANO = 9;
    public static final int SUBGRID_TAMANO = 3;

    public Tablero() {
        this.celdas = new int[TAMANO][TAMANO];
        this.celdasIniciales = new int[TAMANO][TAMANO];
        this.fechaCreacion = LocalDateTime.now();
        this.completado = false;
        this.historialMovimientos = new ArrayList<>();
    }

    public Tablero(String id) {
        this();
        this.id = id;
        // Inicializar tablero vacío
        inicializarTablero();
    }

    private void inicializarTablero() {
        // Inicializar con ceros (celdas vacías)
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                celdas[i][j] = 0;
                celdasIniciales[i][j] = 0;
            }
        }
    }

    // Métodos para manejar celdas
    public int getValor(int fila, int columna) {
        if (esPosicionValida(fila, columna)) {
            return celdas[fila][columna];
        }
        return -1; // Valor inválido
    }

    public boolean setValor(int fila, int columna, int valor) {
        if (esPosicionValida(fila, columna) && esValorValido(valor) && !esCeldaInicial(fila, columna)) {
            celdas[fila][columna] = valor;
            verificarCompletado();
            return true;
        }
        return false;
    }

    public boolean esCeldaInicial(int fila, int columna) {
        if (esPosicionValida(fila, columna)) {
            return celdasIniciales[fila][columna] != 0;
        }
        return false;
    }

    public void setCeldaInicial(int fila, int columna, int valor) {
        if (esPosicionValida(fila, columna) && esValorValido(valor)) {
            celdasIniciales[fila][columna] = valor;
            celdas[fila][columna] = valor;
        }
    }

    // Validaciones
    private boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO;
    }

    private boolean esValorValido(int valor) {
        return valor >= 0 && valor <= TAMANO;
    }

    public boolean esMovimientoValido(int fila, int columna, int valor) {
        if (!esPosicionValida(fila, columna) || !esValorValido(valor) || esCeldaInicial(fila, columna)) {
            return false;
        }

        // Si el valor es 0, se permite borrar la celda
        if (valor == 0) {
            return true;
        }

        // Verificar fila
        for (int c = 0; c < TAMANO; c++) {
            if (c != columna && celdas[fila][c] == valor) {
                return false;
            }
        }

        // Verificar columna
        for (int f = 0; f < TAMANO; f++) {
            if (f != fila && celdas[f][columna] == valor) {
                return false;
            }
        }

        // Verificar subgrid 3x3
        int inicioFila = (fila / SUBGRID_TAMANO) * SUBGRID_TAMANO;
        int inicioColumna = (columna / SUBGRID_TAMANO) * SUBGRID_TAMANO;

        for (int f = inicioFila; f < inicioFila + SUBGRID_TAMANO; f++) {
            for (int c = inicioColumna; c < inicioColumna + SUBGRID_TAMANO; c++) {
                if ((f != fila || c != columna) && celdas[f][c] == valor) {
                    return false;
                }
            }
        }

        return true;
    }

    private void verificarCompletado() {
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                if (celdas[i][j] == 0) {
                    completado = false;
                    return;
                }
            }
        }
        completado = true;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[][] getCeldas() {
        return celdas;
    }

    public void setCeldas(int[][] celdas) {
        this.celdas = celdas;
    }

    public int[][] getCeldasIniciales() {
        return celdasIniciales;
    }

    public void setCeldasIniciales(int[][] celdasIniciales) {
        this.celdasIniciales = celdasIniciales;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public List<Movimiento> getHistorialMovimientos() {
        return historialMovimientos;
    }

    public void setHistorialMovimientos(List<Movimiento> historialMovimientos) {
        this.historialMovimientos = historialMovimientos;
    }

    public void agregarMovimiento(Movimiento movimiento) {
        historialMovimientos.add(movimiento);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tablero ID: ").append(id).append("\n");
        sb.append("Completado: ").append(completado).append("\n");
        sb.append("Fecha creación: ").append(fechaCreacion).append("\n");
        return sb.toString();
    }
}
