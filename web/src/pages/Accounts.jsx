import { useState , useEffect } from "react";
import { api } from "../lib/api";
import { useOutletContext } from "react-router-dom";
import { FaTrash, FaEdit } from "react-icons/fa";
import Modal from "../components/Modal";
import AddAccountForm from "../components/AddAccountForm";
import DeleteAccountForm from "../components/DeleteAccountForm";
import EditAccountForm from "../components/EditAccountForm";

import "../styles/shared.css";

export default function Accounts() {
    const [accounts, setAccounts] = useState([]);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);

    const [deletingAccount, setDeletingAccount] = useState(null);
    const [editingAccount, setEditingAccount] = useState(null);
    const { setTopbarConfig } = useOutletContext();

    useEffect(() => {
        api.get("/accounts").then(({ data }) => setAccounts(data));
    }, []);

    useEffect(() => {
        setTopbarConfig({
            children: (
                <button className="btn cursor-pointer" onClick={() => setShowAddModal(true)}>
                    +Add Account
                </button>
            )
        });
    }, [setTopbarConfig]);

    function handleDelete(account) {
        setDeletingAccount(account);
        setShowDeleteModal(true);
    }

    function handleEdit(account) {
        setEditingAccount(account);
        setShowEditModal(true);
    }

    return (
            <div className="auth-card">
                <h3 className="text-xl font-semibold mb-4">All Accounts</h3>

                <Modal isOpen={showAddModal} onClose={() => setShowAddModal(false)}>
                    <h3 className="text-xl font-semibold mb-4">Add Account</h3>
                    <AddAccountForm onClose={() => setShowAddModal(false)} onSuccess={(newAccount) => setAccounts((prev) => [...prev, newAccount])} />
                </Modal>

                <Modal isOpen={showDeleteModal} onClose={() => setShowDeleteModal(false)}>
                    <h3 className="text-xl font-semibold mb-4">Delete Account</h3>
                    <DeleteAccountForm 
                        onClose={() => setShowDeleteModal(false)} 
                        onSuccess={() => {
                            setAccounts((prev) => prev.filter((acc) => acc.id !== deletingAccount.id));
                            setDeletingAccount(null);
                        }}
                        account={deletingAccount}
                    />
                </Modal>

                <Modal isOpen={showEditModal} onClose={() => setShowEditModal(false)}>
                    <h3 className="text-xl font-semibold mb-4">Edit Account</h3>
                    <EditAccountForm
                        onClose={() => setShowEditModal(false)}
                        onSuccess={(updatedAccount) => {
                            setAccounts((prev) => prev.map((acc) => acc.id === updatedAccount.id ? updatedAccount : acc));
                        }}
                        account={editingAccount}
                    />
                </Modal>
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="border-b border-white/10">
                            <th className="py-2 px-4">Type</th>
                            <th className="py-2 px-4">Label</th>
                            <th className="py-2 px-4">Institution</th>
                            <th className="py-2 px-4">Balance</th>
                            <th className="py-2 px-4">Currency</th>
                            <th className="py-2 px-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {accounts.map((account) => (
                            <tr key={account.id} className="border-b border-white/5">
                                <td className="py-2 px-4">{account.type}</td>
                                <td className="py-2 px-4">{account.customLabel || "-"}</td>
                                <td className="py-2 px-4">{account.institutionCode || "-"}</td>
                                <td className="py-2 px-4">${account.currentBalance}</td>
                                <td className="py-2 px-4">{account.currencyCode}</td>
                                <td className="py-2 px-4 flex gap-3">
                                    <button
                                        onClick={() => handleEdit(account)}
                                        className="text-yellow-400 hover:text-yellow-500"
                                    >
                                        <FaEdit />
                                    </button>
                                    <button
                                        onClick={() => handleDelete(account)}
                                        className="text-red-400 hover:text-red-500"
                                    >
                                        <FaTrash />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
    )
}