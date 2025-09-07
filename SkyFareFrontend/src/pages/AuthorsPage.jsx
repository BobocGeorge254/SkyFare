import { useEffect, useState } from "react";
import styles from "./css/AuthorsPage.module.css";
import SearchBar from "../components/SearchBar";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

export default function AuthorsPage() {
  const { token, userProfileId} = useAuth();
  const [authors, setAuthors] = useState([]);
  const [filteredAuthors, setFilteredAuthors] = useState([]);
  const [newAuthor, setNewAuthor] = useState("");
  const [editingAuthor, setEditingAuthor] = useState(null);
  const [editName, setEditName] = useState("");
  const [newAuthorImage, setNewAuthorImage] = useState(null);
  const [editImage, setEditImage] = useState(null);
  const authHeaders = {
    headers: { Authorization: `Bearer ${token}` },
  };

  console.log("id user", userProfileId);
  const fetchAuthors = async () => {
    try {
      const res = await axios.get(
        "http://localhost:8080/api/authors",
        authHeaders
      );
      setAuthors(res.data);
      setFilteredAuthors(res.data);
      console.log("res.data =", res.data);
    } catch (err) {
      console.error("Error fetching authors:", err);
    }
  };

  useEffect(() => {
    if (token) {
      fetchAuthors();
    }
  }, [token]);

  const handleSearch = (query) => {
    setFilteredAuthors(
      authors.filter((author) =>
        author.name.toLowerCase().includes(query.toLowerCase())
      )
    );
  };

  const handleAddAuthor = async () => {
    if (!newAuthor.trim()) return;
    try {
      const formData = new FormData();
      formData.append("name", newAuthor);
      if (newAuthorImage) {
        formData.append("image", newAuthorImage);
      }

      await axios.post("http://localhost:8080/api/authors", formData, {
        ...authHeaders,
        headers: {
          ...authHeaders.headers,
          "Content-Type": "multipart/form-data",
        },
      });

      setNewAuthor("");
      setNewAuthorImage(null);
      fetchAuthors();
    } catch (err) {
      console.error("Error adding author:", err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(
        `http://localhost:8080/api/authors/${id}`,
        authHeaders
      );
      fetchAuthors();
    } catch (err) {
      console.error("Error deleting author:", err);
    }
  };

  const startEdit = (author) => {
    setEditingAuthor(author);
    setEditName(author.name);
    setEditImage(null);
  };

  const saveEdit = async () => {
    try {
      const formData = new FormData();
      if (editName) formData.append("name", editName);
      if (editImage) formData.append("image", editImage);

      await axios.put(
        `http://localhost:8080/api/authors/${editingAuthor.id}`,
        formData,
        {
          ...authHeaders,
          headers: {
            ...authHeaders.headers,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      setEditingAuthor(null);
      setEditName("");
      setEditImage(null);
      fetchAuthors();
    } catch (err) {
      console.error("Error updating author:", err);
    }
  };

  return (
  <div className={styles.pageContainer}>
    <h2 className={styles.pageTitle}>Authors</h2>

    <SearchBar placeholder="Search authors..." onSearch={handleSearch} />

    {userProfileId === null && (
      <div className={styles.addForm}>
        <input
          type="text"
          value={newAuthor}
          onChange={(e) => setNewAuthor(e.target.value)}
          placeholder="New author name"
          className={styles.input}
        />
        <input
          type="file"
          accept="image/*"
          onChange={(e) => setNewAuthorImage(e.target.files[0])}
          className={styles.input}
        />
        <button onClick={handleAddAuthor} className={styles.addButton}>
          Add
        </button>
      </div>
    )}

    <ul className={styles.list}>
      {Array.isArray(filteredAuthors) &&
        filteredAuthors.map((author) => (
          <li key={author.id} className={styles.listItem}>
            {editingAuthor?.id === author.id ? (
              userProfileId === null ? (
                <div className={styles.editContainer}>
                  <input
                    type="text"
                    value={editName}
                    onChange={(e) => setEditName(e.target.value)}
                    className={styles.input}
                  />
                  <input
                    type="file"
                    accept="image/*"
                    onChange={(e) => setEditImage(e.target.files[0])}
                    className={styles.input}
                  />
                  <button onClick={saveEdit} className={styles.saveButton}>
                    Save
                  </button>
                  <button
                    onClick={() => setEditingAuthor(null)}
                    className={styles.cancelButton}
                  >
                    Cancel
                  </button>
                </div>
              ) : (
                <div className={styles.itemContent}>
                  <div className={styles.authorInfo}>
                    <img
                      src={author.imageUrl || "/default-avatar.png"}
                      alt={author.name}
                      className={styles.authorImage}
                    />
                    <span className={styles.authorName}>{author.name}</span>
                  </div>
                </div>
              )
            ) : (
              <div className={styles.itemContent}>
                <div className={styles.authorInfo}>
                  <img
                    src={author.imageUrl || "/default-avatar.png"}
                    alt={author.name}
                    className={styles.authorImage}
                  />
                  <span className={styles.authorName}>{author.name}</span>
                </div>
                {userProfileId === null && (
                  <div className={styles.actions}>
                    <button
                      onClick={() => startEdit(author)}
                      className={styles.editButton}
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(author.id)}
                      className={styles.deleteButton}
                    >
                      Delete
                    </button>
                  </div>
                )}
              </div>
            )}
          </li>
        ))}
    </ul>
  </div>
);
}
