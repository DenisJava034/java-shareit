package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findAll();

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto findItemById(Long itemId);

    Collection<ItemDto> findItemsByUserId(Long userId);

    void delete(Long itemId);

    Collection<ItemDto> findItemsByText(String text);
}
