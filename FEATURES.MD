# Sebxstt Plugin - Próximas Features

**Versión:** v1.0.0

Este documento describe una serie de **nuevas funcionalidades** propuestas para el plugin **Sebxstt**, un sistema avanzado de gestión de grupos en servidores PaperMC. Estas features buscan enriquecer la experiencia de los jugadores y ofrecer herramientas colaborativas dentro del entorno de Minecraft.

Cada funcionalidad se presenta con su objetivo, flujo de implementación propuesto o ya aplicado, junto con un análisis de sus ventajas y posibles inconvenientes.

---

## ✅ 1. Chat Privado de Grupo (Implementado)

**Objetivo:** Permitir que los miembros de un grupo se comuniquen exclusivamente entre ellos a través de un canal privado persistente.

**Estado:** ✅ Ya implementado.

**Flujo de implementación final:**

1. Comando `/group chat on|off` activa o desactiva el estado de chat grupal del jugador.
2. Este estado se almacena en la propiedad persistente `ChatEnabledGroup` dentro de `PlayerConfig` y se serializa correctamente en `PlayerConfigData`.
3. El evento `AsyncChatEvent` detecta si el jugador tiene el chat grupal activado:
   - Si está activado, el evento se cancela.
   - El mensaje se envía exclusivamente a los miembros del grupo.
   - El formato utilizado es:  
     `"[Grupo: <color>NombreGrupo</color>] <gray>Jugador</gray>: Mensaje"`
4. El estado se restaura automáticamente al reconectar, con una notificación visual al jugador.
5. La implementación es compatible con el sistema de colores y MiniMessage.

**Pros:**

- Mejora la comunicación interna del grupo.
- Se mantiene el orden en el chat global.
- Persistente entre sesiones del jugador.
- Estructura clara y estilizada para mensajes grupales.

**Contras:**

- Cancelar `AsyncChatEvent` requiere gestión cuidadosa para evitar conflictos con otros plugins.
- Necesita control constante del estado de cada jugador.

---

## 2. Warps de Grupo

**Objetivo:** Permitir que los grupos establezcan y usen puntos de teletransporte compartidos.

**Flujo de implementación:**

1. Comando `/g setwarp <nombre>` para registrar una ubicación actual del jugador como warp.
2. Guardar los warps en el objeto del grupo (`PlayersGroup`) con nombre, posición y fecha de creación.
3. Comando `/g warp <nombre>` para teletransportarse.
4. Comando `/g warps` para listar todos los warps disponibles del grupo.
5. Comando `/g delwarp <nombre>` para eliminar un warp.

**Pros:**

- Facilita el acceso rápido a zonas clave del grupo.
- Aumenta la coordinación entre miembros.
- Base sólida para futuros eventos de grupo o regiones.

**Contras:**

- Puede representar riesgo si se abusa para evadir combates o zonas protegidas.
- Necesita sistema de permisos por rol o tipo de miembro.

---

## 3. Roles y Permisos Dentro del Grupo

**Objetivo:** Establecer una jerarquía de control y responsabilidades dentro del grupo.

**Flujo de implementación:**

1. Definir roles preestablecidos como: `LÍDER`, `MANAGER`, `MIEMBRO`, `OBSERVADOR`.
2. Asociar acciones permitidas a cada rol (editar baúl, invitar/kickear miembros, gestionar warps, etc.).
3. Comando `/g setrole <jugador> <rol>` para asignar o cambiar roles.
4. Validar permisos antes de ejecutar acciones restringidas.

**Pros:**

- Ofrece control detallado sobre la estructura del grupo.
- Reduce el riesgo de abuso por parte de miembros no autorizados.
- Escalable para futuros sub-roles o personalización.

**Contras:**

- Aumenta la complejidad del sistema de permisos.
- Requiere documentación clara para los usuarios.
- Puede generar conflictos si no se manejan bien los límites de cada rol.

---

## 4. Sistema de Votación Interna

**Objetivo:** Permitir que los grupos tomen decisiones colectivas mediante votaciones internas.

**Flujo de implementación:**

1. Comando `/g vote <tipo> <objetivo>` (ej. expulsar a un jugador, activar modo PvP, iniciar evento).
2. Todos los miembros reciben notificación y pueden votar `/g yes` o `/g no`.
3. Se define un tiempo límite y un quorum mínimo para que la votación sea válida.
4. Al finalizar, se ejecuta la acción o se informa que no se alcanzó consenso.

**Pros:**

- Democratiza decisiones importantes.
- Fomenta la participación de todos los miembros.
- Adaptable a muchos contextos (PvP, eventos, expulsiones).

**Contras:**

- Requiere manejo de tiempo, votos nulos, empates.
- Puede generar conflictos en grupos con poca participación.
- Necesita interfaz de votación clara y seguimiento de votos.

---

## 5. Eventos de Grupo

**Objetivo:** Crear desafíos o actividades internas diseñadas para fomentar la cooperación entre los miembros del grupo.

**Flujo de implementación:**

1. Se diseñan tipos de eventos: PvE (bosses), PvP amistoso, desafíos de construcción, etc.
2. Comando `/g event start <tipo>` inicia un evento.
3. Los miembros son teletransportados a una zona delimitada.
4. Al finalizar el evento se reparten recompensas automáticas.

**Pros:**

- Refuerza el trabajo en equipo y competitividad sana.
- Mejora la retención de jugadores.
- Base para temporadas o torneos internos.

**Contras:**

- Alto nivel de desarrollo técnico (balanceo, IA, recompensas).
- Requiere control de exploits y zonas protegidas.
- Necesario sistema de cooldowns o límites de participación.

---

> **Nota:** Estas funcionalidades están diseñadas para complementar el sistema actual de grupos en Sebxstt. Se recomienda implementarlas de manera modular, evaluando dependencias y prioridades según las necesidades de la comunidad y la estabilidad del servidor.

---

**¡Manos a la obra!**  
Este plan te servirá como guía de desarrollo y documentación, facilitando la organización del roadmap para Sebxstt v1.0.0 y futuras versiones.
