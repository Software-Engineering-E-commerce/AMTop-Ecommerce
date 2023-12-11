import React from "react";
import { Review } from "./CustomerReviews";
import StarRating from "./StarRating";

export interface Product {
  id: number;
  name: string;
  imageUrl: string;
  description: string;
  price: number;
  postedDate: string;
  productCountAvailable: number;
  productCountSold: number;
  brand: string;
  discountPercentage: number;
  categoryName: string;
  categoryUrl: string;
  reviews: Review[];
}

interface ProductDetailsProps {
  product: Product;
}

const ProductDetails: React.FC<ProductDetailsProps> = ({ product }) => {
  const productRating = calculateProductRating(product.reviews);

  function calculateProductRating(reviews: Review[]): number {
    if (reviews.length === 0) {
      return 0;
    }

    const totalRating = reviews.reduce((sum, review) => sum + review.rating, 0);
    return totalRating / reviews.length;
  }

  const calculateDiscountedPrice = () => {
    if (product.discountPercentage > 0) {
      const discountedPrice =
        ((100.0 - product.discountPercentage) * product.price) / 100.0;
      return discountedPrice.toFixed(2);
    }
    return null;
  };

  return (
    <div>
      <div className="row">
        <div className="col-lg-7 col-md-12">
          <img
            src={product.imageUrl}
            alt={product.name}
            className="img-fluid rounded"
            style={{ width: "80%", height: "auto" }}
          />

          <p style={{ marginTop: "20px" }}>
            <strong className="fs-4">Description:</strong>
            <p className="lead">{product.description}</p>
          </p>
        </div>
        <div className="col-lg-5 col-md-12">
          <h2 className="mt-4 mb-3">{product.name}</h2>

          {product.reviews.length > 0 && (
            <div className="mb-4">
              <div className="d-flex align-items-center">
                <StarRating rating={Math.round(productRating)} />{" "}
                <strong>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(
                  {product.reviews.length} CustomerReviews)
                </strong>
              </div>
            </div>
          )}

          {product.discountPercentage > 0 && (
            <div className="row">
              <div className="col-lg-3 col-md-8 mb-1">
                <p className="fs-5">
                  <strong>
                    Price:<br></br>
                  </strong>
                  <del className="text-danger mb-0 fs-6">${product.price}</del>{" "}
                  <br></br>
                  <strong>${calculateDiscountedPrice()}</strong>
                </p>
              </div>
              <div className="col-lg-2 col-md-4 mb-1">
                <p className="text-danger">
                  <strong className="fs-5">
                    Discount: {product.discountPercentage}%
                  </strong>{" "}
                </p>
              </div>
            </div>
          )}

          {product.discountPercentage <= 0 && (
            <p className="fs-5">
              <strong className="mb-1">Price:</strong> ${product.price}
            </p>
          )}
          {/* 
          <hr className="my-4" /> */}
          <p className="fs-5">
            <strong>Category:</strong> {product.categoryName}
          </p>
          <p className="fs-5">
            <strong>Brand:</strong> {product.brand}
          </p>
          <p className="fs-5">
            <strong>Posted Date:</strong> {product.postedDate}
          </p>
          {product.productCountAvailable > 0 && (
            <p className="fs-5">
              <strong>Availability: In Stock</strong>
              <strong>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Only{" "}
                {product.productCountAvailable} left)
              </strong>
            </p>
          )}
          {product.productCountAvailable == 0 && (
            <p className="fs-5">
              <strong>Availablility: out of stock</strong>
            </p>
          )}
          <p className="fs-5">
            <strong>Sold:</strong> {product.productCountSold}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ProductDetails;
