package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items;
    private Long index = 0L;

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public Item create(Item item) {
        item.setId(++index);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item newItem) {
        Item item = items.get(newItem.getId());
        if (newItem.getName() != null
                && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null
                && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }
        return item;
    }

    @Override
    public Item findItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> findItemsByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .toList();
    }

    @Override
    public boolean isItemExist(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public void delete(Long itemId) {
        items.remove(itemId);
    }

    @Override
    public boolean isOwner(Long userId, Long itemId) {
        return items.get(itemId).getOwner().equals(userId);
    }

    @Override
    public Collection<Item> findItemsByText(String text) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable().equals(true))
                .toList();
    }
}
