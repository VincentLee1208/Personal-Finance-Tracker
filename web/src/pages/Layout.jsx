import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";
import Topbar from "./Topbar";

import "../styles/shared.css";

export default function Layout() {
    return (
        <div className="page-card">
            <Topbar />
            <div className="flex">
                <Sidebar />
                <main className="flex-1 p-8">
                    {/* page main content */}
                    <Outlet />
                </main>
            </div>
        </div>
    )
}