import { useState } from "react";
import { api } from "../lib/api";

export default function DeleteAccountForm({ onSuccess, onClose, account }) {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [deleteConfirmText, setDeleteConfirmText] = useState("");

    async function handleDelete(e) {
        e.preventDefault();
        setLoading(true);

        try {
            await api.delete(`accounts/${account.id}`, {
                withCredentials: true
            });
            onSuccess();
            onClose();
        } catch (err) {
            setError(err.response?.data?.message || "Failed to delete account");
        } finally {
            setLoading(false);
        }
    }

    return (
        <form onSubmit={handleDelete} className="space-y-4">
            <p>Type <strong>YES</strong> to confirm deletion of <em>{account?.customLabel || account?.type}</em></p>
            <input
                type="text"
                className="input my-3"
                onChange={(e) => setDeleteConfirmText(e.target.value)}
            />

            <button type="submit" className="btn bg-red-600 cursor-pointer disabled:bg-gray-500 disabled:cursor-not-allowed" disabled={deleteConfirmText !== "YES"}>
                {loading ? "Deleting..." : "Delete Account"}
            </button>
        </form>
    )
}