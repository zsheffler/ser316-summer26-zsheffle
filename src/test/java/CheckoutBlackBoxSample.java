import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample Black-Box tests for the Checkout system.
 * This class demonstrates how to write black-box tests using:
 * - Equivalence Partitioning (EP)
 * - Boundary Value Analysis (BVA)
 * - Parametrized tests across multiple implementations
 *
 * Black-box testing focuses on testing the SPECIFICATION WITHOUT
 * looking at the implementation.
 *
 * The parameterized structure allows testing all Checkout implementations
 * with the same tests to identify which implementations have bugs.
 */
public class CheckoutBlackBoxSample {

        private Checkout checkout;

        /**
         * Provides the list of Checkout classes to test.
         * Each test will run against ALL implementations.
         */
        @SuppressWarnings("unchecked")
        static Stream<Class<? extends Checkout>> checkoutClassProvider() {
                return (Stream<Class<? extends Checkout>>) Stream.of(
                        Checkout0.class,
                        Checkout1.class,
                        Checkout2.class,
                        Checkout3.class
                );
        }

    // Uncomment when you implement the method in assign 3 and comment the above
//    static Stream<Class<? extends Checkout>> checkoutClassProvider() {
//        return Stream.of(Checkout.class);
//    }


        /**
         * Helper method to create Checkout instance from class using reflection.
         */
        private Checkout createCheckout(Class<? extends Checkout> clazz) throws Exception {
                Constructor<? extends Checkout> constructor = clazz.getConstructor();
                return constructor.newInstance();
        }

        /**
         * SAMPLE TEST 1: Tests successful checkout of an available book
         * This tests the valid equivalence partition - all conditions met.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T2: Successful checkout - available book, eligible patron")
        public void testBookAvailable(Class<? extends Checkout> checkoutClass) throws Exception {
                checkout = createCheckout(checkoutClass);

                // Setup: Create available book and eligible patron
                Book book = new Book("978-0-123456-78-9", "Test Book",
                        "Test Author", Book.BookType.FICTION, 1);

                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);

                checkout.addBook(book); // adding the book to the library
                checkout.registerPatron(patron); // adding a patrol to the system

                // Execute checkout
                double result = checkout.checkoutBook(book, patron);

                // Verify: Should return 0.0 for success
                assertEquals(0.0, result, 0.01,
                        "Expected successful checkout (0.0) for " + checkoutClass.getSimpleName());

                // Verify: Book should now be unavailable
                assertFalse(book.isAvailable(),
                        "Book should be unavailable after checkout for " + checkoutClass.getSimpleName());

                // Verify: Patron should have the book in their checked-out list
                assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                        "Patron should have book in checked-out list for " + checkoutClass.getSimpleName());

                // Verify: Checkout count increased
                assertEquals(1, patron.getCheckoutCount(),
                        "Patron checkout count should be 1 for " + checkoutClass.getSimpleName());
        }

        /**
         * SAMPLE TEST 2: Tests checkout with unavailable book
         * This tests an invalid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T1: Unavailable book returns error code 2.0")
        public void testUnavailableBook(Class<? extends Checkout> checkoutClass) throws Exception {
                checkout = createCheckout(checkoutClass);

                // Setup: Create unavailable book
                Book book = new Book("978-0-123456-78-9", "Test Book",
                        "Test Author", Book.BookType.FICTION, 5);
                book.setAvailableCopies(0);  // We are pretending it has been checked out by others and is not available anymore

                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);

                checkout.addBook(book);
                checkout.registerPatron(patron);

                // Execute checkout
                double result = checkout.checkoutBook(book, patron);

                // Verify: Should return 2.0 for unavailable book
                assertEquals(2.0, result, 0.01,
                        "Expected error code 2.0 for unavailable book for " + checkoutClass.getSimpleName());

                // Verify: Patron should NOT have the book
                assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
                        "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 3: Tests checkout with null book
         * This tests an invalid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T3: Null book returns error code 2.1")
        public void testNullBook(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book = null; // Null book
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
                checkout.addBook(book);
                checkout.registerPatron(patron);

                // Execute checkout
                double result = checkout.checkoutBook(book, patron);
                // Verify: Should return 2.1 for null book
                assertEquals(2.1, result, 0.01,
                        "Expected error code 2.1 for null book for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the book
                assertFalse(patron.hasBookCheckedOut("978-0-123456-78-9"),
                        "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 4: Tests checkout with renewing a book
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T4: Renewing book returns warning code 0.1")
        public void testRenewingAbook(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book = new Book("978-0-123456-78-9", "Test Book",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
                checkout.addBook(book);
                checkout.registerPatron(patron);
                
                // First checkout the book
                checkout.checkoutBook(book, patron);
                // Now try to checkout the same book again (renewal)
                double result = checkout.checkoutBook(book, patron);
                // Verify: Should return 0.1 for renewing a book
                assertEquals(0.1, result, 0.01,
                        "Expected error code 0.1 for renewing a book for " + checkoutClass.getSimpleName());
                // Verify: Patron should still have the book
                assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                        "Patron should still have book in list for " + checkoutClass.getSimpleName());  
        }
        /**
         * SAMPLE TEST 5: Tests checkout with patron with an overdue book
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T5: Patron with overdue book returns warning code 1.0")
        public void testSomeOverdueBook(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Simulate overdue
                patron.setOverdueCount(1);
                // Now try to checkout another book     
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 1.0 for patron with overdue book
                assertEquals(1.0, result, 0.01,
                        "Expected error code 1.0 for patron with overdue book for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the second book
                assertTrue(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should have second book in list for " + checkoutClass.getSimpleName());
        }

        /**
         * SAMPLE TEST 6: Tests checkout with patron Faculty with 18 checked out books
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T6: Patron with 18 checked out books returns warning code 1.1")
        public void testNearMaxFaculty(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book7 = new Book("978-0-123456-78-15", "Test Book7",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book8 = new Book("978-0-123456-78-16", "Test Book8",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book9 = new Book("978-0-123456-78-17", "Test Book9",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book10 = new Book("978-0-123456-78-18", "Test Book10",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book11 = new Book("978-0-123456-78-19", "Test Book11",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book12 = new Book("978-0-123456-78-20", "Test Book12",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book13 = new Book("978-0-123456-78-21", "Test Book13",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book14 = new Book("978-0-123456-78-22", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book15 = new Book("978-0-123456-78-23", "Test Book15",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book16 = new Book("978-0-123456-78-24", "Test Book16",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book17 = new Book("978-0-123456-78-25", "Test Book17",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book18 = new Book("978-0-123456-78-26", "Test Book18",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book19 = new Book("978-0-123456-78-27", "Test Book19",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.FACULTY);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.addBook(book7);
                checkout.addBook(book8);
                checkout.addBook(book9);
                checkout.addBook(book10);
                checkout.addBook(book11);
                checkout.addBook(book12);
                checkout.addBook(book13);
                checkout.addBook(book14);
                checkout.addBook(book15);
                checkout.addBook(book16);
                checkout.addBook(book17);
                checkout.addBook(book18);
                checkout.addBook(book19);
                checkout.registerPatron(patron);
                // First checkout 18 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                checkout.checkoutBook(book6, patron);
                checkout.checkoutBook(book7, patron);
                checkout.checkoutBook(book8, patron);
                checkout.checkoutBook(book9, patron);
                checkout.checkoutBook(book10, patron);
                checkout.checkoutBook(book11, patron);
                checkout.checkoutBook(book12, patron);
                checkout.checkoutBook(book13, patron);
                checkout.checkoutBook(book14, patron);
                checkout.checkoutBook(book15, patron);
                checkout.checkoutBook(book16, patron);
                checkout.checkoutBook(book17, patron);
                checkout.checkoutBook(book18, patron);
                // Now try to checkout the 19th book
                double result = checkout.checkoutBook(book19, patron);
                // Verify: Should return 1.1 for patron with 18 checked out books
                assertEquals(1.1, result, 0.01,
                        "Expected warning code 1.1 for patron with 18 checked out books for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the 19th book
                assertTrue(patron.hasBookCheckedOut(book19.getIsbn()),
                        "Patron should have 19th book in list for " + checkoutClass.getSimpleName());
        }
               /**
         * SAMPLE TEST 7: Tests checkout with patron Student with 8 checked out books
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T7: Patron with 8 checked out books returns warning code 1.1")
        public void testNearMaxStudent(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book7 = new Book("978-0-123456-78-15", "Test Book7",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book8 = new Book("978-0-123456-78-16", "Test Book8",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book9 = new Book("978-0-123456-78-17", "Test Book9",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.addBook(book7);
                checkout.addBook(book8);
                checkout.addBook(book9);
                checkout.registerPatron(patron);
                // First checkout 8 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                checkout.checkoutBook(book6, patron);
                checkout.checkoutBook(book7, patron);
                checkout.checkoutBook(book8, patron);
                // Now try to checkout the 9th book
                double result = checkout.checkoutBook(book9, patron);
                // Verify: Should return 1.1 for patron with 8 checked out books
                assertEquals(1.1, result, 0.01,
                        "Expected warning code 1.1 for patron with 8 checked out books for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the 9th book
                assertTrue(patron.hasBookCheckedOut(book9.getIsbn()),
                        "Patron should have 9th book in list for " + checkoutClass.getSimpleName());
        }
               /**
         * SAMPLE TEST 8: Tests checkout with patron Public with 3 checked out books
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T8: Patron with 3 checked out books returns warning code 1.1")
        public void testNearMaxPublic(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.PUBLIC);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.registerPatron(patron);
                // First checkout 18 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                // Now try to checkout the 4th book
                double result = checkout.checkoutBook(book4, patron);
                // Verify: Should return 1.1 for patron with 3 checked out books
                assertEquals(1.1, result, 0.01,
                        "Expected warning code 1.1 for patron with 3 checked out books for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the 4th book
                assertTrue(patron.hasBookCheckedOut(book4.getIsbn()),
                        "Patron should have 4th book in list for " + checkoutClass.getSimpleName());
        }
       /**
         * SAMPLE TEST 9: Tests checkout with patron Child with 1 checked out books
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T9: Patron with 1 checked out books returns warning code 1.1")
        public void testNearMaxChild(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.CHILD);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.registerPatron(patron);
                // First checkout 18 books
                checkout.checkoutBook(book1, patron);
                // Now try to checkout the 2nd book
                double result = checkout.checkoutBook(book2, patron);
                // Verify: Should return 1.1 for patron with 1 checked out books
                assertEquals(1.1, result, 0.01,
                        "Expected warning code 1.1 for patron with 1 checked out books for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the 2nd book
                assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
                        "Patron should have 2nd book in list for " + checkoutClass.getSimpleName());
        }
       /**
         * SAMPLE TEST 10: Tests checkout with patron Staff with 13 checked out books
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T10: Patron with 13 checked out books returns warning code 1.1")
        public void testNearMaxStaff(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book7 = new Book("978-0-123456-78-15", "Test Book7",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book8 = new Book("978-0-123456-78-16", "Test Book8",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book9 = new Book("978-0-123456-78-17", "Test Book9",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book10 = new Book("978-0-123456-78-18", "Test Book10",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book11 = new Book("978-0-123456-78-19", "Test Book11",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book12 = new Book("978-0-123456-78-20", "Test Book12",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book13 = new Book("978-0-123456-78-21", "Test Book13",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book14 = new Book("978-0-123456-78-22", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STAFF);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.addBook(book7);
                checkout.addBook(book8);
                checkout.addBook(book9);
                checkout.addBook(book10);
                checkout.addBook(book11);
                checkout.addBook(book12);
                checkout.addBook(book13);
                checkout.addBook(book14);
                checkout.registerPatron(patron);
                // First checkout 18 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                checkout.checkoutBook(book6, patron);
                checkout.checkoutBook(book7, patron);
                checkout.checkoutBook(book8, patron);
                checkout.checkoutBook(book9, patron);
                checkout.checkoutBook(book10, patron);
                checkout.checkoutBook(book11, patron);
                checkout.checkoutBook(book12, patron);
                checkout.checkoutBook(book13, patron);
                // Now try to checkout the 14th book
                double result = checkout.checkoutBook(book14, patron);
                // Verify: Should return 1.1 for patron with 13 checked out books
                assertEquals(1.1, result, 0.01,
                        "Expected warning code 1.1 for patron with 13 checked out books for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the 14th book
                assertTrue(patron.hasBookCheckedOut(book14.getIsbn()),
                        "Patron should have 14th book in list for " + checkoutClass.getSimpleName());
        }
       /**
         * SAMPLE TEST 11: Tests checkout with patron null type with 3 checked out books
         * This tests an valid equivalence partition.
         */
       /* @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T11: Patron with 3 checked out books returns warning code 1.1")
        public void testNearMaxNull(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        NullEnum.NULL);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.registerPatron(patron);
                // First checkout 18 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                // Now try to checkout the 4th book
                double result = checkout.checkoutBook(book4, patron);
                // Verify: Should return 1.1 for patron with 3 checked out books
                assertEquals(1.1, result, 0.01,
                        "Expected warning code 1.1 for patron with 3 checked out books for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the 4th book
                assertTrue(patron.hasBookCheckedOut(book4.getIsbn()),
                        "Patron should have 4th book in list for " + checkoutClass.getSimpleName());
        }
*/
       /**
         * SAMPLE TEST 12: Tests checkout with a reference books
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T12: Patron with a reference book returns error code 5.0")
        public void testReferenceBook(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.REFERENCE, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STAFF);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Now try to checkout the reference book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 5.0 for reference book
                assertEquals(5.0, result, 0.01,
                        "Expected error code 5.0 for reference book for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the reference book
                assertFalse(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should NOT have reference book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 13: Tests checkout with a suspended patron
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T13: Patron with suspended account returns error code 3.0")
        public void testSuspendedPatron(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STAFF);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Suspend the patron's account
                patron.setAccountSuspended(true);
                // Now try to checkout the book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 3.0 for suspended account
                assertEquals(3.0, result, 0.01,
                        "Expected error code 3.0 for suspended account for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the book
                assertFalse(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
        }

        /**
         * SAMPLE TEST 14: Tests checkout with a null patron
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T14: Null patron returns error code 3.1")
        public void testNullPatron(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = null; // Null patron
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Now try to checkout the book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 3.1 for null patron
                assertEquals(3.1, result, 0.01,
                        "Expected error code 3.1 for null patron for " + checkoutClass.getSimpleName());
                // Verify: Null patron should NOT have the book
                assertFalse(patron != null && patron.hasBookCheckedOut(book1.getIsbn()),
                        "Null patron should NOT have book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 15: Tests checkout with a patron with 3 overdue books
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T15: Patron with 3 overdue books returns error code 4.0")
        public void testOverdueLimit(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STAFF);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Simulate overdue
                patron.setOverdueCount(3);
                // Now try to checkout the 4th book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 4.0 for patron with 3 overdue books
                assertEquals(4.0, result, 0.01,
                        "Expected error code 4.0 for patron with 3 overdue books for " +checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 4th book
                assertFalse(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should NOT have 4th book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 16: Tests checkout with a patron with too high of a tab
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T16: Patron with high tab returns error code 4.1")
        public void testTooHighOfTab(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STAFF);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Simulate high tab by setting the patron's tab to a high value 
                patron.addFine(10.0);
                // Now try to checkout the book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 4.1 for patron with high tab
                assertEquals(4.1, result, 0.01,
                        "Expected error code 4.1 for patron with high tab for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the book
                assertFalse(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 17: Tests checkout with a patron Faculty trying byond max checkouts
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T17: Patron trying to renew a book returns error code 3.2")
        public void testMaxCheckoutFaculty(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book7 = new Book("978-0-123456-78-15", "Test Book7",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book8 = new Book("978-0-123456-78-16", "Test Book8",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book9 = new Book("978-0-123456-78-17", "Test Book9",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book10 = new Book("978-0-123456-78-18", "Test Book10",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book11 = new Book("978-0-123456-78-19", "Test Book11",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book12 = new Book("978-0-123456-78-20", "Test Book12",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book13 = new Book("978-0-123456-78-21", "Test Book13",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book14 = new Book("978-0-123456-78-22", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book15 = new Book("978-0-123456-78-23", "Test Book15",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book16 = new Book("978-0-123456-78-24", "Test Book16",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book17 = new Book("978-0-123456-78-25", "Test Book17",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book18 = new Book("978-0-123456-78-26", "Test Book18",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book19 = new Book("978-0-123456-78-27", "Test Book19",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book20 = new Book("978-0-123456-78-28", "Test Book20",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book21 = new Book("978-0-123456-78-29", "Test Book21",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.FACULTY);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.addBook(book7);
                checkout.addBook(book8);
                checkout.addBook(book9);
                checkout.addBook(book10);
                checkout.addBook(book11);
                checkout.addBook(book12);
                checkout.addBook(book13);
                checkout.addBook(book14);
                checkout.addBook(book15);
                checkout.addBook(book16);
                checkout.addBook(book17);
                checkout.addBook(book18);
                checkout.addBook(book19);
                checkout.addBook(book20);
                checkout.addBook(book21);
                checkout.registerPatron(patron);
                // First checkout 20 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                checkout.checkoutBook(book6, patron);
                checkout.checkoutBook(book7, patron);
                checkout.checkoutBook(book8, patron);
                checkout.checkoutBook(book9, patron);
                checkout.checkoutBook(book10, patron);
                checkout.checkoutBook(book11, patron);
                checkout.checkoutBook(book12, patron);
                checkout.checkoutBook(book13, patron);
                checkout.checkoutBook(book14, patron);
                checkout.checkoutBook(book15, patron);
                checkout.checkoutBook(book16, patron);
                checkout.checkoutBook(book17, patron);
                checkout.checkoutBook(book18, patron);
                checkout.checkoutBook(book19, patron);
                checkout.checkoutBook(book20, patron);
                // Now try to checkout the 21st book
                double result = checkout.checkoutBook(book21, patron);
                // Verify: Should return 3.2 for patron with 20 checked out books
                assertEquals(3.2, result, 0.01,
                        "Expected error code 3.2 for patron trying to checkout beyond max checkouts for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 21st book
                assertFalse(patron.hasBookCheckedOut(book21.getIsbn()),
                        "Patron should NOT have 21st book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 18: Tests checkout with a patron Student trying byond max checkouts
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T17: Patron trying to renew a book returns error code 3.2")
        public void testMaxCheckoutStudent(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book7 = new Book("978-0-123456-78-15", "Test Book7",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book8 = new Book("978-0-123456-78-16", "Test Book8",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book9 = new Book("978-0-123456-78-17", "Test Book9",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book10 = new Book("978-0-123456-78-18", "Test Book10",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book11 = new Book("978-0-123456-78-19", "Test Book11",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STUDENT);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.addBook(book7);
                checkout.addBook(book8);
                checkout.addBook(book9);
                checkout.addBook(book10);
                checkout.addBook(book11);
                checkout.registerPatron(patron);
                // First checkout 20 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                checkout.checkoutBook(book6, patron);
                checkout.checkoutBook(book7, patron);
                checkout.checkoutBook(book8, patron);
                checkout.checkoutBook(book9, patron);
                checkout.checkoutBook(book10, patron);
                // Now try to checkout the 11th book
                double result = checkout.checkoutBook(book11, patron);
                // Verify: Should return 3.2 for patron with 10 checked out books
                assertEquals(3.2, result, 0.01,
                        "Expected error code 3.2 for patron trying to checkout beyond max checkouts for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 11th book
                assertFalse(patron.hasBookCheckedOut(book11.getIsbn()),
                        "Patron should NOT have 11th book in list for " + checkoutClass.getSimpleName());
        }
/**
         * SAMPLE TEST 19: Tests checkout with a patron Public trying byond max checkouts
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T19: Patron trying to checkout a book returns error code 3.2")
        public void testMaxCheckoutPublic(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.PUBLIC);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.registerPatron(patron);
                // First checkout 20 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                // Now try to checkout the 6th book
                double result = checkout.checkoutBook(book6, patron);
                // Verify: Should return 3.2 for patron with 5 checked out books
                assertEquals(3.2, result, 0.01,
                        "Expected error code 3.2 for patron trying to checkout beyond max checkouts for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 6th book
                assertFalse(patron.hasBookCheckedOut(book6.getIsbn()),
                        "Patron should NOT have 6th book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 20: Tests checkout with a patron Child trying byond max checkouts
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T20: Patron trying to renew a book returns error code 3.2")
        public void testMaxCheckoutChild(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.FACULTY);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.registerPatron(patron);
                // First checkout 20 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                // Now try to checkout the 21st book
                double result = checkout.checkoutBook(book4, patron);
                // Verify: Should return 3.2 for patron with 3 checked out books
                assertEquals(3.2, result, 0.01,
                        "Expected error code 3.2 for patron trying to checkout beyond max checkouts for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 4th book
                assertFalse(patron.hasBookCheckedOut(book4.getIsbn()),
                        "Patron should NOT have 4th book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 21: Tests checkout with a patron Staff trying byond max checkouts
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T21: Patron trying to renew a book returns error code 3.2")
        public void testMaxCheckoutStaff(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book7 = new Book("978-0-123456-78-15", "Test Book7",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book8 = new Book("978-0-123456-78-16", "Test Book8",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book9 = new Book("978-0-123456-78-17", "Test Book9",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book10 = new Book("978-0-123456-78-18", "Test Book10",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book11 = new Book("978-0-123456-78-19", "Test Book11",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book12 = new Book("978-0-123456-78-20", "Test Book12",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book13 = new Book("978-0-123456-78-21", "Test Book13",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book14 = new Book("978-0-123456-78-22", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book15 = new Book("978-0-123456-78-23", "Test Book15",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book16 = new Book("978-0-123456-78-24", "Test Book16",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.STAFF);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.addBook(book7);
                checkout.addBook(book8);
                checkout.addBook(book9);
                checkout.addBook(book10);
                checkout.addBook(book11);
                checkout.addBook(book12);
                checkout.addBook(book13);
                checkout.addBook(book14);
                checkout.addBook(book15);
                checkout.addBook(book16);
                checkout.registerPatron(patron);
                // First checkout 20 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                checkout.checkoutBook(book6, patron);
                checkout.checkoutBook(book7, patron);
                checkout.checkoutBook(book8, patron);
                checkout.checkoutBook(book9, patron);
                checkout.checkoutBook(book10, patron);
                checkout.checkoutBook(book11, patron);
                checkout.checkoutBook(book12, patron);
                checkout.checkoutBook(book13, patron);
                checkout.checkoutBook(book14, patron);
                checkout.checkoutBook(book15, patron);
                // Now try to checkout the 16th book
                double result = checkout.checkoutBook(book16, patron);
                // Verify: Should return 3.2 for patron with 15 checked out books
                assertEquals(3.2, result, 0.01,
                        "Expected error code 3.2 for patron trying to checkout beyond max checkouts for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 16th book
                assertFalse(patron.hasBookCheckedOut(book16.getIsbn()),
                        "Patron should NOT have 16th book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 22: Tests checkout with a patron type null trying byond max checkouts
         * This tests an valid equivalence partition.
         */
       /* @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T22: Patron trying to checkout a book returns error code 3.2")
        public void testMaxCheckoutNull(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book2 = new Book("978-0-123456-78-10", "Test Book2",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book3 = new Book("978-0-123456-78-11", "Test Book3",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book4 = new Book("978-0-123456-78-12", "Test Book4",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book5 = new Book("978-0-123456-78-13", "Test Book5",
                        "Test Author", Book.BookType.FICTION, 5);
                Book book6 = new Book("978-0-123456-78-14", "Test Book6",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.FACULTY);
                checkout.addBook(book1);
                checkout.addBook(book2);
                checkout.addBook(book3);
                checkout.addBook(book4);
                checkout.addBook(book5);
                checkout.addBook(book6);
                checkout.registerPatron(patron);
                // First checkout 20 books
                checkout.checkoutBook(book1, patron);
                checkout.checkoutBook(book2, patron);
                checkout.checkoutBook(book3, patron);
                checkout.checkoutBook(book4, patron);
                checkout.checkoutBook(book5, patron);
                // Now try to checkout the 6th book
                double result = checkout.checkoutBook(book6, patron);
                // Verify: Should return 3.2 for patron with 5 checked out books
                assertEquals(3.2, result, 0.01,
                        "Expected error code 3.2 for patron trying to checkout beyond max checkouts for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 6th book
                assertFalse(patron.hasBookCheckedOut(book6.getIsbn()),
                        "Patron should NOT have 6th book in list for " + checkoutClass.getSimpleName());
        }
*/
        /**
         * SAMPLE TEST 23: Tests checkout with a patron with a high Overdue at 2
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T23: Patron trying to checkout a book returns warning code 1.0")
        public void testWarningHighOverdue(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.FACULTY);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Simulate overdue
                patron.setOverdueCount(2);
                // Now try to checkout the 3rd book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 1.0 for patron with 2 overdue books
                assertEquals(1.0, result, 0.01,
                        "Expected warning code 1.0 for patron with 2 overdue books for " + checkoutClass.getSimpleName());
                // Verify: Patron should have the 3rd book
                assertTrue(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should have 3rd book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 24: Tests checkout with a patron with a high Overdue at 4
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T24: Patron trying to checkout a book returns error code 4.0")
        public void testAboveOverdue(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.FACULTY);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Simulate overdue
                patron.setOverdueCount(4);
                // Now try to checkout the 5th book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 4.0 for patron with 4 overdue books
                assertEquals(4.0, result, 0.01,
                        "Expected error code 4.0 for patron with 4 overdue books for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 5th book
                assertFalse(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should NOT have 5th book in list for " + checkoutClass.getSimpleName());
        }
        /**
         * SAMPLE TEST 25: Tests checkout with a patron with a high tab at 14
         * This tests an valid equivalence partition.
         */
        @ParameterizedTest
        @MethodSource("checkoutClassProvider")
        @DisplayName("T25: Patron trying to checkout a book returns error code 14.0")
        public void testAboveTabLimit(Class<? extends Checkout> checkoutClass) throws Exception {
                //Setup: Create checkout system and patron
                checkout = createCheckout(checkoutClass);
                Book book1 = new Book("978-0-123456-78-9", "Test Book1",
                        "Test Author", Book.BookType.FICTION, 5);
                Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                        Patron.PatronType.FACULTY);
                checkout.addBook(book1);
                checkout.registerPatron(patron);
                // Simulate high tab by setting the patron's tab to a high value 
                patron.addFine(14.0);
                // Now try to checkout the book
                double result = checkout.checkoutBook(book1, patron);
                // Verify: Should return 14.0 for patron with a high tab
                assertEquals(14.0, result, 0.01,
                        "Expected error code 14.0 for patron with a high tab for " + checkoutClass.getSimpleName());
                // Verify: Patron should NOT have the 5th book
                assertFalse(patron.hasBookCheckedOut(book1.getIsbn()),
                        "Patron should NOT have 5th book in list for " + checkoutClass.getSimpleName());
        }
                

}
