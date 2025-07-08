package io.papermc.sebxstt.instances.ingame.inventory.instances;

import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemInventory {
    public UUID id;
    public String name;
    public String description;
    public Material materialType;
    public UUID parent;

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
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getInstance() {
        return instance;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
