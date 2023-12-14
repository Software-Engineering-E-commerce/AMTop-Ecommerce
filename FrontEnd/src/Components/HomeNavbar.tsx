import React, { useState, useEffect, useRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUser,
  faList,
  faHeart,
  faShoppingCart,
  faSignOutAlt,
  faSearch,
} from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "./HomeNavbar.css"; // Make sure to create this CSS file

interface NavbarProps {
  firstName: string;
  lastName: string;
  isAdmin: boolean;
  token: string;
}

const Navbar: React.FC<NavbarProps> = ({
  firstName,
  lastName,
  isAdmin,
  token,
}) => {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLLIElement>(null);
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const navigate = useNavigate();
  const [isCartHovered, setIsCartHovered] = useState(false);
  const [isHeartHovered, setIsHeartHovered] = useState(false);
  const [isMyOrderHovered, setIsMyOrderHovered] = useState(false);
  const [isProfileHovered, setIsProfileHovered] = useState(false);
  const [isLogoutHovered, setIsLogoutHovered] = useState(false);

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  const handleClickOutside = (event: MouseEvent) => {
    if (
      dropdownRef.current &&
      !dropdownRef.current.contains(event.target as Node)
    ) {
      setDropdownOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener("click", handleClickOutside);

    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, []);

  const handleProductsClick = () => {
    navigate("/catalog", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    });
  };

  const handleProfileClick = () => {
    navigate("/home/profile", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstNameNav: firstName,
        lastNameNav: lastName,
      },
    });
  };

  const handleCartClick = () => {
    navigate("/cart", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    });
  };

  const handleWishlistClick = () => {
    navigate("/wishlist", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    });
  };

  const handleHomeClick = () => {
    navigate("/home", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    });
  };

  const handleLogoutClick = () => {
    navigate("/");
  };

  const handleOrdersClick = () => {
    navigate("/dashboard", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    });
  };

  return (
    <div
      style={{ borderBottom: "1px solid #ccc" }}
      className="shadow-sm sticky-top"
    >
      <div
        style={{
          backgroundColor: "rgb(21, 15, 1)",
          paddingTop: "10px",
          paddingBottom: "10px",
        }}
        className="container-fluid"
      >
        <div className="row">
          <div className="col-md-2 my-auto d-none d-sm-none d-md-block d-lg-block">
            <h5 style={{ color: "#fff" }} className="brand-name">
              AMTop
            </h5>
          </div>
          <div className="col-md-5 my-auto">
            <form role="search">
              <div
                className={`input-group ${
                  isSearchFocused ? "search-focused" : ""
                }`}
              >
                <input
                  type="search"
                  placeholder="Search your product"
                  className="form-control bg-white"
                  onFocus={() => setIsSearchFocused(true)}
                  onBlur={() => setIsSearchFocused(false)}
                />
                <button
                  type="submit"
                  className={`btn ${
                    isSearchFocused ? "btn-secondary" : "btn-light"
                  }`}
                >
                  <FontAwesomeIcon icon={faSearch} />
                </button>
              </div>
            </form>
          </div>
          <div className="col-md-5 my-auto">
            <ul className="nav justify-content-end">
              {!isAdmin && (
                <>
                  <li className="nav-item">
                    <button
                      className="nav-link nav-bar-icons"
                      
                      onClick={handleCartClick}
                      style={{color:"white"}}
                    >
                      <FontAwesomeIcon icon={faShoppingCart} />
                    </button>
                  </li>
                  <li className="nav-item">
                    <button
                      className="nav-link nav-bar-icons"
                      
                      onClick={handleWishlistClick}
                      style={{color:"white"}}
                    >
                      <FontAwesomeIcon icon={faHeart} />
                    </button>
                  </li>
                </>
              )}

              <li className="nav-item dropdown" ref={dropdownRef}>
                <button
                  className="nav-link dropdown-toggle nav-bar-icons"
                  
                  id="navbarDropdown"
                  role="button"
                  onClick={toggleDropdown}
                  aria-expanded={dropdownOpen}
                  style={{color:"white"}}
                >
                  <FontAwesomeIcon icon={faUser} /> {firstName + " " + lastName}
                </button>
                <ul
                  className={`dropdown-menu${dropdownOpen ? " show" : ""}`}
                  aria-labelledby="navbarDropdown"
                >
                  <li>
                    <button
                      className="dropdown-item nav-bar-icons"
                      
                      onClick={handleProfileClick}
                      onMouseEnter={() => setIsProfileHovered(true)}
                      onMouseLeave={() => setIsProfileHovered(false)}
                      style={{
                        color: isProfileHovered ? "#064fc4" : "#000",
                        transition: "color 0.3s ease",
                      }}
                    >
                      <FontAwesomeIcon icon={faUser} /> Profile
                    </button>
                  </li>
                  {!isAdmin && (
                    <>
                      <li>
                        <button
                          className="dropdown-item nav-bar-icons"
                          
                          onMouseEnter={() => setIsMyOrderHovered(true)}
                          onMouseLeave={() => setIsMyOrderHovered(false)}
                          style={{
                            color: isMyOrderHovered ? "#064fc4" : "#000",
                            transition: "color 0.3s ease",
                          }}
                        >
                          <FontAwesomeIcon icon={faList} /> My Orders
                        </button>
                      </li>
                      <li>
                        <button
                          className="dropdown-item nav-bar-icons"
                          
                          onClick={handleWishlistClick}
                          onMouseEnter={() => setIsHeartHovered(true)}
                          onMouseLeave={() => setIsHeartHovered(false)}
                          style={{
                            color: isHeartHovered ? "#064fc4" : "#000",
                            transition: "color 0.3s ease",
                          }}
                        >
                          <FontAwesomeIcon icon={faHeart} /> My Wishlist
                        </button>
                      </li>
                      <li>
                        <button
                          className="dropdown-item nav-bar-icons"
                          
                          onClick={handleCartClick}
                          onMouseEnter={() => setIsCartHovered(true)}
                          onMouseLeave={() => setIsCartHovered(false)}
                          style={{
                            color: isCartHovered ? "#064fc4" : "#000",
                            transition: "color 0.3s ease",
                          }}
                        >
                          <FontAwesomeIcon icon={faShoppingCart} /> My Cart
                        </button>
                      </li>
                    </>
                  )}

                  <li>
                    <button
                      className="dropdown-item nav-bar-icons"
                      
                      onClick={handleLogoutClick}
                      onMouseEnter={() => setIsLogoutHovered(true)}
                      onMouseLeave={() => setIsLogoutHovered(false)}
                      style={{
                        color: isLogoutHovered ? "#064fc4" : "#000",
                        transition: "color 0.3s ease",
                      }}
                    >
                      <FontAwesomeIcon icon={faSignOutAlt} /> Logout
                    </button>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <nav
        className="downnav navbar navbar-expand-lg"
        style={{ padding: "0px", backgroundColor: "#ddd" }}
      >
        <div className="container-fluid">
          <button
            className="navbar-brand d-block d-sm-block d-md-none d-lg-none"
            
          >
            AMTop
          </button>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav ms-auto me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <button
                  className="nav-link nav-bar-icons"
                  
                  onClick={handleHomeClick}
                  style={{ color: "black" }}
                >
                  Home
                </button>
              </li>
              <li className="nav-item">
                <button
                  className="nav-link nav-bar-icons"
                  
                  style={{ color: "black" }}
                >
                  Categories
                </button>
              </li>
              <li className="nav-item">
                <button
                  className="nav-link nav-bar-icons"
                  
                  onClick={handleProductsClick}
                  style={{ color: "black" }}
                >
                  Products
                </button>
              </li>
              <li className="nav-item">
                <button
                  className="nav-link nav-bar-icons"
                  
                  onClick={handleOrdersClick}
                  style={{ color: "black" }}
                >
                  Orders
                </button>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
  );
};

export default Navbar;