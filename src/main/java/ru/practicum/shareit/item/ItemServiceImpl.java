package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemDto> findAll() {
        return ItemMapper.toItemsDtoCollection(itemRepository.findAll());
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        validation(userId, null);
        return ItemMapper.toItemDto(itemRepository.create(
                Item.builder()
                        .name(itemDto.getName())
                        .owner(userId)
                        .description(itemDto.getDescription())
                        .available(itemDto.getAvailable())
                        .request(null)
                        .build()
        ));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        validation(userId, itemId);
        itemDto.setId(itemId);
        return ItemMapper.toItemDto(itemRepository.update(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.findItemById(itemId));
    }

    @Override
    public Collection<ItemDto> findItemsByUserId(Long userId) {
        return ItemMapper.toItemsDtoCollection(itemRepository.findItemsByUserId(userId));
    }

    @Override
    public void delete(Long itemId) {
        itemRepository.delete(itemId);
    }

    @Override
    public Collection<ItemDto> findItemsByText(String text) {
        if (text.isBlank() || text.isEmpty()) return List.of();
        return ItemMapper.toItemsDtoCollection(itemRepository.findItemsByText(text));
    }

    private void validation(Long userId, Long itemId) {
        if (userId == null) {
            throw new ValidationException("Owner id not specified!");
        }
        if (!userRepository.isUserExist(userId)) {
            throw new NotFoundException("User (id = " + userId + ") not found!");
        }
        if (itemId != null && !itemRepository.isOwner(userId, itemId)) {
            throw new NotFoundException("Only the owner can edit an item!");
        }
        if (itemId != null && !itemRepository.isItemExist(itemId)) {
            throw new NotFoundException("Item (id = " + itemId + ") not found!");
        }
    }
}
