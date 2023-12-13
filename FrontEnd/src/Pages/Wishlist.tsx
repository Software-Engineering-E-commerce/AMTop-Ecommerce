import React, { ReactNode, useEffect, useRef, useState } from "react";
import WishlistElement from "../Components/WishlistElement";
import "./Cart.css";
import axios from "axios";

const Wishlist = () => {
  const [WishlistElements, setWishlistElements] = useState<WishlistElement[]>(
    []
  );
  // Use state to manage cartElements
  //TODO change it when the home page is done such that it's a useLocation string that's set when clicked on the cart icon
  const userTok =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Y3VzdG9tZXJAZXhhbXBsZS5jb20iLCJpYXQiOjE3MDI0MDc2MjMsImV4cCI6MTcwMjQ5NDAyM30.gkgWGTEs_jsgqfMKttq4FytpjPJiPyyQHTQjpYmk23o";

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
