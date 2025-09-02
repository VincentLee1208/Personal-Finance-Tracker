import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";
import Topbar from "./Topbar";
import { useState } from "react";

import "../styles/shared.css";

export default function Layout() {
    const [topbarConfig, setTopbarConfig] = useState({ children: null });

    return (
        <div className="page-card grid grid-cols-5 grid-rows-10 gap-2">
            <div className="col-span-1 row-span-1 font-bold text-lg flex items-center px-6 rounded-tl-2xl bg-[#161d2a]">Personal Finance</div>

            <Topbar className="col-span-4 row-span-1" children={topbarConfig.children} />

            <Sidebar className="col-span-1 row-span-9 rounded-bl-2xl" />

            <main className="col-span-4 row-span-9 pl-6 pt-4 overflow-y-auto">
                <Outlet context={{ setTopbarConfig }} />
            </main>
        </div>
    )
}