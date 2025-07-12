package com.sebxstt.instances.ingame.inventory.custom_listener;

import com.sebxstt.functions.utils.InPlayer;
import com.sebxstt.instances.enums.InventoryType;
import com.sebxstt.instances.ingame.inventory.NextInventory;
import com.sebxstt.instances.ingame.inventory.instances.NextItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.sebxstt.instances.ingame.inventory.InventoryHelper.next;

public class NextInventoryListener {
    public UUID nextInventory;
    public InventoryType type;

    public NextInventoryListener(UUID nextInventory, InventoryType type) {
        this.nextInventory = nextInventory;
        this.type = type;
    }

    private Consumer<Player> onBackCallback;
    private Consumer<Player> onNextCallback;

    public void onBack(Consumer<Player> onBackCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onBackCallback = onBackCallback;
        } else {
            System.out.println("[ButtonItem] Intento de asignar onBack en inventario no paginado: " + type.toString().toUpperCase());
        }
    }

    public void onNext(Consumer<Player> onNextCallback) {
        if (type == InventoryType.PAGINATION) {
            this.onNextCallback = onNextCallback;
        } else {
            System.out.println("[ButtonItem] Intento de asignar onNext en inventario no paginado: " + type.toString().toUpperCase());
        }
    }

    public void emitBack(Player player) {
        if (onBackCallback != null) {
            onBackCallback.accept(player);
        }
    }

    public void emitNext(Player player) {
        if (onNextCallback != null) {
            onNextCallback.accept(player);
        }
    }
}
