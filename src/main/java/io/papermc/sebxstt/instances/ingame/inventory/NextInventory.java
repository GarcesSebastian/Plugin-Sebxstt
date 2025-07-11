package io.papermc.sebxstt.instances.ingame.inventory;

import io.papermc.sebxstt.instances.enums.InventoryType;
import io.papermc.sebxstt.instances.ingame.inventory.custom_listener.NextInventoryListener;
import io.papermc.sebxstt.instances.ingame.inventory.enums.InventorySizeType;
import io.papermc.sebxstt.instances.ingame.inventory.instances.NextItem;
import io.papermc.sebxstt.instances.ingame.inventory.instances.NextPage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.papermc.sebxstt.instances.ingame.inventory.InventoryHelper.*;

public class NextInventory extends NextInventoryListener {
    public UUID id;
    private String title;
    private InventorySizeType size;

    private InventoryType type;
    private ArrayList<UUID> players = new ArrayList<>();
    private Inventory instance;

    private NextItem back;
    private NextItem current;
    private NextItem next;

    private Integer currentPage = 1;

    private ArrayList<NextItem> actionList = new ArrayList<>();

    private ArrayList<Integer> indexBlockedList = new ArrayList<>();
    private ArrayList<Integer> indexAllowedList = new ArrayList<>();

    private ArrayList<NextPage> pages = new ArrayList<>();
    private ArrayList<NextItem> items = new ArrayList<>();

    public NextInventory(String title, InventorySizeType size, InventoryType type) {
        super(UUID.randomUUID(), type);
        this.id = super.nextInventory;
        this.title = title;
        this.size = size;

        this.instance = Bukkit.createInventory(null, size.getTotalSlots(), title);
        this.type = type;

        NextInventoryProvider.nextInventoryMap.put(this.instance, this);
        NextInventoryProvider.nextInventoryList.add(this);

        this.pages.add(new NextPage(this).index(1));
        resolve(this);
    }

    public NextInventory open(UUID target) throws IllegalStateException {
        if (!this.players.contains(target)) {
            this.players.add(target);
        }

        Player plr = verifyPlayer(target);
        plr.openInventory(this.instance);

        return this;
    }

    public NextInventory close(UUID target) throws IllegalStateException {
        this.players.remove(target);
        Player plr = verifyPlayer(target);
        plr.closeInventory();

        return this;
    }

    public void render() {
        NextPage currentPage = pagination(this.currentPage, this.id);
        if (currentPage == null) return;

        this.instance.clear();
        RenderPagination(this);

        for (UUID item : currentPage.getStack()) {
            NextItem nextItem = item(item, this.id);
            if (nextItem == null) continue;
            this.instance.setItem(nextItem.getIndex(), nextItem.getInstance());
        }
    }

    public void update() {
        this.render();

        String backDescription = (this.currentPage <= 1)
                ? "<red>No puedes retroceder</red>"
                : "Pagina anterior: <yellow>" + (this.currentPage - 1) + "</yellow>";

        String nextDescription = (this.currentPage >= this.pages.size())
                ? "<red>No puedes avanzar</red>"
                : "Pagina siguiente: <yellow>" + (this.currentPage + 1) + "</yellow>";

        this.back.setDescription(backDescription);
        this.current.setDescription("Pagina actual: <yellow>" + this.currentPage + "</yellow>");
        this.next.setDescription(nextDescription);
    }

    public void back() {
        if (this.currentPage <= 0) return;
        this.currentPage--;
        if (this.pages.stream().noneMatch(p -> p.getIndex() == this.currentPage)) {
            this.currentPage++;
            return;
        }

        this.update();
    }

    public void next() {
        if (this.currentPage >= this.pages.size() - 1) return;
        this.currentPage++;
        if (this.pages.stream().noneMatch(p -> p.getIndex() == this.currentPage)) {
            this.currentPage--;
            return;
        }

        this.update();
    }

    public NextItem CustomItem(String name, String description, Material material, int index) {
        Integer indexResolved = originalIndex(this, index);
        NextItem nextItem = new NextItem(name, description, material, this);
        nextItem.setIndex(indexResolved);
        return nextItem;
    }

    public NextInventory pages(int amount) {
        for (int i = 0; i < amount; i++) {
            NextPage newPage = new NextPage(this).index(this.pages.size());
            this.pages.add(newPage);
        }

        return this;
    }

    public void setItem(NextItem nextItem) {
        checkItem(this, nextItem);
        nextItem.registry(true);
        this.instance.setItem(nextItem.getIndex(), nextItem.getInstance());
    }

    public ArrayList<NextItem> Buttons() {
        List<NextItem> buttonsOnly = items.stream()
                .filter(NextItem::isButton)
                .map(it -> (NextItem) it)
                .toList();

        return new ArrayList<>(buttonsOnly);
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

    public void setBack(NextItem back) {
        this.back = back;
    }

    public void setCurrent(NextItem current) {
        this.current = current;
    }

    public void setNext(NextItem next) {
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

    public ArrayList<NextItem> getActionList() {
        return actionList;
    }

    public ArrayList<NextItem> getItems() {
        return items;
    }

    public ArrayList<NextPage> getPages() {
        return pages;
    }

    public InventoryType getType() {
        return type;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public Inventory getInstance() {
        return instance;
    }

    public NextItem getBack() {
        return back;
    }

    public NextItem getCurrent() {
        return current;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public NextItem getNext() {
        return next;
    }
}
