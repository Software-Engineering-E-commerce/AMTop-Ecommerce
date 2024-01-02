import { useEffect, useState } from "react";
import React from "react";
import { Review } from "./CustomerReviews";
import StarRating from "./StarRating";
import { BsCartPlus, BsHeartFill, BsHeart } from "react-icons/bs";
import AddToCart from "./AddToCart";
import AddToWishlist from "./AddToWishlist";

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
  admin: boolean;
  isInWishlist: boolean;
}

interface ProductDetailsProps {
  product: Product;
  token:string;
}

const ProductDetails: React.FC<ProductDetailsProps> = ({ product, token }) => {
  const [imageSrc, setImageSrc] = useState<string | null>(null);

  useEffect(() => {
    const loadImage = async () => {
      try {
        const dynamicImportedImage = await import(
          `../assets${product.imageUrl}`
        );
        setImageSrc(dynamicImportedImage.default);
      } catch (error) {
        console.error("Error loading image:", error);
      }
    };

    loadImage();
  }, [product.imageUrl]);
  const [isInWishlist, setIsInWishlist] = useState(product.isInWishlist);
  const [showAddToCartPopup, setShowAddToCartPopup] = useState(false);
  const [showAddToWishListPopup, setShowAddToWishListPopup] = useState(false);

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

  const handleAddToCart = () => {
    setShowAddToCartPopup(true);
  };
  const handleAddToWishlist = () => {
    // setIsInWishlist(!isInWishlist);
    setShowAddToWishListPopup(true);
  };
  console.log("product from pdetails", product);
  return (
    <div>
      <div className="row">
        <div className="col-lg-7 col-md-12">
          {imageSrc && (
            <img
              src={imageSrc}
              alt={product.name}
              className="img-fluid rounded"
              style={{ width: "50%", height: "auto" }}
            />
          )}

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
              <strong>Availability: </strong>
              <span style={{ color: "#1fa336" }}>
                In Stock
                {/* <strong> */}
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Only{" "}
                {product.productCountAvailable} left)
                {/* </strong> */}
              </span>
            </p>
          )}
          {product.productCountAvailable == 0 && (
            <p className="fs-5">
              <strong>Availablility: </strong>
              <span style={{ color: "red" }}>out of stock</span>
            </p>
          )}
          <p className="fs-5">
            <strong>Sold:</strong> {product.productCountSold}
          </p>

          {!product.admin && (
            <>
              <div className="row">
                <div className="col-md-6 mb-2">
                  <button
                    className="btn btn-primary btn-lg w-100"
                    style={{ marginLeft: 0 }}
                    onClick={handleAddToCart}
                  >
                    <BsCartPlus className="me-2" />
                    Add to Cart
                  </button>
                </div>
                <div className="col-md-6 mb-2">
                  <button
                    className={`btn btn-${
                      isInWishlist ? "danger" : "success"
                    } btn-lg w-100`}
                    onClick={handleAddToWishlist}
                  >
                    {isInWishlist ? (
                      <>
                        <BsHeartFill className="me-2" />
                        Remove from Wishlist
                      </>
                    ) : (
                      <>
                        <BsHeart className="me-2" />
                        Add to Wishlist
                      </>
                    )}
                  </button>
                </div>
              </div>
              {/* AddToCartPopup */}
              {showAddToCartPopup && (
                <AddToCart
                  userTok={token}
                  productId={product.id}
                  onCloseBobUp={() => setShowAddToCartPopup(false)}
                />
              )}
              {/* AddToCartPopup */}
              {showAddToWishListPopup && (
                <AddToWishlist
                userTok={token}
                productId={product.id}
                setInWishlistBoolean={setIsInWishlist}
                onCloseBobUp={() => setShowAddToWishListPopup(false)}
                />
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProductDetails;
