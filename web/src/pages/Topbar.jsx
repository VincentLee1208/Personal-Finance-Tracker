export default function TopBar({ children, className }) {
    return (
        <div className={`flex items-center justify-between px-6 h-16 ${className}`}>
            <div className="flex gap-4 cursor-pointer">{children}</div>
            <button className="rounded-full bg-white/10 px-3 py-1 cursor-pointer">Profile</button>
        </div>
    );
}