package io.papermc.sebxstt.instances.ingame.inventory.instances;

import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;
import io.papermc.sebxstt.instances.ingame.inventory.NextInventoryProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.papermc.sebxstt.instances.ingame.inventory.InventoryHelper.next;

public class ItemInventory {
    private UUID id;
    private String name;
    private String description;
    private Material materialType;
    private UUID parent;

    private boolean draggable = true;

    private ItemStack instance;
    private ItemMeta meta;
    private int index;

    public ItemInventory(String name, String description, Material materialType, NextInventory parent) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.materialType = materialType;

        this.parent = parent.id;
        this.instance = new ItemStack(materialType);
        this.meta = this.instance.getItemMeta();

        meta.setDisplayName(this.name);
        meta.setUnbreakable(true);

        if (!description.isBlank()) {
            List<String> lore = new ArrayList<>();
            lore.add(description);
            meta.setLore(lore);
        }

        this.instance.setItemMeta(this.meta);
        this.index = 0;
        parent.getItems().add(this);
    }

    public ItemInventory draggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public boolean isDraggable() {
        return this.draggable;
    }

    public void render() {
        this.meta = this.instance.getItemMeta();

        meta.setDisplayName(this.name);
        meta.setUnbreakable(true);

        if (!description.isBlank()) {
            List<String> lore = new ArrayList<>();
            lore.add(description);
            meta.setLore(lore);
        }

        this.instance.setItemMeta(this.meta);
        next(this.parent).emitRefresh();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterialType() {
        return materialType;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public int getIndex() {
        return index;
    }

    public UUID getParent() {
        return parent;
    }

    public ItemStack getInstance() {
        return instance;
    }

    public void setIndex(int index) {
        this.render();
        this.index = index;
    }

    public void setName(String name) {
        this.render();
        this.name = name;
    }

    public void setDescription(String description) {
        this.render();
        this.description = description;
    }

    public void setMaterialType(Material materialType) {
        this.render();
        this.materialType = materialType;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }
}
