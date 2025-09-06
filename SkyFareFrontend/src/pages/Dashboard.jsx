import { useAuth } from "../context/AuthContext";

export default function Dashboard() {
  const { token, logout } = useAuth();

return (
  <div className="dashboard-page">
    <h1 className="dashboard-title">Welcome 🎉</h1>
    <button
      onClick={logout}
      className="dashboard-logout-button"
    >
      Logout
    </button>
  </div>
);
}