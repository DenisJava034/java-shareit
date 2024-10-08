package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingDto create(Long userId, BookingRequestDto bookingRequestDto);

    BookingDto setApproved(Long userId, Long bookingId, Boolean approved);

    BookingDto findById(Long bookingId, Long userId);

    Collection<BookingDto> findAllByBookerAndStatus(Long userId, BookingState state);

    Collection<BookingDto> findAllByOwnerAndStatus(Long userId, BookingState state);
}