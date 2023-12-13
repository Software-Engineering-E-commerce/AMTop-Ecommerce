import React, { useEffect, useRef, useState } from "react";
// import NavBar from "../Components/NavBar";
import ProductDetails, { Product } from "../Components/ProductDetails";
import CustomerReviews from "../Components/CustomerReviews";
import { useLocation } from "react-router-dom";

interface ProductDetailsPageProps {}

const ProductDetailsPage: React.FC<ProductDetailsPageProps> = () => {
  const isMounted = useRef<boolean>(true);
  const location = useLocation();
  const { productID, token } = location.state as {
    productID: number;
    token: string;
  };
  const [product, setProduct] = useState<Product | null>(null);

  useEffect(() => {
    if (isMounted.current) {
      isMounted.current = false;
      console.log("productID: ", productID);
      console.log("token: ", token);
      // Fetch product details from the backend
      fetch(
        `http://localhost:9080/api/productDetails/viewProduct?productID=${productID}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
        .then((response) => response.json())
        .then((data) => setProduct(data))
        .catch((error) =>
          console.error("Error fetching product details:", error)
        );
    }
  }, []); // Empty dependency array means it runs once when component mounts

  if (!product) {
    return <div>Loading...</div>;
  }
  return (
    <div style={{ overflowX: "hidden", overflowY: "auto", padding: "30px" }}>
      {/* <NavBar /> */}
      <div className=" mt-1 pt-4">
        <ProductDetails product={product} token={token} />
        <hr className="my-4" /> {/* Separator line */}
        <CustomerReviews
          reviews={product.reviews}
          token={token}
          isAdmin={product.isAdmin}
        />
      </div>
    </div>
  );
};

export default ProductDetailsPage;
