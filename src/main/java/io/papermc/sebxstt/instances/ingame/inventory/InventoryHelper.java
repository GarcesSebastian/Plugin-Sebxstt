package io.papermc.sebxstt.instances.ingame.inventory;

import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.instances.enums.InventoryType;
import io.papermc.sebxstt.instances.ingame.inventory.instances.ButtonItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

import static io.papermc.sebxstt.instances.ingame.inventory.NextInventoryProvider.nextInventoryList;

public class InventoryHelper {
    public static Player verifyPlayer(UUID player) {
        Player plr = InPlayer.instance(player);
        if (plr == null) {
            throw new IllegalStateException("No se encontro el jugador");
        }

        return plr;
    }

    public static Integer originalIndex(NextInventory nextInventory, int index) {
        Integer indexResolved = nextInventory.getAllowedList().get(index);
        if (indexResolved == null) {
            throw new IllegalStateException("No se encontro el indice " + index);
        }

        return indexResolved;
    }

    public static ItemStack blocked() {
        ItemStack blocker = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = blocker.getItemMeta();
        meta.setDisplayName("BLOCKED");
        blocker.setItemMeta(meta);

        return blocker;
    }

    public static void resolve(NextInventory nextInventory) {
        if (nextInventory == null) {
            throw new IllegalStateException("InventoryGUI NULL");
        }

        InventoryType type = nextInventory.getType();

        if (type == InventoryType.PAGINATION) {
            nextInventory.setBack(new ButtonItem("Back Page", "Pagina anterior", Material.ARROW, nextInventory));
            nextInventory.setCurrent(new ButtonItem("Current Page", "Pagina actual", Material.CLOCK, nextInventory));
            nextInventory.setNext(new ButtonItem("Next Page", "Pagina siguiente", Material.ARROW, nextInventory));

            nextInventory.getBack().setIndex(nextInventory.getSize().getTotalSlots() - 6);
            nextInventory.getCurrent().setIndex(nextInventory.getSize().getTotalSlots() - 5);
            nextInventory.getNext().setIndex(nextInventory.getSize().getTotalSlots() - 4);

            nextInventory.setIndexBlockedList(new ArrayList<>(nextInventory.getSize().getBlockedSlots()));
            nextInventory.setIndexAllowedList(new ArrayList<>(nextInventory.getSize().getAllowedSlots()));

            for (Integer index : nextInventory.getBlockedList()) {
                nextInventory.getInstance().setItem(index, blocked());
            }

            nextInventory.getInstance().setItem(nextInventory.getBack().getIndex(), nextInventory.getBack().getInstance());
            nextInventory.getInstance().setItem(nextInventory.getCurrent().getIndex(), nextInventory.getCurrent().getInstance());
            nextInventory.getInstance().setItem(nextInventory.getNext().getIndex(), nextInventory.getNext().getInstance());

            nextInventory.getActionList().add(nextInventory.getBack());
            nextInventory.getActionList().add(nextInventory.getCurrent());
            nextInventory.getActionList().add(nextInventory.getNext());

            nextInventory.getBack().onClick(nextInventory::emitBack);
            nextInventory.getNext().onClick(nextInventory::emitNext);

            return;
        }

        if (type == InventoryType.SCROLLING) {
            return;
        }
    }

    public static NextInventory next(UUID id) {
        NextInventory instance = nextInventoryList.stream().filter(i -> i.id.equals(id)).findFirst().orElse(null);
        if (instance == null) {
            throw new IllegalStateException("No se encontro el inventario");
        }

        return instance;
    }
}
