package com.management.management.Constants;

public class EmailConstants {
    public static final String REMAINDER_TIME = "0 0 9 * * ?";
    public static final String REMAINDER_MAIL_SUBJECT = "Book Return Reminder";
    public static final String REMAINDER_MAIL_BODY = """
            Dear User,
            
            This is a friendly reminder that the return date for your allocated book is approaching.
            Book Details:
            Title: [BOOK_NAME]
            ISBN: [ISBN]
            Return Date: [RETURN_DATE]
            
            Please ensure the book is returned by the due date to avoid any late fees.
            If you've already returned the book, kindly disregard this message.
            
            Thank you for using our library services!
            
            Best regards,
            Library Team""";
}
