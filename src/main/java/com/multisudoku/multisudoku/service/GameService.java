package com.multisudoku.multisudoku.service;

import com.multisudoku.multisudoku.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    
    private final Map<String, Partida> partidas = new ConcurrentHashMap<>();
    private final Map<String, String> jugadorAPartida = new ConcurrentHashMap<>();
    private final SudokuGeneratorService sudokuGenerator;
    
    public GameService(SudokuGeneratorService sudokuGenerator) {
        this.sudokuGenerator = sudokuGenerator;
    }
    
    // Generar IDs únicos
    private String generarId() {
        return UUID.randomUUID().toString();
    }

    // Crear nueva partida
    public Partida crearPartida() {
        return crearPartida("MEDIO"); // Dificultad por defecto
    }
    
    // Crear nueva partida con dificultad específica
    public Partida crearPartida(String dificultad) {
        String partidaId = generarId();
        Tablero tablero = generarTableroPorDificultad(dificultad, partidaId);
        
        Partida partida = new Partida(partidaId, tablero);
        partidas.put(partidaId, partida);
        
        System.out.println("Nueva partida creada: " + partidaId + " (Dificultad: " + dificultad + ")");
        return partida;
    }

    // Unir jugador a partida
    public boolean unirJugadorAPartida(String partidaId, String jugadorId, String nombreJugador) {
        Partida partida = partidas.get(partidaId);
        if (partida == null || !partida.isActiva()) {
            return false;
        }

        // Verificar si el jugador ya está en una partida
        if (jugadorAPartida.containsKey(jugadorId)) {
            return false;
        }

        Jugador jugador = new Jugador(jugadorId, nombreJugador);
        if (partida.agregarJugador(jugador)) {
            jugadorAPartida.put(jugadorId, partidaId);
            System.out.println("Jugador " + nombreJugador + " se unió a la partida " + partidaId);
            return true;
        }
        
        return false;
    }

    // Obtener partida por ID
    public Partida obtenerPartida(String partidaId) {
        return partidas.get(partidaId);
    }

    // Obtener partida por jugador
    public Partida obtenerPartidaPorJugador(String jugadorId) {
        String partidaId = jugadorAPartida.get(jugadorId);
        return partidaId != null ? partidas.get(partidaId) : null;
    }

    // Procesar movimiento
    public boolean procesarMovimiento(String jugadorId, int fila, int columna, int valor) {
        Partida partida = obtenerPartidaPorJugador(jugadorId);
        if (partida == null || !partida.isActiva()) {
            return false;
        }

        String movimientoId = generarId();
        Movimiento movimiento = new Movimiento(movimientoId, jugadorId, fila, columna, valor);
        
        boolean exitoso = partida.procesarMovimiento(movimiento);
        
        if (exitoso) {
            System.out.println("Movimiento procesado: " + movimiento);
        } else {
            System.out.println("Movimiento inválido: " + movimiento);
        }
        
        return exitoso;
    }

    // Obtener todas las partidas activas
    public List<Partida> obtenerPartidasActivas() {
        return partidas.values().stream()
                .filter(Partida::isActiva)
                .toList();
    }

    // Obtener partidas disponibles para unirse
    public List<Partida> obtenerPartidasDisponibles() {
        return partidas.values().stream()
                .filter(partida -> partida.isActiva() && 
                                 partida.tieneEspacio() && 
                                 partida.getEstado().equals("ESPERANDO"))
                .toList();
    }

    // Desconectar jugador
    public void desconectarJugador(String jugadorId) {
        String partidaId = jugadorAPartida.get(jugadorId);
        if (partidaId != null) {
            Partida partida = partidas.get(partidaId);
            if (partida != null) {
                Jugador jugador = partida.getJugadores().get(jugadorId);
                if (jugador != null) {
                    jugador.setConectado(false);
                    System.out.println("Jugador " + jugador.getNombre() + " desconectado");
                }
            }
            jugadorAPartida.remove(jugadorId);
        }
    }

    // Reconectar jugador
    public boolean reconectarJugador(String jugadorId) {
        String partidaId = jugadorAPartida.get(jugadorId);
        if (partidaId != null) {
            Partida partida = partidas.get(partidaId);
            if (partida != null && partida.isActiva()) {
                Jugador jugador = partida.getJugadores().get(jugadorId);
                if (jugador != null) {
                    jugador.setConectado(true);
                    System.out.println("Jugador " + jugador.getNombre() + " reconectado");
                    return true;
                }
            }
        }
        return false;
    }

    // Limpiar partidas completadas (para evitar acumulación de memoria)
    public void limpiarPartidasCompletadas() {
        partidas.entrySet().removeIf(entry -> {
            Partida partida = entry.getValue();
            boolean completada = partida.getEstado().equals("COMPLETADA") || 
                               partida.getEstado().equals("CANCELADA");
            
            if (completada) {
                // Remover referencias de jugadores
                partida.getJugadores().keySet().forEach(jugadorAPartida::remove);
                System.out.println("Partida " + entry.getKey() + " limpiada");
            }
            
            return completada;
        });
    }

    // Generar tablero por dificultad
    private Tablero generarTableroPorDificultad(String dificultad, String partidaId) {
        Tablero tablero = null;
        
        switch (dificultad.toUpperCase()) {
            case "FACIL":
                tablero = sudokuGenerator.generarTableroFacil();
                break;
            case "DIFICIL":
                tablero = sudokuGenerator.generarTableroDificil();
                break;
            case "MEDIO":
            default:
                tablero = sudokuGenerator.generarTableroMedio();
                break;
        }
        
        tablero.setId(partidaId);
        System.out.println("Tablero " + dificultad + " generado para partida: " + partidaId);
        return tablero;
    }

    // Obtener estadísticas del juego
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPartidas", partidas.size());
        stats.put("partidasActivas", obtenerPartidasActivas().size());
        stats.put("partidasDisponibles", obtenerPartidasDisponibles().size());
        stats.put("jugadoresConectados", jugadorAPartida.size());
        return stats;
    }
}
