package com.management.management.service.Impl;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.BookAllocateDto;
import com.management.management.dtos.BookDto;
import com.management.management.entity.AllocationDuration;
import com.management.management.entity.Book;
import com.management.management.entity.User;
import com.management.management.repository.AllocationDurationRepository;
import com.management.management.repository.BookRepository;
import com.management.management.repository.UserRepository;
import com.management.management.response.GenericResponse;
import com.management.management.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AllocationDurationRepository allocationDurationRepository;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository, AllocationDurationRepository allocationDurationRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.allocationDurationRepository = allocationDurationRepository;
    }

    @Override
    public GenericResponse<?> saveBook(BookDto bookDto) {
        try {
            Book book = bookRepository.save(new Book(bookDto.getIsbn(), bookDto.getTitle(), bookDto.getAuthor(), bookDto.getQuantity()));
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book saved", book);
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Error saving book: " + e.getMessage(), null);
        }
    }

    @Override
    public GenericResponse<?> getBookByIsbn(String isbn) {
        try {
            Book book = bookRepository.findByIsbn(isbn);
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book found", book);
        } catch (Exception e) {
            log.error("Error getting book by ISBN: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Error getting book by ISBN: " + e.getMessage(), null);
        }
    }

    @Override
    public GenericResponse<?> allocateBook(BookAllocateDto bookAllocateDto) {
        try {
            Book book = bookRepository.findByIsbn(bookAllocateDto.getIsbn());
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            if (book.getQuantity() <= 0) {
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
            AllocationDuration byUserIdAndBookId = allocationDurationRepository.findByUserIdAndBookId(user.getId(), book.getId());
            if (byUserIdAndBookId == null) {
                AllocationDuration allocationDuration = new AllocationDuration(
                        user.getId(),
                        book.getId(),
                        LocalDate.now(),
                        LocalDate.now().plusDays(bookAllocateDto.getAllocationDuration())
                );
                allocationDurationRepository.save(allocationDuration);
            } else {
                byUserIdAndBookId.setReturned(false);
                byUserIdAndBookId.setIssueDate(LocalDate.now());
                byUserIdAndBookId.setDueDate(LocalDate.now().plusDays(bookAllocateDto.getAllocationDuration()));
                allocationDurationRepository.save(byUserIdAndBookId);
            }
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book allocated", null);
        } catch (Exception e) {
            log.error("Error allocating book: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Error allocating book: " + e.getMessage(), null);
        }
    }

    @Override
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
            AllocationDuration byUserIdAndBookId = allocationDurationRepository.findByUserIdAndBookId(user.getId(), bookToRemove.getId());
            byUserIdAndBookId.setReturned(true);
            allocationDurationRepository.save(byUserIdAndBookId);
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book deallocated", null);
        } catch (Exception e) {
            log.error("Error deallocating book: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Error deallocating book: " + e.getMessage(), null);
        }
    }

    @Override
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
            log.error("Error updating book quantity: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Error updating book quantity: " + e.getMessage(), null);
        }
    }

    @Override
    public GenericResponse<?> deleteBook(String isbn) {
        try {
            Book book = bookRepository.findByIsbn(isbn);
            if (book == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "Book not found", null);
            }
            bookRepository.delete(book);
            return new GenericResponse<>(HttpConstants.SUCCESS, "Book deleted", null);
        } catch (Exception e) {
            log.error("Error deleting book: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Error deleting book: " + e.getMessage(), null);
        }
    }
}
