import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import Auth from './pages/Auth.jsx';
import ProtectedRoute from './routes/ProtectedRoute.jsx';

import Layout from './pages/Layout.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Transactions from './pages/Transactions.jsx';
import Accounts from './pages/Accounts.jsx';

const router = createBrowserRouter([
  { path: "/", element: <Auth /> },
  {
    element: (
      <ProtectedRoute>
        <Layout />
      </ProtectedRoute>
    ),
    children:[
      { path: "/dashboard", element: <Dashboard /> },
      { path: "/transactions", element: <Transactions /> },
      { path: "/accounts", element: <Accounts /> },
    ],
  },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  </StrictMode>,
);
