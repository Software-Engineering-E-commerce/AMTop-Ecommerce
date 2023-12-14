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
  var { userToken, isAdmin, firstName, lastName } = location.state || {};

  // useRef to track whether the component is mounted
  const isMounted = useRef(true);

  const getWishlistElements = async () => {
    try {
      const response = await axios.get(
        "http://localhost:9080/wishlist/getWishlistElements",
        {
          headers: {
            Authorization: `Bearer ${userToken}`,
            "Content-Type": "application/json",
          },
        }
      );
      const wishlistElements: WishlistElement[] = response.data;
      return wishlistElements;
    } catch (error) {
      console.log("Error:", error);
      const wishlistElements: WishlistElement[] = [];
      return wishlistElements;
    }
  };

  const fetchData = async () => {
    const wishlistElements = await getWishlistElements();
    setWishlistElements(wishlistElements);
    console.log(wishlistElements);
    // Load images
    const updatedWishlistElements = await Promise.all(
      wishlistElements.map(async (wishlistElement) => {
        try {
          const dynamicImportedImage = await import(
            `../assets${wishlistElement.imageLink}`
          );
          return {
            ...wishlistElement,
            imageLink: dynamicImportedImage.default,
          };
        } catch (error) {
          console.error("Error loading image:", error);
          return wishlistElement; // Return original product if image loading fails
        }
      })
    );
    setWishlistElements(updatedWishlistElements);
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
        token={userToken}
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
                fname={firstName as string}
                lname={lastName as string}
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
