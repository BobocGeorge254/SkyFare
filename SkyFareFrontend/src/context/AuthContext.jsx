import { createContext, useContext, useState } from "react";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [userProfileId, setUserProfileId] = useState(localStorage.getItem("userProfileId") || null);

  const login = (token, userProfileId) => {
    setToken(token);
    setUserProfileId(userProfileId);
    localStorage.setItem("token", token);
    localStorage.setItem("userProfileId", userProfileId);
  };

  const logout = () => {
    setToken(null);
    setUserProfileId(null);
    localStorage.removeItem("token");
    localStorage.removeItem("userProfileId");
  };

  return (
    <AuthContext.Provider value={{ token, login, logout, userProfileId }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);