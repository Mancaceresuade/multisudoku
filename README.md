# multisudoku

Sudoku multijugador en tiempo real. Varios jugadores resuelven el mismo
tablero 9x9 a la vez y compiten por puntaje.

- **Backend:** Spring Boot (Java 17), comunicación por WebSocket/STOMP.
- **Frontend:** HTML/JS plano, servido como recursos estáticos.

## Requisitos

- Java 17
- Maven (el wrapper `mvnw` necesita conexión a internet la primera vez;
  si no hay red, usar el `mvn` instalado en el sistema)

## Cómo compilar, correr y testear

```bash
mvn compile            # compilar
mvn test               # correr los tests
mvn spring-boot:run    # levantar la aplicación
```

La aplicación queda disponible en <http://localhost:8080>.

## Cómo jugar

1. Abrir <http://localhost:8080> (`index.html`).
2. Crear una partida eligiendo la dificultad (`create-game.html`) y
   compartir el id de la partida.
3. Los demás jugadores se unen con ese id (`join-game.html`).
4. Jugar en `game.html`: completar una celda suma 10 puntos, más 5 por
   cada fila, columna o subgrid que se complete; borrar resta 10.
   Cuando el tablero está lleno, ganan los jugadores con mayor puntaje.

## Estructura

```
src/main/java/com/multisudoku/multisudoku/
  model/        Partida, Jugador, Movimiento, Tablero (estado del juego)
  service/      GameService (partidas y jugadores),
                SudokuGeneratorService (generación de tableros)
  controller/   WebSocketController (mensajes /app/game/*)
  config/       WebSocketConfig (endpoint STOMP /ws)
  dto/          Forma de los mensajes WebSocket
src/main/resources/static/   Páginas HTML del juego
src/test/                    Tests unitarios
specs/                       Especificación funcional
```

## Especificación

En `specs/`:

| Archivo | Contenido |
|---------|-----------|
| `user-stories.md` | Historias de usuario con criterios de aceptación |
| `requirements.md` | Requerimientos funcionales, vinculados a las historias |
| `use-cases.md` | Casos de uso |
| `decision-table.md` | Reglas de validación y puntaje de un movimiento |
| `test-cases.md` | Casos de prueba |

Los puntos marcados `[NEEDS CLARIFICATION]` son decisiones de negocio
pendientes de confirmar.

## Trabajo con asistentes de IA

Las reglas para trabajar en este repo con IA están en `AGENTS.md`
(`CLAUDE.md` y `GEMINI.md` lo importan). En resumen: antes de modificar
archivos, la IA presenta un plan y espera confirmación; no se sale del
alcance pedido; y ante un conflicto de reglas lo marca en vez de elegir
un valor. Las convenciones para escribir specs están en `skill/SKILL.md`.

## Temas pendientes

- **Sin persistencia:** las partidas viven en memoria y se pierden al
  reiniciar el servidor.
- **Sin seguridad:** el id de jugador lo envía el cliente, así que
  cualquiera puede mandar mensajes haciéndose pasar por otro jugador.
- **Funciones sin conectar:** salir de una partida, desconectar/reconectar,
  listar partidas y limpiar partidas terminadas existen en el backend,
  pero ningún mensaje ni tarea las dispara (ver UC-06 a UC-09 en
  `specs/use-cases.md`).
- **Bug en la reconexión:** un jugador desconectado nunca puede
  reconectarse (ver observaciones en `specs/requirements.md`; el test
  TC-35 está deshabilitado documentándolo).
- **Estados como texto:** el estado de la partida es un `String`
  (`"EN_JUEGO"`, etc.) en vez de un `enum`.
- **Logs por consola:** se usa `System.out.println` en vez de un logger.
- **Restos de pruebas:** endpoints `/hello` y `/test` y la página
  `test.html`.
- **Sin integración continua:** no hay nada que corra los tests
  automáticamente en cada cambio.
- **Pocos tests:** la cobertura se limita a modelo y servicios; no hay
  tests del controlador ni de punta a punta.
