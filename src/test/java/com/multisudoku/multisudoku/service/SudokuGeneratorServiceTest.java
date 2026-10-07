package com.multisudoku.multisudoku.service;

import com.multisudoku.multisudoku.model.Tablero;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SudokuGeneratorServiceTest {

    private final SudokuGeneratorService generador = new SudokuGeneratorService();

    private int contarPistas(Tablero tablero) {
        int pistas = 0;
        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            for (int columna = 0; columna < Tablero.TAMANO; columna++) {
                if (tablero.getValor(fila, columna) != 0) {
                    pistas++;
                }
            }
        }
        return pistas;
    }

    @RepeatedTest(5)
    @DisplayName("TC-02: dificultad FACIL genera 45 pistas")
    void tc02_dificultadFacil_45Pistas() {
        assertEquals(45, contarPistas(generador.generarTableroFacil()));
    }

    @RepeatedTest(5)
    @DisplayName("TC-03: dificultad MEDIO genera 35 pistas")
    void tc03_dificultadMedio_35Pistas() {
        assertEquals(35, contarPistas(generador.generarTableroMedio()));
    }

    @RepeatedTest(5)
    @DisplayName("TC-04: dificultad DIFICIL genera 25 pistas")
    void tc04_dificultadDificil_25Pistas() {
        assertEquals(25, contarPistas(generador.generarTableroDificil()));
    }

    @RepeatedTest(5)
    @DisplayName("TC-06: el tablero completo generado no viola reglas de Sudoku")
    void tc06_tableroCompleto_sinDuplicados() {
        Tablero tablero = generador.generarTableroCompleto();

        for (int i = 0; i < Tablero.TAMANO; i++) {
            assertEsPermutacionDe1a9(valoresFila(tablero, i), "fila " + i);
            assertEsPermutacionDe1a9(valoresColumna(tablero, i), "columna " + i);
        }

        for (int subgridFila = 0; subgridFila < Tablero.TAMANO; subgridFila += Tablero.SUBGRID_TAMANO) {
            for (int subgridColumna = 0; subgridColumna < Tablero.TAMANO; subgridColumna += Tablero.SUBGRID_TAMANO) {
                assertEsPermutacionDe1a9(valoresSubgrid(tablero, subgridFila, subgridColumna),
                        "subgrid (" + subgridFila + "," + subgridColumna + ")");
            }
        }
    }

    private int[] valoresFila(Tablero tablero, int fila) {
        int[] valores = new int[Tablero.TAMANO];
        for (int columna = 0; columna < Tablero.TAMANO; columna++) {
            valores[columna] = tablero.getValor(fila, columna);
        }
        return valores;
    }

    private int[] valoresColumna(Tablero tablero, int columna) {
        int[] valores = new int[Tablero.TAMANO];
        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            valores[fila] = tablero.getValor(fila, columna);
        }
        return valores;
    }

    private int[] valoresSubgrid(Tablero tablero, int filaInicio, int columnaInicio) {
        int[] valores = new int[Tablero.SUBGRID_TAMANO * Tablero.SUBGRID_TAMANO];
        int i = 0;
        for (int f = filaInicio; f < filaInicio + Tablero.SUBGRID_TAMANO; f++) {
            for (int c = columnaInicio; c < columnaInicio + Tablero.SUBGRID_TAMANO; c++) {
                valores[i++] = tablero.getValor(f, c);
            }
        }
        return valores;
    }

    private void assertEsPermutacionDe1a9(int[] valores, String contexto) {
        Set<Integer> esperados = new HashSet<>();
        for (int v = 1; v <= 9; v++) {
            esperados.add(v);
        }
        Set<Integer> obtenidos = new HashSet<>();
        for (int v : valores) {
            obtenidos.add(v);
        }
        assertEquals(esperados, obtenidos, contexto + " deberia contener cada valor de 1 a 9 exactamente una vez");
    }
}
