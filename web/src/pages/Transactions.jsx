import { useState, useEffect } from "react";
import { api } from "../lib/api";

export default function Transactions() {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        api.get("/transactions").then(({ data }) => setTransactions(data));
    }, []);

    return (
        <div>
            <h2 className="text-2xl font-semibold mb-4 pl-3">Transactions</h2>

            <div className="auth-card">
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="border-b border-white/10">
                            <th className="py-2 px-4">Date</th>
                            <th className="py-2 px-4">Description</th>
                            <th className="py-2 px-4">Category</th>
                            <th className="py-2 px-4">Amount</th>
                            <th className="py-2 px-4">Account</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transactions.map((transaction) => (
                          <tr key={transaction.id} className="border-b border-white/5">
                            <td className="py-2 px-4">{transaction.date}</td>
                            <td className="py-2 px-4">{transaction.description || "-"}</td>
                            <td className="py-2 px-4">{transaction.category}</td>
                            <td className="py-2 px-4">${transaction.amount}</td>
                            <td className="py-2 px-4">
                                <div className="flex flex-col">
                                    <span className="font-medium">{transaction.accountLabel || "-"}</span>
                                    <span className="text-sm text-gray-400">{transaction.accountInstitution || "-"}</span>
                                </div>
                            </td>
                          </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}