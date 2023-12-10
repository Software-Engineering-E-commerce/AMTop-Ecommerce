import React, { useState } from "react";
import "./cartElement.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const CartElement = ({
  id,
  productName,
  price,
  description,
  imageLink,
  quantity,
  token,
  discountPercentage,
}: CartElement) => {
  const [Qnty, setQnty] = useState(quantity);

  var navigate = useNavigate();

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
    const q = Qnty; //const instance of Qnty
    const url = `http://localhost:9090/cart/setQuantity?quantity=${q}`;
    let cartRequest: CartRequest = { productId: id };
    try {
      const response = await axios.post(url, cartRequest, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      if (
        (response.status == 200 &&
          q === 0 &&
          response.data === "Product is deleted successfully from the cart") ||
        (response.data == 200 &&
          response.data === "Quantity is set successfully for this product")
      ) {
        //so here we'll need here to navigate to the cart page to see the changes
        navigate("/cart"); //TODO make sure that if there's a useLocation we need to set it each time we navigate ther or not
      } else {
        alert(response.data);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleDeleteFromCart = async () => {
    const url = `http://localhost:9090/cart/delete?productId=${id}`;
    try {
      const response = await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      alert(response.data);
      if (
        response.status == 200 &&
        response.data == "Product is deleted successfully from the cart"
      ) {
        navigate("/cart"); //TODO make sure that if there's a useLocation we need to set it each time we navigate ther or not
      }
    } catch (error) {
      alert("Product is not in your cart!");
      console.error(error);
    }
  };

  return (
    <>
      <div className="col-12 product">
        <div className="col-3 imageDiv">
          <img src={imageLink} alt="productImage" />
        </div>

        <div
          className="col-7 middleDiv"
          style={{ backgroundColor: "transparent", paddingLeft: "10px" }}
        >
          <div className="seperator"></div>
          <div className="productName">
            <h3>{productName}</h3>
          </div>
          <div
            className="description"
            style={{ fontWeight: "450", maxWidth: "90%" }}
          >
            {description}
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
          {discountPercentage !== 0 && (
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
                    ${price}
                  </h5>
                </div>
                <div className="discountPrice">
                  <h5 style={{ fontWeight: "650", marginLeft: "10px" }}>
                    ${(((100.0 - discountPercentage) / 100) * price).toFixed(2)}
                  </h5>
                </div>
              </div>
            </>
          )}
          {discountPercentage === 0 && (
            <>
              <div className="the-whole-price">
                <h5 style={{ fontWeight: "650", marginLeft: "10px" }}>
                  ${price}
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
