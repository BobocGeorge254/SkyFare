import { useState, useEffect } from "react";
import styles from "./css/CategoryPage.module.css";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import {
  fetchCategories,
  addCategory,
  updateCategory,
  deleteCategory,
} from "../services/categoriesService";

export default function CategoryPage() {
  const { token, userProfileId } = useAuth();

  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState("");
  const [editingCategory, setEditingCategory] = useState(null);
  const [editCategoryName, setEditCategoryName] = useState("");
  const navigate = useNavigate();
  useEffect(() => {
    if (token) {
      loadCategories();
    }
  }, [token]);

  const loadCategories = async () => {
    try {
      const data = await fetchCategories(token);
      setCategories(data);
    } catch (err) {
      console.error("Error fetching categories:", err);
    }
  };

  const handleAddCategory = async () => {
    if (!newCategory.trim()) return;
    try {
      await addCategory(token, newCategory);
      setNewCategory("");
      loadCategories();
    } catch (err) {
      console.error("Error adding category:", err);
    }
  };

  const handleStartEditCategory = (category) => {
    setEditingCategory(category);
    setEditCategoryName(category.name);
  };

  const handleSaveCategory = async () => {
    if (!editingCategory) return;
    try {
      await updateCategory(token, editingCategory.id, editCategoryName);
      setEditingCategory(null);
      setEditCategoryName("");
      loadCategories();
    } catch (err) {
      console.error("Error updating category:", err);
    }
  };

  const handleDeleteCategory = async (id) => {
    try {
      await deleteCategory(token, id);
      loadCategories();
    } catch (err) {
      console.error("Error deleting category:", err);
    }
  };

  const handleCategoryClick = (categoryId) => {
    console.log("Clicked category:", categoryId);
    navigate(`/books?author=all&category=${categoryId}`);
  };

  return (
  <div className={styles.pageContainer}>
    <h2 className={styles.pageTitle}>Categories</h2>

    {userProfileId === null && (
      <div className={styles.formContainer}>
        <input
          type="text"
          placeholder="New category"
          value={newCategory}
          onChange={(e) => setNewCategory(e.target.value)}
          className={styles.inputField}
        />
        <button
          type="button"
          onClick={handleAddCategory}
          className={styles.addButton}
        >
          Add Category
        </button>
      </div>
    )}

    <ul className={styles.list}>
      {Array.isArray(categories) &&
        categories.map((cat) => (
          <li key={cat.id}>
            {editingCategory?.id === cat.id ? (
              userProfileId === null ? (
                <>
                  <input
                    type="text"
                    value={editCategoryName}
                    onChange={(e) => setEditCategoryName(e.target.value)}
                    className={styles.inputField}
                  />
                  <button
                    onClick={handleSaveCategory}
                    className={styles.saveButton}
                  >
                    Save
                  </button>
                  <button
                    onClick={() => setEditingCategory(null)}
                    className={styles.cancelButton}
                  >
                    Cancel
                  </button>
                </>
              ) : (
                <span>{cat.name}</span>
              )
            ) : (
              <>
                <span className={styles.listItem} onClick={() => handleCategoryClick(cat.id)}>{cat.name}</span>
                {userProfileId === null && (
                  <div className={styles.actions}>
                    <button
                      onClick={() => handleStartEditCategory(cat)}
                      className={styles.editButton}
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDeleteCategory(cat.id)}
                      className={styles.deleteButton}
                    >
                      Delete
                    </button>
                  </div>
                )}
              </>
            )}
          </li>
        ))}
    </ul>
  </div>
);
}
