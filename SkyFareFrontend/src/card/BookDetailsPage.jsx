import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchBookById } from "../services/booksService";
import { fetchReviewsForBook, createReview, deleteReview } from "../services/reviewService";
import { useAuth } from "../context/AuthContext";
import styles from "./css/BookDetailsPage.module.css";

export default function BookDetailsPage() {
  const { id } = useParams();
  const { token, userProfileId } = useAuth();

  const [book, setBook] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [showReviews, setShowReviews] = useState(false);
  const [newReview, setNewReview] = useState({ rating: 5, comment: "" });
  const [sortOrder, setSortOrder] = useState("desc");

  useEffect(() => {
    if (token) loadBook();
  }, [token, id]);

  const loadBook = async () => {
    try {
      const data = await fetchBookById(token, id);
      setBook(data);
    } catch (err) {
      console.error("Error fetching book details:", err);
    }
  };

  const handleToggleReviews = async () => {
    if (showReviews) {
      setShowReviews(false);
      setReviews([]);
    } else {
      try {
        const data = await fetchReviewsForBook(token, id);
        setReviews(sortReviews(data));
        console.log("review", data)
        setShowReviews(true);
      } catch (err) {
        console.error("Error fetching reviews:", err);
      }
    }
  };

  const handleAddReview = async () => {
    if (!newReview.comment.trim()) return;
    try {
      await createReview(token, id, userProfileId, newReview.rating, newReview.comment);
      const data = await fetchReviewsForBook(token, id);
      setReviews(data);
      setNewReview({ rating: 5, comment: "" });
    } catch (err) {
      console.error("Error adding review:", err);
    }
  };

  const handleDeleteReview = async (reviewId) => {
    try {
      await deleteReview(token, reviewId);
      setReviews(reviews.filter((rev) => rev.id !== reviewId));
    } catch (err) {
      console.error("Error deleting review:", err);
    }
  };

  const sortReviews = (reviewsArray) => {
    return [...reviewsArray].sort((a, b) =>
      sortOrder === "asc" ? a.rating - b.rating : b.rating - a.rating
    );
  };

  if (!book) return <p>Loading book...</p>;

    function formatUserName(userProfile) {
    if (!userProfile || !userProfile.user) return "Anonymous";

    const firstName = userProfile.user.firstName || "";
    const lastName = userProfile.user.lastName || "";

    return `${lastName} ${firstName}`.trim() || "Anonymous";
  }

  return (
  <div className={styles.pageContainer}>
    <h2 className={styles.pageTitle}>{book.title}</h2>
    <p className={styles.meta}>
      {book.author?.name || "Unknown"} ({book.category?.name || "Uncategorized"})
    </p>
    {book.imageUrl && (
      <img src={book.imageUrl} alt={book.title} className={styles.bookImage} />
    )}

    <button onClick={handleToggleReviews} className={styles.reviewToggle}>
      {showReviews ? "Hide Reviews" : "Show Reviews"}
    </button>

    {showReviews && (
      <>
        <div className={styles.sortContainer}>
          <label>Sort by rating: </label>
          <select
            value={sortOrder}
            onChange={(e) => {
              setSortOrder(e.target.value);
              setReviews(sortReviews(reviews));
            }}
          >
            <option value="desc">High → Low</option>
            <option value="asc">Low → High</option>
          </select>
        </div>

        <div className={styles.reviews}>
          <h3>Reviews</h3>
          {reviews.length === 0 && <p className={styles.noReviews}>No reviews yet.</p>}
          <ul className={styles.reviewList}>
            {reviews.map((rev) => (
              <li key={rev.id} className={styles.reviewCard}>
                <div className={styles.reviewHeader}>
                  <span className={styles.reviewUser}>
                    {formatUserName(rev.userProfile) || "Anonymous"}
                  </span>
                  <span className={styles.reviewRating}>Rating: {rev.rating}/5</span>
                </div>
                <p className={styles.reviewComment}>{rev.comment}</p>
                {(userProfileId === rev.userProfile.id) && (
                  <div className={styles.reviewActions}>
                    {userProfileId === rev.userProfile.id && (
                      <button className={styles.editButton}>Edit</button>
                    )}
                    <button
                      className={styles.deleteButton}
                      onClick={() => handleDeleteReview(rev.id)}
                    >
                      Delete
                    </button>
                  </div>
                )}
              </li>
            ))}
          </ul>

          {userProfileId && (
            <div className={styles.addReview}>
              <textarea
                value={newReview.comment}
                onChange={(e) =>
                  setNewReview({ ...newReview, comment: e.target.value })
                }
                placeholder="Write your review..."
              />
              <select
                value={newReview.rating}
                onChange={(e) =>
                  setNewReview({ ...newReview, rating: Number(e.target.value) })
                }
              >
                {[1, 2, 3, 4, 5].map((i) => (
                  <option key={i} value={i}>{i}</option>
                ))}
              </select>
              <button onClick={handleAddReview} className={styles.submitButton}>
                Send
              </button>
            </div>
          )}
        </div>
      </>
    )}
  </div>
);
}
