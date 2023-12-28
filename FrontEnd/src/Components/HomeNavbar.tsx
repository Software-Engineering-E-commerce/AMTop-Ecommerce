import React, { useState, useEffect, useRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUser,
  faList,
  faHeart,
  faShoppingCart,
  faSignOutAlt,
  faSearch,
  faUserPlus,
} from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "./HomeNavbar.css"; // Make sure to create this CSS file
import axios from "axios";
import AddAdminPopup from "./AddAdminPopup";


interface NavbarProps {
  firstName: string;
  lastName: string;
  isAdmin: boolean;
  token: string;
  isHome?:boolean;
  isCategories?:boolean;
  isProducts?:boolean;
  isOrders?:boolean;
}

const Navbar: React.FC<NavbarProps> = ({
  firstName,
  lastName,
  isAdmin,
  token,
  isHome=false,
  isCategories = false,
  isProducts = false,
  isOrders = false
}) => {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLLIElement>(null);
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const navigate = useNavigate();
  const [isCartHovered, setIsCartHovered] = useState(false);
  const [isHeartHovered, setIsHeartHovered] = useState(false);
  const [isMyOrderHovered, setIsMyOrderHovered] = useState(false);
  const [isProfileHovered, setIsProfileHovered] = useState(false);
  const [isAddAdminHovered, setIsAddAdminHovered] = useState(false);
  const [isLogoutHovered, setIsLogoutHovered] = useState(false);
  const [searchKey, setSearchKey] = useState('');
  const [showAddAdminPopup, setShowAddAdminPopup] = useState(false);

  const handleAddAdminClick = () => {
    setShowAddAdminPopup(true);
  };

  const handleCloseAddAdminPopup = () => {
    setShowAddAdminPopup(false);
  };

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
        products: []
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
    isAdmin == true ? 
    navigate("/dashboard", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    })  :
    navigate("/customerOrders", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    });
  };

  const handleSearchKeyChange = (e: any) => {
    setSearchKey(e.target.value);
  };

  const handleSearchProduct = async (event: any) => {
    event.preventDefault();

    const returnedProducts = await getSearchedProducts();
    
    navigate("/catalog", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
        passedProducts: returnedProducts
      },
    });
  };



  function handleCategoriesClicked(): void {
    navigate("/categories", {
      state: {
        userToken: token,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
      },
    });
  }


  function toHomePage(): void {
    navigate("/home");
  }

  const [isHomeUse, setIsHomeUse] = useState(true);
  const [isNavbarVisible, setIsNavbarVisible] = useState(true);

  useEffect(() => {
    const handleScroll = () => {
      // Check if the scroll position is greater than or equal to 100vh
      setIsNavbarVisible(window.scrollY < window.innerHeight);

      // Optionally, update the isHome state based on your criteria
      // For example, check if the user is on the home page
      // Replace the condition with your logic
      setIsHomeUse(isHome);
    };

    // Add event listener for scroll
    window.addEventListener('scroll', handleScroll);

    // Remove the event listener when the component unmounts
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);



  const getSearchedProducts = async () => {
    console.log("In get searched products");
    let url = `http://localhost:9080/api/search/${searchKey}`;
    try {
      const response = await axios(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log(response.data);
      const products: Product[] = response.data;
      return products;
    } catch (error) {
      console.log("Error:", error);
      const products: Product[] = [];
      return products;
    }
  }


  return (
    <div
      style={{ borderBottom: "1px solid #ccc" }}
      className={`shadow-sm ${isHome && isNavbarVisible ? "fixed" :"sticky"}-top`}
    >
      <div
        style={{
          backgroundColor: isHome! ? "transparent":"rgb(21, 15, 1)",
        }}
        className="container-fluid"
      >
        <div className="row">
          <div className="col-md-2 my-auto d-none d-sm-none d-md-block d-lg-block">
            <h5 style={{ color: "#fff" }} className="brand-name">
              <img
                id="amTopIcon"
                src="src\assets\OurLogo.png"
                alt="productImage"
                onClick={toHomePage}
              />
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
                  value={searchKey}
                  onChange={handleSearchKeyChange}
                />
                <button
                  className={`btn ${
                    isSearchFocused ? "btn-secondary" : "btn-light"
                  }`}
                  onClick={handleSearchProduct}
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
                      style={{
                        color: "white",
                        position: "relative",
                        marginRight: "10px",
                      }}

                    >
                      <FontAwesomeIcon icon={faShoppingCart} />
                    </button>
                  </li>
                  <li className="nav-item">
                    <button
                      className="nav-link nav-bar-icons"
                      onClick={handleWishlistClick}
                      style={{ color: "white" }}
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
                  style={{ color: "white" }}
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
                  {isAdmin && (
                    <li>
                      <button
                        className="dropdown-item nav-bar-icons"
                        onClick={handleAddAdminClick}
                        onMouseEnter={() => setIsAddAdminHovered(true)}
                        onMouseLeave={() => setIsAddAdminHovered(false)}
                        style={{
                          color: isAddAdminHovered ? "#064fc4" : "#000",
                          transition: "color 0.3s ease",
                        }}
                      >
                        <FontAwesomeIcon icon={faUserPlus} /> Add admin
                      </button>
                    </li>
                  )}
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
        style={{ padding: "0px", backgroundColor: isHome? "transparent":"#ddd"}}
      >
        <div className="container-fluid">
          <button className="navbar-brand d-block d-sm-block d-md-none d-lg-none">
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
                  style={{ color: isHome? "orange":"black" , backgroundColor:`${isHome! ? "rgb(133, 133, 133)" : "none"}`}}
                >
                  <h5>Home</h5>
                </button>
              </li>
              <li className="nav-item">
                <button
                  className="nav-link nav-bar-icons"
                  onClick={handleCategoriesClicked}
                  style={{ color: isHome? "orange":"black",backgroundColor:`${isCategories! ? "rgb(133, 133, 133)" : "none"}`}}

                >
                  <h5>Categories</h5>
                </button>
              </li>
              <li className="nav-item">
                <button
                  className="nav-link nav-bar-icons"
                  onClick={handleProductsClick}
                  style={{color: isHome? "orange":"black" ,backgroundColor:`${isProducts! ? "rgb(133, 133, 133)" : "none"}` }}
                >
                  <h5>Products</h5>
                </button>
              </li>
              <li className="nav-item">
                <button
                  className="nav-link nav-bar-icons"
                  onClick={handleOrdersClick}
                  style={{ color: isHome? "orange":"black",backgroundColor:`${isOrders! ? "rgb(133, 133, 133)" : "none"}` }}
                >
                  <h5>Orders</h5>
                </button>
              </li>
            </ul>
          </div>
        </div>
      </nav>
      <AddAdminPopup
        show={showAddAdminPopup}
        handleClose={handleCloseAddAdminPopup}
        token={token}
      />
    </div>
  );
};

export default Navbar;