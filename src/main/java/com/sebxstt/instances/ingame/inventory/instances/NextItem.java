package com.sebxstt.instances.ingame.inventory.instances;

import com.sebxstt.instances.enums.InventoryType;
import com.sebxstt.instances.ingame.inventory.NextInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.sebxstt.index.mm;
import static com.sebxstt.instances.ingame.inventory.InventoryHelper.*;

public class NextItem {
    private UUID id;
    private UUID parent;
    private UUID pageID;

    private String name;
    private String description;
    private Material materialType;

    private boolean draggable = true;
    private boolean registry = false;
    private boolean button = false;

    private ItemStack instance;
    private ItemMeta meta;
    private int index;

    private Consumer<Player> onClickCallback;

    public NextItem(String name, String description, Material materialType, NextInventory parent) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.materialType = materialType;

        this.parent = parent.id;
        this.instance = new ItemStack(materialType);

        this.update();

        this.index = 0;
        parent.getItems().add(this);
    }

    public void update() {
        this.meta = this.instance.getItemMeta();

        this.meta.displayName(mm.deserialize("<gradient:#ff00ff:#00ffff><bold>" + this.name + "</bold></gradient>"));

        if (!description.isBlank()) {
            List<Component> lore = new ArrayList<>();
            for (String line : description.split("\n")) {
                lore.add(mm.deserialize("<gray>" + line + "</gray>"));
            }
            this.meta.lore(lore);
        }

        this.instance.setItemMeta(this.meta);
    }

    public void render() {
        this.update();

        if (this.pageID == null) return;
        NextInventory nextInventory = next(this.parent);
        nextInventory.render();
    }

    public void onClick(Consumer<Player> onClickCallback) {
        if (!this.button) throw new IllegalStateException("[NextItem] This method only buttons");
        NextInventory inventory = next(this.getParent());
        InventoryType type = inventory.getType();
        if (type != InventoryType.PAGINATION) {
            System.out.println("[ButtonItem] Intento de asignar onClick en inventario no paginado: " + type);
            return;
        }

        this.onClickCallback = onClickCallback;
    }

    public void emitClick(Player player) {
        if (!this.button) {
            System.out.println("[NextItem] emitClick llamado en ítem no marcado como botón.");
            return;
        }

        if (this.onClickCallback != null) {
            this.onClickCallback.accept(player);
        }
    }

    public boolean isRegistry() {
        return this.registry;
    }
    public boolean isDraggable() {
        return this.draggable;
    }
    public boolean isButton() {
        return this.button;
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
    public UUID getPageID() {
        return pageID;
    }

    public NextItem draggable(boolean draggable) {
        this.draggable = draggable;
        this.render();
        return this;
    }
    public NextItem button(boolean button) {
        this.button = button;
        if (!button) {
            this.onClickCallback = null;
        }
        this.render();
        return this;
    }
    public NextItem registry(boolean registry) {
        this.registry = registry;
        this.render();
        return this;
    }
    public NextItem page(int page) {
        NextPage pageInstance = pagination(page, this.parent);
        if (pageInstance == null) throw new IllegalStateException("Page Not Found " + page);
        pageInstance.insert(this);
        return this;
    }
    public NextItem insert(int page) {
        this.page(page);
        NextInventory nextInventory = next(this.getParent());
        checkItem(nextInventory, this);
        this.registry(true);

        if (page == nextInventory.getCurrentPage()) {
            System.out.println("[NextItem] Insert In Current Page:" + nextInventory.getCurrentPage() + " - NextItem:" + this.getName());
            nextInventory.getInstance().setItem(this.getIndex(), this.getInstance());
        }

        return this;
    }

    public void setIndex(int index) {
        this.index = index;
        this.render();
    }
    public void setName(String name) {
        this.name = name;
        this.render();
    }
    public void setDescription(String description) {
        this.description = description;
        this.render();
    }
    public void setMaterialType(Material materialType) {
        this.materialType = materialType;
        this.render();
    }
    public void setParent(UUID parent) {
        this.parent = parent;
    }
    public void setPageID(UUID pageID) {
        this.pageID = pageID;
    }
}
