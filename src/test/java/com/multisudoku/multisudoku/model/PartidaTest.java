package com.multisudoku.multisudoku.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartidaTest {

    // Grilla de Sudoku completa y valida, usada como base para los tests
    // que necesitan un tablero casi terminado (finalizacion, bonus).
    private static final int[][] GRILLA_VALIDA = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    private int[][] copiarGrillaValida() {
        int[][] copia = new int[9][9];
        for (int i = 0; i < 9; i++) {
            copia[i] = GRILLA_VALIDA[i].clone();
        }
        return copia;
    }

    private Partida partidaConTablero(int[][] board, String... jugadorIds) {
        Tablero tablero = new Tablero();
        tablero.setCeldas(board);
        Partida partida = new Partida("p1", tablero);
        for (String jugadorId : jugadorIds) {
            partida.agregarJugador(new Jugador(jugadorId, jugadorId));
        }
        return partida;
    }

    @Test
    @DisplayName("TC-12: movimiento con partida no EN_JUEGO")
    void tc12_partidaNoEnJuego_movimientoRechazado() {
        Partida partida = new Partida("p1", new Tablero());
        // Sin jugadores agregados, la partida queda en "ESPERANDO".

        Movimiento movimiento = new Movimiento("m1", "j1", 0, 0, 5);
        assertFalse(partida.procesarMovimiento(movimiento));
        assertEquals(0, partida.getTablero().getValor(0, 0));
    }

    @Test
    @DisplayName("TC-17: borrar una celda en partida descuenta 10 puntos")
    void tc17_borrarCeldaEnPartida_descuentaPuntos() {
        int[][] board = new int[9][9];
        board[2][2] = 7;
        Partida partida = partidaConTablero(board, "j1");

        Movimiento movimiento = new Movimiento("m1", "j1", 2, 2, 0);
        assertTrue(partida.procesarMovimiento(movimiento));
        assertEquals(-10, movimiento.getPuntosOtorgados());
        assertEquals(-10, partida.getJugadores().get("j1").getPuntuacion());
        assertEquals(0, partida.getTablero().getValor(2, 2));
    }

    @Test
    @DisplayName("TC-18: colocar un valor valido sin completar nada otorga 10 puntos")
    void tc18_movimientoValido_sinBonus() {
        int[][] board = new int[9][9];
        Partida partida = partidaConTablero(board, "j1");

        Movimiento movimiento = new Movimiento("m1", "j1", 4, 4, 5);
        assertTrue(partida.procesarMovimiento(movimiento));
        assertEquals(10, movimiento.getPuntosOtorgados());
        assertEquals(10, partida.getJugadores().get("j1").getPuntuacion());
    }

    @Test
    @DisplayName("TC-23: movimiento que completa una fila otorga bonus de +5")
    void tc23_completarFila_bonus5() {
        int[][] board = new int[9][9];
        board[3] = new int[]{1, 2, 3, 4, 0, 6, 7, 8, 9};
        Partida partida = partidaConTablero(board, "j1");

        Movimiento movimiento = new Movimiento("m1", "j1", 3, 4, 5);
        assertTrue(partida.procesarMovimiento(movimiento));
        assertEquals(15, movimiento.getPuntosOtorgados());
    }

    @Test
    @DisplayName("TC-24: movimiento que completa fila y columna otorga bonus de +10")
    void tc24_completarFilaYColumna_bonus10() {
        int[][] board = new int[9][9];
        board[3] = new int[]{1, 2, 3, 4, 0, 6, 7, 8, 9};
        board[0][4] = 1;
        board[1][4] = 2;
        board[2][4] = 3;
        board[4][4] = 4;
        board[5][4] = 6;
        board[6][4] = 7;
        board[7][4] = 8;
        board[8][4] = 9;
        Partida partida = partidaConTablero(board, "j1");

        Movimiento movimiento = new Movimiento("m1", "j1", 3, 4, 5);
        assertTrue(partida.procesarMovimiento(movimiento));
        assertEquals(20, movimiento.getPuntosOtorgados());
    }

    @Test
    @DisplayName("TC-25: movimiento que completa fila, columna y subgrid otorga bonus de +15")
    void tc25_completarFilaColumnaYSubgrid_bonus15() {
        int[][] board = copiarGrillaValida();
        board[0][0] = 0;
        Partida partida = partidaConTablero(board, "j1");

        Movimiento movimiento = new Movimiento("m1", "j1", 0, 0, 5);
        assertTrue(partida.procesarMovimiento(movimiento));
        assertEquals(25, movimiento.getPuntosOtorgados());
    }

    @Test
    @DisplayName("TC-26: borrar una celda de una fila completa sigue descontando exactamente 10")
    void tc26_borrarEnFilaCompleta_sinBonusNegativo() {
        int[][] board = new int[9][9];
        board[3] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Partida partida = partidaConTablero(board, "j1");

        Movimiento movimiento = new Movimiento("m1", "j1", 3, 4, 0);
        assertTrue(partida.procesarMovimiento(movimiento));
        assertEquals(-10, movimiento.getPuntosOtorgados());
    }

    @Test
    @DisplayName("TC-27: el puntaje de un mismo movimiento no depende de la dificultad")
    void tc27_puntajeIndependienteDeDificultad() {
        // La dificultad no es un campo de Partida/Tablero: se construye el
        // mismo tablero "a mano" para representar las 3 dificultades y se
        // verifica que el mismo movimiento da siempre el mismo puntaje.
        for (String dificultad : new String[]{"FACIL", "MEDIO", "DIFICIL"}) {
            int[][] board = new int[9][9];
            board[3] = new int[]{1, 2, 3, 4, 0, 6, 7, 8, 9};
            Partida partida = partidaConTablero(board, "j1");

            Movimiento movimiento = new Movimiento("m1", "j1", 3, 4, 5);
            partida.procesarMovimiento(movimiento);

            assertEquals(15, movimiento.getPuntosOtorgados(), "Dificultad: " + dificultad);
        }
    }

    @Test
    @DisplayName("TC-28: al completar el tablero la partida pasa a COMPLETADA")
    void tc28_completarTablero_partidaCompletada() {
        int[][] board = copiarGrillaValida();
        board[0][0] = 0;
        Partida partida = partidaConTablero(board, "j1");

        partida.procesarMovimiento(new Movimiento("m1", "j1", 0, 0, 5));

        assertEquals("COMPLETADA", partida.getEstado());
        assertFalse(partida.isActiva());
        assertNotNull(partida.getFechaFin());
    }

    @Test
    @DisplayName("TC-29: al finalizar se declara un unico ganador si no hay empate")
    void tc29_finalizarSinEmpate_unGanador() {
        int[][] board = copiarGrillaValida();
        board[0][0] = 0;
        Partida partida = partidaConTablero(board, "j1", "j2");
        partida.getPuntuaciones().put("j2", 10);

        partida.procesarMovimiento(new Movimiento("m1", "j1", 0, 0, 5)); // j1 suma 25

        assertEquals(List.of("j1"), partida.getGanadores());
    }

    @Test
    @DisplayName("TC-30: al finalizar con empate se declaran todos los ganadores")
    void tc30_finalizarConEmpate_variosGanadores() {
        int[][] board = copiarGrillaValida();
        board[0][0] = 0;
        Partida partida = partidaConTablero(board, "j1", "j2");
        partida.getPuntuaciones().put("j2", 25);

        partida.procesarMovimiento(new Movimiento("m1", "j1", 0, 0, 5)); // j1 tambien queda en 25

        assertEquals(2, partida.getGanadores().size());
        assertTrue(partida.getGanadores().containsAll(List.of("j1", "j2")));
    }

    @Test
    @DisplayName("TC-31: partida con un solo jugador declara a ese jugador como ganador")
    void tc31_unSoloJugador_esGanador() {
        int[][] board = copiarGrillaValida();
        board[0][0] = 0;
        Partida partida = partidaConTablero(board, "j1");

        partida.procesarMovimiento(new Movimiento("m1", "j1", 0, 0, 5));

        assertEquals(List.of("j1"), partida.getGanadores());
    }

    @Test
    @DisplayName("TC-32: remover al unico jugador cancela la partida")
    void tc32_removerUnicoJugador_cancelaPartida() {
        Partida partida = partidaConTablero(new int[9][9], "j1");

        assertTrue(partida.removerJugador("j1"));
        assertEquals("CANCELADA", partida.getEstado());
        assertFalse(partida.isActiva());
        assertNotNull(partida.getFechaFin());
    }

    @Test
    @DisplayName("TC-33: remover un jugador cuando quedan otros no cancela la partida")
    void tc33_removerJugador_quedanOtros_partidaSigueActiva() {
        Partida partida = partidaConTablero(new int[9][9], "j1", "j2");

        assertTrue(partida.removerJugador("j1"));
        assertTrue(partida.isActiva());
        assertFalse(partida.getJugadores().containsKey("j1"));
        assertTrue(partida.getJugadores().containsKey("j2"));
    }
}
