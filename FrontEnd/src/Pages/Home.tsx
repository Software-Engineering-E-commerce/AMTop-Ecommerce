import Navbar from "../Components/HomeNavbar";
import { useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import MyCarousel from "../Components/MyCarousel";
import "./Home.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowUp,
  faHeadphones,
  faKeyboard,
  faLaptop,
  faMobileScreenButton,
} from "@fortawesome/free-solid-svg-icons";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import HomeProductListing from "../Components/HomeProductListing";
import HomeFooter from "../Components/HomeFooter";
import CategoryCardComponent from "../Components/CategoryCardComponent";

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

const Home = () => {
  const location = useLocation();
  var { userToken, from } = location.state || {};
  const [homeInfo, setHomeInfo] = useState<HomeInfo | null>(null);

  // TODO send the request to get these products

  // The products arrays that will be displayed
  const [latestProducts, setLatestProducts] = useState<HomeProductDTO[]>([]);
  const [mostPopularProducts, setMostPopularProducts] = useState<
    HomeProductDTO[]
  >([]);
  const [mostSoldProducts, setMostSoldProducts] = useState<HomeProductDTO[]>(
    []
  );

  // A useState to indicate that the scroll button should appear (after one vh) or not
  const [showScrollButton, setShowScrollButton] = useState(false);

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

  const handleScroll = () => {
    // Set showScrollButton based on the scroll position
    const scrollTop = window.scrollY || document.documentElement.scrollTop;
    setShowScrollButton(scrollTop > window.innerHeight);
  };

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  useEffect(() => {
    // Check if the request has not been made
    if (isMounted.current) {
      setHomeInfo((prev) => ({
        ...prev,
        categoryList: [],
        firstName: prev?.firstName || "",
        lastName: prev?.lastName || "",
        admin: prev?.admin || false,
      }));
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

  var SliderSettings = {
    dots: true,
    infinite: true, // Enable infinite loop
    speed: 500,
    slidesToShow: 4,
    slidesToScroll: 2,
    autoplay: true, // Enable autoplay
    autoplaySpeed: 3000, // Set the autoplay speed in milliseconds
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
    <div style={{ overflowX: "hidden" }}>
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

      <div className="container whatAfterNavbar">
        {homeInfo?.categoryList.length != 0 && (
          <>
            <div className="col-12 homeCategories">
              <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
                Trending Categories
              </h2>
              <Slider {...SliderSettings}>
                {homeInfo?.categoryList.map((category, index) => (
                  <div key={index} className="category-slide">
                    <CategoryCardComponent
                      categoryName={category.categoryName}
                      imageLink={category.imageLink}
                      isAdmin={homeInfo.admin}
                    />
                  </div>
                ))}
              </Slider>
            </div>
          </>
        )}

        {latestProducts.length != 0 && (
          <>
            <div
              className="col-12 homeLatestProducts"
              style={{ marginTop: "100px" }}
            >
              <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
                Latest Products
              </h2>
              <Slider {...SliderSettings}>
                {latestProducts.map((product, index) => (
                  <div className="product-slide">
                    <HomeProductListing
                      firstName={homeInfo?.firstName || ""}
                      lastName={homeInfo?.lastName || ""}
                      isAdmin={homeInfo?.admin || false}
                      userToken={userToken}
                      product={product}
                    />
                  </div>
                ))}
              </Slider>
            </div>
          </>
        )}
      </div>

      <div className="design">
        <div className="image">
          <img
            src="src\assets\mobile.png"
            alt="Mobile"
            style={{ width: "30rem" }}
          />
        </div>
        <div className="text">
          <h2>Experience the Excellence of Our Electronic Devices..</h2>
          <ul>
            <li>
              <FontAwesomeIcon
                icon={faLaptop}
                style={{ marginRight: "10px" }}
              />
              Futuristic Designs for Your Lifestyle
            </li>
            <li>
              <FontAwesomeIcon
                icon={faMobileScreenButton}
                style={{ marginRight: "10px" }}
              />
              Seamless and Contemporary Aesthetics
            </li>
            <li>
              <FontAwesomeIcon
                icon={faKeyboard}
                style={{ marginRight: "10px" }}
              />
              High Performance and Quality
            </li>
            <li>
              <FontAwesomeIcon
                icon={faHeadphones}
                style={{ marginRight: "10px" }}
              />
              Seamless Experience Across All Devices
            </li>
          </ul>
        </div>
      </div>

      <div className="container whatAfterNavbar">
        {mostPopularProducts.length != 0 && (
          <>
            <div
              className="col-12 homeMostPopularProducts"
              style={{ marginTop: "100px" }}
            >
              <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
                Most Popular Products
              </h2>
              <Slider {...SliderSettings}>
                {mostPopularProducts.map((product, index) => (
                  <div className="product-slide">
                    <HomeProductListing
                      firstName={homeInfo?.firstName || ""}
                      lastName={homeInfo?.lastName || ""}
                      isAdmin={homeInfo?.admin || false}
                      userToken={userToken}
                      product={product}
                    />
                  </div>
                ))}
              </Slider>
            </div>
          </>
        )}

        {mostSoldProducts.length != 0 && (
          <>
            <div className="col-12 homeMostSold" style={{ marginTop: "100px" }}>
              <h2 style={{ marginBottom: "50px", fontWeight: "500" }}>
                Most Sold Products
              </h2>
              <Slider {...SliderSettings}>
                {mostSoldProducts.map((product, index) => (
                  <div className="product-slide">
                    <HomeProductListing
                      firstName={homeInfo?.firstName || ""}
                      lastName={homeInfo?.lastName || ""}
                      isAdmin={homeInfo?.admin || false}
                      userToken={userToken}
                      product={product}
                    />
                  </div>
                ))}
              </Slider>
            </div>
          </>
        )}
        {showScrollButton && (
          <button className="scroll-to-top-btn" onClick={scrollToTop}>
            <FontAwesomeIcon icon={faArrowUp} />
          </button>
        )}
      </div>
      <HomeFooter />
    </div>
  );
};

export default Home;