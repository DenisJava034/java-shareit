package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingMapper;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto create(Long userId, BookingRequestDto bookingRequestDto) {
        validate(userId, bookingRequestDto);
        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(() -> {
            throw new NotFoundException("Item id = " + bookingRequestDto.getItemId() + " not found!");
        });
        User booker = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User id = " + userId + " not found!");
        });
        return BookingMapper.toBookingDto(bookingRepository.save(
                Booking.builder()
                        .start(bookingRequestDto.getStart())
                        .end(bookingRequestDto.getEnd())
                        .item(item)
                        .booker(booker)
                        .status(BookingStatus.WAITING)
                        .build()
        ));
    }

    @Override
    public BookingDto setApproved(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new NotFoundException("User does not exist");
        });
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("User id = " + userId + " not found!");
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Only owner can set approved!");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Status is APPROVED!");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new NotFoundException("Booking id = " + bookingId + " not found!");
        });
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Only owner or booker can get Booking!");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id = " + userId + " not found!");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> findAllByBookerAndStatus(Long userId, BookingState state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id = " + userId + " not found!");
        }
        switch (state) {
            case BookingState.ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.WAITING, BookingState.REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(String.valueOf(state))).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            default:
                throw new RuntimeException("Unknown state: " + state);
        }
    }

    @Override
    public Collection<BookingDto> findAllByOwnerAndStatus(Long userId, BookingState state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id = " + userId + " not found!");
        }
        switch (state) {
            case BookingState.ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            case BookingState.WAITING, BookingState.REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(String.valueOf(state))).stream()
                        .map(BookingMapper::toBookingDto)
                        .toList();
            default:
                throw new RuntimeException("Unknown state: " + state);
        }
    }

    private void validate(Long userId, BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto.getEnd().equals(bookingRequestDto.getStart())) {
            throw new ValidationException("End date equals Start date!");
        }
        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(
                () -> new NotFoundException("Item id = " + bookingRequestDto.getItemId() + " not found!"));
        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Item is already booked!");
        }
        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new RuntimeException("Available is not true!");
        }

        if (!bookingRepository.findAllByItemIdAndStartAfterAndStartBefore(
                bookingRequestDto.getItemId(), bookingRequestDto.getStart(), bookingRequestDto.getEnd()).isEmpty()) {
            throw new ValidationException("Crossing dates!");
        }

        if (!bookingRepository.findAllByItemIdAndEndAfterAndEndBefore(
                bookingRequestDto.getItemId(), bookingRequestDto.getStart(), bookingRequestDto.getEnd()).isEmpty()) {
            throw new ValidationException("Crossing dates!");
        }
    }
}