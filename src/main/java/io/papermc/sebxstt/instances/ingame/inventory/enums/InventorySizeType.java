package io.papermc.sebxstt.instances.ingame.inventory.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum InventorySizeType {
    LITTLE(27, 7),
    NORMAL(36, 14),
    MEDIUM(45, 21),
    LARGE(54, 28);

    private final int totalSlots;
    private final int contentSlots;
    private final List<Integer> blockedSlots;
    private final List<Integer> allowedSlots;

    InventorySizeType(int totalSlots, int contentSlots) {
        this.totalSlots   = totalSlots;
        this.contentSlots = contentSlots;

        var blocked = new ArrayList<Integer>();
        var allowed = new ArrayList<Integer>();

        int rows = totalSlots / 9;
        for (int row = 0; row < rows; row++) {
            int offset = row * 9;
            for (int col = 0; col < 9; col++) {
                int slot = offset + col;
                if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                    blocked.add(slot);
                } else {
                    allowed.add(slot);
                }
            }
        }

        if (blocked.size() + allowed.size() != totalSlots) {
            throw new IllegalStateException("Slots mismatch in " + name());
        }

        this.blockedSlots = Collections.unmodifiableList(blocked);
        this.allowedSlots = Collections.unmodifiableList(allowed);
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getContentSlots() {
        return contentSlots;
    }

    public List<Integer> getBlockedSlots() {
        return blockedSlots;
    }

    public List<Integer> getAllowedSlots() {
        return allowedSlots;
    }
}
