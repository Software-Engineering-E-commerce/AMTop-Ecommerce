import Navbar from "../Components/HomeNavbar";
import { useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import MyCarousel from "../Components/MyCarousel";
import "./Home.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowUp,
  faChevronLeft,
  faChevronRight,
} from "@fortawesome/free-solid-svg-icons";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import HomeProductListing from "../Components/HomeProductListing";
import HomeFooter from "../Components/HomeFooter";

interface Categorie {
  categoryName: string;
  imageLink: string;
}

interface HomeInfo {
  firstName: string;
  lastName: string;
  admin: boolean;
  categoryList: Categorie[];
}

// const yourSliderSettings = {
//   dots: true,
//   infinite: true,
//   speed: 500,
//   slidesToShow: 3, // Adjust the number of slides shown
//   slidesToScroll: 1,
//   // ... (other slick settings)
// };

const Home = () => {
  const location = useLocation();
  var { userToken, from } = location.state || {};
  const [homeInfo, setHomeInfo] = useState<HomeInfo | null>(null);
  const isMounted = useRef<boolean>(true);

  const fetchData = async () => {
    try {
      const response = await axios.get("http://localhost:9080/Home/startup", {
        headers: {
          Authorization: `Bearer ${userToken}`,
          "Content-Type": "application/json",
        },
      });
      console.log("response");
      console.log(response.data);
      setHomeInfo(response.data);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
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
    // Check if the request has not been made
    if (isMounted.current) {
      fetchData();
      isMounted.current = false;
    }
  }, []);

  const [showScrollButton, setShowScrollButton] = useState(false);

  const handleScroll = () => {
    // Set showScrollButton based on the scroll position
    const scrollTop = window.scrollY || document.documentElement.scrollTop;
    setShowScrollButton(scrollTop > window.innerHeight);
  };

  useEffect(() => {
    // Check if the request has not been made
    if (isMounted.current) {
      fetchData();
      isMounted.current = false;
    }

    // Add scroll event listener when the component mounts
    window.addEventListener("scroll", handleScroll);

    // Remove scroll event listener when the component unmounts
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  let categories: Categorie[] = [
    {
      categoryName: "Iphone",
      imageLink:
        "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51Pzh68ERNL._AC_AA180_.jpg",
    },
    {
      categoryName: "Iphone 13",
      imageLink:
        "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/81yiZag3xhL._AC_AA180_.jpg",
    },
    {
      categoryName: "Iphone",
      imageLink:
        "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51Pzh68ERNL._AC_AA180_.jpg",
    },
    {
      categoryName: "Iphone",
      imageLink:
        "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51Pzh68ERNL._AC_AA180_.jpg",
    },
    {
      categoryName: "Iphone",
      imageLink:
        "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51Pzh68ERNL._AC_AA180_.jpg",
    },
    {
      categoryName: "Iphone",
      imageLink:
        "https://f.nooncdn.com/p/v1657621583/N53329645A_1.jpg?format=avif&width=240",
    },
    {
      categoryName: "Smart watch",
      imageLink:
        "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/71Zmcw24IML._AC_AA180_.jpg",
    },
    {
      categoryName: "iphone",
      imageLink:
        "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51Pzh68ERNL._AC_AA180_.jpg",
    },
  ];

  let cus: Customer = {
    firstName: "Adel",
    lastName: "Mahmoud",
    id: 1,
  };

  let reviews: Review[] = [
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
    { rating: 3, customer: cus, comment: "abdc", date: new Date() },
  ];

  let product: HomeProductDTO = {
    id: 5,
    productName: "string",
    price: 18.75,
    imageLink:
      "https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/71Zmcw24IML._AC_AA180_.jpg",
    discountPercentage: 50,
    reviews: reviews,
    inWishlist: false,
    description: "Here's awsome product",
    productCountAvailable: 60,
    categoryName: "Laptops",
    brand: "Dell",
  };

  let products: HomeProductDTO[] = [
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
    { ...product },
  ];

  var SliderSettings = {
    dots: true,
    infinite: false,
    speed: 500,
    slidesToShow: 4,
    slidesToScroll: 4,
    initialSlide: 0,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
          infinite: true,
          dots: true,
        },
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2,
          initialSlide: 2,
        },
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
        },
      },
    ],
  };
  return (
    <>
      <div className="home-navbar-container" style={{ height: "100vh" }}>
        <Navbar
          firstName={homeInfo?.firstName || ""}
          lastName={homeInfo?.lastName || ""}
          isAdmin={homeInfo?.admin || false}
          token={userToken}
          isHome={true}
        />
        <MyCarousel />
      </div>

      <div className="container whatAfterNavbar" style={{ height: "100vh" }}>
        <div className="col-12 homeCategories">
          <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
            Trending Categories
          </h2>
          <Slider {...SliderSettings}>
            {categories.map((category, index) => (
              <div key={index} className="category-slide">
                <div className="subSlider">
                  <div className="subSliderWrap">
                    <div className="categoryImage">
                      <img
                        style={{ maxHeight: "10rem" }}
                        src={category.imageLink}
                        alt={category.categoryName}
                      />
                    </div>
                    <div className="categoryName">
                      <h5 style={{ marginTop: "30px" }}>
                        {category.categoryName}
                      </h5>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </Slider>
        </div>
        <div
          className="col-12 homeLatestProducts"
          style={{ marginTop: "100px" }}
        >
          <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
            Latest Products
          </h2>
          <Slider {...SliderSettings}>
            {products.map((product, index) => (
              <div className="product-slide">
                <HomeProductListing
                  firstName={"Adel"}
                  lastName={"Mahmoud"}
                  isAdmin={false}
                  userToken={"ewfwef"}
                  product={product}
                />
              </div>
            ))}
          </Slider>
        </div>

        <div
          className="col-12 homeMostPopularProducts"
          style={{ marginTop: "100px" }}
        >
          <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
            Most Popular Products
          </h2>
          <Slider {...SliderSettings}>
            {products.map((product, index) => (
              <div className="product-slide">
                <HomeProductListing
                  firstName={"Adel"}
                  lastName={"Mahmoud"}
                  isAdmin={false}
                  userToken={"ewfwef"}
                  product={product}
                />
              </div>
            ))}
          </Slider>
        </div>

        <div className="col-12 homeMostSold" style={{ marginTop: "100px" }}>
          <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
            Most Sold Products
          </h2>
          <Slider {...SliderSettings}>
            {products.map((product, index) => (
              <div className="product-slide">
                <HomeProductListing
                  firstName={"Adel"}
                  lastName={"Mahmoud"}
                  isAdmin={false}
                  userToken={"ewfwef"}
                  product={product}
                />
              </div>
            ))}
          </Slider>
        </div>

        {showScrollButton && (
          <button className="scroll-to-top-btn" onClick={scrollToTop}>
            <FontAwesomeIcon icon={faArrowUp} />
          </button>
        )}
        <HomeFooter />
      </div>
    </>
  );
};

const PrevArrow: React.FC<{ onClick?: () => void }> = ({ onClick }) => {
  return (
    <div className="slider-arrow" onClick={onClick}>
      <FontAwesomeIcon icon={faChevronLeft} />
    </div>
  );
};

const NextArrow: React.FC<{ onClick?: () => void }> = ({ onClick }) => {
  return (
    <div className="slider-arrow" onClick={onClick}>
      <FontAwesomeIcon icon={faChevronRight} />
    </div>
  );
};
export default Home;
