import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login as loginUser, register as registerUser } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import '../index.css';

export default function AuthPage() {
  const [mode, setMode] = useState("login");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    firstName: "",
    lastName: "",
  });
  const [error, setError] = useState(null);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validateEmail = (email) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  };

  const validatePassword = (password) => {
    const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]).{8,}$/;
    return regex.test(password);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (mode === "register") {
      if (!formData.firstName.trim() || !formData.lastName.trim()) {
        setError("First Name and Last Name are required.");
        return;
      }
      if (!validateEmail(formData.email)) {
        setError("Invalid email address.");
        return;
      }
      if (!validatePassword(formData.password)) {
        setError(
          "Password must be at least 8 characters, include 1 uppercase letter, 1 number, and 1 special character."
        );
        return;
      }
    }

    try {
      if (mode === "login") {
        const res = await loginUser({ email: formData.email, password: formData.password });
        if (res.token) {
          login(res.token);
          navigate("/dashboard");
        } else {
          setError(res.error || "Login failed");
        }
      } else {
        const res = await registerUser(formData);
        if (res.message) {
          setMode("login");
        } else {
          setError(res.error || "Registration failed");
        }
      }
    } catch (err) {
      setError("Something went wrong");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <h2 className="auth-title">{mode === "login" ? "Login" : "Register"}</h2>
        {error && <div className="auth-error">{error}</div>}
        <form onSubmit={handleSubmit} className="auth-form">
          {mode === "register" && (
            <>
              <input
                type="text"
                name="firstName"
                placeholder="First Name"
                value={formData.firstName}
                onChange={handleChange}
                className="input-field"
              />
              <input
                type="text"
                name="lastName"
                placeholder="Last Name"
                value={formData.lastName}
                onChange={handleChange}
                className="input-field"
              />
            </>
          )}
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            className="input-field"
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            className="input-field"
          />
          <button type="submit" className="submit-button">
            {mode === "login" ? "Login" : "Register"}
          </button>
        </form>
        <p className="toggle-text">
          {mode === "login" ? "Don't have an account?" : "Already have an account?"}{" "}
          <button
            onClick={() => setMode(mode === "login" ? "register" : "login")}
            className="toggle-button"
          >
            {mode === "login" ? "Register" : "Login"}
          </button>
        </p>
      </div>
    </div>
  );
}