interface ButtonProps {
    children: React.ReactNode;
    onClick?: () => void;
    type?: 'button' | 'submit';
    disabled?: boolean;
}

export default function Button({ children, onClick, type = 'button', disabled = false}: ButtonProps) {

    return (
        <button
            className="w-full p-3 bg-blue-600 text-white text-lg rounded disabled:bg-gray-400 cursor-pointer disabled:cursor-not-allowed"
            type={type}
            onClick={onClick}
            disabled={disabled}
        >
            {children}
        </button>
    );
}