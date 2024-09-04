package ru.practicum.shareit.item.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDateInfoDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@UtilityClass
public class ItemMapper {
    public Collection<ItemDto> toItemsDtoCollection(Collection<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public ItemDto toItemDto(Item item) {
        if (item == null) return null;
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemInfoDto toItemInfoDto(Item item,
                                            BookingDateInfoDto lastBooking,
                                            BookingDateInfoDto nextBooking,
                                            Collection<CommentDto> commentDtos) {
        if (item == null) return null;
        return ItemInfoDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(commentDtos)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}
