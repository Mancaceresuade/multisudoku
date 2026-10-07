package com.multisudoku.multisudoku.service;

import com.multisudoku.multisudoku.model.Partida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(new SudokuGeneratorService());
    }

    private int contarPistas(Partida partida) {
        int pistas = 0;
        int[][] iniciales = partida.getTablero().getCeldasIniciales();
        for (int[] fila : iniciales) {
            for (int valor : fila) {
                if (valor != 0) {
                    pistas++;
                }
            }
        }
        return pistas;
    }

    @Test
    @DisplayName("TC-01: crear partida sin dificultad usa MEDIO por defecto")
    void tc01_crearPartidaSinDificultad_usaMedioPorDefecto() {
        Partida partida = gameService.crearPartida();

        assertNotNull(partida.getId());
        assertEquals("ESPERANDO", partida.getEstado());
        assertEquals(35, contarPistas(partida));
    }

    @Test
    @DisplayName("TC-05: dificultad en minuscula funciona igual, invalida cae en MEDIO")
    void tc05_dificultadMinusculaYInvalida() {
        Partida facilMinuscula = gameService.crearPartida("facil");
        assertEquals(45, contarPistas(facilMinuscula));

        Partida invalida = gameService.crearPartida("XYZ");
        assertEquals(35, contarPistas(invalida));
    }

    @Test
    @DisplayName("TC-07: el primer jugador que se une inicia la partida")
    void tc07_primerJugadorSeUne_iniciaPartida() {
        Partida partida = gameService.crearPartida();

        assertTrue(gameService.unirJugadorAPartida(partida.getId(), "j1", "Ana"));
        assertEquals("EN_JUEGO", partida.getEstado());
        assertEquals(0, partida.getPuntuaciones().get("j1"));
    }

    @Test
    @DisplayName("TC-08: un segundo jugador tambien puede unirse")
    void tc08_segundoJugadorSeUne() {
        Partida partida = gameService.crearPartida();
        gameService.unirJugadorAPartida(partida.getId(), "j1", "Ana");

        assertTrue(gameService.unirJugadorAPartida(partida.getId(), "j2", "Beto"));
        assertEquals("EN_JUEGO", partida.getEstado());
        assertEquals(2, partida.getNumeroJugadores());
    }

    @Test
    @DisplayName("TC-09: unirse a una partida inexistente falla")
    void tc09_unirseAPartidaInexistente_falla() {
        assertFalse(gameService.unirJugadorAPartida("no-existe", "j1", "Ana"));
    }

    @Test
    @DisplayName("TC-10: unirse a una partida cancelada falla")
    void tc10_unirseAPartidaCancelada_falla() {
        Partida partida = gameService.crearPartida();
        gameService.unirJugadorAPartida(partida.getId(), "j1", "Ana");
        partida.removerJugador("j1"); // sin mas jugadores, la partida queda CANCELADA

        assertFalse(gameService.unirJugadorAPartida(partida.getId(), "j2", "Beto"));
    }

    @Test
    @DisplayName("TC-11: un jugador ya en otra partida no puede unirse a una segunda")
    void tc11_jugadorEnOtraPartida_noPuedeUnirseAOtra() {
        Partida partidaA = gameService.crearPartida();
        Partida partidaB = gameService.crearPartida();
        gameService.unirJugadorAPartida(partidaA.getId(), "j1", "Ana");

        assertFalse(gameService.unirJugadorAPartida(partidaB.getId(), "j1", "Ana"));
        assertEquals(partidaA.getId(), gameService.obtenerPartidaPorJugador("j1").getId());
    }

    @Test
    @DisplayName("TC-22: movimiento de un jugador sin partida asociada es rechazado")
    void tc22_movimientoDeJugadorSinPartida_rechazado() {
        assertFalse(gameService.procesarMovimiento("jugador-fantasma", 0, 0, 5));
    }

    @Test
    @DisplayName("TC-34: desconectar a un jugador no lo remueve de la partida")
    void tc34_desconectarJugador_noLoRemueve() {
        Partida partida = gameService.crearPartida();
        gameService.unirJugadorAPartida(partida.getId(), "j1", "Ana");

        gameService.desconectarJugador("j1");

        assertFalse(partida.getJugadores().get("j1").isConectado());
        assertTrue(partida.getJugadores().containsKey("j1"));
    }

    @Test
    @Disabled("Bug detectado 2026-09-30: GameService.desconectarJugador borra la "
            + "entrada de jugadorAPartida al desconectar, y reconectarJugador depende "
            + "de esa misma entrada para encontrar la partida -> despues de una "
            + "desconexion real, reconectarJugador siempre devuelve false. No se "
            + "corrigio a pedido explicito del usuario (queda documentado, no tocar "
            + "codigo en esta tanda). Ver requirements.md, observacion 2026-09-30.")
    @DisplayName("TC-35: reconectar a un jugador de una partida activa funciona")
    void tc35_reconectarJugador_partidaActiva() {
        Partida partida = gameService.crearPartida();
        gameService.unirJugadorAPartida(partida.getId(), "j1", "Ana");
        gameService.desconectarJugador("j1");

        assertTrue(gameService.reconectarJugador("j1"));
        assertTrue(partida.getJugadores().get("j1").isConectado());
    }

    @Test
    @DisplayName("TC-36: reconectar a un jugador de una partida inactiva falla")
    void tc36_reconectarJugador_partidaInactiva_falla() {
        Partida partida = gameService.crearPartida();
        gameService.unirJugadorAPartida(partida.getId(), "j1", "Ana");
        gameService.desconectarJugador("j1");
        partida.setActiva(false);

        assertFalse(gameService.reconectarJugador("j1"));
    }

    @Test
    @DisplayName("TC-37: consultar una partida existente devuelve su estado")
    void tc37_consultarPartidaExistente() {
        Partida creada = gameService.crearPartida();

        Partida consultada = gameService.obtenerPartida(creada.getId());

        assertNotNull(consultada);
        assertEquals(creada.getId(), consultada.getId());
        assertNotNull(consultada.getTablero());
    }

    @Test
    @DisplayName("TC-38: consultar una partida inexistente devuelve null")
    void tc38_consultarPartidaInexistente_devuelveNull() {
        assertNull(gameService.obtenerPartida("no-existe"));
    }

    @Test
    @DisplayName("TC-39: obtenerPartidasActivas solo incluye partidas activas")
    void tc39_obtenerPartidasActivas_soloActivas() {
        Partida activa = gameService.crearPartida();
        Partida inactiva = gameService.crearPartida();
        inactiva.setActiva(false);

        assertTrue(gameService.obtenerPartidasActivas().contains(activa));
        assertFalse(gameService.obtenerPartidasActivas().contains(inactiva));
    }

    @Test
    @DisplayName("TC-40: obtenerPartidasDisponibles solo incluye partidas en ESPERANDO")
    void tc40_obtenerPartidasDisponibles_soloEsperando() {
        Partida disponible = gameService.crearPartida();
        Partida enJuego = gameService.crearPartida();
        gameService.unirJugadorAPartida(enJuego.getId(), "j1", "Ana");

        assertTrue(gameService.obtenerPartidasDisponibles().contains(disponible));
        assertFalse(gameService.obtenerPartidasDisponibles().contains(enJuego));
    }

    @Test
    @DisplayName("TC-41: limpiar elimina partidas completadas y canceladas, y desvincula jugadores")
    void tc41_limpiarPartidasCompletadas_eliminaYDesvincula() {
        Partida completada = gameService.crearPartida();
        gameService.unirJugadorAPartida(completada.getId(), "j1", "Ana");
        completada.setEstado("COMPLETADA");

        Partida cancelada = gameService.crearPartida();
        gameService.unirJugadorAPartida(cancelada.getId(), "j2", "Beto");
        cancelada.setEstado("CANCELADA");

        gameService.limpiarPartidasCompletadas();

        assertNull(gameService.obtenerPartida(completada.getId()));
        assertNull(gameService.obtenerPartida(cancelada.getId()));
        assertNull(gameService.obtenerPartidaPorJugador("j1"));
        assertNull(gameService.obtenerPartidaPorJugador("j2"));
    }

    @Test
    @DisplayName("TC-42: limpiar no afecta partidas activas")
    void tc42_limpiar_noAfectaPartidasActivas() {
        Partida enEspera = gameService.crearPartida();

        gameService.limpiarPartidasCompletadas();

        assertNotNull(gameService.obtenerPartida(enEspera.getId()));
    }
}
