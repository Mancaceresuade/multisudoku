# Casos de uso

> Cada caso de uso indica las historias de usuario (`user-stories.md`)
> que cubre.

## UC-01: Crear partida

**Historias de usuario:** HU-01

**Actor:** Jugador anfitrión

**Precondición:** ninguna (no requiere partida ni sesión previa).

**Flujo principal:**
1. El jugador envía un mensaje `crear-partida` con una dificultad
   (FACIL/MEDIO/DIFICIL; si no la envía, se usa MEDIO).
2. El sistema genera un tablero de Sudoku con pistas según la dificultad
   (RF-01, RF-02).
3. El sistema crea la partida en estado "ESPERANDO" y le asigna un id
   único.
4. El sistema devuelve la partida creada (tablero, id, estado) al
   jugador.

**Flujo(s) alternativo(s):**
- Si ocurre una excepción al crear la partida, el sistema responde un
  mensaje de tipo `ERROR` con el detalle.

**Postcondición:** existe una partida activa en estado "ESPERANDO", sin
jugadores todavía.

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| — | UC-02 Unirse a partida | — | No hay relación de include/extend: crear partida no requiere que nadie se una (una partida puede quedar en "ESPERANDO" sin jugadores), y unirse tampoco requiere haber creado la partida en la misma operación. |

## UC-02: Unirse a partida

**Historias de usuario:** HU-03

**Actor:** Jugador

**Precondición:** existe una partida activa (UC-01 ya ejecutado) y el
jugador no está asociado a ninguna otra partida (RF-03).

**Flujo principal:**
1. El jugador envía un mensaje `unirse` con el id de la partida, su id de
   jugador y su nombre.
2. El sistema verifica que la partida exista y esté activa.
3. El sistema verifica que el jugador no esté ya en otra partida.
4. El sistema agrega al jugador a la partida con puntuación inicial 0.
5. Si es el primer jugador, el sistema pasa la partida a estado
   "EN_JUEGO" (RF-05).
6. El sistema devuelve el estado actualizado de la partida a los
   suscriptos al tópico de la partida.

**Flujo(s) alternativo(s):**
- Si la partida no existe, no está activa, o el jugador ya está en otra
  partida, el sistema responde `ERROR` sin modificar nada.

**Postcondición:** el jugador queda vinculado a la partida y puede
enviar movimientos (UC-03).

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| requiere | UC-01 Crear partida | include | Unirse siempre necesita una partida ya creada — no tiene sentido ejecutar este flujo sin que UC-01 haya corrido antes (aunque sea en otra sesión). |

## UC-03: Realizar movimiento

**Historias de usuario:** HU-04, HU-05, HU-06

**Actor:** Jugador (debe estar unido a una partida — UC-02)

**Precondición:** el jugador está en una partida activa en estado
"EN_JUEGO".

**Flujo principal:**
1. El jugador envía fila, columna y valor.
2. El sistema valida que la partida esté activa y en "EN_JUEGO".
3. El sistema valida el movimiento contra el tablero: posición dentro de
   rango, valor entre 0 y 9, celda no inicial, y sin repetir el valor en
   fila/columna/subgrid (si el valor no es 0) (RF-06, RF-07).
4. Si es válido, el sistema aplica el valor al tablero.
5. El sistema calcula el ajuste de puntuación del movimiento: si el
   valor completa una celda (≠ 0), otorga puntos (RF-08); si el valor
   es 0 (borra una celda), descuenta 10 puntos (RF-07), y aplica el
   ajuste a la puntuación del jugador (RF-09).
6. El sistema verifica si el tablero quedó completo.
7. El sistema devuelve el resultado del movimiento (éxito/fallo, puntos,
   estado del tablero) a los suscriptos al tópico de la partida.

**Flujo(s) alternativo(s):**
- Si el movimiento es inválido (paso 3), el sistema responde
  `MOVIMIENTO_INVALIDO` con 0 puntos, sin modificar el tablero.
- Si la partida no está activa o no está "EN_JUEGO", el movimiento se
  rechaza sin evaluar reglas de Sudoku.

**Postcondición:** el tablero refleja el movimiento (si fue válido) y la
puntuación del jugador está actualizada.

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| dispara | UC-04 Finalizar partida | extend | Solo se dispara bajo una condición específica (que el movimiento deje el tablero 100% completo, RF-10); la mayoría de los movimientos no la disparan, así que el flujo base de UC-03 es completo sin ella. |

## UC-04: Finalizar partida

**Historias de usuario:** HU-07

**Actor:** ninguno directo — se dispara automáticamente desde UC-03.

**Precondición:** un movimiento válido dejó todas las celdas del tablero
llenas.

**Flujo principal:**
1. El sistema marca la partida como "COMPLETADA" y registra la fecha de
   fin.
2. El sistema determina la puntuación máxima alcanzada entre todos los
   jugadores de la partida.
3. El sistema declara ganadores a todos los jugadores cuya puntuación
   sea igual a esa puntuación máxima (RF-10) — si dos o más jugadores
   empatan en el máximo, todos son ganadores, no se elige uno solo.

**Flujo(s) alternativo(s):**
- Ninguno adicional. **Decisión confirmada 2026-09-30**
  (docente/cliente): en caso de empate se muestran todos los ganadores
  empatados.

**Postcondición:** la partida queda inactiva, con uno o más ganadores
asignados.

**Relaciones con otros casos de uso:** ver UC-03 (relación extend
declarada ahí).

## UC-05: Consultar estado de partida

**Historias de usuario:** HU-10

**Actor:** Jugador (o cualquier cliente con el id de partida)

**Precondición:** la partida existe (activa o no).

**Flujo principal:**
1. El cliente envía el id de partida.
2. El sistema busca la partida.
3. El sistema devuelve tablero actual, celdas iniciales, jugadores,
   puntuaciones, estado y ganador (RF-13).

**Flujo(s) alternativo(s):**
- Si la partida no existe, el sistema responde `ERROR`.

**Postcondición:** ninguna (operación de solo lectura).

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| — | UC-01, UC-02, UC-03 | — | No es include ni extend de ninguno: se puede consultar el estado en cualquier momento, independientemente de qué otro caso de uso se haya ejecutado antes. |

## UC-06: Listar partidas

**Historias de usuario:** HU-02

**Actor:** Jugador

**Precondición:** ninguna.

**Flujo principal:**
1. El jugador pide la lista de partidas.
2. El sistema devuelve las partidas activas (RF-14).
3. El sistema devuelve, por separado, las partidas disponibles para
   unirse: activas, con espacio y en estado "ESPERANDO" (RF-14).

**Flujo(s) alternativo(s):**
- Si no hay partidas, el sistema devuelve listas vacías.

**Postcondición:** ninguna (operación de solo lectura).

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| — | UC-02 Unirse a partida | — | Listar ayuda a elegir una partida, pero unirse no lo requiere: alcanza con conocer el id. |

> [NEEDS CLARIFICATION: hoy el sistema calcula ambas listas pero no
> expone ningún mensaje para que el jugador las pida. Confirmar si este
> caso de uso debe estar disponible para el jugador.]

## UC-07: Salir de partida

**Historias de usuario:** HU-08

**Actor:** Jugador (debe estar unido a una partida — UC-02)

**Precondición:** el jugador está en una partida.

**Flujo principal:**
1. El jugador pide salir de la partida.
2. El sistema quita al jugador y su puntuación de la partida (RF-11).
3. Si no queda ningún jugador, el sistema pasa la partida a
   "CANCELADA" y la marca como inactiva (RF-11).

**Flujo(s) alternativo(s):**
- Si el jugador no está en la partida, no se modifica nada.

**Postcondición:** el jugador ya no forma parte de la partida.

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| requiere | UC-02 Unirse a partida | include | Solo puede salir un jugador que se haya unido antes. |

> [NEEDS CLARIFICATION: hoy la lógica de quitar al jugador existe en la
> partida, pero ningún mensaje ni servicio la invoca, así que el jugador
> no tiene forma de salir. Tampoco está definido si, al salir, el
> jugador queda libre para unirse a otra partida.]

## UC-08: Desconectarse y reconectarse

**Historias de usuario:** HU-09

**Actor:** Jugador (debe estar unido a una partida — UC-02)

**Precondición:** el jugador está en una partida activa.

**Flujo principal:**
1. El jugador pierde la conexión.
2. El sistema lo marca como desconectado, sin quitarlo de la partida
   (RF-12).
3. El jugador vuelve a conectarse.
4. Si la partida sigue activa, el sistema lo marca como conectado y
   conserva su puntuación (RF-12).

**Flujo(s) alternativo(s):**
- Si la partida ya no está activa, la reconexión se rechaza.

**Postcondición:** el jugador sigue en la partida, conectado y con su
puntuación.

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| requiere | UC-02 Unirse a partida | include | Solo se puede desconectar/reconectar un jugador que ya está en una partida. |

> **Bug conocido (2026-09-30):** al desconectarse se pierde el vínculo
> entre el jugador y su partida, así que la reconexión (paso 4) siempre
> falla. Ver observaciones en `requirements.md`.
>
> [NEEDS CLARIFICATION: hoy ningún mensaje ni evento de conexión dispara
> la desconexión o la reconexión. Confirmar cómo se detecta que el
> jugador se desconectó y cómo pide reconectarse.]

## UC-09: Limpiar partidas terminadas

**Historias de usuario:** HU-11

**Actor:** Administrador del sistema

**Precondición:** ninguna.

**Flujo principal:**
1. Se ejecuta la limpieza.
2. El sistema elimina las partidas en estado "COMPLETADA" o "CANCELADA"
   (RF-15).
3. El sistema libera a los jugadores de esas partidas, que quedan
   disponibles para unirse a otra.

**Flujo(s) alternativo(s):**
- Si no hay partidas terminadas, no se elimina nada.

**Postcondición:** solo quedan en memoria las partidas "ESPERANDO" y
"EN_JUEGO".

**Relaciones con otros casos de uso:**

| Relación | Caso de uso relacionado | Tipo | Justificación |
|----------|--------------------------|------|----------------|
| — | UC-04, UC-07 | — | No los extiende ni los incluye: limpia partidas que esos casos de uso dejaron terminadas, pero se ejecuta por separado. |

> [NEEDS CLARIFICATION: hoy la limpieza existe pero nada la ejecuta (ni
> un mensaje del administrador ni una tarea periódica). Confirmar
> quién la dispara y cada cuánto.]
