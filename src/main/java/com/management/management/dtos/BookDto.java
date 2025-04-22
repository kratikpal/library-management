package com.management.management.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    @NotNull(message = "Title is required")
    private String title;
    @NotNull(message = "Author is required")
    private String author;
    @NotNull(message = "ISBN is required")
    private String isbn;
    @NotNull(message = "Quantity is required")
    private int quantity;
}
