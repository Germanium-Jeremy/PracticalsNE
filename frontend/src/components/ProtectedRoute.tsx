import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import Loader from './ui/Loader';


export function ProtectedRoute() {
    const { token, loading } = useAuth();

    if (loading) return <Loader />


    if (!token) {
        return <Navigate to="/auth" replace />;
    }

    return <Outlet />;
}

export function RoleProtectedRoute({ allowedRoles }: { allowedRoles: string[] }) {
    const { loading, user } = useAuth();

    if (loading) return <Loader />
    if (!user || !allowedRoles.includes(user.role)) return <Navigate to="/dashboard" replace />
    return <Outlet />
}