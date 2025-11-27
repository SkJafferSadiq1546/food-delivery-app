import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import axios from 'axios';
import './Navbar.css';

const Navbar = () => {
    const { currentUser, logout } = useAuth();
    const { cartItems } = useCart();
    const navigate = useNavigate();
    
    // State for Profile Dropdown
    const [open, setOpen] = useState(false);
    const dropdownRef = useRef(null);

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const handleLogout = () => {
        logout();
        navigate('/signin');
        setOpen(false);
    };

    const handleDeleteAccount = async () => {
        const isConfirmed = window.confirm("Are you sure? This action is permanent.");
        if (isConfirmed && currentUser) {
            try {
                await axios.delete(`http://localhost:8080/api/user/profile/${currentUser.id}`);
                alert("Account deleted successfully.");
                handleLogout();
            } catch (error) {
                alert("Failed to delete account.");
            }
        }
    };

    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/home" className="navbar-logo">
                    🍔 FoodDelivery
                </Link>

                <div className="navbar-links">
                    <Link to="/home" className="nav-link">Home</Link>
                    
                    {currentUser ? (
                        <>
                            <Link to="/orders" className="nav-link">My Orders</Link>
                            <Link to="/cart" className="nav-link cart-link">
                                Cart
                                {cartItems.length > 0 && <span className="cart-badge">{cartItems.length}</span>}
                            </Link>
                            
                            {/* Profile Dropdown Section */}
                            <div className="profile-wrapper" ref={dropdownRef}>
                                {/* The Avatar Button */}
                                <div onClick={() => setOpen(!open)} className="profile-avatar">
                                    👤
                                </div>

                                {open && (
                                    <div className="profile-dropdown">
                                        <div className="dropdown-header">Hello, {currentUser.name || 'User'}</div>
                                        <Link to="/profile" onClick={() => setOpen(false)} className="dropdown-link">
                                            👤 Profile
                                        </Link>
                                        <Link to="/cart" onClick={() => setOpen(false)} className="dropdown-link">
                                            🛒 Cart
                                        </Link>
                                        
                                        {/* Normal style logout (No Blue Background) */}
                                        <button onClick={handleLogout} className="dropdown-link logout-btn">
                                            🔓 Logout
                                        </button>
                                        
                                        <button onClick={handleDeleteAccount} className="dropdown-link delete-account">
                                            🗑️ Delete Account
                                        </button>
                                    </div>
                                )}
                            </div>
                        </>
                    ) : (
                        <div className="auth-buttons">
                            <Link to="/signin" className="btn-signin">Sign In</Link>
                            <Link to="/signup" className="btn-signup">Sign Up</Link>
                        </div>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;