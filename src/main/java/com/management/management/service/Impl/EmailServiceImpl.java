package com.management.management.service.Impl;

import com.management.management.Constants.EmailConstants;
import com.management.management.entity.Book;
import com.management.management.entity.User;
import com.management.management.repository.AllocationDurationRepository;
import com.management.management.repository.BookRepository;
import com.management.management.repository.UserRepository;
import com.management.management.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final AllocationDurationRepository allocationDurationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public EmailServiceImpl(JavaMailSender javaMailSender, AllocationDurationRepository allocationDurationRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.allocationDurationRepository = allocationDurationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }

    @Override
    @Scheduled(cron = EmailConstants.REMAINDER_TIME)
    public void sendRemainderEmail() {
        try {
            allocationDurationRepository.findAllByReturnedFalse().forEach(allocationDuration -> {
                LocalDate dueDate = allocationDuration.getDueDate();
                if (LocalDate.now().plusDays(2).isAfter(dueDate)) {
                    Optional<User> userOptional = userRepository.findById(allocationDuration.getUserId());
                    if (userOptional.isEmpty()) {
                        return;
                    }
                    User user = userOptional.get();
                    Optional<Book> bookOptional = bookRepository.findById(allocationDuration.getBookId());
                    if (bookOptional.isEmpty()) {
                        return;
                    }
                    Book book = bookOptional.get();
                    String message = EmailConstants.REMAINDER_MAIL_BODY
                            .replace("[BOOK_NAME]", book.getTitle())
                            .replace("[ISBN]", book.getIsbn())
                            .replace("[RETURN_DATE]", allocationDuration.getDueDate().toString());
                    sendEmail(user.getEmail(), EmailConstants.REMAINDER_MAIL_SUBJECT, message);
                    allocationDuration.setNotified(true);
                    allocationDurationRepository.save(allocationDuration);
                }
            });
            log.info("Remainder email sent");
        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }
}
