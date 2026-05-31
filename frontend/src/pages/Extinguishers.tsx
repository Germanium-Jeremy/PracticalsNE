import { useEffect, useState } from 'react';
import api from '../lib/api';
import Button from '../components/ui/Button';
import { useAuth } from '../contexts/AuthContext';
import Loader from '../components/ui/Loader';

interface FireExtinguisher {
    id: number;
    user_id: number | null;
    created_at: string;
    bought_at: string | null;
    status: string;
    returned_at: string | null;
    expires_at: string;
}

export default function ExtinguishersPage() {
    const [extinguishers, setExtinguishers] = useState<FireExtinguisher[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [isAdmin, setIsAdmin] = useState(false); // TODO: Replace with real auth check
    const { user, token } = useAuth();

    useEffect(() => {
        fetchExtinguishers();
        if (user?.role === 'admin') {
            setIsAdmin(true);
        }
    }, []);

    async function fetchExtinguishers() {
        setLoading(true);
        setError(null);

        try {
            const res = await api.get('/api/extenguisher/all', { headers: { 'Authorization': `Bearer ${token}` } });
            setExtinguishers(res.data.extinguishers || []);
        } catch (err: any) {
            setError(err.message || 'Failed to fetch extinguishers');
        } finally {
            setLoading(false);
        }
    }

    async function handleAdd() {
        try {
            await api.post('/api/extenguisher/add', {}, { headers: { 'Authorization': `Bearer ${token}` }});
            fetchExtinguishers();
        } catch (err: any) {
            alert('Failed to add extinguisher');
            console.warn(err)
        }
    }

    async function handleBuy(id: number) {
        try {
            await api.post(`/api/extenguisher/buy`, { extinguisherId: id, userId: user?.userId }, { headers: { 'Authorization': `Bearer ${token}` }});
            fetchExtinguishers();
        } catch (err: any) {
            alert('Failed to buy extinguisher');
            console.warn(err)
        }
    }

    async function handleReturn(id: number) {
        try {
            await api.post(`/api/extenguisher/return`, { extinguisherId: id, userId: user?.userId }, { headers: { 'Authorization': `Bearer ${token}` }});
            fetchExtinguishers();
        } catch (err: any) {
            alert('Failed to return extinguisher');
        }
    }

    return (
        <div>
            <h1>Fire Extinguishers</h1>
            {isAdmin && <Button onClick={handleAdd}>Add Extinguisher</Button>}
            {loading ? <Loader /> : null}
            {error ? <p style={{ color: 'red' }}>{error}</p> : null}
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Status</th>
                        <th>Expires At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                {extinguishers.map((ex) => (
                    <tr key={ex.id}>
                        <td>{ex.id}</td>
                        <td>{ex.status}</td>
                        <td>{ex.expires_at}</td>
                        <td>
                            {ex.status === 'in_stock' && user?.userId && (
                                <Button onClick={() => handleBuy(ex.id)}>Buy</Button>
                            )}
                            {ex.status === 'sold' && ex.user_id === user?.userId && (
                                <Button onClick={() => handleReturn(ex.id)}>Return</Button>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
