import React, { ReactNode, useEffect, useRef, useState } from "react";
import "./Cart.css";
import axios from "axios";
import CartElement from "../Components/CartElement";
import GenericAlertModal from "../Components/GenericAlertModal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";

const Cart = () => {
  console.log("Cart component rendered");
  const [cartElements, setCartElements] = useState<CartElement[]>([]); // Use state to manage cartElements
  const [totalItems, setTotalItems] = useState(0); //useState to get the total number of items for the customer
  const [totalPrice, setTotalPrice] = useState(""); //and here for the total price
  const [checkOutResponse, setCheckOutResponse] = useState("");

  //TODO change it when the home page is done such that it's a useLocation string that's set when clicked on the cart icon
  const userTok =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Y3VzdG9tZXJAZXhhbXBsZS5jb20iLCJpYXQiOjE3MDIzMDg3MjAsImV4cCI6MTcwMjM5NTEyMH0.VNkjOKE6pbuwGiFk8uesLujrrRvDWNWT4JQ_J9Tr4lY";

  // useRef to track whether the component is mounted
  const isMounted = useRef(true);

  const fetchData = async () => {
    setCheckOutResponse("");
    try {
      const response = await axios.get(
        "http://localhost:9090/cart/getCartElements",
        {
          headers: {
            Authorization: `Bearer ${userTok}`,
            "Content-Type": "application/json",
          },
        }
      );
      setCartElements([]);
      setCartElements(response.data);
      processResponse(response.data);
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

  // Function to handle successful delete operation
  const causeRemountCart = () => {
    fetchData();
  };

  const processResponse = (arr: CartElement[]) => {
    let totalQuantity: number = 0;
    let totalPrice: number = 0;
    for (let i = 0; i < arr.length; i++) {
      let currentPrice = arr[i].price;
      let currentDiscount = arr[i].discountPercentage;
      totalPrice +=
        ((100.0 - currentDiscount) / 100.0) * currentPrice * arr[i].quantity;
      totalQuantity += arr[i].quantity;
    }
    setTotalPrice(totalPrice.toFixed(2));
    setTotalItems(totalQuantity);
  };

  //function to handle the checkout request
  const handleCheckout = async () => {
    try {
      const response = await axios.post(
        "http://localhost:9090/cart/checkout",
        {},
        {
          headers: {
            Authorization: `Bearer ${userTok}`,
            "Content-Type": "application/json",
          },
        }
      );
      setCheckOutResponse(response.data);
      //causeRemountCart();
      // You can perform additional actions based on the response if needed
    } catch (error) {
      // Handle errors here
      if (axios.isAxiosError(error)) {
        // This type assertion tells TypeScript that error is an AxiosError
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          //alert(axiosError.response.data);
          setCheckOutResponse(axiosError.response.data as string);
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
        } else if (axiosError.request) {
          // The request was made but no response was received
          console.error("No response received:", axiosError.request);
        } else {
          // Something happened in setting up the request that triggered an Error
          console.error("Error:", axiosError.message);
        }
      } else {
        // Handle non-Axios errors
        console.error("Non-Axios error:", error);
      }
    }
  };

  return (
    <>
      {checkOutResponse === "Order has been placed successfully !" && (
        <GenericAlertModal
          onConfirm={causeRemountCart}
          show={true}
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
                {checkOutResponse}
              </h5>
            </>
          }
        />
      )}
      {checkOutResponse !== "" &&
        checkOutResponse !== "Order has been placed successfully !" && (
          <GenericAlertModal
            onConfirm={causeRemountCart}
            show={true}
            body={
              <>
                <h5 style={{ color: "red" }}>{checkOutResponse}</h5>
              </>
            }
          />
        )}
      <div
        className="col-12 container cart-container"
        style={{ backgroundColor: "white", display: "flex" }}
      >
        <div className="shopping-cart col-9">
          <div className="top-bar">
            <h3>Your Shopping Cart</h3>
          </div>
          <div className="horizontal-line"></div>
          <div className="cart-elements">
            {cartElements.map((cartElement) => (
              <CartElement
                cartElement={cartElement}
                causeRemountCart={causeRemountCart}
              />
            ))}
          </div>

          {totalItems >= 1 && (
            <>
              <h5 className="totalPrice">Your subtotal is: ${totalPrice}</h5>
            </>
          )}
        </div>

        {totalItems >= 1 && (
          <>
            <div className="checkout-container col-3">
              <div className="checkout">
                <div className="subtotal">
                  <span style={{ fontSize: "1.2em", fontWeight: "400" }}>
                    {`Subtotal (${totalItems} ${
                      totalItems === 1 ? "item" : "items"
                    }):`}
                  </span>{" "}
                  <span style={{ fontSize: "1.2em", fontWeight: "600" }}>
                    ${totalPrice}
                  </span>
                </div>
                <button
                  type="button"
                  className="btn btn-success"
                  onClick={handleCheckout}
                  style={{ marginTop: "30px", width: "100%" }}
                >
                  Proceed to Checkout
                </button>
              </div>
            </div>
          </>
        )}
      </div>
    </>
  );
};

export default Cart;
