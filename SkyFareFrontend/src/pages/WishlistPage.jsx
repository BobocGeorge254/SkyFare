import { useEffect, useState } from "react";
import styles from "./css/WishlistPage.module.css";
import { useAuth } from "../context/AuthContext";
import { getWishlistBooks, removeBookFromWishlist } from "../services/wishlistService";
import { useNavigate } from "react-router-dom";
import { fetchCategories } from "../services/categoriesService";

export default function WishlistPage() {
  const { token, userProfileId } = useAuth();
  const [wishlist, setWishlist] = useState([]);
  const [categories, setCategories] = useState([]);
  const navigate = useNavigate();

useEffect(() => {
  if (token && userProfileId) {
    loadWishlist();

    const loadCategories = async () => {
      try {
        const data = await fetchCategories(token);
        setCategories(data);
      } catch (err) {
        console.error("Failed to fetch categories", err);
      }
    };

    loadCategories();
  }
}, [token, userProfileId]);

  const loadWishlist = async () => {
    try {
      const data = await getWishlistBooks(token, userProfileId);
      setWishlist(data);
      console.log("wishlist", data);
    } catch (err) {
      console.error("Error fetching wishlist:", err);
    }
  };

  const handleRemove = async (bookId) => {
    try {
      await removeBookFromWishlist(token, userProfileId, bookId);
      loadWishlist();
    } catch (err) {
      console.error("Error removing book:", err);
    }
  };

  const handleSeeDetails = (bookId) => {
    navigate(`/books/${bookId}`);
  };

  const getCategoryName = (categoryId) => {
    const category = categories.find((cat) => cat.id === categoryId);
    return category ? category.name : "Uncategorized";
  };

  return (
    <div className={styles.pageContainer}>
      <h2 className={styles.pageTitle}>My Wishlist</h2>

      <ul className={styles.list}>
        {wishlist.length > 0 ? (
          wishlist.map((book) => (
            <li key={book.id} className={styles.listItem}>
              <div className={styles.bookInfo}>
                <img
                  src={book.imageUrl || "/default-avatar.png"}
                  alt={book.title}
                  className={styles.bookImage}
                />
                <div>
                  <h3 className={styles.bookTitle}>{book.title}</h3>
                  <p className={styles.bookMeta}>
                    {book.author?.name || "Unknown"} (
                    {getCategoryName(book.category.id)})
                  </p>
                </div>
              </div>

              <div className={styles.actions}>
                <button
                  onClick={() => handleSeeDetails(book.id)}
                  className={styles.detailsButton}
                >
                  See Details
                </button>
                <button
                  onClick={() => handleRemove(book.id)}
                  className={styles.removeButton}
                >
                  Remove
                </button>
              </div>
            </li>
          ))
        ) : (
          <p>Your wishlist is empty.</p>
        )}
      </ul>
    </div>
  );
}
