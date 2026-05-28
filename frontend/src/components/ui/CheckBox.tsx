interface CheckboxProps {
    label: string;
    checked: boolean;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function Checkbox({
    label,
    checked,
    onChange
}: CheckboxProps) {

    return (
        <label className="flex gap-2.5">
            <input
                type="checkbox"
                checked={checked}
                onChange={onChange}
            />

            {label}
        </label>
    );
}