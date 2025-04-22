package com.management.management.service;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.BookDto;
import com.management.management.entity.Book;
import com.management.management.repository.BookRepository;
import com.management.management.utility.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public GenericResponse<?> saveBook(BookDto bookDto){
        try {
            Book book = bookRepository.save(new Book(bookDto.getTitle(), bookDto.getAuthor()));
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book saved", book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
