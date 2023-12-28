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
import { Prev } from "react-bootstrap/esm/PageItem";
import Categories from "./Categories";
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

  const getHomeInfo = async () => {
    try {
      const response = await axios.get("http://localhost:9080/Home/startup", {
        headers: {
          Authorization: `Bearer ${userToken}`,
          "Content-Type": "application/json",
        },
      });
      const tmpResponse: HomeInfo = response.data;
      return tmpResponse;
    } catch (error) {
      return null;
    }
  };

  const fetchData = async () => {
    const homeInformation = await getHomeInfo();
    setHomeInfo(homeInformation);
    // Load images
    if (homeInformation?.categoryList != undefined) {
      const updatedHomeInfo = await Promise.all(
        homeInformation.categoryList.map(async (cat) => {
          try {
            const dynamicImportedImage = await import(
              `../assets${cat.imageLink}`
            );
            return { ...cat, imageLink: dynamicImportedImage.default };
          } catch (error) {
            console.error("Error loading image:", error);
            return cat; // Return original product if image loading fails
          }
        })
      );
      setHomeInfo((prev) => ({
        ...prev,
        categoryList: updatedHomeInfo,
        firstName: prev?.firstName ?? "",
        lastName: prev?.lastName ?? "",
        admin: prev?.admin ?? false,
      }));
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
    infinite: false,
    speed: 500,
    slidesToShow: 4,
    slidesToScroll: 1,
    autoplay: true, // Enable autoplay
    autoplaySpeed: 3000, // Set the autoplay speed in milliseconds
    initialSlide: 0,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
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
                  <div key={category.categoryName} className="category-slide">
                    <CategoryCardComponent
                      categoryName={category.categoryName}
                      userToken={userToken}
                      imageLink={category.imageLink}
                      isAdmin={homeInfo?.admin}
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
