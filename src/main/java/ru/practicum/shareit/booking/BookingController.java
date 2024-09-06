package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.header.Headers;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(Headers.USER_HEADER) Long userId,
                             @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info(String.valueOf("Добавление запроса на бронирование: {} "), bookingRequestDto);
        return bookingService.create(userId, bookingRequestDto);
    }

    @GetMapping
    public Collection<BookingDto> findAll(
            @RequestHeader(Headers.USER_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info(String.valueOf("Получение списка бронирований пользователя: {} "), userId);
        return bookingService.findAllByBookerAndStatus(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByOwnerAndStatus(
            @RequestHeader(Headers.USER_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info(String.valueOf("Получение списка бронирований для всех вещей пользователя: {} "), userId);
        return bookingService.findAllByOwnerAndStatus(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setApproved(@RequestHeader(Headers.USER_HEADER) Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved) {
        log.info(String.valueOf("Подтверждение или отклонение запроса на бронирование: {} "), approved);
        return bookingService.setApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(Headers.USER_HEADER) Long userId,
                               @PathVariable Long bookingId) {
        log.info(String.valueOf("Получение данных о бронировании: {} "), bookingId);
        return bookingService.findById(bookingId, userId);
    }
}
