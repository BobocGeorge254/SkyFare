import { useState, useEffect } from "react";
import styles from "./css/BooksPage.module.css";
import SearchBar from "../components/SearchBar";
import { useAuth } from "../context/AuthContext";
import { fetchAuthors } from "../services/authorsService";
import { fetchCategories } from "../services/categoriesService";
import { fetchReviewsForBook, createReview } from "../services/reviewService";
import { addBookToWishlist } from "../services/wishlistService";
import { fetchBooks, createBook, updateBook, deleteBook } from "../services/booksService";
import { useSearchParams } from "react-router-dom";
import { useNavigate } from "react-router-dom"; 


export default function BooksPage() {
  const { token, userProfileId } = useAuth();

  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [authors, setAuthors] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [selectedAuthor, setSelectedAuthor] = useState("all");
  const [newReview, setNewReview] = useState({ rating: 5, comment: "" });
  const navigate = useNavigate();

  const [newBook, setNewBook] = useState({
    title: "",
    authorId: "",
    categoryId: "",
    image: null,
  });

  const [editingBook, setEditingBook] = useState(null);
  const [searchParams] = useSearchParams();

  useEffect(() => {
    if (token) {
      const authorParam = searchParams.get("author") || "all";
      const categoryParam = searchParams.get("category") || "all";
      setSelectedAuthor(authorParam);
      setSelectedCategory(categoryParam);
      loadBooks(authorParam, categoryParam);
      loadAuthors();
      loadCategories();
    }
  }, [token, searchParams]);

  const loadBooks = async (author = selectedAuthor, category = selectedCategory) => {
    try {
      const data = await fetchBooks(token, { authorId: author, categoryId: category });
      setBooks(data);
      setFilteredBooks(data);
    } catch (err) {
      console.error("Error fetching books:", err);
    }
  };

  const loadAuthors = async () => {
    try {
      const data = await fetchAuthors(token);
      setAuthors(data);
    } catch (err) {
      console.error("Error fetching authors:", err);
    }
  };

  const loadCategories = async () => {
    try {
      const data = await fetchCategories(token);
      setCategories(data);
    } catch (err) {
      console.error("Error fetching categories:", err);
    }
  };

const handleSearch = (query) => {
  let filtered = books;
  if (query) {
    filtered = filtered.filter((book) =>
      book.title.toLowerCase().includes(query.toLowerCase())
    );
  }
  setFilteredBooks(filtered);
};


  const handleCategoryChange = async (e) => {
    const category = e.target.value;
    setSelectedCategory(category);
    loadBooks(selectedAuthor, category);
  };

  const handleAuthorChange = async (e) => {
    const author = e.target.value;
    setSelectedAuthor(author);
    loadBooks(author, selectedCategory);
  };


  const handleInputChange = (e) => {
    const { name, value, files } = e.target;
    setNewBook({
      ...newBook,
      [name]: files ? files[0] : value,
    });
  };

  const handleCreateBook = async (e) => {
    e.preventDefault();
    try {
      await createBook(token, newBook);
      loadBooks();
      setNewBook({ title: "", authorId: "", categoryId: "", image: null });
    } catch (err) {
      console.error("Error creating book:", err);
    }
  };

  const handleEditBook = (book) => {
    setEditingBook(book);
    setNewBook({
      title: book.title,
      authorId: book.author?.id || "",
      categoryId: book.category?.id || "",
      image: null,
    });
  };

  const handleUpdateBook = async (e) => {
    e.preventDefault();
    if (!editingBook) return;

    try {
      await updateBook(token, editingBook.id, newBook);
      loadBooks();
      setEditingBook(null);
      setNewBook({ title: "", authorId: "", categoryId: "", image: null });
    } catch (err) {
      console.error("Error updating book:", err);
    }
  };

  const handleDeleteBook = async (id) => {
    try {
      await deleteBook(token, id);
      loadBooks();
    } catch (err) {
      console.error("Error deleting book:", err);
    }
  };

  const handleAddToWishlist = async (bookId) => {
    try {
      await addBookToWishlist(token, userProfileId, bookId);
      alert("Book added to wishlist!");
    } catch (err) {
      console.error("Error adding book to wishlist:", err);
      alert("Failed to add book to wishlist.");
    }
  };
  const handleSeeDetails = (bookId) => {
    navigate(`/books/${bookId}`);
  };

  return (
  <div className={styles.pageContainer}>
    <h2 className={styles.pageTitle}>Books</h2>

    <SearchBar placeholder="Search books..." onSearch={handleSearch} />

    <div className={styles.filters}>
      <select
        value={selectedCategory}
        onChange={handleCategoryChange}
        className={styles.inputField}
      >
        <option value="all">All Categories</option>
        {categories.map((cat) => (
          <option key={cat.id} value={cat.id}>
            {cat.name}
          </option>
        ))}
      </select>

      <select
        value={selectedAuthor}
        onChange={handleAuthorChange}
        className={styles.inputField}
      >
        <option value="all">All Authors</option>
        {authors.map((author) => (
          <option key={author.id} value={author.id}>
            {author.name}
          </option>
        ))}
      </select>
    </div>

    {userProfileId === null && (
      <form
        onSubmit={editingBook ? handleUpdateBook : handleCreateBook}
        className={styles.formContainer}
      >
        <input
          type="text"
          name="title"
          placeholder="Book title"
          value={newBook.title}
          onChange={handleInputChange}
          className={styles.inputField}
          required
        />

        <select
          name="authorId"
          value={newBook.authorId}
          onChange={handleInputChange}
          className={styles.inputField}
          required
        >
          <option value="">Select author</option>
          {Array.isArray(authors) &&
            authors.map((author) => (
              <option key={author.id} value={author.id}>
                {author.name}
              </option>
            ))}
        </select>

        <select
          name="categoryId"
          value={newBook.categoryId}
          onChange={handleInputChange}
          className={styles.inputField}
          required
        >
          <option value="">Select category</option>
          {Array.isArray(categories) &&
            categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
        </select>

        <input
          type="file"
          name="image"
          onChange={handleInputChange}
          className={styles.inputField}
        />

        <button type="submit" className={styles.submitButton}>
          {editingBook ? "Update Book" : "Add Book"}
        </button>
        {editingBook && (
          <button
            type="button"
            onClick={() => {
              setEditingBook(null);
              setNewBook({
                title: "",
                authorId: "",
                categoryId: "",
                image: null,
              });
            }}
            className={styles.cancelButton}
          >
            Cancel
          </button>
        )}
      </form>
    )}

      <ul className={styles.list}>
        {filteredBooks.map((book) => (
          <li key={book.id} className={styles.listItem}>
            <div className={styles.bookHeader}>
              <span className={styles.bookTitle}>{book.title}</span>
              <div className={styles.bookMeta}>
                {book.author?.name || "Unknown"} ({book.category?.name || "Uncategorized"})
              </div>

              {book.imageUrl && (
                <img
                  src={book.imageUrl || "/default-avatar.png"}
                  alt={book.title}
                  className={styles.bookImage}
                />
              )}

              <div className={styles.bookActions}>
                {(userProfileId || userProfileId === null) && (
                  <>
                    <button
                      onClick={() => handleSeeDetails(book.id)}
                      className={`${styles.actionButton} ${styles.seeDetailsButton}`}
                    >
                      See Details
                    </button>

                    {userProfileId && (
                      <button
                        onClick={() => handleAddToWishlist(book.id)}
                        className={`${styles.actionButton} ${styles.wishlistButton}`}
                      >
                        Add to Wishlist
                      </button>
                    )}

                    {userProfileId === null && (
                      <>
                        <button
                          onClick={() => handleEditBook(book)}
                          className={`${styles.actionButton} ${styles.editButton}`}
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleDeleteBook(book.id)}
                          className={`${styles.actionButton} ${styles.deleteButton}`}
                        >
                          Delete
                        </button>
                      </>
                    )}
                  </>
                )}
              </div>
            </div>
        </li>
      ))}
    </ul>
  </div>
);
}
