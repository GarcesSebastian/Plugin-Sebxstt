package io.papermc.sebxstt.instances.ingame.inventory.listener;

import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;
import io.papermc.sebxstt.instances.ingame.inventory.NextInventoryProvider;
import io.papermc.sebxstt.instances.ingame.inventory.instances.NextItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Inventory clicked = event.getClickedInventory();
        if (clicked == null) return;
        NextInventory inv = NextInventoryProvider.nextInventoryMap.get(clicked);
        if (inv == null) return;
        int slot = event.getRawSlot();
        if (inv.getBlockedList().contains(slot)) {
            System.out.println("[InventoryListener] Slots blocked " + slot);
            event.setCancelled(true);
            return;
        }
        for (NextItem bt : inv.Buttons()) {
            if (bt.getIndex() == slot && bt.isRegistry()) {
                event.setCancelled(true);
                bt.emitClick(player);
                return;
            }
        }

        for (NextItem bt : inv.getActionList()) {
            if (bt.getIndex() == slot) {
                event.setCancelled(true);
                bt.emitClick(player);
                return;
            }
        }

        Optional<NextItem> maybeItem = inv.getItems().stream()
                .filter(item -> item.getIndex() == slot && item.isRegistry())
                .findFirst();
        if (maybeItem.isPresent() && !maybeItem.get().isDraggable()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Inventory top = player.getOpenInventory().getTopInventory();
        NextInventory inv = NextInventoryProvider.nextInventoryMap.get(top);
        if (inv == null) return;
        for (int slot : event.getRawSlots()) {
            Optional<NextItem> maybeItem = inv.getItems().stream()
                    .filter(item -> item.getIndex() == slot)
                    .findFirst();
            if (maybeItem.isPresent() && !maybeItem.get().isDraggable()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
