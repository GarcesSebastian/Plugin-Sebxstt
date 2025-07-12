package io.papermc.sebxstt.instances.ingame.inventory;

import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.instances.enums.InventoryType;
import io.papermc.sebxstt.instances.ingame.inventory.instances.NextItem;
import io.papermc.sebxstt.instances.ingame.inventory.instances.NextPage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Optional;
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

    public static void RenderPagination(NextInventory nextInventory) {
        for (NextItem bt : nextInventory.getActionList()) {
            nextInventory.getInstance().setItem(bt.getIndex(), bt.getInstance());
        }

        for (Integer index : nextInventory.getBlockedList()) {
            nextInventory.getInstance().setItem(index, blocked());
        }
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
            int slots = nextInventory.getSize().getTotalSlots();
            int[] indexes = new int[]{slots - 6, slots - 5, slots - 4};
            ArrayList<Integer> blockedList = new ArrayList<>(nextInventory.getSize().getBlockedSlots());

            for (int i : indexes) {
                blockedList.remove((Integer) i);
            }

            nextInventory.setIndexBlockedList(blockedList);
            nextInventory.setIndexAllowedList(new ArrayList<>(nextInventory.getSize().getAllowedSlots()));

            NextItem BackItem = new NextItem("Retroceder", "---", Material.ARROW, nextInventory);
            BackItem.setIndex(indexes[0]);
            BackItem.button(true).draggable(false);
            nextInventory.setBack(BackItem);
            nextInventory.getActionList().add(nextInventory.getBack());
            nextInventory.getItems().remove(BackItem);

            NextItem CurrentItem = new NextItem("Pagina Actual", "Pagina actual: 1", Material.CLOCK, nextInventory);
            CurrentItem.setIndex(indexes[1]);
            CurrentItem.draggable(false);
            nextInventory.setCurrent(CurrentItem);
            nextInventory.getActionList().add(nextInventory.getCurrent());
            nextInventory.getItems().remove(CurrentItem);

            NextItem NextItem = new NextItem("Avanzar", "Pagina siguiente: 2", Material.ARROW, nextInventory);
            NextItem.setIndex(indexes[2]);
            NextItem.button(true).draggable(false);
            nextInventory.setNext(NextItem);
            nextInventory.getActionList().add(nextInventory.getNext());
            nextInventory.getItems().remove(NextItem);

            RenderPagination(nextInventory);

            nextInventory.getBack().onClick(player -> {
                System.out.println("[InventoryHelper] Clicked Back " + player.getName() + " - pages: " + nextInventory.getPages().size() + " currentPage: " + nextInventory.getCurrentPage());
                nextInventory.back();
                nextInventory.emitBack(player);
            });
            nextInventory.getNext().onClick(player -> {
                System.out.println("[InventoryHelper] Clicked Next " + player.getName() + " - pages: " + nextInventory.getPages().size() + " currentPage: " + nextInventory.getCurrentPage());
                nextInventory.next();
                nextInventory.emitNext(player);
            });
            return;
        }

        if (type == InventoryType.SCROLLING) {
            return;
        }
    }

    public static NextInventory next(UUID id) {
        NextInventory instance = nextInventoryList.stream().filter(i -> i.id.equals(id)).findFirst().orElse(null);
        if (instance == null) throw new IllegalStateException("Inventory Not Found " + id);
        return instance;
    }

    public static NextItem item(UUID id, UUID inventory) {
        NextInventory instance = next(inventory);
        NextItem itemInstance = instance.getItems().stream().filter(im -> im.getId().equals(id)).findFirst().orElse(null);
        if (itemInstance == null) {
            System.out.println("Item Not Found " + id);
            return null;
        }
        return itemInstance;
    }

    public static NextPage pagination(int index, UUID inventory) {
        NextInventory instance = next(inventory);
        NextPage pageInstance = instance.getPages().stream().filter(p -> p.getIndex() == index).findFirst().orElse(null);
        if (pageInstance == null) {
            System.out.println("Page Not Found " + index);
            return null;
        }
        return pageInstance;
    }

    public static NextPage pagination(UUID page, UUID inventory) {
        NextInventory instance = next(inventory);
        NextPage pageInstance = instance.getPages().stream().filter(p -> p.getId().equals(page)).findFirst().orElse(null);
        if (pageInstance == null) {
            System.out.println("Page Not Found " + page);
            return null;
        }
        return pageInstance;
    }

    public static void checkItem(NextInventory nextInventory, NextItem nextItem) {
        Optional<NextItem> maybeItemInventory = nextInventory.getItems().stream()
                .filter(maybe -> maybe.getIndex() == nextItem.getIndex() && maybe.isRegistry())
                .findFirst();

        if (maybeItemInventory.isPresent()) {
            throw new IllegalStateException("Index " +  nextItem.getIndex() + " registered");
        }
    }
}
