import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from '../context/CartContext';
import axios from 'axios';
import { toast } from 'react-toastify';
import "./Home.css";

const Home = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("Home");
  const [search, setSearch] = useState("");
  
  // Sidebar state
  const [menuOpen, setMenuOpen] = useState(true); 
  
  const { addToCart } = useCart();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMenuItems = async () => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/api/menu-items`);
        setMenuItems(response.data);
      } catch (error) {
        console.error("Failed to fetch menu items:", error);
      }
    };
    fetchMenuItems();
  }, []);
  
  const categories = [ "Home", "BurgerPizza", "Biryani", "Ice Cream", "Beverages", "Salads", "Desserts", "Starters", "My Orders" ];

  const filteredItems = menuItems.filter((item) => {
    const lowerCaseSearch = search.toLowerCase();
    const matchesSearch = item.name.toLowerCase().includes(lowerCaseSearch) || item.restaurant.toLowerCase().includes(lowerCaseSearch);

    if (selectedCategory === "Home") {
        return matchesSearch;
    }
    if (selectedCategory === "Starters") {
        return item.category.startsWith("Starters") && matchesSearch;
    }
    return item.category === selectedCategory && matchesSearch;
  });

  const handleAddToCart = (e, item) => {
    e.stopPropagation();
    addToCart(item);
    toast.success(`${item.name} added to cart!`);
  };

  return (
    <div className="home-layout"> 
      
      {/* Sidebar Menu - No Close Button, just list */}
      <aside className={`home-sidebar ${menuOpen ? "open" : "closed"}`}>
        <div className="sidebar-header">
            <h3 className="menu-heading">🍽 Menu</h3>
        </div>
        <ul className="category-list">
          {categories.map((cat) => (
            <li key={cat} className={selectedCategory === cat ? "active" : ""} onClick={() => {
                if (cat === "My Orders") {
                  navigate("/orders");
                } else {
                  setSelectedCategory(cat);
                }
                // Auto close on mobile only
                if (window.innerWidth <= 768) setMenuOpen(false);
              }}>
              {cat}
            </li>
          ))}
        </ul>
      </aside>

      {/* Main Content Area */}
      <main className="home-content">
        
        <div className="home-top-bar">
          {/* Toggle Button */}
          <button className="menu-toggle-btn" onClick={() => setMenuOpen(!menuOpen)}>
            ☰
          </button>
          
          <div className="search-wrapper">
             <input
                type="text"
                placeholder="Search..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="home-search-bar"
              />
          </div>
        </div>

        <div className="content-scrollable">
            <h1 className="category-header">
                {selectedCategory === 'Home' ? '🍴 Delicious Food For You' : `🍴 ${selectedCategory}`}
            </h1>
            
            {selectedCategory === 'Starters' ? (
                <>
                    <h2 className="sub-header">🥗 Veg Starters</h2>
                    <div className="menu-grid">
                        {menuItems
                            .filter(it => it.category === 'Starters-Veg' && (it.name.toLowerCase().includes(search.toLowerCase())))
                            .map(item => (
                                <MenuCard key={item.id} item={item} handleAddToCart={handleAddToCart} />
                            ))
                        }
                    </div>

                    <h2 className="sub-header" style={{ marginTop: '40px' }}>🍗 Non-Veg Starters</h2>
                    <div className="menu-grid">
                        {menuItems
                            .filter(it => it.category === 'Starters-NonVeg' && (it.name.toLowerCase().includes(search.toLowerCase())))
                            .map(item => (
                                <MenuCard key={item.id} item={item} handleAddToCart={handleAddToCart} />
                            ))
                        }
                    </div>
                </>
            ) : (
                <div className="menu-grid">
                    {filteredItems.length > 0 ? (
                        filteredItems.map((item) => (
                            <MenuCard key={item.id} item={item} handleAddToCart={handleAddToCart} />
                        ))
                    ) : (
                        <div className="no-items">
                            <p>No items found for this category.</p>
                        </div>
                    )}
                </div>
            )}
        </div>
      </main>
    </div>
  );
};

const MenuCard = ({ item, handleAddToCart }) => (
    <div className="menu-card">
        <Link to={`/food/${item.id}`} className="menu-card-link">
            <div className="menu-image-container">
                <img 
                    src={item.image ? (item.image.startsWith('http') ? item.image : process.env.PUBLIC_URL + item.image) : "https://via.placeholder.com/200?text=Food"} 
                    alt={item.name} 
                    className="menu-image"
                />
            </div>
            <div className="menu-details">
                <h3>{item.name}</h3>
                <div className="menu-meta">
                    <span className="menu-price">₹{item.price}</span>
                    <span className="menu-rating">⭐ {item.rating || '4.0'}</span>
                </div>
                <p className="menu-restaurant">{item.restaurant}</p>
            </div>
        </Link>
        <button className="btn-add-cart" onClick={(e) => handleAddToCart(e, item)}>
            ADD +
        </button>
    </div>
);

export default Home;