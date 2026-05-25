import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Loader from '../components/ui/Loader';

import { registerUser } from '../services/auth.services';

export default function Signup() {

    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    async function handleSignup(e: React.FormEvent) {
        e.preventDefault();

        try {
            setLoading(true);
            setError('');

            await registerUser(username, email, password);

            navigate('/auth');

        } catch (err: any) {
            setError(err.response?.data?.error || 'Signup failed');
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className='max-w-100 m-12.5 mx-auto'>

            <h1>Signup</h1>

            <form onSubmit={handleSignup}>

                <Input
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <Input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <Input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                {loading ? <Loader /> : <Button type="submit">Signup</Button>}

                {error && (
                    <p className="text-red-500">
                        {error}
                    </p>
                )}

            </form>

            <p>
                Already have an account?
                <Link to="/auth"> Login</Link>
            </p>

        </div>
    );
}