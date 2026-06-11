import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Sample White-Box tests for the Checkout system.
 * This class demonstrates how to write white-box tests using:
 * - Control Flow Graph (CFG) analysis
 * - Statement coverage
 * - Branch coverage
 * - Path coverage
 *
 * White-box testing focuses on testing the IMPLEMENTATION by
 * examining the code structure and ensuring all paths are tested.
 */
public class CheckoutWhiteBoxSample {

    private Checkout checkout;

    @BeforeEach
    public void setUp() {
        checkout = new Checkout();
    }

    @Test
    @DisplayName("WB Test1: countBooksByType - null type nodes")
    public void testCountBooksByType_NullType() {
        // Branch: type == null → TRUE
        int result = checkout.countBooksByType(null, false);
        assertEquals(0, result, "Should return 0 for null type");
    }
    
    @Test
    @DisplayName("WB Test2: countBooksByType - Mixed booklist nodes")
    public void testCountBooksByType_MixedBooklist() {
         // Add some books to inventory
        Book book1 = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        Book book2 = new Book("0123456789", "Mystery Novel", "Jane Doe",
                              Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-9999-8888-7", "Data Structures Reference", "Bob Johnson",
                              Book.BookType.REFERENCE, 1);

        Book book4 = new Book("978-1-1111-2222-3", "Children's Stories", "Alice Wonder",
                              Book.BookType.CHILDREN, 10);
        Book book5 = null;

        checkout.addBook(book1);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        try {
            checkout.addBook(book5);
        } catch (Exception e) {
            // Ignore exception from adding null book since we are testing countBooksByType behavior
        }
        

        int result = checkout.countBooksByType(Book.BookType.FICTION, false);
        assertEquals(1, result, "Should return 1 for mixed booklist");
        result = checkout.countBooksByType(Book.BookType.FICTION, true);
        assertEquals(1, result, "Should return 1 for mixed booklist");
    }

    @Test
    @DisplayName("WB Test3: countBooksByType - null type edges")
    public void testCountBooksByType_NullTypeEdges() {
        // Branch: type == null → TRUE
        int result = checkout.countBooksByType(null, true);
        assertEquals(0, result, "Should return 0 for null type");
    }
    
    @Test
    @DisplayName("WB Test4: countBooksByType -  type is avaiable edges")
    public void testCountBooksByType_TypeAvailableEdges() {
         // Add some books to inventory
        Book book1 = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        Book book2 = new Book("0123456789", "Mystery Novel", "Jane Doe",
                              Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-9999-8888-7", "Data Structures Reference", "Bob Johnson",
                              Book.BookType.REFERENCE, 1);

        Book book4 = new Book("978-1-1111-2222-3", "Children's Stories", "Alice Wonder",
                              Book.BookType.CHILDREN, 10);

        checkout.addBook(book1);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);

        int result = checkout.countBooksByType(Book.BookType.FICTION, true);
        assertEquals(1, result, "Should return 1 for mixed booklist");
    }

    @Test
    @DisplayName("WB Test5: countBooksByType -  type is not avaiable edges")
    public void testCountBooksByType_TypeNotAvailableEdges() {
         // Add some books to inventory
        Book book1 = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
       // Book book2 = new Book("0123456789", "Mystery Novel", "Jane Doe",
         //                     Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-9999-8888-7", "Data Structures Reference", "Bob Johnson",
                              Book.BookType.REFERENCE, 1);

        Book book4 = new Book("978-1-1111-2222-3", "Children's Stories", "Alice Wonder",
                              Book.BookType.CHILDREN, 10);

        checkout.addBook(book1);
        //checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);

        int result = checkout.countBooksByType(Book.BookType.FICTION, true);
        assertEquals(0, result, "Should return 0 for mixed booklist");
    }
    
    @Test
    @DisplayName("WB Test4: countBooksByType - a book in list is null edges")
    public void testCountBooksByType_BookInListNullEdges() {
         // Add some books to inventory
        Book book1 = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        Book book2 = new Book("0123456789", "Mystery Novel", "Jane Doe",
                              Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-9999-8888-7", "Data Structures Reference", "Bob Johnson",
                              Book.BookType.REFERENCE, 1);

        Book book4 = new Book("978-1-1111-2222-3", "Children's Stories", "Alice Wonder",
                              Book.BookType.CHILDREN, 10);
        Book book5 = null;

        checkout.addBook(book1);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        try {
            checkout.addBook(book5);
        } catch (Exception e) {
            // Ignore exception from adding null book since we are testing countBooksByType behavior
        }

        int result = checkout.countBooksByType(Book.BookType.FICTION, false);
        assertEquals(1, result, "Should return 1 for mixed booklist");
    }    

    @Test
    @DisplayName("WB Test7: getTitle - Title is saved correctly")
    public void testGetTitle_TitleSavedCorrectly() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        assertEquals("Introduction to Java", book.getTitle(), "getTitle should return the correct title");
    }

    @Test
    @DisplayName("WB Test9: getAuthor - Author is saved correctly")
    public void testGetAuthor_AuthorSavedCorrectly() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        assertEquals("John Smith", book.getAuthor(), "getAuthor should return the correct author");
    }

    @Test
    @DisplayName("WB Test11: getTotalCopies - Total copies is saved correctly")
    public void testGetTotalCopies_TotalCopiesSavedCorrectly() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        assertEquals(3, book.getTotalCopies(), "getTotalCopies should return the correct number of copies");
    }

    @Test
    @DisplayName("WB Test12: getAvailableCopies - Available copies is saved correctly")
    public void testGetAvailableCopies_AvailableCopiesSavedCorrectly() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        assertEquals(3, book.getAvailableCopies(), "getAvailableCopies should return the correct number of available copies");
    }

    @Test
    @DisplayName("WB Test13: setAvailableCopies - Available copies is updated correctly to 0")
    public void testSetAvailableCopies_AvailableCopiesUpdatedCorrectlyTo0() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        book.setAvailableCopies(0);
        assertEquals(0, book.getAvailableCopies(), "setAvailableCopies should update the number of available copies to 0");
    }
    
    @Test
    @DisplayName("WB Test14: setAvailableCopies - Available copies is updated correctly to 2")
    public void testSetAvailableCopies_AvailableCopiesUpdatedCorrectlyTo2() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        book.setAvailableCopies(2);
        assertEquals(2, book.getAvailableCopies(), "setAvailableCopies should update the number of available copies to 2");
    }

    @Test
    @DisplayName("WB Test15: resetAvailablity - Available copies is reset to total copies")
    public void testResetAvailability_AvailableCopiesResetToTotalCopies() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        book.setAvailableCopies(0);
        book.resetAvailability();
        assertEquals(3, book.getAvailableCopies(), "resetAvailability should reset the number of available copies to the total copies");
    }

    @Test
    @DisplayName("WB Test16: CalculateFine - No fine for on-time return")
    public void testCalculateFine_NoFineForOnTimeReturn() {
        double fine = checkout.calculateFine(0, Book.BookType.FICTION);
        assertEquals(0.0, fine, "No fine should be charged for on-time return");
    }

    @Test
    @DisplayName("WB Test17: CalculateFine - Fine for late return of fiction book for 5 days")
    public void testCalculateFine_FineForLateReturnOfFictionBook5Days() {
        // Simulate returning a fiction book 5 days late
        double fine = checkout.calculateFine(5, Book.BookType.FICTION);
        assertEquals(1.25, fine, "Fine should be charged for late return of fiction book");
    }
    
    @Test
    @DisplayName("WB Test18: CalculateFine - Fine for late return of reference book for 5 days")
    public void testCalculateFine_FineForLateReturnOfReferenceBook5Days() {
        // Simulate returning a reference book 5 days late
        double fine = checkout.calculateFine(5, Book.BookType.REFERENCE);
        assertEquals(2.50, fine, "Fine should be charged for late return of reference book");
    }

    @Test
    @DisplayName("WB Test19: CalculateFine - Fine for late return of fiction for 10 days")
    public void testCalculateFine_FineForLateReturnOfFictionBook10Days() {
        // Simulate returning a fiction 10 days late
        double fine = checkout.calculateFine(10, Book.BookType.FICTION);
        assertEquals(3.25, fine, "Fine should be charged for late return of fiction book");
    }

    @Test
    @DisplayName("WB Test20: CalculateFine - Fine for late return of fiction book for 15 days")
    public void testCalculateFine_FineForLateReturnOfFictionBook15Days() {
        // Simulate returning a fiction book 15 days late
        double fine = checkout.calculateFine(15, Book.BookType.FICTION);
        assertEquals(6.25, fine, "Fine should be charged for late return of fiction book");
    }

    @Test
    @DisplayName("WB Test22: getName - Name is saved correctly")
    public void testGetName_NameSavedCorrectly() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        assertEquals("Test Patron", patron.getName(), "getName should return the correct name");
    } 

    @Test
    @DisplayName("WB Test23: getEmail - Email is saved correctly")
    public void testGetEmail_EmailSavedCorrectly() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        assertEquals("test@example.com", patron.getEmail(), "getEmail should return the correct email");
    }

    @Test
    @DisplayName("WB Test24: getMemberSince - Patron member since date is saved correctly")
    public void testGetMemberSince_MemberSinceDateSavedCorrectly() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        LocalDate memberDate = LocalDate.now();
        assertNotNull(patron.getMemberSince(), "getMemberSince should return a non-null date");
        assertEquals(memberDate, patron.getMemberSince(), "getMemberSince should return the correct member since date");   
    }

    @Test
    @DisplayName("WB Test25: resetFines - Fine balance is reset to 0")
    public void testResetFines_FineBalanceResetToZero() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        patron.addFine(10.0);
        patron.resetFines();
        assertEquals(0.0, patron.getFineBalance(), "resetFines should reset the fine balance to 0");
    }   

    @Test
    @DisplayName("WB Test26: chkSuspended - Account suspension status is checked correctly")
    public void testChkSuspended_AccountSuspensionStatusCheckedCorrectly() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        assertFalse(patron.chkSuspended(), "chkSuspended should return false for new patron");
        patron.setAccountSuspended(true);
        assertTrue(patron.chkSuspended(), "chkSuspended should return true after account is suspended");
    }

    @Test
    @DisplayName("WB Test27: payFine - Overdue fine is updated")
    public void testPayFine() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        patron.addFine(15);
        patron.payFine(10.0);
        assertEquals(5, patron.getFineBalance(), "payFine should update the fine balance");
    }   

    @Test
    @DisplayName("WB Test28: removeCheckedOutBook - Checked out book is removed from patron's list correctly")
    public void testRemoveCheckedOutBook() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        patron.addCheckedOutBook("978-0-1234-5678-9", LocalDate.now().plusDays(14));
        patron.removeCheckedOutBook("978-0-1234-5678-9");
        assertFalse(patron.getCheckedOutBooks().containsKey("978-0-1234-5678-9"), "removeCheckedOutBook should remove the book from the patron's checked out list");
    }
    
    @Test
    @DisplayName("WB Test29: returnBook - Available copies is incremented correctly when book is returned")
    public void testReturnBook_AvailableCopiesIncrementedCorrectly() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        book.checkout(); // Available copies should now be 2
        book.returnBook(); // Available copies should now be back to 3
        assertEquals(3, book.getAvailableCopies(), "returnBook should increment available copies correctly when book is returned");
    }

    @Test
    @DisplayName("WB Test30: checkout - Available copies is not incremented when available copies is over 100")
    public void testCheckout_AvailableCopiesNotIncrementedWhenOver100() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        book.setAvailableCopies(100); // Set available copies to 100
        book.returnBook(); // Available copies should still be 100 since it cannot go over 100
        assertEquals(100, book.getAvailableCopies(), "checkout should not increment available copies when it is already at 100");
    }

    @Test
    @DisplayName("WB Test31: checkAvailability - Availability status is checked correctly")
    public void testCheckAvailability_AvailabilityStatusCheckedCorrectly() {
        Book book = new Book("978-0-1234-5678-9", "Introduction to Java", "John Smith",
                              Book.BookType.TEXTBOOK, 3);
        assertTrue(book.checkAvailability(), "checkAvailability should return true when available copies are greater than 0");
        book.setAvailableCopies(0);
        assertFalse(book.checkAvailability(), "checkAvailability should return false when available copies are 0");
    }

    @Test
    @DisplayName("WB Test32: addFine - Fine of 0 or less should not be added to the fine balance")
    public void testAddFine_NonPositiveFineNotAdded() {
        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
        patron.addFine(10.0);
        patron.addFine(0.0); // Should not change fine balance
        patron.addFine(-5.0); // Should not change fine balance
        assertEquals(10.0, patron.getFineBalance(), "addFine should not add non-positive fine amounts to the fine balance");
    }

}