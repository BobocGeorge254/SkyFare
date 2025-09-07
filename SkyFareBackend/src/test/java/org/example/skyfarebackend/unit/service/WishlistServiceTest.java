package org.example.skyfarebackend.unit.service;

import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.dto.userprofile.UserProfileResponse;
import org.example.skyfarebackend.model.entities.Author;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.model.entities.User;
import org.example.skyfarebackend.model.entities.UserProfile;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.UserProfileRepository;
import org.example.skyfarebackend.service.WishlistService;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    private UserProfileRepository userProfileRepository;
    private BookRepository bookRepository;
    private WishlistService wishlistService;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void setUp() {
        userProfileRepository = mock(UserProfileRepository.class);
        bookRepository = mock(BookRepository.class);
        wishlistService = new WishlistService(userProfileRepository, bookRepository);
        securityContextHolderMock = mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMock.close();
    }

    private User mockCurrentUser(Long id) {
        User user = new User();
        user.setId(id);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        return user;
    }

    private Book buildBook(Long id, String title) {
        Author author = new Author();
        author.setId(101L);
        author.setName("Author " + id);

        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        return book;
    }

    @Test
    void addBookToWishlist_shouldAddBookAndReturnDto() {
        User currentUser = mockCurrentUser(1L);

        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setUser(currentUser);
        profile.setWishlist(new HashSet<>());

        Book book = buildBook(10L, "Book Title");

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(userProfileRepository.save(profile)).thenReturn(profile);

        UserProfileResponse response = wishlistService.addBookToWishlist(1L, 10L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1, response.getWishlist().size());
        assertTrue(response.getWishlist().stream().anyMatch(b -> b.getId().equals(10L)));
    }

    @Test
    void addBookToWishlist_shouldThrowIfNotOwner() {
        mockCurrentUser(99L); // different user

        UserProfile profile = new UserProfile();
        profile.setId(1L);
        User otherUser = new User();
        otherUser.setId(1L);
        profile.setUser(otherUser);

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> wishlistService.addBookToWishlist(1L, 10L));
        assertTrue(ex.getMessage().contains("cannot modify"));
    }

    @Test
    void removeBookFromWishlist_shouldRemoveBookAndReturnDto() {
        User currentUser = mockCurrentUser(1L);

        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setUser(currentUser);

        Book book = buildBook(10L, "Book Title");
        profile.setWishlist(new HashSet<>(Set.of(book)));

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(userProfileRepository.save(profile)).thenReturn(profile);

        UserProfileResponse response = wishlistService.removeBookFromWishlist(1L, 10L);

        assertNotNull(response);
        assertEquals(0, response.getWishlist().size());
    }

    @Test
    void getWishlistBooks_shouldReturnBooks() {
        UserProfile profile = new UserProfile();
        profile.setId(1L);

        Book book1 = buildBook(10L, "First Book");
        Book book2 = buildBook(20L, "Second Book");
        profile.setWishlist(Set.of(book1, book2));

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Set<BookResponse> wishlistBooks = wishlistService.getWishlistBooks(1L);

        assertEquals(2, wishlistBooks.size());
        assertTrue(wishlistBooks.stream().anyMatch(b -> b.getId().equals(10L)));

    }
}
