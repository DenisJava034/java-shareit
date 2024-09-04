package ru.practicum.shareit.booking.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDateInfoDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemMapper;
import ru.practicum.shareit.user.dao.UserMapper;

@UtilityClass
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus().name())
                .build();
    }

    public BookingDateInfoDto toBookingDateInfoDto(Booking booking) {
        if (booking == null) return null;
        return BookingDateInfoDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
