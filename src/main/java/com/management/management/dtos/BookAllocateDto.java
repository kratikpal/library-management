package com.management.management.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAllocateDto {
    @NotNull(message = "ISBN is required")
    private String isbn;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Duration is required")
    private int allocationDuration;
}
