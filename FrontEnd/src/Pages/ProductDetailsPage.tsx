import React from "react";
import NavBar from "../Components/NavBar";
import ProductDetails, { Product } from "../Components/ProductDetails";
import CustomerReviews from "../Components/CustomerReviews";
import sampleImage from "../assets/laptop.jpeg";

interface ProductDetailsPageProps {
  productID: number;
}

const ProductDetailsPage: React.FC<ProductDetailsPageProps> = ({ productID }) => {
  const sampleProduct: Product = {
    id: 1,
    name: "Sample Product",
    imageUrl: sampleImage, // Use a placeholder image for demonstration
    description:
      "This is a sample product description. This is a sample product description. This is a sample product description.This is a sample product description.This is a sample product description.This is a sample product description. This is a sample product description. This is a sample product description. This is a sample product description.",
    price: 49.99,
    postedDate: "2023-12-15",
    productCountAvailable: 10,
    productCountSold: 20,
    brand: "Sample Brand",
    discountPercentage: 10,
    categoryName: "Electronics",
    categoryUrl: "/electronics",
    reviews: [
      {
        id: 1,
        customerName: "John Doe",
        rating: 4.5,
        reviewText:
          "Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.Great product! Highly recommended.",
        date: "2023-12-16",
      },
      {
        id: 2,
        customerName: "John Doe",
        rating: 3.5,
        reviewText: "Great product! Highly recommended.",
        date: "2023-12-16",
      },
      {
        id: 3,
        customerName: "John Doe",
        rating: 3,
        reviewText: "Great product! Highly recommended.",
        date: "2023-12-16",
      },
      {
        id: 4,
        customerName: "John Doe",
        rating: 1,
        reviewText: "Great product! Highly recommended.",
        date: "2023-12-16",
      },
      // // Add more reviews as needed
    ],
  };
  return (
    <div style={{ overflowX: "hidden", overflowY: "auto", padding: "30px" }}>
      <NavBar />
      <div className=" mt-1 pt-4">
        <ProductDetails product={sampleProduct} />
        <hr className="my-4" /> {/* Separator line */}
        <CustomerReviews reviews={sampleProduct.reviews} />
      </div>
    </div>
  );
};

export default ProductDetailsPage;
