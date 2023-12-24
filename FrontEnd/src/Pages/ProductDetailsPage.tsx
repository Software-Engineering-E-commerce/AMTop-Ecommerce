import React, { useEffect, useRef, useState } from "react";
import ProductDetails, { Product } from "../Components/ProductDetails";
import CustomerReviews from "../Components/CustomerReviews";
import { useLocation } from "react-router-dom";
import Navbar from "../Components/HomeNavbar";

interface ProductDetailsPageProps {}

const ProductDetailsPage: React.FC<ProductDetailsPageProps> = () => {
  const isMounted = useRef<boolean>(true);
  const location = useLocation();
  const { firstName, lastName, isAdmin, productID, token } = location.state as {
    firstName: string;
    lastName: string;
    isAdmin: boolean;
    productID: number;
    token: string;
  };
  const [product, setProduct] = useState<Product | null>(null);

  const fetchProduct = async () => {
    try {
    } catch (error) {
      console.error("Error:", error);
      alert(error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          `http://localhost:9080/api/productDetails/viewProduct?productID=${productID}`,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const data = await response.json();
        setProduct(data);
      } catch (error) {
        console.error("Error fetching product details:", error);
      }
    };
    if (isMounted.current) {
      isMounted.current = false;
      console.log("productID: ", productID);
      console.log("token: ", token);

      fetchData();
    }
  }, []); // Empty dependency array means it runs once when component mounts

  if (!product) {
    return <div>Loading...</div>;
  }
  return (
    <div>
      <Navbar
        firstName={firstName}
        lastName={lastName}
        isAdmin={isAdmin}
        token={token}
      />
      <div
        className=" mt-1 pt-4"
        style={{ overflowX: "hidden", overflowY: "auto", padding: "30px" }}
      >
        <ProductDetails product={product} token={token} />
        <hr className="my-4" /> {/* Separator line */}
        <CustomerReviews
          reviews={product.reviews}
          token={token}
          isAdmin={product.admin}
        />
      </div>
    </div>
  );
};

export default ProductDetailsPage;
