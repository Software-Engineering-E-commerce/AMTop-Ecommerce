import React, { useState } from "react";
import "./WishlishtElement.css";
import "./cartElement.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faTrash,
  faCartPlus,
  faCircleCheck,
} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import GenericAlertModal from "./GenericAlertModal";
import { useNavigate } from "react-router";

interface WishlistElementProps {
  wishlistElement: WishlistElement;
  fname: string;
  lname: string;
  causeRemountWishlist: () => void;
}

const WishlistElement = ({
  wishlistElement,
  fname,
  lname,
  causeRemountWishlist,
}: WishlistElementProps) => {
  const navigate = useNavigate();

  const [responseData, setResponseData] = useState("");

  const handleDeleteFromWhishlist = async () => {
    const url = `http://localhost:9080/wishlist/delete?productId=${wishlistElement.id}`;
    try {
      await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${wishlistElement.token}`,
          "Content-Type": "application/json",
        },
      });
      // Here means that the response is OK and the product's been deleted successfully
      causeRemountWishlist();
    } catch (error) {
      // Handle errors here (non-OK responses)
      if (axios.isAxiosError(error)) {
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // Here means that a non-okay reponse has come
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
          setResponseData(axiosError.response.data as string);
        } else if (axiosError.request) {
          console.error("No response received:", axiosError.request);
        } else {
          console.error("Error:", axiosError.message);
        }
      } else {
        // Handle non-Axios errors
        console.error("Non-Axios error:", error);
      }
    }
  };

  const handleAddToCart = async () => {
    const url = `http://localhost:9080/wishlist/addToCart`;
    let wishlistRequest: WishlistRequest = { productId: wishlistElement.id };
    try {
      let response = await axios.post(url, wishlistRequest, {
        headers: {
          Authorization: `Bearer ${wishlistElement.token}`,
          "Content-Type": "application/json",
        },
      });
      setResponseData(response.data);
    } catch (error) {
      // Handle errors here (non-OK responses)
      if (axios.isAxiosError(error)) {
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // Here means that a non-okay reponse has come
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
          setResponseData(axiosError.response.data as string);
        } else if (axiosError.request) {
          console.error("No response received:", axiosError.request);
        } else {
          console.error("Error:", axiosError.message);
        }
      } else {
        // Handle non-Axios errors
        console.error("Non-Axios error:", error);
      }
    }
  };
  const toDetails = () => {
    console.log(fname);
    console.log(lname);
    navigate("/product-details", {
      state: {
        firstName: fname as string,
        lastName: lname as string,
        isAdmin: false,
        productID: wishlistElement.id,
        token: wishlistElement.token,
      },
    });
  };

  const resetResponseData = () => {
    setResponseData("");
  };

  return (
    <>
      {responseData !== "" &&
        responseData === "Product added successfully to your cart" && (
          <>
            <GenericAlertModal
              show={true}
              resetResponseData={resetResponseData}
              onClose={causeRemountWishlist}
              body={
                <>
                  <h5 style={{ color: "green" }}>
                    <FontAwesomeIcon
                      icon={faCircleCheck}
                      style={{
                        color: "green",
                        fontSize: "18px",
                        marginRight: "10px",
                      }}
                    />
                    {responseData}
                  </h5>
                </>
              }
            />
          </>
        )}
      {responseData !== "" &&
        responseData != "Product added successfully to your cart" && (
          <>
            <GenericAlertModal
              show={true}
              resetResponseData={resetResponseData}
              onClose={causeRemountWishlist}
              body={
                <>
                  <h5 style={{ color: "red" }}>{responseData}</h5>
                </>
              }
            />
          </>
        )}
      <div className="col-12 product">
        <div className="col-3 imageDiv">
          <img
            src={wishlistElement.imageLink}
            className="cartImage"
            alt="productImage"
            onClick={toDetails}
          />
        </div>

        <div
          className="col-7 middleDiv"
          style={{ paddingLeft: "10px", backgroundColor: "transparent" }}
        >
          <div className="seperator"></div>
          <div className="productName">
            <h3>{wishlistElement.productName}</h3>
          </div>
          <div
            className="description"
            style={{ fontWeight: "450", maxWidth: "90%" }}
          >
            {wishlistElement.description}
          </div>

          <div className="qty_and_delete">
            <button
              className="delete-from-wishlist"
              onClick={handleDeleteFromWhishlist}
            >
              <FontAwesomeIcon
                icon={faTrash}
                style={{
                  padding: "10px",
                  color: "red",
                  fontSize: "30px",
                }}
              />
            </button>

            <div className="vertical-line"></div>
            <button className="add-to-cart" onClick={handleAddToCart}>
              <FontAwesomeIcon
                icon={faCartPlus}
                style={{
                  padding: "10px",
                  color: "green",
                  fontSize: "30px",
                }}
              />
            </button>
          </div>
        </div>

        <div className="col-2 priceDiv">
          {wishlistElement.discountPercentage !== 0 && (
            <>
              <div className="the-whole-price">
                <div className="originalPrice">
                  <h5
                    style={{
                      fontWeight: "650",
                      marginLeft: "10px",
                      color: "red",
                      textDecoration: "line-through",
                    }}
                  >
                    ${wishlistElement.price}
                  </h5>
                </div>
                <div className="discountPrice">
                  <h5 style={{ fontWeight: "650", marginLeft: "10px" }}>
                    $
                    {(
                      ((100.0 - wishlistElement.discountPercentage) / 100) *
                      wishlistElement.price
                    ).toFixed(2)}
                  </h5>
                </div>
              </div>
            </>
          )}
          {wishlistElement.discountPercentage === 0 && (
            <>
              <div className="the-whole-price">
                <h5 style={{ fontWeight: "650", marginLeft: "10px" }}>
                  ${wishlistElement.price}
                </h5>
              </div>
            </>
          )}
        </div>
      </div>
      <div className="horizontal-line col-12"></div>
    </>
  );
};

export default WishlistElement;
