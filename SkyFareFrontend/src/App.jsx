import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import Authors from "./pages/AuthorsPage";
import Books from "./pages/BooksPage";
import Categories from "./pages/CategoryPage";
import BookDetailsPage from "./card/BookDetailsPage";
import Wishlist from "./pages/WishlistPage";
import ProtectedRoute from "./routes/ProtectedRoute";
import ProtectedLayout from "./layouts/ProtectedLayout";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/auth" element={<AuthPage />} />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <ProtectedLayout />
            </ProtectedRoute>
          }
        >
          <Route path="authors" element={<Authors />} />
          <Route path="books" element={<Books />} />
          <Route path="category" element={<Categories/>} />
          <Route path="wishlist" element={<Wishlist/>} />
          <Route path="books/:id" element={<BookDetailsPage />} />
        </Route>

        {/* Default fallback */}
        <Route path="*" element={<AuthPage />} />
      </Routes>
    </Router>
  );
}
