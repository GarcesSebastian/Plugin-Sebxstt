# Sebxstt Plugin - Próximas Features

**Versión:** v1.0.0

Este documento describe una serie de **nuevas funcionalidades** propuestas para el plugin **Sebxstt**, un sistema avanzado de gestión de grupos en servidores PaperMC. Estas features buscan enriquecer la experiencia de los jugadores y ofrecer herramientas colaborativas dentro del entorno de Minecraft.

## 📋 Resumen de Features

| #  | Funcionalidad                 | Estado           | Fecha         |
|----|-------------------------------|------------------|---------------|
| 1  | Chat Privado de Grupo         | ✅ Implementado   | 07/07/2025    |
| 2  | Warps de Grupo                | ✅ Implementado   | 08/07/2025    |
| 3  | Roles y Permisos              | 📝 Planeado      | -             |
| 4  | Sistema de Votación Interna   | 📝 Planeado      | -             |
| 5  | Eventos de Grupo              | 📝 Planeado      | -             |
| 6  | Menú Interactivo de Grupo     | 🔄 En desarrollo | -             |

Cada funcionalidad se presenta con su objetivo, flujo de implementación propuesto o ya aplicado, junto con un análisis de sus ventajas y posibles inconvenientes.

---

## 1. Chat Privado de Grupo

**Objetivo:** Permitir que los miembros de un grupo se comuniquen exclusivamente entre ellos a través de un canal privado persistente.

**Estado:** ✅ Implementado (07/07/2025)

**Flujo de implementación final:**

1. Comando `/gchat on|off` activa o desactiva el estado de chat grupal del jugador.
2. Este estado se almacena en la propiedad persistente `ChatEnabledGroup` dentro de `PlayerConfig` y se serializa correctamente en `PlayerConfigData`.
3. El evento `AsyncChatEvent` detecta si el jugador tiene el chat grupal activado:
   - Si está activado, el evento se cancela.
   - El mensaje se envía exclusivamente a los miembros del grupo.
   - El formato utilizado es:  
     `"[Grupo: <color>NombreGrupo</color>] <gray>Jugador</gray>: Mensaje"`
4. El estado se restaura automáticamente al reconectar, con una notificación visual al jugador.
5. El comando actual registrado para esto es `/gchat`, y se recomienda también el alias corto `gc`.

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

**Estado:** ✅ Implementado (08/07/2025)  

**Flujo de implementación final:**

1. Comando `/gwarp create <warp>`: crea un nuevo warp grupal en la ubicación actual del jugador.
2. Comando `/gwarp delete <warp>`: elimina un warp existente del grupo.
3. Comando `/gwarp list`: muestra todos los warps disponibles para el grupo.
4. Comando `/gwarp tp <warp>`: teletransporta al jugador que ejecuta el comando al warp especificado.
5. Comando `/gwarp all <warp>`: teletransporta a **todos los miembros del grupo** al warp.
6. Comando `/gwarp post <cargo> <warp>`: teletransporta **solo a los miembros con un cargo específico** al warp.
7. Comando registrado como `/gwarp`, con alias corto sugerido: `gwp`.

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

**Estado:** 🔄 En desarrollo

**Flujo de implementación:**

1. Definir roles preestablecidos como: `LÍDER`, `OFFI`, `MIEMBRO`, `OBSERVADOR`.
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

**Estado:** 📝 Planeado

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

**Estado:** 📝 Planeado

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

## 6. Menú Interactivo de Grupo (GUI In-Game)

**Objetivo:** Ofrecer a los jugadores una manera rápida, accesible y visual de gestionar su grupo sin necesidad de escribir comandos manualmente, mediante un sistema de inventarios personalizados (GUIs) construido sobre la clase `InventoryGUI`.

**Estado:** 🔄 En desarrollo

---

### 🧩 Plan Estratégico de Desarrollo del Sistema GUI

Para implementar correctamente el menú interactivo del grupo y asegurar su escalabilidad y mantenibilidad, se plantea la construcción de una librería modular con los siguientes objetivos y componentes:

#### 🎯 Objetivo General
Desarrollar un sistema extensible y reutilizable para crear y gestionar interfaces gráficas (inventarios) que permita construir menús tipo cofre con acciones dinámicas, soporte para paginación, navegación, condiciones, y más.

---

### 🧱 Componentes Fundamentales

#### 1. Núcleo: `InventoryGUI`
- Controla el estado de un inventario (UUID, tamaño, título, tipo).
- Soporta diferentes tipos: `NORMAL`, `PAGINATION`, `SCROLLING`.
- Permite abrir/cerrar el inventario y acceder a la instancia real.

#### 2. Manejador de Eventos Global
- Captura y dirige todos los clics, arrastres y cierres.
- Asocia automáticamente las acciones al inventario correspondiente.
- Previene interacciones indebidas mediante `event.setCancelled(true)`.

#### 3. Sistema de Componentes Interactivos
- Permite crear ítems con comportamiento:
   - 📦 Botón con acción
   - 📄 Elemento estático
   - 🔁 Navegador de páginas
   - 📜 Selector de opciones
   - 🧠 Condicional según permisos o estado del jugador

#### 4. Renderizado Dinámico
- Métodos `render()` o `refresh()` para actualizar contenido.
- Ideal para menús que reflejan el estado del grupo en tiempo real.
- Opcionalmente permite animaciones simples o rotación de ítems.

#### 5. Navegación Interna
- Implementar historial o pila de navegación (`goBack()`).
- Soporte para submenús con retorno.
- Configurable por permisos o estados (ej. solo líderes acceden a ciertas secciones).

#### 6. Sistema de Condiciones
- Asocia componentes con condiciones dinámicas:
   - `si jugador es líder`
   - `si tiene x ítem`
   - `si está en cooldown`
- Evita mostrar botones que no aplican al jugador actual.

#### 7. Soporte para Archivos Externos (Opcional)
- Definir menús o plantillas desde `.yml` o `.json`.
- Útil para modificar GUIs sin recompilar el plugin.

#### 8. Herramientas de Debug y Log
- Activar logs para aperturas, clics, errores.
- Modo debug para desarrolladores (`/gmenu debug`).

---

### 🧭 Flujo de Interacción Propuesto

1. El jugador ejecuta `/gmenu` o `/group menu`.
2. Se abre un inventario tipo cofre con las siguientes secciones:
   - 📜 Información del grupo
   - 🎭 Gestión de miembros
   - 🚪 Warps del grupo
   - ✉️ Invitaciones pendientes
   - ⚙️ Configuraciones
3. Cada sección abre submenús específicos.
4. Los botones ejecutan acciones o cambian la vista.
5. El menú se cierra automáticamente tras ciertas acciones o se puede navegar hacia atrás.

---

### ✅ Ventajas

- ✅ Experiencia intuitiva y visual
- ✅ Accesibilidad para usuarios sin comandos
- ✅ Modular y fácilmente ampliable
- ✅ Reducción de errores humanos por comandos mal escritos

### ⚠️ Desafíos

- ⚠️ Manejo robusto de eventos y navegación
- ⚠️ Sincronización en tiempo real con datos persistentes
- ⚠️ Control de permisos por rol dentro del grupo
- ⚠️ Necesidad de pruebas cuidadosas para prevenir exploits

---

> **Nota:** Este sistema GUI servirá como base para todos los menús interactivos del plugin Sebxstt. Su diseño modular permitirá integrar futuras funcionalidades sin reescribir lógica existente, manteniendo el código limpio y desacoplado.

---

**¡Manos a la obra!**  
Este plan te servirá como guía de desarrollo y documentación, facilitando la organización del roadmap para Sebxstt v1.0.0 y futuras versiones.
