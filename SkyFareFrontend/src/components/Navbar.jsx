import { Link } from "react-router-dom";
import styles from './css/Navbar.module.css';
import { useAuth } from "../context/AuthContext";


export default function Navbar() {
  const { token, logout } = useAuth();
  return (
    <nav className={styles.navbar}>
      <h1 className={styles.navbarLogo}>My Library</h1>
      <ul className={styles.navbarMenu}>
        <li>
          <Link to="/books" className={styles.navbarLink}>Books</Link>
        </li>
        <li>
          <Link to="/authors" className={styles.navbarLink}>Authors</Link>
        </li>
        <li>
          <Link to="/category" className={styles.navbarLink}>Category</Link>
        </li>
        <li>
          <Link to="/wishlist" className={styles.navbarLink}>Wishlist</Link>
        </li>
        <li>
          <Link onClick={logout} className={styles.navbarLink}>Logout</Link>
        </li>
      </ul>
    </nav>
  );
}
