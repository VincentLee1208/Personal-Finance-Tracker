import { useState } from "react";
import { api } from "../lib/api";

import "../styles/shared.css";
import "../styles/Auth.css";

export default function Auth() {
    const [showSigninPassword, setShowSigninPassword] = useState(false);
    const [showSignupPassword, setShowSignupPassword] = useState(false);
    const [signupMsg, setSignupMsg] = useState(null);

    // sign in states
    const [signinEmail, setSigninEmail] = useState("");
    const [signinPassword, setSigninPassword] = useState("");

    async function handleSignIn(e) {
        e.preventDefault();
    }

    // sign up states

    const [signupEmail, setSignupEmail] = useState("");
    const [signupPassword, setSignupPassword] = useState("");

    const passwordStrength = signupPassword.length === 0 ? 0 : signupPassword.length < 8 ? 25 :
    /(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9])/.test(signupPassword) ? 100 : 60;

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
                <div className="card">
                    <h2 className="title">Sign in</h2>
                    <form onSubmit={handleSignIn} className="form">
                        <div className="field">
                            <label className="label">Email</label>
                            <input
                                className="input" 
                                type="email" 
                                value={signinEmail} 
                                onChange={(e) => setSigninEmail(e.target.value)} 
                                required
                            />
                        </div>

                        <div className="field">
                            <div className="flex items-center justify-between">
                                <label className="label">Password</label>
                            </div>
                                <div className="relative">
                                    <input
                                        className="input" 
                                        type={showSigninPassword ? "text" : "password"}
                                        value={signinPassword}
                                        onChange={(e) => setSigninPassword(e.target.value)}
                                        required
                                    />
                                    <button 
                                        type="button"
                                        onClick={() => setShowSigninPassword((s) => !s)}
                                        className="absolute inset-y-0 right-3 flex items-center bg-transparent text-slate-300 hover:text-white border-none outline-none font-light"
                                    >
                                        {showSigninPassword ? "Hide" : "Show"}    
                                    </button>                            
                                </div>
                        </div>

                        <button className="btn !bg-[#1552ad]" type="submit">Sign in</button>
                    </form> 
                </div>

                {/* Sign up */}
                <div className="card bg-[#121924] border-white/5">
                    <h2 className="title text-white">Sign up</h2>
                    <form onSubmit={handleSignUp} className="space-y-5">
                        <div className="field text-white">
                            <label className="label text-slate-300 !font-light">Email</label>
                            <input
                                className="input" 
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
                                className="input pr-12" 
                                type={showSignupPassword ? "text" : "password"}
                                value={signupPassword} 
                                onChange={(e) => setSignupPassword(e.target.value)}
                                placeholder="Password"
                                required
                                />
                                <button
                                    type="button" 
                                    onClick={() => setShowSignupPassword((s) => !s)}
                                    className="absolute inset-y-0 right-3 flex items-center !bg-transparent text-slate-300 hover:text-white !border-none !outline-none !font-light"
                                >
                                    {showSignupPassword ? "Hide" : "Show"}
                                </button>
                            </div>

                            <div className="mt-2 flex items-center space-x-2 text-sm">
                                {signupPassword.length >= 8 ? (
                                    <span className="text-green-500">✅</span>
                                ) : (
                                    <span className="text-red-500">❌</span>
                                )}
                                <span className={ signupPassword.length >= 8 ? "text-green-500" : "text-red-500" }>
                                    At least 8 characters
                                </span>
                            </div>

                            <div className="mt-2 flex items-center gap-3">
                                <div className="password-bar">
                                    <div className="password-fill" style={{ width: `${passwordStrength}%` }} />
                                </div>
                            </div>
                            <span className="text-xs text-slate-300">
                                    {passwordStrength === 100 ? "Strong" : passwordStrength >= 60 ? "Medium" : "Weak"}
                                </span>
                        </div>

                        <button className="btn" type="submit">Sign up</button>
                    </form>

                    {signupMsg && (
                        <p className={`alert ${signupMsg.type === "success" ? "alert-success" : "alert-error"}`}>
                            {signupMsg.text}
                        </p>
                    )}
                </div>
            </div>
        </div>
    )
}