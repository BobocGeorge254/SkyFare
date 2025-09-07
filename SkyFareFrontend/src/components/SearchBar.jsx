import { useState } from "react";
import styles from './css/SearchBar.module.css';

export default function SearchBar({ placeholder, onSearch }) {
  const [query, setQuery] = useState("");

  const handleChange = (e) => {
    setQuery(e.target.value);
    onSearch(e.target.value);
  };

  return (
    <div className={styles.searchBar}>
      <input
        type="text"
        value={query}
        onChange={handleChange}
        placeholder={placeholder}
        className={styles.searchInput}
      />
    </div>
  );
}
