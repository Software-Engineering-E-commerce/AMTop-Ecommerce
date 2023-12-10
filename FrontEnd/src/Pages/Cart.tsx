import React, { useEffect, useRef, useState } from "react";
import "./Cart.css";
import axios from "axios";
import CartElement from "../Components/CartElement";

const Cart = () => {
  const [cartElements, setCartElements] = useState<CartElement[]>([]); // Use state to manage cartElements
  const [totalItems, setTotalItems] = useState(0); //useState to get the total number of items for the customer
  const [totalPrice, setTotalPrice] = useState(""); //and here for the total price

  //TODO change it when the home page is done such that it's a useLocation string that's set when clicked on the cart icon
  const userTok =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Y3VzdG9tZXJAZXhhbXBsZS5jb20iLCJpYXQiOjE3MDIxNDY0MzUsImV4cCI6MTcwMjIzMjgzNX0.3LBsm64lYk-pTG3jg6xB3_M_pGtG1AqLPNQPi1EoseA";

  // useRef to track whether the component is mounted
  const isMounted = useRef(true);

  // useEffect runs on component mount
  useEffect(() => {
    const fetchData = async () => {
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
        setCartElements(response.data);
        processResponse(response.data);
      } catch (error) {
        console.error(error);
      }
    };

    if (isMounted.current) {
      fetchData();
      isMounted.current = false;
    }
  }, [userTok]);

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

  return (
    <>
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
                key={cartElement.id}
                imageLink={cartElement.imageLink}
                id={cartElement.id}
                productName={cartElement.productName}
                description={cartElement.description}
                price={cartElement.price}
                quantity={cartElement.quantity}
                token={cartElement.token}
                discountPercentage={cartElement.discountPercentage}
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
