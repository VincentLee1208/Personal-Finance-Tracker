import { useState } from "react";

export default function AddTransactionForm({ onSuccess, onClose, accounts }) {
    const [form, setForm] = useState({
        category: "DINING",
        description: "",
        amount: "",
        currencyCode: "CAD",
        date: "",
        accountId: ""
    });
    const [loading, setLoading] = useState(false);

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e) {

    }

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <div>
                <label className="block text-sm mb-1">Account</label>
                <select
                    name="accountId"
                    value={form.accountId}
                    onChange={handleChange}
                    className="input"
                >
                    <option value="">No Account</option>
                    {accounts.map((acc) => (
                        <option key={acc.id} value={acc.id}>
                            {acc.customLabel || acc.type } ({acc.institutionCode || "-"})
                        </option>
                    ))}
                </select>
            </div>

            <div>
                <label className="block text-sm mb-1">Category</label>
                <select
                    name="category"
                    value={form.category}
                    onChange={handleChange}
                    className="input z-60"
                >
                    <option value="INCOME">Income</option>
                    <option value="DINING">Dining</option>
                    <option value="ENTERTAINMENT">Entertainment</option>
                    <option value="TRANSPORTATION">Transportation</option>
                    <option value="GROCERIES">Groceries</option>
                    <option value="UTILITIES">Utilities</option>
                    <option value="TRANSFER">Transfer</option>
                    <option value="OTHER">Other</option>
                </select>
            </div>

            <div>
                <label className="block text-sm mb-1">Description</label>
                <input
                    name="description"
                    value={form.description}
                    onChange={handleChange}
                    className="input"
                />
            </div>

            <div>
                <label className="block text-sm mb-1">
                    Amount<span className="text-red-500">*</span>
                </label>
                <div className="flex gap-2">
                    <input
                        name="amount"
                        type="number"
                        step="0.01"
                        value={form.amount}
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
                    </select>
                </div>
            </div>

            <div>
                <label className="block text-sm mb-1">
                    Date<span className="text-red-500">*</span>
                </label>
                <input
                    name="date"
                    type="date"
                    value={form.date}
                    onChange={handleChange}
                    className="input"
                />
            </div>

            <button 
                type="submit"
                className={`btn ${(!form.amount || !form.date) ? "opacity-50 cursor-not-allowed" : ""}`}
                disabled={!form.amount || !form.date || loading}
            >
                {loading ? "Adding..." : "Add Transaction"}
            </button>
        </form>
    )
}