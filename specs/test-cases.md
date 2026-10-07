# Casos de prueba

> Derivados de `requirements.md`, `use-cases.md` y
> `decision-table.md`. Cada
> caso referencia el RF, caso de uso o regla de la tabla de decisión que
> lo sustenta. Este documento es la base para los tests unitarios (paso
> siguiente, todavía no implementado).

## UC-01: Crear partida

| ID | Objetivo | Entrada | Resultado esperado | Referencia |
|----|----------|---------|----------------------|------------|
| TC-01 | Crear partida sin especificar dificultad | `crearPartida()` | Se crea con dificultad MEDIO (35 pistas), estado "ESPERANDO", id único no nulo | RF-01, RF-02 |
| TC-02 | Crear partida con dificultad FACIL | `crearPartida("FACIL")` | Tablero con 45 celdas iniciales (pistas) | RF-02 |
| TC-03 | Crear partida con dificultad MEDIO | `crearPartida("MEDIO")` | Tablero con 35 celdas iniciales | RF-02 |
| TC-04 | Crear partida con dificultad DIFICIL | `crearPartida("DIFICIL")` | Tablero con 25 celdas iniciales | RF-02 |
| TC-05 | Crear partida con dificultad en minúsculas o inválida | `crearPartida("facil")` / `crearPartida("XYZ")` | "facil" (minúscula) genera 45 pistas (case-insensitive); "XYZ" cae en el `default` → 35 pistas (MEDIO) | RF-01, RF-02 |
| TC-06 | Las pistas iniciales generadas no violan reglas de Sudoku | tablero recién creado, cualquier dificultad | Ninguna fila, columna ni subgrid 3x3 tiene un valor repetido entre las celdas iniciales | RF-06 (consistencia del tablero base) |

## UC-02: Unirse a partida

| ID | Objetivo | Precondición | Entrada | Resultado esperado | Referencia |
|----|----------|---------------|---------|----------------------|------------|
| TC-07 | Primer jugador se une a una partida en ESPERANDO | partida recién creada | `unirJugadorAPartida(id, "j1", "Ana")` | `true`; partida pasa a estado "EN_JUEGO"; puntuación inicial de "j1" = 0 | RF-03, RF-05 |
| TC-08 | Segundo jugador se une a una partida ya EN_JUEGO | partida con 1 jugador | `unirJugadorAPartida(id, "j2", "Beto")` | `true`; partida sigue "EN_JUEGO"; ahora tiene 2 jugadores | RF-03, RF-04 |
| TC-09 | Unirse a una partida inexistente | ninguna | `unirJugadorAPartida("no-existe", "j1", "Ana")` | `false`; no se crea ningún jugador | RF-03 |
| TC-10 | Unirse a una partida cancelada | partida sin jugadores (CANCELADA) | `unirJugadorAPartida(id, "j1", "Ana")` | `false` (partida no activa) | RF-03, RF-11 |
| TC-11 | Un jugador ya asociado a otra partida intenta unirse a una segunda | "j1" ya está en partida A | `unirJugadorAPartida(partidaB, "j1", "Ana")` | `false`; "j1" sigue asociado únicamente a la partida A | RF-03 |

## UC-03: Realizar movimiento — validación (Tablero.esMovimientoValido)

| ID | Objetivo | Entrada | Resultado esperado | Referencia (Regla decision-table) |
|----|----------|---------|----------------------|--------------------------------------|
| TC-12 | Movimiento con partida no "EN_JUEGO" | partida en "ESPERANDO", movimiento válido en sí | `procesarMovimiento` devuelve `false`; tablero sin cambios | Regla 1 |
| TC-13 | Posición fuera de rango — fila | `fila=9, columna=0, valor=5` | Rechazado | Regla 2 |
| TC-14 | Posición fuera de rango — columna negativa | `fila=0, columna=-1, valor=5` | Rechazado | Regla 2 |
| TC-15 | Valor fuera de rango | `valor=10` (u otro > 9) | Rechazado | Regla 3 |
| TC-16 | Movimiento sobre celda inicial (pista) | celda con `esCeldaInicial=true`, cualquier valor | Rechazado, sin importar si el valor sería válido por Sudoku | Regla 4 |
| TC-17 | Borrar una celda no inicial ya completada | celda no inicial con valor propio puesto antes, `valor=0` | Aceptado; celda vuelve a 0; puntuación del jugador **-10** | Regla 5, RF-07 |
| TC-18 | Colocar un valor válido sin completar nada | celda vacía, valor sin repetir en fila/columna/subgrid | Aceptado; celda actualizada; **+10** puntos, sin bonus | Regla 6, RF-08 |
| TC-19 | Valor repetido en la misma fila | mismo valor ya presente en otra columna de la fila | Rechazado; tablero sin cambios; 0 puntos | Regla por defecto |
| TC-20 | Valor repetido en la misma columna | mismo valor ya presente en otra fila de la columna | Rechazado | Regla por defecto |
| TC-21 | Valor repetido en el mismo subgrid 3x3 | mismo valor ya presente en otra celda del subgrid | Rechazado | Regla por defecto |
| TC-22 | Movimiento de un jugador sin partida asociada | `procesarMovimiento("jugador-fantasma", 0, 0, 5)` en `GameService` | Rechazado (no se encuentra partida del jugador) | RF-06 (precondición) |

## UC-03: Realizar movimiento — puntuación (Partida.calcularPuntos)

| ID | Objetivo | Entrada | Resultado esperado | Referencia |
|----|----------|---------|----------------------|------------|
| TC-23 | Movimiento válido que completa una fila | último valor faltante de una fila, sin completar columna ni subgrid | +10 (base) + 5 (fila) = **15** puntos | RF-08 |
| TC-24 | Movimiento válido que completa fila y columna | último valor faltante compartido por fila y columna | +10 + 5 + 5 = **20** puntos | RF-08 |
| TC-25 | Movimiento válido que completa fila, columna y subgrid | última celda vacía del tablero (caso límite) | +10 + 5 + 5 + 5 = **25** puntos | RF-08 |
| TC-26 | Borrar una celda no otorga bonus aunque "rompa" una fila completa | celda de una fila ya completa, `valor=0` | Puntuación **-10** exactos, nunca bonus positivo | RF-07, Regla 5 |
| TC-27 | El puntaje no depende de la dificultad de la partida | mismo movimiento (completa 1 fila) en partidas FACIL, MEDIO y DIFICIL | Mismos 15 puntos en los tres casos | RF-08 |

## UC-04: Finalizar partida

| ID | Objetivo | Precondición | Resultado esperado | Referencia |
|----|----------|---------------|----------------------|------------|
| TC-28 | Movimiento que completa la última celda del tablero | tablero con una sola celda vacía | Partida pasa a "COMPLETADA"; `activa=false`; `fechaFin` no nula | RF-10 |
| TC-29 | Determinar ganador único | al completarse, un jugador tiene puntaje estrictamente mayor a los demás | `ganadores` contiene exactamente el id de ese jugador | RF-10 |
| TC-30 | Determinar ganadores empatados | al completarse, dos jugadores tienen el mismo puntaje máximo | `ganadores` contiene los ids de ambos jugadores, ningún otro | RF-10 |
| TC-31 | Partida con un solo jugador que la completa | 1 jugador en toda la partida | `ganadores` contiene solo ese id, aunque su puntaje sea el único | RF-10 |

## Jugadores: remover / desconectar / reconectar

| ID | Objetivo | Precondición | Resultado esperado | Referencia |
|----|----------|---------------|----------------------|------------|
| TC-32 | Remover al único jugador de una partida | partida con 1 jugador | Partida pasa a "CANCELADA"; `activa=false`; `fechaFin` asignada | RF-11 |
| TC-33 | Remover a un jugador cuando quedan otros | partida con 2+ jugadores | Jugador removido desaparece de `getJugadores()`; partida sigue activa | RF-11 |
| TC-34 | Desconectar a un jugador | jugador conectado en partida activa | `isConectado()` pasa a `false`; el jugador NO se remueve de la partida | RF-12 |
| TC-35 | Reconectar a un jugador de una partida activa | jugador previamente desconectado, partida sigue activa | Reconexión devuelve `true`; `isConectado()` vuelve a `true` | RF-12 |
| TC-36 | Reconectar a un jugador de una partida ya no activa | partida "COMPLETADA" o "CANCELADA" | Reconexión devuelve `false` | RF-12 |

## UC-05: Consultar estado de partida

| ID | Objetivo | Entrada | Resultado esperado | Referencia |
|----|----------|---------|----------------------|------------|
| TC-37 | Consultar una partida existente | id de partida válido | Devuelve tablero, celdas iniciales, jugadores, puntuaciones, estado y ganadores actuales | RF-13 |
| TC-38 | Consultar una partida inexistente | id inválido | Respuesta de tipo `ERROR` | RF-13 |

## Listado y limpieza de partidas (GameService)

| ID | Objetivo | Precondición | Resultado esperado | Referencia |
|----|----------|---------------|----------------------|------------|
| TC-39 | Listar partidas activas | mezcla de partidas ESPERANDO/EN_JUEGO/COMPLETADA/CANCELADA | Solo incluye las que tienen `activa=true` | RF-14 |
| TC-40 | Listar partidas disponibles para unirse | mezcla de estados | Solo incluye activas, con espacio y en estado "ESPERANDO" | RF-14 |
| TC-41 | Limpiar partidas completadas/canceladas | al menos una partida "COMPLETADA" y una "CANCELADA" | Ambas se eliminan del mapa interno; sus jugadores quedan desvinculados (`obtenerPartidaPorJugador` devuelve `null`) | RF-15 |
| TC-42 | Limpiar no afecta partidas activas | partidas en "ESPERANDO"/"EN_JUEGO" | Esas partidas siguen presentes después de limpiar | RF-15 |

## Observaciones

- No hay casos de prueba para `SudokuGeneratorService.resolverTablero` /
  `tieneSolucionUnica`: no se invocan desde ningún controller (ver
  `requirements.md`, tabla de "Fragmentos que NO son requerimientos
  funcionales") — no son una función expuesta al usuario, así que no se
  incluyen como casos de prueba de negocio. Si en el futuro se expone
  esta función, se deberían agregar sus propios casos.
- Los casos TC-27 y TC-06 requieren generar tableros reales (no solo
  mockear `Tablero`) porque dependen del algoritmo de
  `SudokuGeneratorService` — al implementarlos como test unitario van a
  necesitar más de una corrida por la aleatoriedad del generador.
