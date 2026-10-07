# Historias de usuario

> Cada historia se vincula con los requerimientos funcionales de
> `requirements.md` (columna "Historia(s) de usuario").

## HU-01: Crear una partida

**Como** jugador **quiero** crear una partida nueva eligiendo la
dificultad **para** jugar un Sudoku acorde a mi nivel.

**Criterios de aceptación:**
- Puedo elegir FACIL, MEDIO o DIFICIL; si no elijo, la partida es MEDIO.
- La partida recibe un id único que puedo compartir con otros jugadores.
- El tablero es un Sudoku 9x9 válido con 45 (FACIL), 35 (MEDIO) o 25
  (DIFICIL) celdas iniciales.

**Requerimientos:** RF-01, RF-02

## HU-02: Ver partidas disponibles

**Como** jugador **quiero** ver la lista de partidas a las que me puedo
unir **para** elegir una sin necesitar que me pasen el id.

**Criterios de aceptación:**
- Se listan las partidas activas.
- Por separado se listan las disponibles para unirse: activas, con
  espacio y en estado "ESPERANDO".

**Requerimientos:** RF-14

## HU-03: Unirme a una partida

**Como** jugador **quiero** unirme a una partida existente con mi nombre
**para** jugar junto a otros jugadores.

**Criterios de aceptación:**
- Me uno indicando el id de partida, mi id de jugador y mi nombre.
- No me puedo unir si ya estoy asociado a otra partida.
- No hay un máximo de jugadores por partida. [NEEDS CLARIFICATION:
  confirmar si el límite ilimitado de jugadores es una decisión de
  producto deliberada.]
- Cuando se une el primer jugador, la partida pasa a "EN_JUEGO".

**Requerimientos:** RF-03, RF-04, RF-05

## HU-04: Colocar un número

**Como** jugador **quiero** colocar un número en una celda del tablero
**para** avanzar en la resolución del Sudoku.

**Criterios de aceptación:**
- El movimiento se rechaza si la celda es una pista inicial.
- El movimiento se rechaza si la posición está fuera del tablero o el
  valor está fuera de 0-9.
- El movimiento se rechaza si el valor ya está en la misma fila, columna
  o subgrid 3x3.

**Requerimientos:** RF-06

## HU-05: Borrar un número

**Como** jugador **quiero** borrar el número de una celda que no sea
inicial **para** corregir un error.

**Criterios de aceptación:**
- Borrar equivale a un movimiento con valor 0.
- No se pueden borrar las pistas iniciales.
- Borrar descuenta 10 puntos al jugador que borra (decisión confirmada
  2026-09-30).

**Requerimientos:** RF-07

## HU-06: Sumar puntos

**Como** jugador **quiero** ganar puntos por cada celda que completo
**para** competir con los demás jugadores.

**Criterios de aceptación:**
- Cada celda completada con un valor distinto de 0 suma 10 puntos.
- Se suman 5 puntos extra por cada fila, columna o subgrid 3x3 que quede
  completo con ese movimiento.
- El puntaje no varía según la dificultad (decisión confirmada
  2026-09-30).
- Mi puntuación se acumula durante toda la partida.

**Requerimientos:** RF-08, RF-09

## HU-07: Saber quién ganó

**Como** jugador **quiero** saber quién ganó cuando se completa el
tablero **para** conocer el resultado de la partida.

**Criterios de aceptación:**
- Cuando todas las celdas están llenas, la partida pasa a "COMPLETADA".
- Ganan todos los jugadores que tengan la puntuación máxima; en caso de
  empate hay varios ganadores (decisión confirmada 2026-09-30).

**Requerimientos:** RF-10

## HU-08: Salir de una partida

**Como** jugador **quiero** salir de una partida **para** dejar de
participar en ella.

**Criterios de aceptación:**
- Al salir, dejo de formar parte de la partida.
- Si no queda ningún jugador, la partida pasa a "CANCELADA".

**Requerimientos:** RF-11

## HU-09: Reconectarme a una partida

**Como** jugador **quiero** poder reconectarme si pierdo la conexión
**para** seguir jugando sin perder mi lugar ni mis puntos.

**Criterios de aceptación:**
- Si me desconecto, sigo figurando en la partida como desconectado.
- Puedo reconectarme mientras la partida siga activa.
- Hoy no se cumple por un bug detectado el 2026-09-30 (ver observaciones
  en `requirements.md`).

**Requerimientos:** RF-12

## HU-10: Consultar el estado de la partida

**Como** jugador **quiero** ver el estado completo de la partida **para**
saber cómo va el tablero y los puntajes.

**Criterios de aceptación:**
- Veo el tablero actual y cuáles son las celdas iniciales.
- Veo los jugadores, sus puntuaciones, el estado de la partida y los
  ganadores si ya terminó.

**Requerimientos:** RF-13

## HU-11: Liberar partidas terminadas

**Como** administrador del sistema **quiero** que se eliminen las
partidas terminadas **para** no acumular partidas que ya no se usan.

**Criterios de aceptación:**
- Al ejecutar la limpieza se eliminan las partidas "COMPLETADA" y
  "CANCELADA".
- Las partidas "ESPERANDO" y "EN_JUEGO" no se eliminan.

**Requerimientos:** RF-15
