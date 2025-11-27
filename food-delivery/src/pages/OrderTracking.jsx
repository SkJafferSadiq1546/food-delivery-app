import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../api/axiosConfig';
import './OrderTracking.css';

const OrderTracking = () => {
    const { orderId } = useParams();
    const [order, setOrder] = useState(null);
    const navigate = useNavigate();

    const steps = [
        { key: "PLACED", label: "Order Placed", icon: "📝" },
        { key: "ACCEPTED", label: "Order Accepted", icon: "👨‍🍳" },
        { key: "PREPARING", label: "Preparing Food", icon: "🔥" },
        { key: "OUT_FOR_DELIVERY", label: "Out for Delivery", icon: "🛵" },
        { key: "DELIVERED", label: "Delivered", icon: "🏠" }
    ];

    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const response = await apiClient.get(`/${orderId}`);
                setOrder(response.data);
            } catch (error) {
                console.error("Error fetching order:", error);
            }
        };
        fetchOrder();
        
        // Auto-refresh every 5 seconds to catch status updates
        const interval = setInterval(fetchOrder, 5000);
        return () => clearInterval(interval);
    }, [orderId]);

    if (!order) return <div className="ot-container">Loading Tracking...</div>;

    // --- FIX: Normalize status to ensure animation works ---
    // Converts "Order Placed" -> "PLACED" to match the keys
    const normalizeStatus = (status) => {
        if (!status) return "PLACED";
        return status.toUpperCase().replace(/\s+/g, '_');
    };

    const statusOrder = ["PLACED", "ACCEPTED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED"];
    const currentStatus = normalizeStatus(order.status);
    const currentStatusIndex = statusOrder.indexOf(currentStatus);

    return (
        <div className="ot-container">
            {/* Header Card */}
            <div className="ot-card header-card">
                <button className="ot-back-btn" onClick={() => navigate('/orders')}>
                    ← Back
                </button>
                <div className="ot-header-content">
                    <h1>Order Tracking</h1>
                    <p className="ot-order-id">Order #{order.id}</p>
                </div>
                <div className="ot-status-badge">
                    {currentStatus.replace(/_/g, ' ')}
                </div>
            </div>

            <div className="ot-grid">
                {/* Timeline Card */}
                <div className="ot-card timeline-card">
                    <h2>Live Status</h2>
                    <div className="ot-timeline">
                        {steps.map((step, index) => {
                            // Logic: 
                            // completed = Step is finished (Green Check)
                            // current   = This is the active step (Pulsing Orange)
                            const isCompleted = index < currentStatusIndex;
                            const isCurrent = index === currentStatusIndex;

                            return (
                                <div 
                                    key={step.key} 
                                    className={`ot-step ${isCompleted ? 'completed' : ''} ${isCurrent ? 'current' : ''}`}
                                >
                                    <div className="ot-icon-box">
                                        {step.icon}
                                    </div>
                                    <div className="ot-step-content">
                                        <span className={`ot-label ${isCurrent ? 'label-active' : ''}`}>
                                            {step.label}
                                        </span>
                                        {isCompleted && <span className="ot-check">✔</span>}
                                        {isCurrent && <span className="ot-processing">...</span>}
                                    </div>
                                    
                                    {/* Line connecting steps */}
                                    {index < steps.length - 1 && (
                                        <div className={`ot-line ${index < currentStatusIndex ? 'line-active' : ''}`}></div>
                                    )}
                                </div>
                            );
                        })}
                    </div>
                </div>

                {/* Details Card */}
                <div className="ot-card details-card">
                    <h2>Delivery Details</h2>
                    
                    <div className="ot-detail-row">
                        <span className="ot-detail-icon">📍</span>
                        <div>
                            <label>Delivery Address</label>
                            <p>{order.deliveryAddress}</p>
                        </div>
                    </div>

                    <div className="ot-detail-row">
                        <span className="ot-detail-icon">⏰</span>
                        <div>
                            <label>Estimated Time</label>
                            <p>{order.estimatedDeliveryTime || "30-45 mins"}</p>
                        </div>
                    </div>

                    <div className="ot-detail-row total-row">
                        <span className="ot-detail-icon">💰</span>
                        <div>
                            <label>Total Amount</label>
                            <p className="ot-amount">₹{order.totalAmount.toFixed(2)}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderTracking;