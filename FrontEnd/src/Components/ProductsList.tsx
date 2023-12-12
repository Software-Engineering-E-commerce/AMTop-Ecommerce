import { useState, useEffect, useRef } from 'react';
import Pagination from './Pagination';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCartShopping, faHeart, faTrash } from '@fortawesome/free-solid-svg-icons';
import './ProductsList.css';
import StarRating from './StarRating';

interface Props {
  isAdmin: boolean,
  getProducts: () => Promise<Product[]>,
  addProduct: () => void,
  removeProduct: () => void
  updateProduct: () => void
}

const ProductsList = ({
  isAdmin,
  getProducts,
  addProduct,
  removeProduct,
  updateProduct
}: Props) => {
  const nullCustomer: Customer = {
    id: 0,
    firstName: '',
    lastName: ''
  }
  const nullCategory: Category = {
    categoryName: '',
    imageLink: ''
  }
  const [products, setProducts] = useState<Product[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [productsPerPage] = useState(15);
  const [fadeAnimation, setFadeAnimation] = useState('');
  const [wishlistStatus, setWishlistStatus] = useState(new Map());

  const handleProductClick = (product: Product) => {
    // Route to the product page
  };

  const handleRemoveProduct = (productId: number) => {
    // Your logic for removing a product can go here
  };

  const handleAddToWishlist = (productId: number) => {
    // Handle add to wishlist
  };

  const handleAddToCart = (productId: number) => {
    // Handle add to cart
  };

  const toggleWishlist = (productId: number) => {
    setWishlistStatus(prevStatus => new Map(prevStatus).set(productId, !prevStatus.get(productId)));
    for (let i = 0; i < currentProducts.length; i++) {
      const product = currentProducts[i];
      if (product.id == productId){
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
    const fetchProducts = async () => {
      getProducts().then(products => {
        setProducts(products)
      });
    };
    fetchProducts();
    const review1: Review = {
      customer: nullCustomer,
      rating: 3,
      comment: "",
      date: new Date
    }
    const review2: Review = {
      customer: nullCustomer,
      rating: 5,
      comment: "",
      date: new Date
    }
    const product: Product = {
      id: 1,
      price: 200,
      productName: "sampleProduct",
      inWishlist: true,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://th.bing.com/th/id/OIP.6UXDZLxvYowXUEFyY6XxKQHaHa?rs=1&pid=ImgDetMain',
      discountPercentage: 10,
      category: nullCategory,
      reviews: [review1, review2]
    }
    const product2: Product = {
      id: 2,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://th.bing.com/th/id/R.c3cd4911a07125516be51b24a64e8be5?rik=N2IdHBDdgzHZyw&riu=http%3a%2f%2fi.kinja-img.com%2fgawker-media%2fimage%2fupload%2ft_original%2fv5680o7hxelioej5wmwi.jpg&ehk=9lj%2fEdCXM7Cv6EQOBVyDnUpd04dxDFhtGAbaTvFkM4Q%3d&risl=&pid=ImgRaw&r=0',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product3: Product = {
      id: 3,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://th.bing.com/th/id/OIP.okPHK-lOk_E5nzOZsGx2dwHaFI?w=6200&h=4300&rs=1&pid=ImgDetMain',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product4: Product = {
      id: 4,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://th.bing.com/th/id/R.955ed18c0c597ac5c7e42a54c25d930c?rik=8meqYWp%2fRiAmDw&riu=http%3a%2f%2fstore.hp.com%2fUKStore%2fHtml%2fMerch%2fImages%2fc05475056_1750x1285.jpg&ehk=hFHW0bsoA4Vrq9NqTnBn9ZROJe9jXLuXOG2U%2bb2O7Wk%3d&risl=&pid=ImgRaw&r=0',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product5: Product = {
      id: 5,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://3.bp.blogspot.com/-ufxpXjVLGTc/UrQUHAbW1oI/AAAAAAAAC6A/H-F4XqKRgdM/s1600/81jpEBBBxgL._SL1500_.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product6: Product = {
      id: 6,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product7: Product = {
      id: 7,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product8: Product = {
      id: 8,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product9: Product = {
      id: 9,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product10: Product = {
      id: 10,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product11: Product = {
      id: 11,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product12: Product = {
      id: 12,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product13: Product = {
      id: 13,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product14: Product = {
      id: 14,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product15: Product = {
      id: 15,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const product16: Product = {
      id: 16,
      price: 30,
      productName: "sampleProduct2",
      inWishlist: false,
      postedDate: new Date,
      description: '',
      productCountAvailable: 0,
      productSoldCount: 0,
      brand: '',
      imageLink: 'https://www.doncaprio.com/wp-content/uploads/2015/03/laptop-2.jpg',
      discountPercentage: 0,
      category: nullCategory,
      reviews: []
    }
    const curr: Product[] = [product, product2, product3, product4, product5, product6, product7, product8, product9, product10, product11, product12, product13, product14, product15, product16];
    setProducts(curr);
    for (let i = 0; i < curr.length; i++) {
      const product = curr[i];
      if (product.inWishlist){
        setWishlistStatus(prevStatus => new Map(prevStatus).set(product.id, true));
      } else setWishlistStatus(prevStatus => new Map(prevStatus).set(product.id, false));
    }
  }, []);

  // Get current products
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = products.slice(indexOfFirstProduct, indexOfLastProduct);

  // Change page
  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);
  const handlePaginationClick = (pageNumber: number) => {
    setFadeAnimation('fade-out');
    setTimeout(() => {
      paginate(pageNumber);
      setFadeAnimation('fade-in');
    }, 300);
  };

  return (
    <div>
      <div className={`products-list ${fadeAnimation}`}>
        {currentProducts.map(product => (
          <div key={product.id} className='product-card' onClick={() => handleProductClick(product)}>
            <div
            style={{ width: "90%", height: "50%", position: "relative", marginBottom: "8px",
                    top: "0", left: "50%", transform: "translate(-50%, 0)"}}>
              <img
                src={product.imageLink}
                alt={product.productName}
                style={{ width: "100%", height: "auto" }}
              />
            </div>
            <div
            style={{ height: "50%", position: "relative" }}>
              <div style={{ marginBottom: "8px" }}>
                <StarRating rating={Math.round(calculateProductRating(product.reviews))} /><br></br>
                <strong>({product.reviews.length})</strong>
              </div>
              <p><strong>{product.productName}</strong></p>
              {product.discountPercentage > 0 && (
                <div>
                  <div>
                    <p className="fs-5">
                      <strong>Price: ${calculateDiscountedPrice(product)}</strong>
                        <del className="text-danger mb-0 fs-6"
                        style={{ position: "relative", left: "8px" }}>
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
                <button className="product-delete-button" onClick={(e) => { e.stopPropagation(); handleRemoveProduct(product.id); }}>
                  <FontAwesomeIcon icon={faTrash} />
                </button>
              )}
              <div className='buttons-container'>
                {!isAdmin && (
                  <button className="product-cart-button" onClick={(e) => { e.stopPropagation(); handleAddToCart(product.id); }}>
                    <FontAwesomeIcon icon={faCartShopping} />
                    <span className="button-text-go-left">Add to Cart</span>
                  </button>
                )}
                {!isAdmin && (
                  <button 
                  className={`product-wishlist-button ${wishlistStatus.get(product.id) ? 'in-wishlist' : ''}`}
                  onClick={(e) => { e.stopPropagation(); handleAddToWishlist(product.id); toggleWishlist(product.id); }}>
                    <FontAwesomeIcon icon={faHeart} />
                    {product.inWishlist && 
                      <span className="button-text-go-right">Forget</span>
                    }
                    {!product.inWishlist && 
                      <span className="button-text-go-right">Wish!</span>
                    }
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
    </div>
  );
};

export default ProductsList;
