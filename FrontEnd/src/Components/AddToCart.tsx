import axios from "axios";
import React, { ReactNode, useEffect, useRef, useState } from "react";
import GenericAlertModal from "./GenericAlertModal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";

interface Props {
  userTok: string;
  productId: number;
}

const AddToCart = ({ userTok, productId }: Props) => {
  const isMounted = useRef(true);
  const [responseData, setResponseData] = useState("");

  // The function that handels the addToCart request to the api
  const addToCart = async () => {
    try {
      let cartRequst: CartRequest = { productId: productId };
      let url: string = "http://localhost:9080/cart/add";

      const response = await axios.post(url, cartRequst, {
        headers: {
          Authorization: `Bearer ${userTok}`,
          "Content-Type": "application/json",
        },
      });
      // Here means that the response is Ok and the product is added successfully
      setResponseData(response.data);
    } catch (error) {
      // Handle errors here
      if (axios.isAxiosError(error)) {
        // This type assertion tells TypeScript that error is an AxiosError
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
          setResponseData(axiosError.response.data as string);
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

  useEffect(() => {
    if (isMounted.current) {
      addToCart();
      isMounted.current = false;
    }
  }, []);

  const resetResponseData = () => {
    setResponseData("");
  };

  // The final return HTML element 
  return (
    <>
      {responseData === "Product is added successfully to the cart" && (
        <GenericAlertModal
          onConfirm={resetResponseData}
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
                {responseData}
              </h5>
            </>
          }
        />
      )}
      {responseData !== "" &&
        responseData !== "Product is added successfully to the cart" && (
          <GenericAlertModal
            onConfirm={resetResponseData}
            show={true}
            body={
              <>
                <h5 style={{ color: "red" }}>
                  {responseData}
                </h5>
              </>
            }
          />
        )}
    </>
  );
};

export default AddToCart;
