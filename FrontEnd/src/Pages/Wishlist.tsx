import React, { ReactNode, useEffect, useRef, useState } from "react";
import WishlistElement from "../Components/WishlistElement";
import "./Cart.css";
import axios from "axios";
import { useLocation } from "react-router-dom";
import Navbar from "../Components/HomeNavbar";

const Wishlist = () => {
  const [WishlistElements, setWishlistElements] = useState<WishlistElement[]>(
    []
  );
  // Use state to manage cartElements
  const location = useLocation();
  var {userTok, isAdmin, firstName, lastName} = location.state || {};

  // useRef to track whether the component is mounted
  const isMounted = useRef(true);

  const fetchData = async () => {
    try {
      const response = await axios.get(
        "http://localhost:9080/wishlist/getWishlistElements",
        {
          headers: {
            Authorization: `Bearer ${userTok}`,
            "Content-Type": "application/json",
          },
        }
      );
      setWishlistElements([]);
      setWishlistElements(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  // useEffect runs on component mount
  useEffect(() => {
    if (isMounted.current) {
      fetchData();
      isMounted.current = false;
    }
  }, []);

  // Function that causes remount
  const causeRemountWishlist = () => {
    fetchData();
  };

  return (
    <>
      <Navbar 
        firstName={firstName}
        lastName={lastName}
        isAdmin={isAdmin}
        token={userTok} 
      />
      <div
        className="col-12 container wishlist-container"
        style={{ backgroundColor: "white", display: "flex" }}
      >
        <div className="wishlist col-9 mx-auto">
          <div className="top-bar">
            <h3>Your Wishlist</h3>
          </div>
          <div className="horizontal-line"></div>
          <div className="wishlist-elements">
            {WishlistElements.map((element) => (
              <WishlistElement
                key={element.id}
                wishlistElement={element}
                causeRemountWishlist={causeRemountWishlist}
              />
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default Wishlist;
