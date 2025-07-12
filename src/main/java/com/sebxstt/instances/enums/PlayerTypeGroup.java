package com.sebxstt.instances.enums;

public enum PlayerTypeGroup {
    LEADER,       // Máxima autoridad del grupo: todo el control (kick, roles, warps, baúl, disolver)
    OFFICER,      // Puede invitar, gestionar warps, editar el baúl y ayudar en la moderación
    MEMBER,       // Participa, chatea, accede al baúl, usa warps, pero sin permisos de edición
    GUEST,        // Solo lectura y visualización (no puede interactuar con baúl ni warps)
    NONE        // Usuario sin grupo
}
