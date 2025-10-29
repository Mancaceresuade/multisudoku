package com.multisudoku.multisudoku.service;

import com.multisudoku.multisudoku.model.Tablero;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SudokuGeneratorService {
    
    private static final int TAMANO = 9;
    private final Random random = new Random();

    // Generar un tablero completo de Sudoku
    public Tablero generarTableroCompleto() {
        Tablero tablero = new Tablero();
        
        // Generar tablero completo
        generarTableroCompletoRecursivo(tablero, 0, 0);
        
        return tablero;
    }

    // Generar tablero con pistas (para juego)
    public Tablero generarTableroConPistas(int numeroPistas) {
        Tablero tableroCompleto = generarTableroCompleto();
        Tablero tableroConPistas = new Tablero();
        
        // Copiar algunas celdas como pistas
        List<int[]> posiciones = generarPosicionesAleatorias(numeroPistas);
        
        for (int[] pos : posiciones) {
            int fila = pos[0];
            int columna = pos[1];
            int valor = tableroCompleto.getValor(fila, columna);
            tableroConPistas.setCeldaInicial(fila, columna, valor);
        }
        
        return tableroConPistas;
    }

    // Generar tablero completo recursivamente
    private boolean generarTableroCompletoRecursivo(Tablero tablero, int fila, int columna) {
        if (fila == TAMANO) {
            return true; // Tablero completo
        }
        
        if (columna == TAMANO) {
            return generarTableroCompletoRecursivo(tablero, fila + 1, 0);
        }
        
        // Mezclar números para mayor aleatoriedad
        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= TAMANO; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros, random);
        
        for (int numero : numeros) {
            if (tablero.esMovimientoValido(fila, columna, numero)) {
                tablero.setValor(fila, columna, numero);
                
                if (generarTableroCompletoRecursivo(tablero, fila, columna + 1)) {
                    return true;
                }
                
                tablero.setValor(fila, columna, 0); // Backtrack
            }
        }
        
        return false;
    }

    // Generar posiciones aleatorias para pistas
    private List<int[]> generarPosicionesAleatorias(int numeroPistas) {
        List<int[]> posiciones = new ArrayList<>();
        
        // Generar todas las posiciones posibles
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                posiciones.add(new int[]{i, j});
            }
        }
        
        // Mezclar y tomar las primeras N posiciones
        Collections.shuffle(posiciones, random);
        return posiciones.subList(0, Math.min(numeroPistas, posiciones.size()));
    }

    // Generar tablero fácil (más pistas)
    public Tablero generarTableroFacil() {
        return generarTableroConPistas(45); // ~50% de pistas
    }

    // Generar tablero medio
    public Tablero generarTableroMedio() {
        return generarTableroConPistas(35); // ~40% de pistas
    }

    // Generar tablero difícil
    public Tablero generarTableroDificil() {
        return generarTableroConPistas(25); // ~30% de pistas
    }

    // Validar si un tablero tiene solución única
    public boolean tieneSolucionUnica(Tablero tablero) {
        Tablero copia = copiarTablero(tablero);
        return contarSoluciones(copia, 0, 0) == 1;
    }

    // Contar número de soluciones
    private int contarSoluciones(Tablero tablero, int fila, int columna) {
        if (fila == TAMANO) {
            return 1; // Solución encontrada
        }
        
        if (columna == TAMANO) {
            return contarSoluciones(tablero, fila + 1, 0);
        }
        
        if (tablero.getValor(fila, columna) != 0) {
            return contarSoluciones(tablero, fila, columna + 1);
        }
        
        int soluciones = 0;
        for (int numero = 1; numero <= TAMANO && soluciones < 2; numero++) {
            if (tablero.esMovimientoValido(fila, columna, numero)) {
                tablero.setValor(fila, columna, numero);
                soluciones += contarSoluciones(tablero, fila, columna + 1);
                tablero.setValor(fila, columna, 0); // Backtrack
            }
        }
        
        return soluciones;
    }

    // Copiar tablero
    private Tablero copiarTablero(Tablero original) {
        Tablero copia = new Tablero();
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                int valor = original.getValor(i, j);
                if (valor != 0) {
                    copia.setCeldaInicial(i, j, valor);
                }
            }
        }
        return copia;
    }

    // Resolver tablero (si es posible)
    public boolean resolverTablero(Tablero tablero) {
        return resolverTableroRecursivo(tablero, 0, 0);
    }

    // Resolver tablero recursivamente
    private boolean resolverTableroRecursivo(Tablero tablero, int fila, int columna) {
        if (fila == TAMANO) {
            return true; // Tablero resuelto
        }
        
        if (columna == TAMANO) {
            return resolverTableroRecursivo(tablero, fila + 1, 0);
        }
        
        if (tablero.getValor(fila, columna) != 0) {
            return resolverTableroRecursivo(tablero, fila, columna + 1);
        }
        
        for (int numero = 1; numero <= TAMANO; numero++) {
            if (tablero.esMovimientoValido(fila, columna, numero)) {
                tablero.setValor(fila, columna, numero);
                
                if (resolverTableroRecursivo(tablero, fila, columna + 1)) {
                    return true;
                }
                
                tablero.setValor(fila, columna, 0); // Backtrack
            }
        }

        return false;
    }
}
