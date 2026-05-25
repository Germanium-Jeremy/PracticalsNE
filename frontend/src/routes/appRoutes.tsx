import { BrowserRouter, Routes, Route, Outlet } from 'react-router-dom';

import Login from '../pages/Login';
import Signup from '../pages/Signup';
import Home from '../pages/Home';
import ProtectedRoute from '../components/ProtectedRoute';

export default function AppRoutes() {

    return (
        <BrowserRouter>

            <Routes>
                <Route path="/auth" element={ <Outlet /> }>
                    <Route index element={ <Login /> } />
                    <Route path="register" element={ <Signup /> } />
                </Route>

                <Route element={ <ProtectedRoute /> }>
                    <Route path="/dashboard" element={ <Outlet /> }>
                        <Route index element={ <Home /> } />
                    </Route>
                </Route>
            </Routes>

        </BrowserRouter>
    );
}