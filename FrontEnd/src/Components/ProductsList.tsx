import { useState, useEffect, useRef } from "react";
import Pagination from "./Pagination";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCartShopping,
  faHeart,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import "./ProductsList.css";
import StarRating from "./StarRating";
import AddToCart from "./AddToCart";
import AddToWishlist from "./AddToWishlist";
import EditAddProduct, { EditedProduct } from "./EditAddProduct";

interface Props {
  firstName: string;
  lastName: string;
  isAdmin: boolean;
  getProducts: () => Promise<Product[]>;
  userToken: string;
}

const ProductsList = ({
  firstName,
  lastName,
  isAdmin,
  getProducts,
  userToken,
}: Props) => {
  const [editedProductDTO, setEditedProductDTO] = useState<EditedProduct>();
  const [products, setProducts] = useState<Product[]>([]);
  const [cartProductId, setCartProductId] = useState(0);
  const [wishlistProductId, setWishlistProductId] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [productsPerPage] = useState(15);
  const [fadeAnimation, setFadeAnimation] = useState("");
  const [wishlistStatus, setWishlistStatus] = useState(new Map());
  const [dummyBool, setDummyBool] = useState(false);
  const [showCartPopUp, setShowCartPopUp] = useState(false);
  const [showWishlistPopUp, setShowWishlistPopUp] = useState(false);
  const [editProduct, setEditProduct] = useState(false);
  const [addProduct, setAddProduct] = useState(false);
  const navigate = useNavigate();

  const handleProductClick = (product: Product) => {
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

  const handleEditProduct = (product: Product) => {
    console.log(product);
    // Your logic for editing a product can go here
    setEditedProductDTO({
      name: product.productName,
      description: product.description,
      price: String(product.price),
      countAvailable: String(product.productCountAvailable),
      category: product.category.categoryName,
      brand: product.brand,
      id: String(product.id),
      discountPercentage: String(product.discountPercentage),
    });
    setEditProduct(true);
  };

  const handleAddProduct = () => {
    setAddProduct(true);
  };

  const handleAddToWishlist = (productId: number) => {
    setWishlistProductId(productId);
    setShowWishlistPopUp(true);
  };

  const handleAddToCart = (productId: number) => {
    setCartProductId(productId);
    setShowCartPopUp(true);
  };

  const handleCloseCartWindow = () => {
    setShowCartPopUp(false);
  };

  const handleCloseWishlistWindow = () => {
    setShowWishlistPopUp(false);
  };

  const toggleWishlist = (productId: number) => {
    setShowWishlistPopUp(true);
    setWishlistProductId(productId);
    for (let i = 0; i < currentProducts.length; i++) {
      const product = currentProducts[i];
      if (product.id == productId) {
        product.inWishlist = !product.inWishlist;
        break;
      }
    }
  };

  const calculateDiscountedPrice = (product: Product) => {
    if (product.discountPercentage > 0) {
      const discountedPrice =
        ((100.0 - product.discountPercentage) * product.price) / 100.0;
      return discountedPrice.toFixed(2);
    }
    return null;
  };

   function calculateProductRating(reviews: Review[]): number {
    if (reviews.length === 0) {
      return 0;
    }

    const totalRating = reviews.reduce((sum, review) => sum + review.rating, 0);
    return totalRating / reviews.length;
  }

  // To not fetch products twice on mounting the component
  const hasFetchedProducts = useRef(false);
  useEffect(() => {
    if (hasFetchedProducts.current) {
      return;
    }
    hasFetchedProducts.current = true;

    const fetchData = async () => {
      const products = await getProducts();

      // Fetch products
      setProducts(products);

      // Set wishlist status
      for (let i = 0; i < products.length; i++) {
        const product = products[i];
        if (product.inWishlist) {
          setWishlistStatus((prevStatus) =>
            new Map(prevStatus).set(product.id, true)
          );
        } else {
          setWishlistStatus((prevStatus) =>
            new Map(prevStatus).set(product.id, false)
          );
        }
      }

      // Load images
      const updatedProducts = await Promise.all(
        products.map(async (product) => {
          try {
            const dynamicImportedImage = await import(
              `../assets${product.imageLink}`
            );
            return { ...product, imageLink: dynamicImportedImage.default };
          } catch (error) {
            console.error("Error loading image:", error);
            return product; // Return original product if image loading fails
          }
        })
      );

      setProducts(updatedProducts);
    };

    fetchData();
  }, []);

  // Get current products
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = products.slice(
    indexOfFirstProduct,
    indexOfLastProduct
  );

  // Change page
  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);
  const handlePaginationClick = (pageNumber: number) => {
    setFadeAnimation("fade-out");
    setTimeout(() => {
      paginate(pageNumber);
      setFadeAnimation("fade-in");
    }, 300);
  };

  const resetEditButton = () => {
    setEditProduct(false);
  };

  const resetAddButton = () => {
    setAddProduct(false);
  };

  return (
    <div>
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
      {addProduct && (
        <>
          <EditAddProduct
            isEdit={false}
            adminToken={userToken}
            resetButton={resetAddButton}
          />
        </>
      )}
      {isAdmin && (
        <div className="addNewProduct">
          <button
            className="btn btn-primary"
            onClick={() => setAddProduct(true)}
            style={{ marginLeft: "20px", marginTop: "20px" }}
          >
            +Add product
          </button>
        </div>
      )}

      <div className={`products-list ${fadeAnimation}`}>
        {currentProducts.map((product) => (
          <div
            key={product.id}
            className="product-card"
            onClick={() => handleProductClick(product)}
          >
            <div
              style={{
                width: "90%",
                height: "50%",
                position: "relative",
                marginBottom: "8px",
                top: "0",
                left: "50%",
                transform: "translate(-50%, 0)",
              }}
            >
              {product.imageLink && (
                <img
                  src={product.imageLink}
                  alt={product.productName}
                  style={{ width: "100%", height: "auto" }}
                />
              )}
            </div>
            <div style={{ height: "50%", position: "relative" }}>
              <div style={{ marginBottom: "8px" }}>
                <StarRating
                  rating={Math.round(calculateProductRating(product.reviews))}
                />
                <br></br>
                <strong>({product.reviews.length})</strong>
              </div>
              <p>
                <strong>{product.productName}</strong>
              </p>
              {product.discountPercentage > 0 && (
                <div>
                  <div>
                    <p className="fs-5">
                      <strong>
                        Price: ${calculateDiscountedPrice(product)}
                      </strong>
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
                    handleEditProduct(product);
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
                      handleAddToCart(product.id);
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
                      handleAddToWishlist(product.id);
                      toggleWishlist(product.id);
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
        ))}
      </div>
      <footer>
        <Pagination
          itemsPerPage={productsPerPage}
          totalItems={products.length}
          paginate={handlePaginationClick}
          currentPage={currentPage}
        />
      </footer>
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
    </div>
  );
};

export default ProductsList;
