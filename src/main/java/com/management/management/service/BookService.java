package com.management.management.service;

import com.management.management.dtos.BookAllocateDto;
import com.management.management.dtos.BookDto;
import com.management.management.utility.GenericResponse;

public interface BookService {
    GenericResponse<?> saveBook(BookDto bookDto);

    GenericResponse<?> getBookByIsbn(String isbn);

    GenericResponse<?> allocateBook(BookAllocateDto bookAllocateDto);

    GenericResponse<?> deallocateBook(BookAllocateDto bookAllocateDto);

    GenericResponse<?> updateBookQuantity(String isbn, Integer quantity);

    GenericResponse<?> deleteBook(String isbn);
}
