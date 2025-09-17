import { useState } from "react";
import { api } from "../lib/api";

export default function AddAccountForm({ onSuccess, onClose }) {
    const [form, setForm] = useState({
        type: "CHEQUING",
        customLabel: "",
        currencyCode: "CAD",
        currentBalance: "",
        institutionCode: ""
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const { data } = await api.post("/accounts/create", form, {
                withCredentials: true,
            });
            onSuccess(data);
            onClose();
        } catch (err) {
            setError(err.response?.data?.message || "Failed to create account");
        } finally {
            setLoading(false);
        }
    }

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <div>
                <label className="block text-sm mb-1">Type</label>
                <select
                    name="type"
                    value={form.type}
                    onChange={handleChange}
                    className="input z-60"
                >
                    <option value="CHEQUING">Chequing</option>
                    <option value="SAVINGS">Savings</option>
                    <option value="CREDIT_CARD">Credit Card</option>
                    <option value="INVESTMENT">Investment</option>
                    <option value="CASH">Cash</option>
                    <option value="OTHER">Other</option>
                </select>
            </div>

            <div>
                <label className="block text-sm mb-1">Custom Label</label>
                <input
                    name="customLabel"
                    value={form.customLabel}
                    onChange={handleChange}
                    className="input"
                />
            </div>

            <div>
                <label className="block text-sm mb-1">Balance</label>
                <div className="flex gap-2">
                    <input
                        name="currentBalance"
                        type="number"
                        step="0.01"
                        value={form.currentBalance}
                        onChange={handleChange}
                        className="input flex-1"
                    />

                    <select
                        name="currencyCode"
                        value={form.currencyCode}
                        onChange={handleChange}
                        className="input w-28"
                    >
                        <option value="CAD">CAD</option>
                        <option value="USD">USD</option>
                        <option value="EUR">EUR</option>
                        <option value="JPY">JPY</option>
                        <option value="CNY">CNY</option>
                        <option value="NTD">NTD</option>
                    </select>
                </div>
            </div>

             <div>
                <label className="block text-sm mb-1">Institution</label>
                <input
                    name="institutionCode"
                    value={form.institutionCode}
                    onChange={handleChange}
                    className="input"
                />
            </div>

            <button type="submit" className="btn" disabled={loading}>{loading ? "Creating..." : "Create Account"}</button>
        </form>
    )
}