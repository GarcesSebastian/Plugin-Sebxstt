package io.papermc.sebxstt.instances.ingame.inventory.instances;

import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;

import java.util.ArrayList;
import java.util.UUID;

public class NextPage {
    private UUID id;
    private int index;

    private Integer maxStack;
    private ArrayList<UUID> stack = new ArrayList<>();

    public NextPage (NextInventory nextInventory) {
        this.id = UUID.randomUUID();
        this.maxStack = nextInventory.getSize().getContentSlots();
    }

    public NextPage insert(UUID instance) {
        if (stack.size() > maxStack) {
            throw new IllegalStateException("This Page is Full");
        }
        this.stack.add(instance);
        return this;
    }

    public NextPage index(int index) {
        this.index = index;
        System.out.println("[NextPage] index: " + index);
        return this;
    }

    public UUID getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<UUID> getStack() {
        return stack;
    }

    public Integer getMaxStack() {
        return maxStack;
    }
}
