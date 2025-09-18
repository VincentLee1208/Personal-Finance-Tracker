import { useState, useEffect } from "react";
import { api } from "../lib/api";
import { useOutletContext } from "react-router-dom";
import { FaTrash } from "react-icons/fa";
import Modal from "../components/Modal";
import AddTransactionForm from "../components/AddTransactionForm";
import DeleteConfirmForm from "../components/DeleteConfirmForm";

export default function Transactions() {
    const [transactions, setTransactions] = useState([]);
    const [accounts, setAccounts] = useState([]);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);

    const [deletingTransaction, setDeletingTransaction] = useState(null);
    const { setTopbarConfig } = useOutletContext();

    useEffect(() => {
        api.get("/transactions").then(({ data }) => setTransactions(data));
    }, []);

    useEffect(() => {
        api.get("/accounts").then(({ data }) => setAccounts(data));
    }, []);

    useEffect(() => {
        setTopbarConfig({
            children: (
                <button className="btn cursor-pointer" onClick={() => setShowAddModal(true)}>
                    +Add Transaction
                </button>
            )
        })
    }, [setTopbarConfig]);

    function handleDelete(transaction) {
        setDeletingTransaction(transaction);
        setShowDeleteModal(true);
    }

    return (
        <div>
            <h2 className="text-2xl font-semibold mb-4 pl-3">Transactions</h2>

            <div className="auth-card">
                <Modal isOpen={showAddModal} onClose={() => setShowAddModal(false)}>
                    <h3 className="text-xl font-semibold mb-4">Add Transaction</h3>
                    <AddTransactionForm accounts={accounts} onClose={() => setShowAddModal(false)} onSuccess={(newTransaction) => setTransactions((prev) => [...prev, newTransaction])} />
                </Modal>
                <Modal isOpen={showDeleteModal} onClose={() => setShowDeleteModal(false)}>
                    <h3 className="text-xl font-semibold mb-4">Delete Transaction</h3>
                    <DeleteConfirmForm
                        resourceName="transaction"
                        resourceLabel={deletingTransaction?.description || `#${deletingTransaction?.id}`}
                        deleteUrl={`transactions/${deletingTransaction?.id}`}
                        onSuccess={() => {
                            setTransactions((prev) => prev.filter((trans) => trans.id !== deletingTransaction.id));
                            setDeletingTransaction(null);
                        }}
                        onClose={() => setShowDeleteModal(false)}
                    />
                </Modal>
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="border-b border-white/10">
                            <th className="py-2 px-3">Date</th>
                            <th className="py-2 px-3">Description</th>
                            <th className="py-2 px-3">Category</th>
                            <th className="py-2 px-3">Amount</th>
                            <th className="py-2 px-3">Account</th>
                            <th className="py-2 px-3">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transactions.map((transaction) => (
                          <tr key={transaction.id} className="border-b border-white/5">
                            <td className="py-2 px-3">
                                {new Date(transaction.date).toLocaleDateString("en-US", {
                                    year: "numeric",
                                    month: "short",
                                    day: "numeric",
                                })}
                            </td>
                            <td className="py-2 px-3">{transaction.description || "-"}</td>
                            <td className="py-2 px-3">{transaction.category}</td>
                            <td className="py-2 px-3">${Number(transaction.amount).toFixed(2)}</td>
                            <td className="py-2 px-3">
                                <div className="flex flex-col">
                                    <span className="font-medium">{transaction.accountLabel || "-"}</span>
                                    <span className="text-sm text-gray-400">{transaction.accountInstitution || "-"}</span>
                                </div>
                            </td>
                            <td className="py-2 px-3 gap-3">
                                <button
                                    onClick={() => handleDelete(transaction)}
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
        </div>
    )
}