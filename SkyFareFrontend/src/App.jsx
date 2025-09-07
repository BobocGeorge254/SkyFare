import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import Dashboard from "./pages/Dashboard";
import Authors from "./pages/AuthorsPage";
import Books from "./pages/BooksPage";
import Categories from "./pages/CategoryPage";
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
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="authors" element={<Authors />} />
          <Route path="books" element={<Books />} />
          <Route path="category" element={<Categories/>} />
        </Route>

        {/* Default fallback */}
        <Route path="*" element={<AuthPage />} />
      </Routes>
    </Router>
  );
}
