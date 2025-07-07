# Sebxstt

¡Bienvenido a la documentación de **Sebxstt**! Este es un plugin para PaperMC que implementa sistemas de checkpoints y grupos, permitiendo a los jugadores crear puntos de teletransporte y gestionar equipos con diferentes roles.

---

## Índice

1. [Características Principales](#características-principales)
2. [Instalación](#instalación)
3. [Configuración](#configuración)
4. [Sistema de Grupos](#sistema-de-grupos)
   - [Roles y Funcionalidades](#roles-y-funcionalidades)
5. [Comandos](#comandos)
   - [Comandos de Checkpoint](#comandos-de-checkpoint)
   - [Comandos de Grupo](#comandos-de-grupo)
   - [Comandos de Jugador](#comandos-de-jugador)
6. [Almacenamiento de Datos](#almacenamiento-de-datos)

---

## Características Principales

- **Sistema de Checkpoints**: Crea puntos personales de teletransporte con nombres personalizados.
- **Sistema de Grupos**: Permite crear y gestionar equipos con diferentes roles y colores distintivos.
- **Invitaciones a Grupos**: Sistema de invitaciones con aceptación/rechazo por parte de los jugadores.
- **Regreso al Punto de Muerte**: Comando para volver al lugar donde moriste por última vez (con cooldown configurable).
- **Almacenamiento Optimizado**: Toda la información se guarda en un único archivo JSON.
- **Sistema de Compartición de EXP**: Distribución automática de experiencia entre miembros cercanos del mismo grupo.

---

## Instalación

1. Descarga el archivo `Sebxstt-1.0.jar` desde la página de releases.
2. Coloca el archivo `.jar` en la carpeta `plugins/` de tu servidor PaperMC.
3. Reinicia el servidor. Se creará automáticamente la carpeta `plugins/Sebxstt/`.

---

## Configuración

El plugin utiliza un archivo `config.yml` simple con los siguientes parámetros:
```yaml
CooldownLastDeathCheckPoint: 1    # Tiempo de cooldown en minutos para el comando de retorno al último punto de muerte
MaxCheckPoints: 3               # Número máximo de checkpoints que puede tener un jugador
fileDataSaved: "data.json"      # Nombre del archivo de datos JSON
AutoSaveTime: 5                # Intervalo de autoguardado en minutos
```

---

## Sistema de Grupos

El plugin implementa un sistema de grupos que permite a los jugadores formar equipos con diferentes colores distintivos. Los grupos tienen un sistema de roles para administrar los permisos.

### Roles y Funcionalidades

Existen 4 roles en el sistema de grupos:

- **MANAGER**: Rol con control total sobre el grupo (manejo de equipo y miembros)
- **CONTROLLER**: Rol con permisos limitados para colaborar con los manejadores del grupo
- **VIEWER**: Miembro que puede involucrarse en acciones del equipo como inspección
- **DENIED**: Miembro común sin permisos especiales en el grupo

### Características de los Grupos

- Cada grupo tiene un color distintivo que se muestra en el chat
- Sistema de inventario compartido para los miembros del grupo
- Protección de PvP entre miembros del mismo grupo
- Distribución de experiencia entre miembros cercanos

---

## Comandos

### Comando Checkpoint

| Comando | Descripción | Permisos |
|---------|-------------|----------|
| `/checkpoint save [nombre]` | Guarda un checkpoint en tu ubicación actual | `sebxstt.command.checkpoint` |
| `/checkpoint delete <nombre>` | Elimina un checkpoint guardado | `sebxstt.command.checkpoint` |
| `/checkpoint teleport <nombre>` | Te teletransporta a un checkpoint guardado | `sebxstt.command.checkpoint` |
| `/checkpoint tp <nombre>` | Alias para teleport | `sebxstt.command.checkpoint` |

### Comando Group

| Comando                       | Descripción                         | Requisitos |
|-------------------------------|-------------------------------------|------------|
| `/group create <nombre> <color>` | Crea un nuevo grupo                 | `sebxstt.command.group` |
| `/group chat`                 | Activar o desactivar el chat grupal | Miembro de grupo |
| `/group storage`              | Abre el inventario compartido del grupo | Miembro de grupo |
| `/group info [grupo]`         | Muestra información sobre tu grupo o uno especificado | `sebxstt.command.group` |
| `/group post <jugador> <cargo>` | Cambia el cargo de un miembro       | MANAGER/CONTROLLER |
| `/group leave`                | Abandona tu grupo actual            | Miembro de grupo |
| `/group invite <jugador>`     | Invita a un jugador a tu grupo      | MANAGER/CONTROLLER |
| `/group kick <jugador>`       | Expulsa a un jugador de tu grupo    | MANAGER/CONTROLLER |
| `/group disband`              | Disuelve el grupo                   | Solo MANAGER |

### Comando Player

| Comando | Descripción | Permisos |
|---------|-------------|----------|
| `/test` | Comando de prueba | `sebxstt.command.player` |
| `/stats` o `/est` | Muestra estadísticas del jugador | `sebxstt.command.player` |
| `/clearteams` | Limpia equipos del jugador | `sebxstt.command.player` |
| `/return` | Te teletransporta a tu último punto de muerte | `sebxstt.command.player` |
| `/invitations` o `/inv` | Muestra invitaciones pendientes a grupos | `sebxstt.command.player` |

---

## Almacenamiento de Datos

Toda la información del plugin se almacena en un único archivo JSON definido en la configuración (por defecto `data.json`). Este archivo contiene:

- Información de los jugadores y sus configuraciones
- Checkpoints guardados
- Grupos creados y sus miembros
- Roles de los miembros en cada grupo

El plugin guarda los datos automáticamente en los siguientes momentos:

- Al desconectarse un jugador
- Al apagar el servidor
- Periódicamente según el intervalo definido en la configuración (AutoSaveTime)

El plugin guarda todos los datos en formato JSON en la carpeta `plugins/Sebxstt/`.

- **`data.json`**: Almacena todos los datos del plugin (jugadores, grupos, checkpoints, y relaciones).

---

## Contribuir

Si encuentras algún problema o tienes sugerencias para mejorar el plugin, por favor abre un issue en el repositorio del proyecto.

## Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo LICENSE para más detalles.

---

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![PaperMC](https://img.shields.io/badge/API-PaperMC-yellow.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-green.svg)
![Java](https://img.shields.io/badge/Java-21-red.svg)

&copy; 2025 Sebxstt - Desarrollado para servidores PaperMC
