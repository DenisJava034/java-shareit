package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Collection<Item> findAll();

    Item create(Item item);

    Item update(Item item);

    Item findItemById(Long itemId);

    Collection<Item> findItemsByUserId(Long userId);

    boolean isItemExist(Long itemId);

    void delete(Long itemId);

    boolean isOwner(Long userId, Long itemId);

    Collection<Item> findItemsByText(String text);
}
