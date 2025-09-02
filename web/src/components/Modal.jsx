import ReactDOM from "react-dom";

export default function Modal({ isOpen, onClose, children }) {
    if (!isOpen) {
        return null;
    }

    const modalRoot = document.getElementById("modal-root");

    return ReactDOM.createPortal(
        <div className="fixed inset-0 z-50 h-full flex items-center justify-center bg-black/50 text-white">
            <div className="bg-[#121924] rounded-xl shadow-lg w-full max-w-md p-6 relative">
            <button
                onClick={onClose}
                className="absolute top-7 right-7 text-slate-400 hover:text-white"
            >
                ✕
            </button>

            {children}
            </div>
        </div>,
        modalRoot
    );
}