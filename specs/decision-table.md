# Tabla de decisión

> Detalla las reglas del caso de uso UC-03 (`use-cases.md`) y de las
> historias HU-04, HU-05 y HU-06 (`user-stories.md`).

## ¿Se acepta un movimiento sobre una celda del tablero?

> La **Regla 5 (borrar)** implementa la decisión de negocio confirmada
> el 2026-09-30.

| Condición / Acción | Regla 1 | Regla 2 | Regla 3 | Regla 4 | Regla 5 | Regla 6 | Regla por defecto |
|----------------------|---------|---------|---------|---------|---------|---------|---------------------|
| Partida activa y en estado "EN_JUEGO" | No | - | - | - | - | - | No → rechazado |
| Posición (fila, columna) dentro del tablero (0-8) | - | No | - | - | - | - | - |
| Valor dentro de rango permitido (0-9) | - | - | No | - | - | - | - |
| Celda es una pista inicial | - | - | - | Sí | - | - | - |
| Valor = 0 (borrar) | - | - | - | - | Sí | No | - |
| Valor repetido en la misma fila, columna o subgrid 3x3 (solo aplica si valor ≠ 0) | - | - | - | - | - | No | Sí |
| **Acción: Movimiento aceptado** | No | No | No | No | Sí | Sí | No |
| **Acción: Valor aplicado a la celda** | No | No | No | No | Sí (celda vuelve a 0) | Sí | No |
| **Acción: Ajuste de puntuación** | 0 | 0 | 0 | 0 | **-10** | +10 + bonus* | 0 |

`*` bonus: +5 por cada fila, +5 por cada columna y +5 por cada subgrid
3x3 que quede completo como resultado del movimiento (0, 5, 10 o 15
puntos extra, acumulables entre sí). El bonus no aplica a la Regla 5
(borrar nunca completa una fila/columna/subgrid).

**Regla por defecto:** cualquier combinación no cubierta explícitamente
por las reglas 1 a 6 (en particular: valor distinto de 0 que repite en
fila, columna o subgrid) se rechaza sin aplicar el valor y sin ajustar
puntuación.

**Cómo se lee esta tabla:** las reglas se evalúan en orden de arriba
hacia abajo: primero se corta por partida inactiva
(Regla 1), luego por posición fuera de rango (Regla 2), luego por valor
fuera de rango (Regla 3), luego por celda inicial (Regla 4). Solo si
ninguna de esas aplica se distingue si el valor es 0 (Regla 5, borrar,
siempre válido) o distinto de 0 sin repetir en fila/columna/subgrid
(Regla 6), cayendo en la regla por defecto si repite.

## Observaciones

- **Conflictos detectados:** ninguno.
- **Decisiones de negocio resueltas e implementadas (2026-09-30,
  confirmadas por el docente/cliente):**
  (a) borrar una celda descuenta 10 puntos (Regla 5 de la tabla);
  (b) el puntaje no varía según la dificultad de la partida;
  (c) en caso de empate de puntaje al finalizar la partida se declaran
  ganadores a todos los jugadores empatados en el máximo (ver UC-04 en
  `use-cases.md`).
