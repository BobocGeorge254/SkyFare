import { useState, useEffect } from "react";
import axios from "axios";
import styles from "./css/BooksPage.module.css";
import SearchBar from "../components/SearchBar";
import { useAuth } from "../context/AuthContext";
import { fetchAuthors } from "../services/authorsService";
import { fetchCategories } from "../services/categoriesService";

export default function BooksPage() {
  const { token } = useAuth();

  const [books, setBooks] = useState([]);
  const [filteredBooks, setFilteredBooks] = useState([]);
  const [authors, setAuthors] = useState([]);
  const [categories, setCategories] = useState([]);

  const [newBook, setNewBook] = useState({
    title: "",
    authorId: "",
    categoryId: "",
    image: null,
  });

  const [editingBook, setEditingBook] = useState(null);

  useEffect(() => {
    if (token) {
      fetchBooks();
      loadAuthors();
      loadCategories();
    }
  }, [token]);

  const fetchBooks = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/books", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setBooks(res.data.content || []);
      setFilteredBooks(res.data.content || []);
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
    setFilteredBooks(
      books.filter((book) =>
        book.title.toLowerCase().includes(query.toLowerCase())
      )
    );
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
      const formData = new FormData();
      formData.append("title", newBook.title);
      formData.append("authorId", newBook.authorId);
      formData.append("categoryId", newBook.categoryId);
      if (newBook.image) formData.append("image", newBook.image);

      await axios.post("http://localhost:8080/api/books", formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });

      fetchBooks();
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
      const formData = new FormData();
      if (newBook.title) formData.append("title", newBook.title);
      if (newBook.authorId) formData.append("authorId", newBook.authorId);
      if (newBook.categoryId) formData.append("categoryId", newBook.categoryId);
      if (newBook.image) formData.append("image", newBook.image);

      await axios.put(
        `http://localhost:8080/api/books/${editingBook.id}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      fetchBooks();
      setEditingBook(null);
      setNewBook({ title: "", authorId: "", categoryId: "", image: null });
    } catch (err) {
      console.error("Error updating book:", err);
    }
  };

  const handleDeleteBook = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/books/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchBooks();
    } catch (err) {
      console.error("Error deleting book:", err);
    }
  };

  return (
    <div className={styles.pageContainer}>
      <h2 className={styles.pageTitle}>Books</h2>

      <SearchBar placeholder="Search books..." onSearch={handleSearch} />

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

      <ul className={styles.list}>
        {filteredBooks.map((book) => (
          <li key={book.id} className={styles.listItem}>
            <span>
              {book.title} - {book.author?.name || "Unknown"} (
              {book.category?.name || "Uncategorized"})
            </span>
            <div className={styles.actions}>
              <button
                onClick={() => handleEditBook(book)}
                className={styles.editButton}
              >
                Edit
              </button>
              <button
                onClick={() => handleDeleteBook(book.id)}
                className={styles.deleteButton}
              >
                Delete
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
