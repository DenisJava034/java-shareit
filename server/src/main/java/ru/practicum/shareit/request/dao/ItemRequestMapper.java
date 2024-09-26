package ru.practicum.shareit.request.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dao.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserMapper;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {

    public List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        }
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(UserMapper.toUserDto(itemRequest.getRequestor()))
                .created(itemRequest.getCreated())
                .items(ItemMapper.toItemsDtoCollection(itemRequest.getItems() != null ? itemRequest.getItems() : List.of()))
                .build();
    }
}