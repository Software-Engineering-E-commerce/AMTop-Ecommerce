import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import GenericAlertModal from "./GenericAlertModal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";

interface Props {
  userTok: string;
  productId: number;
  setInWishlistBoolean: React.Dispatch<React.SetStateAction<boolean>>;
}

const AddToWishlist = ({ userTok, productId, setInWishlistBoolean }: Props) => {
  const isMounted = useRef(true);
  const [responseData, setResponseData] = useState("");

  const addToWishlist = async () => {
    try {
      let wishlistRequest: WishlistRequest = { productId: productId };
      let url: string = "http://localhost:9080/wishlist/add";

      const response = await axios.post(url, wishlistRequest, {
        headers: {
          Authorization: `Bearer ${userTok}`,
          "Content-Type": "application/json",
        },
      });
      // Here means that the response is Ok and the product is added successfully
      // Setting the message of the pop up window
      setResponseData(response.data);
      // Then we need to set the boolean useState to true (to have the heart colored in red)
      setInWishlistBoolean(true);
    } catch (error) {
      // Handle errors here (non-OK responses)
      if (axios.isAxiosError(error)) {
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
          let responseDataAsString = axiosError.response.data as string;
          if (responseDataAsString === "Product's already in the wishlist !") {
            // Then we need to delete it from the wishlist
            deleteFromWishlist();
          } else {
            // Then something else happened along the way
            setResponseData(responseDataAsString);
          }
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

  const deleteFromWishlist = async () => {
    const url = `http://localhost:9080/wishlist/delete?productId=${productId}`;
    try {
      const response = await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${userTok}`,
          "Content-Type": "application/json",
        },
      });
      // Here means that the response is OK and the product's been deleted successfully
      setResponseData(response.data);
      // Then we need to set the useState boolean to false to notify the heart element that it's been deleted
      setInWishlistBoolean(false);
    } catch (error) {
      // Handle errors here (non-OK responses)
      if (axios.isAxiosError(error)) {
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // Here means that a non-okay reponse has come then we pop it up to the user
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

  useEffect(() => {
    if (isMounted.current) {
      addToWishlist(); // The default method that's called upon mounting
      isMounted.current = false;
    }
  }, []);

  const resetResponseData = () => {
    setResponseData("");
  };

  return (
    <>
      {(responseData === "Product is added successfully to the wishlist" ||
        responseData ===
          "Product is deleted successfully from the wishlist") && (
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
        responseData !== "Product is added successfully to the wishlist" &&
        responseData !==
          "Product is deleted successfully from the wishlist" && (
          <GenericAlertModal
            onConfirm={resetResponseData}
            show={true}
            body={
              <>
                <h5 style={{ color: "red" }}>{responseData}</h5>
              </>
            }
          />
        )}
    </>
  );
};
export default AddToWishlist;
