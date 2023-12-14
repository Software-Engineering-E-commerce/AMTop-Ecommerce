import React, { useState } from "react";
import "./cartElement.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck, faTrash } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import GenericAlertModal from "./GenericAlertModal";
import { useNavigate } from "react-router";

interface CartElementProps {
  cartElement: CartElement;
  fname: string;
  lname: string;
  causeRemountCart: () => void;
}

const CartElement = ({
  cartElement,
  causeRemountCart,
  fname,
  lname,
}: CartElementProps) => {
  const navigate = useNavigate();
  const [responseData, setResponseData] = useState("");
  const [Qnty, setQnty] = useState(cartElement.quantity);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // Allow only numeric values and limit to three digits
    let numericValue = e.target.value.replace(/[^0-9]/g, "");

    // Remove leading zeros for numbers with more than one digit
    numericValue = numericValue.replace(/^0+/, "");

    // Limit to three digits
    numericValue = numericValue.slice(0, 3);

    setQnty(numericValue == "" ? 0 : parseInt(numericValue, 10));
  };

  const handleUpdateQuantity = async () => {
    const q = Qnty; // const instance of Qnty
    const url = `http://localhost:9080/cart/setQuantity?quantity=${q}`;
    let cartRequest: CartRequest = { productId: cartElement.id };
    try {
      const response = await axios.post(url, cartRequest, {
        headers: {
          Authorization: `Bearer ${cartElement.token}`,
          "Content-Type": "application/json",
        },
      });

      setResponseData(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const toDetails = () => {
    console.log(fname)
    console.log(lname)
    navigate("/product-details", {
      state: {
        firstName: fname as string,
        lastName: lname as string,
        isAdmin: false,
        productID: cartElement.id,
        token: cartElement.token,
      },
    });
  };

  const handleDeleteFromCart = async () => {
    const url = `http://localhost:9080/cart/delete?productId=${cartElement.id}`;
    try {
      const response = await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${cartElement.token}`,
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

  const resetResponseData = () => {
    setResponseData("");
  };

  return (
    <>
      {responseData !== "" &&
        (responseData === "Product is deleted successfully from the cart" ||
          responseData === "Quantity is set successfully for this product") && (
          <>
            <GenericAlertModal
              show={true}
              resetResponseData={resetResponseData}
              onClose={causeRemountCart}
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
        responseData !== "Product is deleted successfully from the cart" &&
        responseData !== "Quantity is set successfully for this product" && (
          <>
            <GenericAlertModal
              show={true}
              resetResponseData={resetResponseData}
              onClose={causeRemountCart}
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
            src={cartElement.imageLink}
            className="cartImage"
            alt="productImage"
            onClick={toDetails}
          />
        </div>

        <div
          className="col-7 middleDiv"
          style={{ backgroundColor: "transparent", paddingLeft: "10px" }}
        >
          <div className="seperator"></div>
          <div className="productName">
            <h3>{cartElement.productName}</h3>
          </div>
          <div
            className="description"
            style={{ fontWeight: "450", maxWidth: "90%" }}
          >
            {cartElement.description}
          </div>

          <div className="qty_and_delete">
            <div className="qty">
              <label htmlFor="numericQty" style={{ fontWeight: "500" }}>
                Qty:
              </label>

              <input
                type="text"
                id="numericQty"
                style={{ fontWeight: "500" }}
                value={Qnty}
                onChange={handleInputChange}
              />

              <button
                type="button"
                onClick={handleUpdateQuantity}
                className="update-qty"
                style={{ fontWeight: "500", padding: "3px" }}
              >
                Update
              </button>

              <div className="vertical-line"></div>
            </div>

            <button className="delete" onClick={handleDeleteFromCart}>
              <FontAwesomeIcon
                icon={faTrash}
                style={{
                  padding: "10px",
                  color: "red",
                  fontSize: "28px",
                }}
              />
            </button>
          </div>
        </div>

        <div className="col-2 priceDiv">
          {cartElement.discountPercentage !== 0 && (
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
                    ${cartElement.price}
                  </h5>
                </div>
                <div className="discountPrice">
                  <h5 style={{ fontWeight: "650", marginLeft: "10px" }}>
                    $
                    {(
                      ((100.0 - cartElement.discountPercentage) / 100) *
                      cartElement.price
                    ).toFixed(2)}
                  </h5>
                </div>
              </div>
            </>
          )}
          {cartElement.discountPercentage === 0 && (
            <>
              <div className="the-whole-price">
                <h5 style={{ fontWeight: "650", marginLeft: "10px" }}>
                  ${cartElement.price}
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

export default CartElement;
