package com.multisudoku.multisudoku.controller;

import com.multisudoku.multisudoku.dto.*;
import com.multisudoku.multisudoku.model.*;
import com.multisudoku.multisudoku.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private GameService gameService;

    // Endpoints de prueba (mantener para testing)
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        Thread.sleep(1000); // Simula procesamiento
        return "Hola, recibí tu mensaje: " + message + " - " + java.time.LocalDateTime.now();
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String test(String message) {
        return "Mensaje de prueba: " + message;
    }

    // Endpoints del juego de Sudoku
    @MessageMapping("/game/crear-partida")
    @SendTo("/topic/game/partida-creada")
    public GameMessage crearPartida(GameMessage message) {
        try {
            String dificultad = message.getDatos() != null ? message.getDatos().toString() : "MEDIO";
            Partida partida = gameService.crearPartida(dificultad);
            
            // Convertir a DTO para incluir toda la información
            PartidaDTO partidaDTO = convertirAPartidaDTO(partida);
            
            GameMessage respuesta = new GameMessage("PARTIDA_CREADA", message.getJugadorId(), partida.getId(), partidaDTO);
            respuesta.setMensaje("Partida creada exitosamente");
            
            return respuesta;
        } catch (Exception e) {
            GameMessage error = new GameMessage("ERROR", message.getJugadorId(), null, null);
            error.setMensaje("Error al crear partida: " + e.getMessage());
            return error;
        }
    }

    @MessageMapping("/game/unirse")
    @SendTo("/topic/game/jugador-unido")
    public GameMessage unirsePartida(GameMessage message) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> datos = (Map<String, String>) message.getDatos();
            String partidaId = datos.get("partidaId");
            String nombreJugador = datos.get("nombreJugador");
            
            boolean exitoso = gameService.unirJugadorAPartida(partidaId, message.getJugadorId(), nombreJugador);
            
            if (exitoso) {
                // Obtener el estado actualizado de la partida
                Partida partida = gameService.obtenerPartida(partidaId);
                PartidaDTO partidaDTO = convertirAPartidaDTO(partida);
                
                GameMessage respuesta = new GameMessage("JUGADOR_UNIDO", message.getJugadorId(), partidaId, partidaDTO);
                respuesta.setMensaje("Te uniste exitosamente a la partida");
                return respuesta;
            } else {
                GameMessage error = new GameMessage("ERROR", message.getJugadorId(), partidaId, null);
                error.setMensaje("No se pudo unir a la partida");
                return error;
            }
        } catch (Exception e) {
            GameMessage error = new GameMessage("ERROR", message.getJugadorId(), null, null);
            error.setMensaje("Error al unirse a la partida: " + e.getMessage());
            return error;
        }
    }

    @MessageMapping("/game/movimiento")
    @SendTo("/topic/game/movimiento")
    public GameMessage procesarMovimiento(GameMessage message) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> datos = (Map<String, Object>) message.getDatos();
            int fila = (Integer) datos.get("fila");
            int columna = (Integer) datos.get("columna");
            int valor = (Integer) datos.get("valor");
            
            boolean exitoso = gameService.procesarMovimiento(message.getJugadorId(), fila, columna, valor);
            
            if (exitoso) {
                Partida partida = gameService.obtenerPartida(message.getPartidaId());
                Jugador jugador = partida.getJugadores().get(message.getJugadorId());
                
                MovimientoDTO movimientoDTO = new MovimientoDTO(
                    message.getJugadorId(),
                    jugador.getNombre(),
                    fila,
                    columna,
                    valor,
                    true,
                    calcularPuntos(partida, fila, columna)
                );
                
                // Incluir el estado completo del tablero en la respuesta
                PartidaDTO partidaDTO = convertirAPartidaDTO(partida);
                movimientoDTO.setPartidaDTO(partidaDTO);
                
                GameMessage respuesta = new GameMessage("MOVIMIENTO_EXITOSO", message.getJugadorId(), message.getPartidaId(), movimientoDTO);
                respuesta.setMensaje("Movimiento procesado exitosamente");
                return respuesta;
            } else {
                GameMessage error = new GameMessage("MOVIMIENTO_INVALIDO", message.getJugadorId(), message.getPartidaId(), null);
                error.setMensaje("Movimiento inválido");
                return error;
            }
        } catch (Exception e) {
            GameMessage error = new GameMessage("ERROR", message.getJugadorId(), message.getPartidaId(), null);
            error.setMensaje("Error al procesar movimiento: " + e.getMessage());
            return error;
        }
    }

    @MessageMapping("/game/estado")
    @SendTo("/topic/game/estado")
    public GameMessage obtenerEstadoPartida(GameMessage message) {
        try {
            Partida partida = gameService.obtenerPartida(message.getPartidaId());
            if (partida != null) {
                PartidaDTO partidaDTO = convertirAPartidaDTO(partida);
                GameMessage respuesta = new GameMessage("ESTADO_PARTIDA", message.getJugadorId(), message.getPartidaId(), partidaDTO);
                return respuesta;
            } else {
                GameMessage error = new GameMessage("ERROR", message.getJugadorId(), message.getPartidaId(), null);
                error.setMensaje("Partida no encontrada");
                return error;
            }
        } catch (Exception e) {
            GameMessage error = new GameMessage("ERROR", message.getJugadorId(), message.getPartidaId(), null);
            error.setMensaje("Error al obtener estado: " + e.getMessage());
            return error;
        }
    }

    // Métodos auxiliares
    private int calcularPuntos(Partida partida, int fila, int columna) {
        // Lógica simplificada de cálculo de puntos
        return 10; // Puntos base
    }

    private PartidaDTO convertirAPartidaDTO(Partida partida) {
        PartidaDTO dto = new PartidaDTO();
        dto.setId(partida.getId());
        dto.setTablero(partida.getTablero().getCeldas());
        dto.setCeldasIniciales(partida.getTablero().getCeldasIniciales());
        dto.setEstado(partida.getEstado());
        dto.setActiva(partida.isActiva());
        dto.setNumeroJugadores(partida.getNumeroJugadores());
        dto.setMaxJugadores(partida.getMaxJugadores());
        dto.setGanador(partida.getGanador());
        
        // Convertir jugadores y puntuaciones
        Map<String, String> jugadores = new HashMap<>();
        Map<String, Integer> puntuaciones = new HashMap<>();
        
        partida.getJugadores().forEach((id, jugador) -> {
            jugadores.put(id, jugador.getNombre());
            puntuaciones.put(id, jugador.getPuntuacion());
        });
        
        dto.setJugadores(jugadores);
        dto.setPuntuaciones(puntuaciones);
        
        return dto;
    }
}
