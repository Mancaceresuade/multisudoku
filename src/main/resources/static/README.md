# Multisudoku - Páginas del Juego

## Descripción
Multisudoku es un juego de Sudoku multijugador que permite a varios jugadores competir en tiempo real usando WebSockets.

## Páginas Disponibles

### 1. Página de Inicio (`index.html`)
- **Propósito**: Página principal de bienvenida
- **Funcionalidad**: 
  - Presenta el juego y sus características
  - Proporciona enlaces para crear o unirse a partidas
  - No requiere conexión WebSocket

### 2. Crear Partida (`create-game.html`)
- **Propósito**: Permite a un jugador crear una nueva partida
- **Funcionalidad**:
  - Conexión WebSocket automática
  - Formulario para ingresar nombre del jugador
  - Selección de dificultad (Fácil, Medio, Difícil)
  - Creación de partida con ID único
  - Compartir ID de partida con otros jugadores
  - Redirección automática al juego

### 3. Unirse a Partida (`join-game.html`)
- **Propósito**: Permite a un jugador unirse a una partida existente
- **Funcionalidad**:
  - Conexión WebSocket automática
  - Formulario para ingresar nombre del jugador
  - Campo para ingresar ID de partida
  - Validación de partida existente
  - Redirección automática al juego

### 4. Juego Principal (`game.html`)
- **Propósito**: Interfaz principal del juego
- **Funcionalidad**:
  - Tablero de Sudoku interactivo
  - Pad numérico para seleccionar números
  - Sistema de puntuaciones en tiempo real
  - Mensajes del juego
  - Estado de conexión
  - Información de la partida

## Flujo de Uso

1. **Inicio**: El usuario visita `index.html`
2. **Crear Partida**: 
   - Hace clic en "Crear Partida"
   - Va a `create-game.html`
   - Ingresa su nombre y selecciona dificultad
   - Crea la partida y obtiene un ID
   - Comparte el ID con otros jugadores
3. **Unirse a Partida**:
   - Hace clic en "Unirse a Partida"
   - Va a `join-game.html`
   - Ingresa su nombre y el ID de partida
   - Se une a la partida existente
4. **Jugar**:
   - Ambos jugadores son redirigidos a `game.html`
   - Pueden jugar en tiempo real
   - Los movimientos se sincronizan automáticamente

## Características Técnicas

- **WebSockets**: Comunicación en tiempo real
- **STOMP**: Protocolo de mensajería sobre WebSocket
- **Responsive Design**: Funciona en dispositivos móviles y desktop
- **Local Storage**: Persistencia de información del jugador
- **Interfaz Moderna**: Diseño atractivo y fácil de usar

## Navegación

- `index.html` → `create-game.html` → `game.html`
- `index.html` → `join-game.html` → `game.html`

## Dependencias

- SockJS para WebSocket
- STOMP.js para mensajería
- CSS3 para estilos
- JavaScript vanilla para funcionalidad
