import { useState } from "react";
import { api } from "../lib/api";

export default function DeleteConfirmForm({ resourceName, resourceLabel, deleteUrl, onSuccess, onClose }) {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [deleteConfirmText, setDeleteConfirmText] = useState("");

    async function handleDelete(e) {
        e.preventDefault();
        setLoading(true);

        try {
            await api.delete(deleteUrl, { withCredentials: true });
            onSuccess?.();
            onClose?.();
        } catch (err) {
            setError(err.response?.data?.message || `Failed to delete ${resourceName}`);
        } finally {
            setLoading(false);
        }
    }

    return (
        <form onSubmit={handleDelete} className="space-y-4">
            <p>
                Type <strong>YES</strong> to confirm deletion of <em>{resourceLabel}</em> {resourceName}.
            </p>
            <input
                type="text"
                className="input my-3"
                onChange={(e) => setDeleteConfirmText(e.target.value)}
            />
            {error && <p className="text-red-500 text-sm">{error}</p>}
            <button
                type="submit"
                className="btn bg-red-600 cursor-pointer disabled:bg-gray-500 disabled:cursor-not-allowed"
                disabled={deleteConfirmText !== "YES" || loading}
            >
                {loading ? "Deleting..." : `Delete ${resourceName}`}
            </button>
        </form>
    );
}