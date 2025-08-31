import { createContext, useContext, useEffect, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [booting, setBooting] = useState(true);

    useEffect(() => {
        const u = localStorage.getItem("user");
        if(u) {
            setUser(JSON.parse(u));
        }
        setBooting(false);
    }, []);

    function setAuthedUser(u) {
        setUser(u);
        localStorage.setItem("user", JSON.stringify(u));
    }

    function clearAuthedUser(u) {
        setUser(null);
        localStorage.removeItem("user");
    }

    return (
        <AuthContext.Provider value={{ user, setAuthedUser, clearAuthedUser, booting, isAuthed: !!user }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}