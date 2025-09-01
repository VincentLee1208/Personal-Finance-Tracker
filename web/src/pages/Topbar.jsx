export default function TopBar({ children }) {
  return (
    <div className="topbar flex items-center justify-between px-6 py-3 border-b border-white/10">
      <div className="font-bold text-lg">Personal Finance</div>
      <div className="flex items-center gap-4">{children}</div>
      <button className="rounded-full bg-white/10 px-3 py-1">Profile</button>
    </div>
  );
}