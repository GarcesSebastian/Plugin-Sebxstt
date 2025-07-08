package io.papermc.sebxstt.instances.ingame.inventory;

import io.papermc.sebxstt.instances.enums.InventoryType;
import io.papermc.sebxstt.instances.ingame.inventory.custom_listener.NextInventoryListener;
import io.papermc.sebxstt.instances.ingame.inventory.enums.InventorySizeType;
import io.papermc.sebxstt.instances.ingame.inventory.instances.ButtonItem;
import io.papermc.sebxstt.instances.ingame.inventory.instances.ItemInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

import static io.papermc.sebxstt.instances.ingame.inventory.InventoryHelper.resolve;
import static io.papermc.sebxstt.instances.ingame.inventory.InventoryHelper.verifyPlayer;

public class NextInventory extends NextInventoryListener {
    public UUID id;
    public String title;
    public InventorySizeType size;

    private InventoryType type;
    private UUID player;
    private Inventory instance;

    private ButtonItem back;
    private ButtonItem current;
    private ButtonItem next;

    public ArrayList<ButtonItem> actionList = new ArrayList<>();

    public ArrayList<Integer> indexBlockedList = new ArrayList<>();
    public ArrayList<Integer> indexAllowedList = new ArrayList<>();

    public NextInventory(String title, InventorySizeType size, UUID player, InventoryType type) {
        super(type);
        this.id = UUID.randomUUID();
        this.title = title;
        this.size = size;

        this.player = player;
        this.instance = Bukkit.createInventory(null, size.getTotalSlots(), title);
        this.type = type;

        resolve(this);

        NextInventoryProvider.nextInventoryMap.put(this.instance, this);
        NextInventoryProvider.nextInventoryList.add(this);
    }

    public void open() throws IllegalStateException {
        Player plr = verifyPlayer(this.player);
        plr.openInventory(this.instance);
    }

    public void close() throws IllegalStateException {
        Player plr = verifyPlayer(this.player);
        plr.closeInventory();
    }

    public ItemInventory CustomItem(String name, String description, Material material, int index) {
        if (!this.indexAllowedList.contains(index)) {
            throw new IllegalStateException("Este indice no esta disponible: "  + index);
        }

        ItemInventory itemInventory = new ItemInventory(name, description, material, this);
        itemInventory.setIndex(index);
        return itemInventory;
    }

    public ButtonItem CustomButton(String name, String description, Material material, int index) {
        if (!this.indexAllowedList.contains(index)) {
            throw new IllegalStateException("Este indice no esta disponible: "  + index);
        }
        
        ButtonItem bt = new ButtonItem(name, description, material, this);
        bt.setIndex(index);
        return bt;
    }

    public void setBack(ButtonItem back) {
        this.back = back;
    }

    public void setCurrent(ButtonItem current) {
        this.current = current;
    }

    public void setNext(ButtonItem next) {
        this.next = next;
    }

    public void setIndexBlockedList(ArrayList<Integer> indexBlockedList) {
        this.indexBlockedList = indexBlockedList;
    }

    public void setIndexAllowedList(ArrayList<Integer> indexAllowedList) {
        this.indexAllowedList = indexAllowedList;
    }

    public InventoryType getType() {
        return type;
    }

    public UUID getPlayer() {
        return player;
    }

    public Inventory getInstance() {
        return instance;
    }

    public ButtonItem getBack() {
        return back;
    }

    public ButtonItem getCurrent() {
        return current;
    }

    public ButtonItem getNext() {
        return next;
    }
}
