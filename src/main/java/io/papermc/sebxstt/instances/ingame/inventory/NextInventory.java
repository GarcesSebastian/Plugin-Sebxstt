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

import static io.papermc.sebxstt.instances.ingame.inventory.InventoryHelper.*;

public class NextInventory extends NextInventoryListener {
    public UUID id;
    private String title;
    private InventorySizeType size;

    private InventoryType type;
    private UUID player;
    private Inventory instance;

    private ButtonItem back;
    private ButtonItem current;
    private ButtonItem next;

    private ArrayList<ButtonItem> actionList = new ArrayList<>();

    private ArrayList<Integer> indexBlockedList = new ArrayList<>();
    private ArrayList<Integer> indexAllowedList = new ArrayList<>();

    private ArrayList<ButtonItem> itemsButton = new ArrayList<>();
    private ArrayList<ItemInventory> items = new ArrayList<>();

    public NextInventory(String title, InventorySizeType size, UUID player, InventoryType type) {
        super(type);
        this.id = UUID.randomUUID();
        this.title = title;
        this.size = size;

        this.player = player;
        this.instance = Bukkit.createInventory(null, size.getTotalSlots(), title);
        this.type = type;

        NextInventoryProvider.nextInventoryMap.put(this.instance, this);
        NextInventoryProvider.nextInventoryList.add(this);

        resolve(this);
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
        Integer indexResolved = originalIndex(this, index);
        ItemInventory itemInventory = new ItemInventory(name, description, material, this);
        itemInventory.setIndex(indexResolved);
        return itemInventory;
    }

    public ButtonItem CustomButton(String name, String description, Material material, int index) {
        Integer indexResolved = originalIndex(this, index);
        ButtonItem bt = new ButtonItem(name, description, material, this);
        bt.setIndex(indexResolved);
        return bt;
    }

    public void setItem(ItemInventory itemInventory) {
        this.instance.setItem(itemInventory.getIndex(), itemInventory.getInstance());
    }

    public void setItem(ButtonItem buttonItem) {
        this.instance.setItem(buttonItem.getIndex(), buttonItem.getInstance());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(InventorySizeType size) {
        this.size = size;
    }

    public void setType(InventoryType type) {
        this.type = type;
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

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public InventorySizeType getSize() {
        return size;
    }

    public ArrayList<Integer> getAllowedList() {
        return indexAllowedList;
    }

    public ArrayList<Integer> getBlockedList() {
        return indexBlockedList;
    }

    public ArrayList<ButtonItem> getActionList() {
        return actionList;
    }

    public ArrayList<ItemInventory> getItems() {
        return items;
    }

    public ArrayList<ButtonItem> getItemsButton() {
        return itemsButton;
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
