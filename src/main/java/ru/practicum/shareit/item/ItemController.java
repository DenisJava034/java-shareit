package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.header.Headers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemInfoDto> findAll(@RequestHeader(Headers.USER_HEADER) Long userId) {
        log.info(String.valueOf("Количество вещей ользователя: {}"), itemService.findItemsByUserId(userId).size());
        return itemService.findItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItemDto(@RequestHeader(Headers.USER_HEADER) Long userId,
                                  @PathVariable Long itemId) {
        log.info(String.valueOf("Просмотр вещи: {}"), itemService.findItemById(userId, itemId));
        return itemService.findItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(Headers.USER_HEADER) Long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info(String.valueOf("Добавление вещи: {}"), itemDto);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(Headers.USER_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info(String.valueOf("Редактрование вещи: {}"), itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemDto(
            @RequestHeader(Headers.USER_HEADER) Long userId,
            @RequestParam(defaultValue = "", required = false) String text) {
        log.info(String.valueOf("Поиск вещи: {}"), text);
        return itemService.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(Headers.USER_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.info(String.valueOf("Добавление комментария: {}"), commentRequestDto);
        return itemService.addComment(userId, itemId, commentRequestDto);
    }
}
