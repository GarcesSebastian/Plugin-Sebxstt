package com.sebxstt.instances.ingame.inventory.instances;

import com.sebxstt.instances.ingame.inventory.NextInventory;

import java.util.ArrayList;
import java.util.UUID;

import static com.sebxstt.instances.ingame.inventory.InventoryHelper.pagination;

public class NextPage {
    private UUID id;
    private int index;

    private Integer maxStack;
    private ArrayList<UUID> stack = new ArrayList<>();

    public NextPage (NextInventory nextInventory) {
        this.id = UUID.randomUUID();
        this.maxStack = nextInventory.getSize().getContentSlots();
    }

    public NextPage insert(NextItem instance) throws IllegalStateException {
        if (stack.size() > maxStack) {
            throw new IllegalStateException("This Page is Full");
        }

        if (instance.getPageID() != null) {
            NextPage beforePage = pagination(instance.getPageID(), instance.getParent());
            if (beforePage == null) throw new IllegalStateException("[NextPage] Not Page Found " + instance.getPageID());
            beforePage.remove(instance);
        }

        this.stack.add(instance.getId());
        instance.setPageID(this.id);
        return this;
    }

    public NextPage remove(NextItem instance) {
        this.stack.remove(instance.getId());
        instance.setPageID(null);

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
