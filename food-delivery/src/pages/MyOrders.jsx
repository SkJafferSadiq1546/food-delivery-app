import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import apiClient from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';
import './MyOrders.css';

const MyOrders = () => {
    const { currentUser } = useAuth();
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchOrders = async () => {
            if (!currentUser) return;
            try {
                const response = await apiClient.get(`/user/${currentUser.id}`);
                const sortedOrders = response.data.sort((a, b) => b.id - a.id);
                setOrders(sortedOrders);
            } catch (error) {
                console.error("Error fetching orders:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchOrders();
    }, [currentUser]);

    const handleSort = () => {
        const sorted = [...orders].reverse();
        setOrders(sorted);
    };

    if (!currentUser) return <div className="mo-container">Please log in.</div>;
    if (loading) return <div className="mo-container">Loading...</div>;

    return (
        <div className="mo-container">
            <div className="mo-header">
                <div className="mo-title-group">
                    <span className="mo-emoji-icon">📦</span>
                    <h2>My Orders</h2>
                </div>
                <button className="mo-sort-btn" onClick={handleSort}>
                    Sort: Newest ⇅
                </button>
            </div>

            {orders.length === 0 ? (
                <div className="mo-empty">
                    <p>No orders yet. Hungry?</p>
                </div>
            ) : (
                <div className="mo-list">
                    {orders.map((order) => (
                        <div key={order.id} className="mo-card">
                            {/* Card Top: ID and Date */}
                            <div className="mo-card-top">
                                <div className="mo-order-id">
                                    <span className="mo-label">ORDER</span>
                                    <h3>#{order.id}</h3>
                                </div>
                                <div className="mo-order-date">
                                    {new Date(order.orderDate).toLocaleDateString()} at {new Date(order.orderDate).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                                </div>
                            </div>
                            
                            {/* Card Middle: Address & Total */}
                            <div className="mo-card-details">
                                <div className="mo-detail-row">
                                    <span className="mo-icon-small">📍</span>
                                    <span className="mo-address">{order.deliveryAddress}</span>
                                </div>
                                <div className="mo-detail-row">
                                    <span className="mo-icon-small">💳</span>
                                    <span className="mo-total">₹{order.totalAmount.toFixed(2)}</span>
                                </div>
                            </div>

                            {/* Items Section */}
                            <div className="mo-items-box">
                                {order.items && Array.isArray(order.items) ? (
                                    order.items.map((item, index) => (
                                        <span key={index} className="mo-item-tag">
                                            {item.quantity} x {item.menuItemName}
                                        </span>
                                    ))
                                ) : (
                                    <span className="mo-item-tag">{order.items || "Items info unavailable"}</span>
                                )}
                            </div>

                            {/* Full Width Green Footer */}
                            <div 
                                className="mo-card-footer"
                                onClick={() => navigate(`/tracking/${order.id}`)}
                            >
                                <div className="mo-footer-content">
                                    {order.status === 'DELIVERED' ? (
                                        <>
                                            <span className="mo-footer-icon">✔</span>
                                            <span>Order Delivered</span>
                                        </>
                                    ) : (
                                        <>
                                            <span className="mo-footer-icon truck-anim">🚚</span>
                                            <span>{order.status.replace(/_/g, ' ')} • <strong>Track Order</strong></span>
                                        </>
                                    )}
                                </div>
                                <span className="mo-arrow">→</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyOrders;