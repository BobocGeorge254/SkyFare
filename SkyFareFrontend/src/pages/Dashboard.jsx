import { useAuth } from "../context/AuthContext";
import styles from './css/Dashboard.module.css';

export default function Dashboard() {
  const { token, logout } = useAuth();

return (
  <div className={styles.dashboardPage}>
    <h1 className={styles.dashboardTitle}>Welcome 🎉</h1>
    <button
      onClick={logout}
      className="dashboardLogoutButton"
    >
      Logout
    </button>
  </div>
);
}