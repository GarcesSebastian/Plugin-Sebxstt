package io.papermc.sebxstt.instances.ingame.inventory.instances;

import io.papermc.sebxstt.instances.enums.InventoryType;
import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

import static io.papermc.sebxstt.instances.ingame.inventory.InventoryHelper.next;

public class ButtonItem extends ItemInventory {
    private Consumer<Player> onClickCallback;

    public ButtonItem(String name, String description, Material materialType, NextInventory parent) {
        super(name, description, materialType, parent);
        parent.getItemsButton().add(this);
    }

    public void onClick(Consumer<Player> onClickCallback) {
        NextInventory inventory = next(this.getParent());
        InventoryType type = inventory.getType();
        if (type == InventoryType.PAGINATION) {
            this.onClickCallback = onClickCallback;
        } else {
            System.out.println("[ButtonItem] Intento de asignar onClick en inventario no paginado: " + type);
        }
    }

    public void emitClick(Player player) {
        if (this.onClickCallback != null) {
            this.onClickCallback.accept(player);
        }
    }
}
