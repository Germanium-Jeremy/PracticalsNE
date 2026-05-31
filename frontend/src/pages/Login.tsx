import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Loader from '../components/ui/Loader';

import { loginUser } from '../services/auth.services';
import { useAuth } from '../contexts/AuthContext';

export default function Login() {

    const navigate = useNavigate();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const { login } = useAuth();

    async function handleLogin(e: React.FormEvent) {
        e.preventDefault();

        try {
            setLoading(true);
            setError('');

            const data = await loginUser(email, password);
            login(data.token, {
                email: data.email,
                username: data.username,
                userId: data.userId,
                role: data.role,
            });

            navigate('/dashboard');
        } catch (err: any) {
            setError(err.response?.data?.error || 'Login failed');
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="max-w-100 m-12.5 mx-auto">

            <h1>Login</h1>

            <form onSubmit={handleLogin}>

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

                {loading ? <Loader /> : <Button type="submit">Login</Button>}

                {error && (
                    <p className="text-red-500">
                        {error}
                    </p>
                )}

                   </form>

            <p>
                No account?
                <Link to="/auth/register"> Signup</Link>
            </p>

        </div>
    );
}