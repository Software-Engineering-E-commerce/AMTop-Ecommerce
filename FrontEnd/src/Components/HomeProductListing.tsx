import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import StarRating from "./StarRating";
import "./ProductsList.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping, faHeart } from "@fortawesome/free-solid-svg-icons";
import EditAddProduct, { EditedProduct } from "./EditAddProduct";
import AddToCart from "./AddToCart";
import AddToWishlist from "./AddToWishlist";

interface Props {
  firstName: string;
  lastName: string;
  isAdmin: boolean;
  userToken: string;
  product: HomeProductDTO;
}

const HomeProductListing = ({
  firstName,
  lastName,
  isAdmin,
  userToken,
  product,
}: Props) => {
  const [editedProductDTO, setEditedProductDTO] = useState<EditedProduct>();
  const [editProduct, setEditProduct] = useState(false);
  const [fadeAnimation, setFadeAnimation] = useState("");
  const [wishlistStatus, setWishlistStatus] = useState(new Map());
  const [showCartPopUp, setShowCartPopUp] = useState(false);
  const [wishlistProductId, setWishlistProductId] = useState(0);
  const [dummyBool, setDummyBool] = useState(false);
  const [cartProductId, setCartProductId] = useState(0);
  const [showWishlistPopUp, setShowWishlistPopUp] = useState(false);

  const navigate = useNavigate();

  const handleProductClick = () => {
    const id = product.id;
    navigate("/product-details", {
      state: {
        firstName: firstName,
        lastName: lastName,
        isAdmin: isAdmin,
        productID: id,
        token: userToken,
      },
    });
  };

  function calculateProductRating(reviews: number[]): number {
    if (reviews.length === 0) {
      return 0;
    }
    const totalRating = reviews.reduce((sum, review) => sum + review, 0);
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

  const handleEditProduct = () => {
    console.log(product);
    // Your logic for editing a product can go here
    setEditedProductDTO({
      name: product.productName,
      description: product.description,
      price: String(product.price),
      countAvailable: String(product.productCountAvailable),
      category: product.categoryName,
      brand: product.brand,
      id: String(product.id),
      discountPercentage: String(product.discountPercentage),
    });
    setEditProduct(true);
  };

  const handleAddToWishlist = () => {
    setWishlistProductId(product.id);
    setShowWishlistPopUp(true);
  };

  const handleAddToCart = () => {
    setCartProductId(product.id);
    setShowCartPopUp(true);
  };

  const handleCloseCartWindow = () => {
    setShowCartPopUp(false);
  };

  const handleCloseWishlistWindow = () => {
    setShowWishlistPopUp(false);
  };

  const toggleWishlist = () => {
    setShowWishlistPopUp(true);
    setWishlistProductId(product.id);
    product.inWishlist = !product.inWishlist;
  };

  const resetEditButton = () => {
    setEditProduct(false);
  }

  return (
    <>
      {editProduct && (
        <>
          <EditAddProduct
            isEdit={true}
            adminToken={userToken}
            product={editedProductDTO}
            resetButton={resetEditButton}
          />
        </>
      )}

      <div
        key={product.id}
        className="subSlider"
        onClick={() => handleProductClick()}
      >
        <div className="subSliderWrap">
          <div
            style={{
              width: "90%",
              height: "50%",
              marginBottom: "8px",
            }}
          >
            {product.imageLink && (
              <img
                src={product.imageLink}
                alt={product.productName}
                style={{ maxHeight: "10rem" }}
              />
            )}
          </div>
          <div
            style={{ height: "50%", position: "relative", marginTop: "20px" }}
          >
            <div style={{ marginBottom: "8px" }}>
              <StarRating
                rating={Math.round(calculateProductRating(product.reviews))}
              />
              <br></br>
            </div>
            <h5 style={{ marginTop: "30px" }}>{product.productName}</h5>
            {product.discountPercentage > 0 && (
              <div>
                <div>
                  <p className="fs-5">
                    <strong>Price: ${calculateDiscountedPrice()}</strong>
                    <del
                      className="text-danger mb-0 fs-6"
                      style={{ position: "relative", left: "8px" }}
                    >
                      ${product.price}
                    </del>
                  </p>
                </div>
              </div>
            )}

            {product.discountPercentage <= 0 && (
              <p className="fs-5">
                <strong className="mb-1">Price:</strong> ${product.price}
              </p>
            )}
            {isAdmin && (
              <button
                className="btn btn-primary"
                type="button"
                onClick={(e) => {
                  e.stopPropagation();
                  handleEditProduct();
                }}
                style={{ width: "100%" }}
              >
                Edit
              </button>
            )}
            <div className="buttons-container">
              {!isAdmin && (
                <button
                  className="product-cart-button"
                  onClick={(e) => {
                    e.stopPropagation();
                    handleAddToCart();
                  }}
                >
                  <FontAwesomeIcon icon={faCartShopping} />
                  <span className="button-text-go-left">Add to Cart</span>
                </button>
              )}
              {!isAdmin && (
                <button
                  className={`product-wishlist-button ${
                    wishlistStatus.get(product.id) ? "in-wishlist" : ""
                  }`}
                  onClick={(e) => {
                    e.stopPropagation();
                    handleAddToWishlist();
                    toggleWishlist();
                  }}
                >
                  <FontAwesomeIcon icon={faHeart} />
                  {product.inWishlist && (
                    <span className="button-text-go-right">Forget</span>
                  )}
                  {!product.inWishlist && (
                    <span className="button-text-go-right">Wish!</span>
                  )}
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
      {showCartPopUp && (
        <AddToCart
          userTok={userToken}
          productId={cartProductId}
          onCloseBobUp={handleCloseCartWindow}
        />
      )}
      {showWishlistPopUp && (
        <AddToWishlist
          userTok={userToken}
          productId={wishlistProductId}
          setInWishlistBoolean={setDummyBool}
          setInWishlistBooleanMap={setWishlistStatus}
          onCloseBobUp={handleCloseWishlistWindow}
        />
      )}
    </>
  );
};

export default HomeProductListing;
