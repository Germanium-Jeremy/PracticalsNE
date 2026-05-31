import { useNavigate } from 'react-router-dom';

import Button from '../components/ui/Button';
import { useAuth } from '../contexts/AuthContext';

export default function Home() {
    const navigate = useNavigate();
    const { logout } = useAuth();

    function handleLogout() {
        logout();
        navigate('/auth');
    }

    return (
        <div className="p-10">

            <h1>Home Page</h1>

            <p>
                Authentication successful.
            </p>

            <div className="max-w-50 flex flex-col gap-4 mt-6">
                <Button onClick={() => navigate('/dashboard/extinguishers')}>
                    Go to extinguishers
                </Button>
                <Button onClick={handleLogout}>
                    Logout
                </Button>
            </div>

        </div>
    );
}