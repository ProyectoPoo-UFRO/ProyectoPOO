import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import { UserProvider } from './context/UserContext.jsx';
import { VendingProvider } from './context/VendingContext.jsx';
import { CartProvider } from './context/CartContext.jsx';
import "./styles/global.css";

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <UserProvider>
            <VendingProvider>
                <CartProvider>
                    <App />
                </CartProvider>
            </VendingProvider>
        </UserProvider>
    </React.StrictMode>
);
