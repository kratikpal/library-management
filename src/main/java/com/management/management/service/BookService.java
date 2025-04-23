package com.management.management.service;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.BookAllocateDto;
import com.management.management.dtos.BookDto;
import com.management.management.entity.Book;
import com.management.management.entity.User;
import com.management.management.repository.BookRepository;
import com.management.management.repository.UserRepository;
import com.management.management.utility.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public GenericResponse<?> saveBook(BookDto bookDto){
        try {
            Book book = bookRepository.save(new Book(bookDto.getIsbn(), bookDto.getTitle(), bookDto.getAuthor(), bookDto.getQuantity()));
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book saved", book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GenericResponse<?> getBookByIsbn(String isbn) {
        try {
            Book book = bookRepository.findByIsbn(isbn);
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book found", book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GenericResponse<?> allocateBook(BookAllocateDto bookAllocateDto) {
        try {
            Book book = bookRepository.findByIsbn(bookAllocateDto.getIsbn());
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            if(book.getQuantity() <= 0){
                return new GenericResponse<>(HttpConstants.FAILURE, "Book is out of stock", null);
            }
            User user = userRepository.findByEmail(bookAllocateDto.getEmail());
            if (user == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "User not found", null);
            }
            if (user.getBooks().contains(book)) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book already allocated", null);
            }
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
            user.getBooks().add(book);
            userRepository.save(user);
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book allocated", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public GenericResponse<?> deallocateBook(BookAllocateDto bookAllocateDto) {
        try {
            Book book = bookRepository.findByIsbn(bookAllocateDto.getIsbn());
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            User user = userRepository.findByEmail(bookAllocateDto.getEmail());
            if (user == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "User not found", null);
            }
            Book bookToRemove = user.getBooks().stream()
                    .filter(b -> b.getIsbn().equals(book.getIsbn()))
                    .findFirst()
                    .orElse(null);
            if (bookToRemove == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not allocated", null);
            }
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);
            user.getBooks().remove(bookToRemove);
            userRepository.save(user);
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book deallocated", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GenericResponse<?> updateBookQuantity(String isbn, Integer quantity) {
        try {
            Book book = bookRepository.findByIsbn(isbn);
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            book.setQuantity(quantity);
            bookRepository.save(book);
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book quantity updated", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GenericResponse<?> deleteBook(String isbn) {
        try {
            Book book = bookRepository.findByIsbn(isbn);
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            bookRepository.delete(book);
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book deleted", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
