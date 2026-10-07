# multisudoku

Sudoku multijugador en tiempo real. Backend Spring Boot (Java 17) que
expone un juego de Sudoku 9x9 por WebSocket/STOMP; frontend en HTML/JS
plano servido como recursos estáticos.

## Build y ejecución

- Compilar: `mvn -o compile` (hay `mvnw`/`mvnw.cmd`, pero el wrapper no
  puede descargar el jar sin red — usar el `mvn` del sistema si está
  disponible).
- Correr: `mvn spring-boot:run` (levanta en `http://localhost:8080`,
  puerto por defecto de Spring Boot — no está fijado en
  `application.properties`).
- Páginas estáticas: `index.html`, `create-game.html`, `join-game.html`,
  `game.html`, `test.html` en `src/main/resources/static/`.

## Arquitectura

- `model/`: `Partida`, `Jugador`, `Movimiento`, `Tablero` — estado del
  juego en memoria (sin base de datos, todo vive en
  `ConcurrentHashMap` dentro de `GameService`; se pierde al reiniciar).
- `service/GameService.java`: alta de partidas, unión de jugadores,
  procesamiento de movimientos, desconexión/reconexión, limpieza de
  partidas terminadas.
- `service/SudokuGeneratorService.java`: generación de tableros por
  backtracking y dificultad (FACIL/MEDIO/DIFICIL).
- `controller/WebSocketController.java` + `config/WebSocketConfig.java`:
  endpoint STOMP en `/ws`, mensajes de entrada bajo `/app/game/*`,
  broadcasts en `/topic/game/*`.
- `dto/`: `GameMessage`, `PartidaDTO`, `MovimientoDTO` — forma de los
  mensajes WebSocket.

Reglas de negocio (puntaje, validación de movimientos, fin de partida)
documentadas en `specs/` — ver ahí
antes de asumir cómo se calculan puntos o cuándo termina una partida:
`user-stories.md` (historias de usuario), `requirements.md` (RF, cada
uno vinculado a su historia), `use-cases.md`, `decision-table.md` y
`test-cases.md`.

## Cómo trabajar en este repo

Estas reglas aplican a cualquier asistente de IA que trabaje en este
repositorio (Claude, Gemini, Copilot, Cursor, Codex u otro).

### Confirmación obligatoria antes de crear o modificar código

Antes de crear o modificar cualquier archivo, presentar primero un plan
breve: qué archivos se van a crear/modificar y el enfoque general (2-4
líneas). Esperar confirmación explícita del usuario ("dale", "sí",
"procedé") antes de escribir una sola línea de código. No asumir
aprobación por el solo hecho de que el pedido sonara claro.

### Alcance por defecto — no agregar sin pedirlo

- Resolver únicamente lo que el pedido dice, ni una función más. Si el
  pedido es "completá el RF-06", no tocar RF-07 aunque esté vacío al
  lado y parezca lo mismo.
- No agregar manejo de errores, validaciones, logging, tests,
  comentarios ni documentación que no se hayan pedido explícitamente,
  aunque parezcan "buenas prácticas" obvias.
- No refactorizar, renombrar, reordenar ni "prolijar" código o specs
  existentes como parte de un pedido distinto — ni siquiera si están
  mal escritos.
- No crear archivos nuevos (specs, docs, configuración) que no fueron
  pedidos, aunque la convención de `skill/SKILL.md` los sugiera como
  recomendable.
- No agregar dependencias, librerías ni herramientas nuevas por
  iniciativa propia.
- Si al hacer el trabajo pedido aparece un problema aparte (un bug, un
  hueco, una inconsistencia), no corregirlo de oficio: reportarlo por
  separado y esperar que se pida explícitamente arreglarlo.
  (Señal de que algo NO fue pedido: lo notaste "de paso" mientras
  hacías otra cosa.)
- Ante la duda de si algo entra en el alcance pedido, tratarlo como que
  NO entra, y preguntar antes de agregarlo — nunca asumir "seguramente
  también querés esto".
- Un pedido amplio ("armá los specs") no habilita inventar secciones,
  columnas o convenciones nuevas que no estén ya en la plantilla — si
  falta algo para completar lo pedido, se marca como hueco, no se
  completa por cuenta propia.

### Puntos pendientes y conflictos

- `specs/user-stories.md`, `specs/requirements.md`, `specs/use-cases.md` y
  `specs/decision-table.md` tienen puntos marcados
  `[NEEDS CLARIFICATION]` que son decisiones de negocio pendientes, no
  bugs a "resolver" por cuenta propia.
- Si se detecta un conflicto de reglas o un comportamiento dudoso, **no
  elegir un valor arbitrariamente**: marcar el requerimiento, la fila de
  la tabla de decisión o el caso de uso afectado con
  `[NEEDS CLARIFICATION: <descripción breve del conflicto>]` y listarlo
  en la sección de observaciones del archivo correspondiente.

### Convenciones de specs

Para escribir o modificar specs (requerimientos, casos de uso, tablas
de decisión), seguir las convenciones completas de `skill/SKILL.md`.
