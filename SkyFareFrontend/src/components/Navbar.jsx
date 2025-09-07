import { Link } from "react-router-dom";
import styles from './css/Navbar.module.css';

export default function Navbar() {
  return (
    <nav className={styles.navbar}>
      <h1 className={styles.navbarLogo}>My Library</h1>
      <ul className={styles.navbarMenu}>
        <li>
          <Link to="/dashboard" className={styles.navbarLink}>Dashboard</Link>
        </li>
        <li>
          <Link to="/authors" className={styles.navbarLink}>Authors</Link>
        </li>
        <li>
          <Link to="/books" className={styles.navbarLink}>Books</Link>
        </li>
        <li>
          <Link to="/category" className={styles.navbarLink}>Category</Link>
        </li>
      </ul>
    </nav>
  );
}
