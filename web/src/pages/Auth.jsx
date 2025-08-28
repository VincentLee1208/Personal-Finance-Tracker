import { useState } from "react";
import { api } from "../lib/api";

import "../styles/shared.css";
import "../styles/Auth.css";

export default function Auth() {
    const [showSignupPassword, setShowSignupPassword] = useState(false);
    const [signupMsg, setSignupMsg] = useState(null);

    // sign up states

    const [signupEmail, setSignupEmail] = useState("");
    const [signupPassword, setSignupPassword] = useState("");

    async function handleSignUp(e) {
        e.preventDefault();
        setSignupMsg(null);

        try {
            const { data } = await api.post("/auth/signup", {
                email: signupEmail,
                password: signupPassword
            });
            setSignupMsg({ type: "success", text: `Account created for ${data.email}`});
            setSignupPassword("");
        } catch (e) {
            setSignupMsg({ type: "error", text: e.response?.data?.error || "Signup failed"});
        }
    }

    return (
        <div className="auth-page">
            <div className="auth-grid">
                {/* Sign in */}


                {/* Sign up */}
                <div className="card bg-[#121924] border-white/5">
                    <h2 className="title text-white">Sign up</h2>
                    <form onSubmit={handleSignUp} className="space-y-5">
                        <div className="field text-white">
                            <label className="label text-slate-300 !font-light">Email</label>
                            <input
                                className="input border-white/10 bg-[#0f1720]" 
                                type="email" 
                                value={signupEmail} 
                                onChange={(e) => setSignupEmail(e.target.value)} 
                                placeholder="example@gmail.com" 
                                required
                            />
                        </div>

                        <div className="field text-white">
                            <div className="flex items-center justify-between">
                                <label className="label text-slate-300 !font-light">Password</label>
                            </div>

                            <div className="relative">
                                <input
                                className="input pr-12 border-white/10 bg-[#0f1720]" 
                                type={showSignupPassword ? "text" : "password"}
                                value={signupPassword} 
                                onChange={(e) => setSignupPassword(e.target.value)}
                                placeholder="At least 8 characters"
                                required
                                />
                                <button
                                    type="button" 
                                    onClick={() => setShowSignupPassword((s) => !s)}
                                    className="absolute inset-y-0 right-0 flex items-center !bg-transparent text-slate-300 hover:text-white !border-none !outline-none !font-light"
                                >
                                    {showSignupPassword ? "Hide" : "Show"}
                                </button>
                            </div>
                        </div>

                        <button className="btn !bg-[#1552ad]" type="submit">Sign up</button>
                    </form>
                </div>
            </div>
        </div>
    )
    
}