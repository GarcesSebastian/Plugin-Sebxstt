# Sebxstt

¡Bienvenido a la documentación de **Sebxstt**! Este es un plugin para PaperMC que implementa sistemas avanzados de checkpoints y grupos, permitiendo a los jugadores crear puntos de teletransporte personales y gestionar equipos con diferentes roles y funcionalidades colaborativas.

---

## Índice

1. [Características Principales](#características-principales)
2. [Instalación](#instalación)
3. [Configuración](#configuración)
4. [Sistema de Grupos](#sistema-de-grupos)
   - [Roles y Funcionalidades](#roles-y-funcionalidades)
   - [Chat de Grupo](#chat-de-grupo)
   - [Warps de Grupo](#warps-de-grupo)
5. [Comandos](#comandos)
   - [Comandos de Checkpoint](#comandos-de-checkpoint)
   - [Comandos de Grupo](#comandos-de-grupo)
   - [Comandos de Warps](#comandos-de-warps)
   - [Comandos de Jugador](#comandos-de-jugador)
6. [Almacenamiento de Datos](#almacenamiento-de-datos)

---

## Características Principales

- **Sistema de Checkpoints**: Crea puntos personales de teletransporte con nombres personalizados.
- **Sistema de Grupos**: Permite crear y gestionar equipos con diferentes roles y colores distintivos.
- **Chat Privado de Grupo**: Comunícate exclusivamente con los miembros de tu grupo mediante un canal privado.
- **Warps de Grupo**: Establece y comparte puntos de teletransporte para todo el equipo.
- **Invitaciones a Grupos**: Sistema de invitaciones con aceptación/rechazo por parte de los jugadores.
- **Regreso al Punto de Muerte**: Comando para volver al lugar donde moriste por última vez (con cooldown configurable).
- **Almacenamiento Optimizado**: Toda la información se guarda en un único archivo JSON.
- **Sistema de Compartición de EXP**: Distribución automática de experiencia entre miembros cercanos del mismo grupo.

---

## Instalación

1. Descarga el archivo `Sebxstt-1.0.0.jar` desde la página de releases.
2. Coloca el archivo `.jar` en la carpeta `plugins/` de tu servidor PaperMC.
3. Reinicia el servidor. Se creará automáticamente la carpeta `plugins/Sebxstt/`.

---

## Configuración

El plugin utiliza un archivo `config.yml` simple con los siguientes parámetros:
```yaml
CooldownLastDeathCheckPoint: 1  # Tiempo de cooldown en minutos para el comando de retorno al último punto de muerte
MaxCheckPoints: 3               # Número máximo de checkpoints que puede tener un jugador
fileDataSaved: "data"           # Nombre del archivo de datos
AutoSaveTime: 5                 # Intervalo de autoguardado en minutos
```

---

## Sistema de Grupos

El plugin implementa un sistema de grupos que permite a los jugadores formar equipos con diferentes colores distintivos. Los grupos tienen un sistema de roles para administrar los permisos.

### Roles y Funcionalidades

Existen 4 roles en el sistema de grupos:

- **LEADER**: Propietario del grupo con control total sobre todas las funcionalidades.
- **OFFICER**: Rol con permisos amplios para gestionar el grupo y sus miembros.
- **MEMBER**: Miembro regular que puede utilizar las funcionalidades básicas del grupo.
- **GUEST**: Miembro con permisos limitados, principalmente de visualización.

### Permisos por Rol

| Permiso | LEADER | OFFICER | MEMBER | GUEST |
|---------|:------:|:-------:|:-------:|:----------:|
| Gestionar miembros (invitar/expulsar) | ✅ | ✅ | ❌ | ❌ |
| Cambiar roles de miembros | ✅ | ✅ | ❌ | ❌ |
| Disolver grupo | ✅ | ❌ | ❌ | ❌ |
| Gestionar warps (crear/eliminar) | ✅ | ✅ | ❌ | ❌ |
| Usar warps | ✅ | ✅ | ✅ | ✅ |
| Teleportar a todos miembros | ✅ | ✅ | ❌ | ❌ |
| Acceder al almacenamiento | ✅ | ✅ | ✅ | ✅ |
| Usar chat de grupo | ✅ | ✅ | ✅ | ✅ |

### Chat de Grupo

El plugin incorpora un sistema de chat privado de grupo que permite a los miembros comunicarse exclusivamente entre ellos:

- Se activa/desactiva con el comando `/gchat on|off`
- Los mensajes son visibles solo para los miembros del grupo
- El formato del mensaje incluye el nombre y color del grupo
- El estado del chat grupal persiste entre sesiones

### Warps de Grupo

Los grupos pueden establecer puntos de teletransporte compartidos:

- Cada grupo puede crear múltiples warps en diferentes ubicaciones
- Los warps son accesibles para todos los miembros del grupo
- Miembros con permisos avanzados pueden teleportar a todos los miembros del grupo a un warp
- También es posible teleportar solo a miembros con un cargo específico

---

## Comandos

### Comandos de Checkpoint

| Comando | Alias | Descripción | Permisos |
|---------|-------|-------------|----------|
| `/checkpoint save <nombre>` | `/cp save` | Guarda un checkpoint en tu ubicación actual | `sebxstt.command.checkpoint` |
| `/checkpoint delete <nombre>` | `/cp delete` | Elimina un checkpoint guardado | `sebxstt.command.checkpoint` |
| `/checkpoint tp <nombre>` | `/cp tp` | Te teletransporta a un checkpoint guardado | `sebxstt.command.checkpoint` |
| `/checkpoint list` | `/cp list` | Muestra todos tus checkpoints guardados | `sebxstt.command.checkpoint` |

### Comandos de Grupo

| Comando | Alias | Descripción | Requisitos |
|---------|-------|-------------|------------|
| `/gcreate <color> <nombre>` | `/gc` | Crea un nuevo grupo con color y nombre | `sebxstt.command.group` |
| `/ginfo` | `/gi` | Muestra la información actual del grupo | Miembro de grupo |
| `/gleave` | `/lv` | Abandona tu grupo actual | No ser LEADER |
| `/ginvite <cargo> <jugador>` | `/iv` | Invita a un jugador con cargo específico | LEADER/OFFICER |
| `/gkick <jugador>` | `/gk` | Expulsa a un jugador del grupo | LEADER/OFFICER |
| `/gdisband` | `/gd` | Disuelve el grupo completamente | Solo LEADER |
| `/grole <cargo> <jugador>` | `/gr` | Cambia el cargo de un miembro | LEADER/OFFICER |
| `/gstorage` | `/st` | Abre el almacenamiento compartido del grupo | Miembro de grupo |
| `/gchat <on\|off>` | `/ch` | Activa/desactiva el chat de grupo | Miembro de grupo |

### Comandos de Warps

| Comando | Descripción | Requisitos |
|---------|-------------|------------|
| `/gwarp create <nombre>` | Crea un warp en tu ubicación actual | LEADER/OFFICER |
| `/gwarp delete <nombre>` | Elimina un warp existente | LEADER/OFFICER |
| `/gwarp list` | Muestra todos los warps del grupo | Miembro de grupo |
| `/gwarp tp <nombre>` | Te teletransporta al warp especificado | Miembro de grupo |
| `/gwarp all <nombre>` | Teletransporta a todos los miembros al warp | LEADER/OFFICER |
| `/gwarp post <cargo> <nombre>` | Teletransporta a miembros con cargo específico | LEADER/OFFICER |

### Comandos de Jugador

| Comando | Alias | Descripción | Permisos |
|---------|-------|-------------|----------|
| `/stats` | `/est` | Muestra estadísticas del jugador | `sebxstt.command.player` |
| `/clearteams` | - | Limpia equipos del jugador | `sebxstt.command.player` |
| `/return` | - | Te teletransporta a tu último punto de muerte | `sebxstt.command.player` |
| `/invitations` | `/inv` | Gestiona invitaciones pendientes a grupos | `sebxstt.command.player` |

---

## Almacenamiento de Datos

Toda la información del plugin se almacena en un único archivo JSON definido en la configuración (por defecto `data.json`). Este archivo contiene:

- Información de los jugadores y sus configuraciones
- Checkpoints guardados
- Grupos creados y sus miembros
- Roles de los miembros en cada grupo
- Warps de grupo
- Configuración del chat de grupo

El plugin guarda los datos automáticamente en los siguientes momentos:

- Al desconectarse un jugador
- Al apagar el servidor
- Periódicamente según el intervalo definido en la configuración (AutoSaveTime)

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
