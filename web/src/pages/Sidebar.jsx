import { NavLink } from "react-router-dom";

const link = "flex items-center gap-3 px-2 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-white/5";
const active = "bg-white/10 text-white";

export default function Sidebar() {
    return (
        <aside className="w-56 bg-[#121924] border-r border-white/5 flex flex-col p-4">
            <h1 className="text-2xl font-bold mb-10">Personal Finance</h1>

            <nav className="flex flex-col gap-2">
                <NavLink to="/dashboard" className={({isActive}) => `${link} ${isActive ? active : ""}`}>Dashboard</NavLink>
                <NavLink to="/transactions" className={({isActive}) => `${link} ${isActive ? active : ""}`}>Transactions</NavLink>
                <NavLink to="/accounts" className={({isActive}) => `${link} ${isActive ? active : ""}`}>Accounts</NavLink>
                <NavLink to="/budgets" className={({isActive}) => `${link} ${isActive ? active : ""}`}>Budgets</NavLink>
                <NavLink to="/reports" className={({isActive}) => `${link} ${isActive ? active : ""}`}>Reports</NavLink>
                <NavLink to="/settings" className={({isActive}) => `${link} ${isActive ? active : ""}`}>Settings</NavLink>
            </nav>
        </aside>
    );
}