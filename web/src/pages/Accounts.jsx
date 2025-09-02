import { useState , useEffect } from "react";
import { api } from "../lib/api";
import { useOutletContext } from "react-router-dom";

import "../styles/shared.css";

export default function Accounts() {
    const [accounts, setAccounts] = useState([]);
    const { setTopbarConfig } = useOutletContext();

    useEffect(() => {
        api.get("/accounts").then(({ data }) => setAccounts(data));
    }, []);

    useEffect(() => {
        setTopbarConfig({
            children: (
                <button className="btn">
                    +Add Account
                </button>
            )
        });
    }, [setTopbarConfig]);

    return (
            <div className="auth-card">
                <h3 className="text-xl font-semibold mb-4">All Accounts</h3>
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="border-b border-white/10">
                            <th className="py-2 px-4">Type</th>
                            <th className="py-2 px-4">Label</th>
                            <th className="py-2 px-4">Institution</th>
                            <th className="py-2 px-4">Balance</th>
                            <th className="py-2 px-4">Currency</th>
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
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
    )
}