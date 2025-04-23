package com.management.management.Controller;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.BookAllocateDto;
import com.management.management.dtos.BookDto;
import com.management.management.exception.HttpException;
import com.management.management.service.BookService;
import com.management.management.utility.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) {
        try {
            GenericResponse<?> response = bookService.saveBook(bookDto);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBookByIsbn(@RequestParam String isbn) {
        try {
            GenericResponse<?> response = bookService.getBookByIsbn(isbn);
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
    public ResponseEntity<?> allocateBook(@RequestBody BookAllocateDto bookAllocateDto) {
        try {
            GenericResponse<?> response = bookService.allocateBook(bookAllocateDto);
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
    public ResponseEntity<?> deallocateBook(@RequestBody BookAllocateDto bookAllocateDto) {
        try {
            GenericResponse<?> response = bookService.deallocateBook(bookAllocateDto);
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
            GenericResponse<?> response = bookService.updateBookQuantity(isbn, quantity);
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
    public ResponseEntity<?> deleteBook(@RequestParam String isbn) {
        try {
            GenericResponse<?> response = bookService.deleteBook(isbn);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }
}