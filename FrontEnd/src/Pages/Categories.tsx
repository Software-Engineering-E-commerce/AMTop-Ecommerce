import React, { useEffect, useRef, useState } from "react";
import { useLocation } from "react-router";
import Navbar from "../Components/HomeNavbar";
import Slider from "react-slick";
import CategoryCardComponent from "../Components/CategoryCardComponent";

interface CategoryDTO {
  imageLink: string;
  categoryName: string;
}

const Categories = () => {
  const location = useLocation();
  var { userToken, isAdmin, firstName, lastName } = location.state || {};

  const [categories, setCategories] = useState<CategoryDTO[]>([]);

  const isMounted = useRef(true);

  console.log(
    "🚀 ~ file: Categories.tsx:7 ~ Categories ~  userToken, isAdmin, firstName, lastName :",
    userToken,
    isAdmin,
    firstName,
    lastName
  );

  let categoris: CategoryDTO[] = [
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

  useEffect(() => {
    if (isMounted.current) {
      setCategories(categoris);
      isMounted.current = false;
    }
  }, []);

  return (
    <>
      <Navbar
        firstName={firstName}
        lastName={lastName}
        isAdmin={isAdmin}
        token={userToken}
        isCategories={true}
      />
      <div
        className="container categories-page-container"
        style={{ marginTop: "50px" }}
      >
        {true && (
          <>
            <button type="button" className="btn btn-primary" style={{marginBottom:"20px"}}>
              Add Category
            </button>
          </>
        )}
        <Slider {...SliderSettings}>
          {categories.map((category, index) => (
            <div key={index} className="category-slide">
              <CategoryCardComponent
                categoryName={category.categoryName}
                imageLink={category.imageLink}
                isAdmin={true}
              />
            </div>
          ))}
        </Slider>
      </div>
    </>
  );
};

export default Categories;
