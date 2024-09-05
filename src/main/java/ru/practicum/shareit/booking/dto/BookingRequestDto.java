package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull
    private Long itemId;
    @FutureOrPresent(message = "Start date before now!")
    @NotNull(message = "Start date is null!")
    private LocalDateTime start;
    @Future(message = "End date before Start date!")
    @NotNull(message = "End date is null!")
    private LocalDateTime end;
}
