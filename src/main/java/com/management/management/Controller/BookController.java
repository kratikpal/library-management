package com.management.management.Controller;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.BookAllocateDto;
import com.management.management.dtos.BookDto;
import com.management.management.exception.HttpException;
import com.management.management.response.GenericResponse;
import com.management.management.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookServiceImpl;

    public BookController(BookService bookServiceImpl) {
        this.bookServiceImpl = bookServiceImpl;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDto bookDto) {
        try {
            GenericResponse<?> response = bookServiceImpl.saveBook(bookDto);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @GetMapping
    public ResponseEntity<?> getBookByIsbn(@Valid @RequestParam String isbn) {
        try {
            GenericResponse<?> response = bookServiceImpl.getBookByIsbn(isbn);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new GenericResponse<>(
                    HttpConstants.FAILURE,
                    e.getMessage(),
                    null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/allocate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> allocateBook(@Valid @RequestBody BookAllocateDto bookAllocateDto) {
        try {
            GenericResponse<?> response = bookServiceImpl.allocateBook(bookAllocateDto);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @PostMapping("/deallocate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deallocateBook(@Valid @RequestBody BookAllocateDto bookAllocateDto) {
        try {
            GenericResponse<?> response = bookServiceImpl.deallocateBook(bookAllocateDto);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @PutMapping("/update-quantity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookQuantity(@RequestParam String isbn, @RequestParam Integer quantity) {
        try {
            GenericResponse<?> response = bookServiceImpl.updateBookQuantity(isbn, quantity);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(@Valid @RequestParam String isbn) {
        try {
            GenericResponse<?> response = bookServiceImpl.deleteBook(isbn);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }
}