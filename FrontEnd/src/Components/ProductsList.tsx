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
import { Button } from "react-bootstrap";
import '@fortawesome/fontawesome-free/css/all.css';

interface Props {
  firstName: string;
  lastName: string;
  isAdmin: boolean;
  getProducts: () => Promise<Product[]>;
  getSortedProducts: (sortBy: any, sortOrder: any) => Promise<Product[]>;
  getFilteredProducts: (filter: FilterProductDto) => Promise<Product[]>;
  userToken: string;
}

const ProductsList = ({
  firstName,
  lastName,
  isAdmin,
  getProducts,
  getSortedProducts,
  getFilteredProducts,
  userToken
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
  const [sortParams, setSortParams] = useState({ sortBy: '', sortOrder: true });
  const [showSortModal, setShowSortModal] = useState(false);
  const [filter, setFilter] = useState<FilterProductDto>({
    productName: '',
    fromPrice: 0,
    toPrice: 20000,
    description: '',
    available: false,
    brand: '',
    fromDiscountPercentage: 0,
    toDiscountPercentage: 100,
    category: ''
  });
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

  const handleSortButtonClick = async() => {
    const products = await getSortedProducts(sortParams.sortBy, sortParams.sortOrder);
    setProducts(products);
  };

  const toggleSortModal = () => {
    setShowSortModal(prev => !prev);
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

  const handleFilterButtonClick = async() => {
    const products = await getFilteredProducts(filter);
    setProducts(products);
  };

  const handleInputChange = (e: any) => {
    const { name, value, type, checked } = e.target;
    setFilter(prevFilter => ({
      ...prevFilter,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  function calculateProductRating(reviews: Review[]): number {
    if (reviews.length === 0) {
      return 0;
    }

    const totalRating = reviews.reduce((sum, review) => sum + review.rating, 0);
    return totalRating / reviews.length;
  }

  function updateMinPrice() {
    const firstElem = document.getElementById("minPriceValue");
    const secondElem = document.getElementById("minPrice") as HTMLInputElement;
    if (firstElem != null && secondElem != null) {
      firstElem.textContent = secondElem.value;
    }
  }

  function updateMaxPrice() {
    const firstElem = document.getElementById("maxPriceValue");
    const secondElem = document.getElementById("maxPrice") as HTMLInputElement;
    if (firstElem != null && secondElem != null) {
      firstElem.textContent = secondElem.value;
    }
  }

  function updateMinDiscountPercentage() {
    const firstElem = document.getElementById("minDiscountPercentageValue");
    const secondElem = document.getElementById("minDiscountPercentage") as HTMLInputElement;
    if (firstElem != null && secondElem != null) {
      firstElem.textContent = secondElem.value;
    }
  }

  function updateMaxDiscountPercentage() {
    const firstElem = document.getElementById("maxDiscountPercentageValue");
    const secondElem = document.getElementById("maxDiscountPercentage") as HTMLInputElement;
    if (firstElem != null && secondElem != null) {
      firstElem.textContent = secondElem.value;
    }
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
      <button className="sort-button" onClick={toggleSortModal}>
        Sort
      </button>
      <button className="btn btn-primary btn-filter" type="button" data-bs-toggle="offcanvas"
                  data-bs-target="#offcanvasScrolling" aria-controls="offcanvasScrolling">
        <i className="fas fa-filter"></i>
      </button>
      <div className="offcanvas offcanvas-start" data-bs-scroll="true" data-bs-backdrop="false"
       tabIndex={-1} id="offcanvasScrolling" aria-labelledby="offcanvasScrollingLabel">
        <div className="offcanvas-header">
          <h5 className="offcanvas-title" id="offcanvasScrollingLabel">Filter Options</h5>
            <button type="button" className="btn-close text-reset" data-bs-dismiss="offcanvas"
                  aria-label="Close"></button>
        </div>
        <div className="offcanvas-body">
          <label>Product Name:</label>
          <input type="text" name="productName" className="form-control" value={filter.productName} onChange={handleInputChange} />

          <div className="mb-3">
            <label htmlFor="minPrice">Minimum Price: <span id="minPriceValue">0</span></label>
            <input type="range" className="form-range" min="0" max="20000" id="minPrice" onInput={updateMinPrice}
             name="fromPrice" value={filter.fromPrice} onChange={handleInputChange}/>
          </div>

          <div className="mb-3">
            <label htmlFor="maxPrice">Maximum Price: <span id="maxPriceValue">20000</span></label>
            <input type="range" className="form-range" min="0" max="20000" id="maxPrice" onInput={updateMaxPrice}
             name="toPrice" value={filter.toPrice} onChange={handleInputChange}/>
          </div>

          <label>Product Description:</label>
          <input type="text" name="description" className="form-control" value={filter.description} onChange={handleInputChange} />

          <div className="mb-3 mt-3">
            <label className="form-check-label" htmlFor="outOfStockCheckbox">
              Include Out of Stock
            </label>
            <input name="available" className="form-check-input" type="checkbox" id="outOfStockCheckbox" 
            checked={filter.available} style={{ marginLeft: "10px", position: "relative", bottom: "4px" }} onChange={handleInputChange} />
          </div>

          <label>Brand:</label>
          <input type="text" name="brand" className="form-control" value={filter.brand} onChange={handleInputChange} />

          <div className="mb-3">
            <label htmlFor="minDiscountPercentage">Minimum Discount Percentage: <span id="minDiscountPercentageValue">0</span></label>
            <input type="range" className="form-range" min="0" max="100" id="minDiscountPercentage" onInput={updateMinDiscountPercentage}
             name="fromDiscountPercentage" value={filter.fromDiscountPercentage} onChange={handleInputChange}/>
          </div>

          <div className="mb-3">
            <label htmlFor="maxDiscountPercentage">Maximum Discount Percentage: <span id="maxDiscountPercentageValue">100</span></label>
            <input type="range" className="form-range" min="0" max="100" id="maxDiscountPercentage" onInput={updateMaxDiscountPercentage}
             name="toDiscountPercentage" value={filter.toDiscountPercentage} onChange={handleInputChange}/>
          </div>

          <label>Category:</label>
          <input type="text" name="category" className="form-control" value={filter.category} onChange={handleInputChange} />

          <div className="d-flex justify-content-end mt-2">
            <Button variant="primary" onClick={() => handleFilterButtonClick()}>
              Filter
            </Button>
          </div>
        </div>
      </div>
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
      {showSortModal && (
        <div className="sort-modal">
          <div className="sort-modal-content">
            <div className="sort-option">
              <label className="sort-label">
                Sort by:
                <select className="sort-select" onChange={(e) => setSortParams(prev => ({ ...prev, sortBy: e.target.value }))} value={sortParams.sortBy}>
                  <option value="">Select Criteria</option>
                  <option value="productName">Name</option>
                  <option value="price">Price</option>
                  <option value="averageRating">Rating</option>
                  <option value="numberOfReviews">Reviews</option>
                  <option value="postedDate">Date Added</option>
                  <option value="productCountAvailable">Remaining in Stock</option>
                  <option value="productSoldCount">Sold Count</option>
                  <option value="brand">Brand</option>
                </select>
              </label>
            </div>
            <div className="sort-order">
              <label className="sort-label">
                <input type="radio" name="sortOrder" checked={sortParams.sortOrder} onChange={() => setSortParams(prev => ({ ...prev, sortOrder: true }))} />
                Ascending
              </label>
              <label className="sort-label">
                <input type="radio" name="sortOrder" checked={!sortParams.sortOrder} onChange={() => setSortParams(prev => ({ ...prev, sortOrder: false }))} />
                Descending
              </label>
            </div>
            <div className="modal-buttons">
              <button className="apply-button" onClick={() => { handleSortButtonClick(); toggleSortModal(); }}>Apply</button>
              <button className="cancel-button" onClick={toggleSortModal}>Cancel</button>
            </div>
          </div>
        </div>      
      )}
    </div>
  );
};

export default ProductsList;
