import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        checkout.addBook(book5);

        int result = checkout.countBooksByType(Book.BookType.FICTION, false);
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

        int result = checkout.countBooksByType(Book.BookType.FICTION, false);
        assertEquals(1, result, "Should return 1 for mixed booklist");
    }

    @Test
    @DisplayName("WB Test5: countBooksByType -  type is not avaiable edges")
    public void testCountBooksByType_TypeNotAvailableEdges() {
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
        //checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);

        int result = checkout.countBooksByType(Book.BookType.FICTION, false);
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
        checkout.addBook(book5);

        int result = checkout.countBooksByType(Book.BookType.FICTION, false);
        assertEquals(1, result, "Should return 1 for mixed booklist");
    }    
}
