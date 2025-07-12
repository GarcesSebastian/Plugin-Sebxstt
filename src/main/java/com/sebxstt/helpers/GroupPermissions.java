package com.sebxstt.helpers;

import com.sebxstt.instances.enums.PlayerTypeGroup;

public class GroupPermissions {
    public static boolean canUseWarps(PlayerTypeGroup type) {
        return type == PlayerTypeGroup.LEADER
                || type == PlayerTypeGroup.OFFICER
                || type == PlayerTypeGroup.MEMBER;
    }

    public static boolean canManageWarps(PlayerTypeGroup type) {
        return type == PlayerTypeGroup.LEADER || type == PlayerTypeGroup.OFFICER;
    }

    public static boolean canManagerMembers(PlayerTypeGroup type) {
        return type == PlayerTypeGroup.LEADER || type == PlayerTypeGroup.OFFICER;
    }

    public static boolean canEditStorage(PlayerTypeGroup type) {
        return type == PlayerTypeGroup.LEADER || type == PlayerTypeGroup.OFFICER;
    }

    public static boolean canOpenStorage(PlayerTypeGroup type) {
        return type == PlayerTypeGroup.LEADER || type == PlayerTypeGroup.OFFICER ||  type == PlayerTypeGroup.MEMBER;
    }
}