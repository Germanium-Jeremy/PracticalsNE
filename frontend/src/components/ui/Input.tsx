interface InputProps {
    type?: string;
    placeholder?: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function Input({
    type = 'text',
    placeholder,
    value,
    onChange
}: InputProps) {

    return (
        <input
            className="w-full p-3 mb-4 rounded border border-gray-300"
            type={type}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
        />
    );
}