package io.papermc.sebxstt.instances.ingame.inventory.listener;

import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;
import io.papermc.sebxstt.instances.ingame.inventory.NextInventoryProvider;
import io.papermc.sebxstt.instances.ingame.inventory.instances.ButtonItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory clicked = event.getClickedInventory();
        if (clicked == null) return;

        NextInventory inv = NextInventoryProvider.nextInventoryMap.get(clicked);
        if (inv == null) return;

        int slot = event.getRawSlot();

        for (Integer index : inv.indexBlockedList) {
            if (index == slot) {
                event.setCancelled(true);
                break;
            }
        }

        for (ButtonItem bt : inv.actionList) {
            if (bt.getIndex() == slot) {
                event.setCancelled(true);
                bt.emitClick(player);
                break;
            }
        }
    }

}
