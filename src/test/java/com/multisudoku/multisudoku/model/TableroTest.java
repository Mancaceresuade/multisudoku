package com.multisudoku.multisudoku.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableroTest {

    @Test
    @DisplayName("TC-13: posicion fuera de rango - fila")
    void tc13_posicionFueraDeRango_fila() {
        Tablero tablero = new Tablero();

        assertFalse(tablero.esMovimientoValido(9, 0, 5));
        assertFalse(tablero.setValor(9, 0, 5));
    }

    @Test
    @DisplayName("TC-14: posicion fuera de rango - columna negativa")
    void tc14_posicionFueraDeRango_columnaNegativa() {
        Tablero tablero = new Tablero();

        assertFalse(tablero.esMovimientoValido(0, -1, 5));
        assertFalse(tablero.setValor(0, -1, 5));
    }

    @Test
    @DisplayName("TC-15: valor fuera de rango")
    void tc15_valorFueraDeRango() {
        Tablero tablero = new Tablero();

        assertFalse(tablero.esMovimientoValido(0, 0, 10));
        assertFalse(tablero.setValor(0, 0, 10));
    }

    @Test
    @DisplayName("TC-16: movimiento sobre celda inicial (pista)")
    void tc16_movimientoSobreCeldaInicial() {
        Tablero tablero = new Tablero();
        tablero.setCeldaInicial(0, 0, 5);

        assertTrue(tablero.esCeldaInicial(0, 0));
        assertFalse(tablero.esMovimientoValido(0, 0, 7));
        assertFalse(tablero.setValor(0, 0, 7));
        assertEquals(5, tablero.getValor(0, 0));
    }

    @Test
    @DisplayName("TC-17: borrar una celda no inicial ya completada")
    void tc17_borrarCeldaNoInicial() {
        Tablero tablero = new Tablero();
        tablero.setValor(2, 2, 7);

        assertTrue(tablero.esMovimientoValido(2, 2, 0));
        assertTrue(tablero.setValor(2, 2, 0));
        assertEquals(0, tablero.getValor(2, 2));
    }

    @Test
    @DisplayName("TC-18: colocar un valor valido sin completar nada")
    void tc18_colocarValorValido() {
        Tablero tablero = new Tablero();

        assertTrue(tablero.esMovimientoValido(4, 4, 5));
        assertTrue(tablero.setValor(4, 4, 5));
        assertEquals(5, tablero.getValor(4, 4));
    }

    @Test
    @DisplayName("TC-19: valor repetido en la misma fila")
    void tc19_valorRepetidoEnFila() {
        int[][] board = new int[9][9];
        board[0][1] = 5;
        Tablero tablero = new Tablero();
        tablero.setCeldas(board);

        assertFalse(tablero.esMovimientoValido(0, 3, 5));
    }

    @Test
    @DisplayName("TC-20: valor repetido en la misma columna")
    void tc20_valorRepetidoEnColumna() {
        int[][] board = new int[9][9];
        board[1][0] = 7;
        Tablero tablero = new Tablero();
        tablero.setCeldas(board);

        assertFalse(tablero.esMovimientoValido(4, 0, 7));
    }

    @Test
    @DisplayName("TC-21: valor repetido en el mismo subgrid 3x3")
    void tc21_valorRepetidoEnSubgrid() {
        int[][] board = new int[9][9];
        board[0][0] = 9;
        Tablero tablero = new Tablero();
        tablero.setCeldas(board);

        assertFalse(tablero.esMovimientoValido(2, 2, 9));
    }
}
