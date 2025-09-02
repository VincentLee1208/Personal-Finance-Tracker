import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function ProtectedRoute({ children }) {
    const { isAuthed, booting } = useAuth();
    const location  = useLocation();

    if(booting) {
        return null;
    }

    return isAuthed ? children : <Navigate to="/" replace state={{ from: location }} />;
}