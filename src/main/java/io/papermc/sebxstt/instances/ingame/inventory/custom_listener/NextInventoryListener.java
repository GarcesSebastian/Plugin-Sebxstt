package io.papermc.sebxstt.instances.ingame.inventory.custom_listener;

import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.instances.enums.InventoryType;
import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;
import io.papermc.sebxstt.instances.ingame.inventory.instances.NextItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static io.papermc.sebxstt.instances.ingame.inventory.InventoryHelper.next;

public class NextInventoryListener {
    public UUID nextInventory;
    public InventoryType type;

    public NextInventoryListener(UUID nextInventory, InventoryType type) {
        this.nextInventory = nextInventory;
        this.type = type;
    }

    private Consumer<Player> onBackCallback;
    private Consumer<Player> onNextCallback;
    private boolean emittingRefresh = false;

    private List<Runnable> onRefreshCallbacks = new ArrayList<>();

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

    public void onRefresh(Runnable callback) {
        this.onRefreshCallbacks.add(callback);
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

    public void emitRefresh() {
        if (emittingRefresh) return;
        emittingRefresh = true;

        try {
            NextInventory nxt = next(nextInventory);

            for (UUID idPlayer : nxt.getPlayers()) {
                Player p = InPlayer.instance(idPlayer);
                if (p == null) continue;
                Inventory inv = p.getOpenInventory().getTopInventory();
                for (NextItem item : nxt.getItems()) {
                    inv.setItem(item.getIndex(), item.getInstance());
                }
            }

            for (Runnable callback : onRefreshCallbacks) {
                try {
                    System.out.println("[NextInventoryListener] Run Callback OnRefresh");
                    callback.run();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } finally {
            emittingRefresh = false;
        }
    }

}
