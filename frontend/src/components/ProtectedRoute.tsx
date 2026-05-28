import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';


export default function ProtectedRoute() {
    const { token, loading } = useAuth();

    if (loading) {
        return (
            <div className='flex h-screen justify-center align-middle'>
                <div className='border-y-2 border-blue-500 rounded-full animate-spin w-10 h-10 my-auto'></div>
            </div>
        )
    }

    if (!token) {
        return <Navigate to="/auth" replace />;
    }

    return <Outlet />;
}