# Requerimientos

> La columna "Historia(s) de usuario" vincula cada requerimiento con su
> historia en `user-stories.md`.

## Requerimientos funcionales

| ID | Requerimiento | Historia(s) de usuario |
|----|---------------|------------------------|
| RF-01 | El sistema debe crear una partida nueva generando un tablero de Sudoku 9x9 con dificultad FACIL, MEDIO o DIFICIL (MEDIO por defecto), asignándole un id único. | HU-01 |
| RF-02 | El sistema debe generar el tablero con una cantidad de celdas iniciales (pistas) según la dificultad: 45 (FACIL), 35 (MEDIO), 25 (DIFICIL), sobre un tablero base resuelto válidamente. | HU-01 |
| RF-03 | El sistema debe permitir que un jugador se una a una partida activa mediante id de partida, id de jugador y nombre, siempre que ese jugador no esté ya asociado a otra partida. | HU-03 |
| RF-04 | El sistema no debe limitar la cantidad de jugadores que pueden unirse a una partida. | HU-03 |
| RF-05 | El sistema debe marcar la partida como "EN_JUEGO" en cuanto se une el primer jugador. | HU-03 |
| RF-06 | El sistema debe permitir que un jugador coloque un valor en una celda del tablero, rechazando el movimiento si la celda es una pista inicial, si la posición está fuera del tablero, si el valor está fuera de 0-9, o si el valor ya está presente en la misma fila, columna o subgrid 3x3. | HU-04 |
| RF-07 | El sistema debe permitir borrar el valor de una celda no inicial (movimiento con valor 0), descontando 10 puntos de la puntuación del jugador que borra. | HU-05 |
| RF-08 | El sistema debe otorgar 10 puntos base por cada movimiento que complete una celda con un valor distinto de 0 (no aplica a borrar, ver RF-07), más 5 puntos adicionales por cada fila, columna o subgrid 3x3 que quede completo como resultado de ese movimiento. El puntaje no varía según la dificultad de la partida (FACIL/MEDIO/DIFICIL) — la dificultad solo afecta la cantidad de pistas iniciales (RF-02). | HU-06 |
| RF-09 | El sistema debe acumular la puntuación de cada jugador a lo largo de la partida. | HU-06 |
| RF-10 | El sistema debe marcar la partida como "COMPLETADA" cuando todas las celdas del tablero quedan llenas, y debe declarar como ganadores a todos los jugadores que compartan la puntuación máxima en ese momento (puede ser uno o varios en caso de empate). | HU-07 |
| RF-11 | El sistema debe permitir remover a un jugador de una partida; si no queda ningún jugador, la partida pasa a estado "CANCELADA". | HU-08 |
| RF-12 | El sistema debe permitir marcar a un jugador como desconectado sin removerlo de la partida, y permitir marcarlo como reconectado mientras la partida siga activa. | HU-09 |
| RF-13 | El sistema debe permitir consultar el estado completo de una partida por su id (tablero actual, celdas iniciales, jugadores, puntuaciones, ganador, estado). | HU-10 |
| RF-14 | El sistema debe permitir listar las partidas activas y, por separado, las partidas disponibles para unirse (activas, con espacio y en estado "ESPERANDO"). | HU-02 |
| RF-15 | El sistema debe eliminar de memoria las partidas en estado "COMPLETADA" o "CANCELADA" cuando se ejecuta la limpieza. | HU-11 |

## Fragmentos que NO son requerimientos funcionales

Se listan aspectos del código que NO son comportamiento de negocio sino
decisiones de implementación — para no mezclarlos con los RF de arriba:

| Aspecto | Categoría | Justificación |
|---------|-----------|----------------|
| Transporte por WebSocket/STOMP sobre `/ws`, tópicos `/topic/game/*` | Restricción técnica | Es la tecnología de comunicación elegida, no una función visible para el negocio (podría implementarse igual sobre REST). |
| Logging por consola (`System.out.println`) de eventos de partida | Dato de implementación | No es una función solicitable ni verificable como requerimiento de negocio. |
| Generación del tablero completo por backtracking recursivo | Restricción/detalle técnico | Describe el algoritmo interno, no el resultado observable (el resultado observable ya está en RF-01/RF-02). |
| Resolución del tablero y verificación de solución única | Detalle técnico | No se usa desde ninguna función expuesta al jugador — no es comportamiento de negocio. |

## Conflictos u observaciones detectadas

- **Defecto que impedía compilar — corregido 2026-09-30:** la generación
  de tableros tenía un método declarado dos veces (uno anidado dentro
  del otro) y un `;` faltante. Se corrigió y el proyecto compila.
- **Resuelto e implementado 2026-09-30 (decisión del docente/cliente):**
  borrar una celda descuenta 10 puntos (simétrico al valor base que
  otorga completarla), el puntaje no varía según la dificultad, y en
  caso de empate de puntuación al finalizar la partida se declaran
  ganadores a todos los jugadores empatados en el máximo (ver RF-07,
  RF-08, RF-10).
- **Bug detectado 2026-09-30 (por el test TC-35, no corregido a pedido
  explícito del usuario):** al desconectar a un jugador se borra el
  vínculo entre el jugador y su partida, y la reconexión depende de ese
  vínculo. Consecuencia: un jugador desconectado nunca puede
  reconectarse, contradiciendo RF-12. El test TC-35 queda deshabilitado
  documentando este bug en vez de ocultarlo.
- **Sin límite de jugadores (RF-04):** hoy cualquier partida acepta
  jugadores sin tope. Queda como RF porque es el comportamiento actual,
  verificable y reproducible. [NEEDS CLARIFICATION: confirmar si el límite ilimitado
  de jugadores es una decisión de producto deliberada.]
- **Funciones sin forma de dispararse (detectado 2026-09-30):** RF-11
  (salir), RF-12 (desconectar/reconectar), RF-14 (listar partidas) y
  RF-15 (limpieza) existen en el sistema, pero hoy ningún mensaje del
  jugador ni tarea automática los ejecuta. [NEEDS CLARIFICATION: ver
  UC-06 a UC-09 en `use-cases.md`.]
