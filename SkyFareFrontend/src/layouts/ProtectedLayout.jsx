import Navbar from "../components/Navbar";
import { Outlet } from "react-router-dom";

export default function ProtectedLayout() {
  return (
    <div className="protectedLayout">
      <Navbar />
      <div className="pageContent">
        <Outlet />
      </div>
    </div>
  );
}
