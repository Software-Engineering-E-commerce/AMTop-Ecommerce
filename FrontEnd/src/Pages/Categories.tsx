import React, { useEffect, useRef, useState } from "react";
import { useLocation } from "react-router";
import Navbar from "../Components/HomeNavbar";
import Slider from "react-slick";
import CategoryCardComponent from "../Components/CategoryCardComponent";
import axios from "axios";
import EditAddCategory from "../Components/EditAddCategory";

interface categoryDTO {
  name: string;
  imageUrl: string;
}

const Categories = () => {
  const location = useLocation();
  var { userToken, isAdmin, firstName, lastName } = location.state || {};

  const [categories, setCategories] = useState<categoryDTO[]>([]);
  const [addCategory, setAddCategory] = useState(false);

  const isMounted = useRef(true);

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

  const getCategoryDtoList = async () => {
    try {
      const response = await axios.get(
        "http://localhost:9080/api/categoryDetails/getAllCategories",
        {
          headers: {
            Authorization: `Bearer ${userToken}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log(
        "🚀 ~ file: Categories.tsx:69 ~ getCategoryDtoList ~ response:",
        response
      );

      const categoryDtoList: categoryDTO[] = response.data;
      return categoryDtoList;
    } catch (error) {
      console.log("Error:", error);
      const categoryDtoList: categoryDTO[] = [];
      return categoryDtoList;
    }
  };

  const fetchData = async () => {
    const cats = await getCategoryDtoList();
    setCategories(cats);
    // Load images
    const updatedCategories = await Promise.all(
      cats.map(async (cat) => {
        try {
          const dynamicImportedImage = await import(`../assets${cat.imageUrl}`);
          return { ...cat, imageUrl: dynamicImportedImage.default };
        } catch (error) {
          console.error("Error loading image:", error);
          return cat; // Return original product if image loading fails
        }
      })
    );
    setCategories(updatedCategories);
  };

  useEffect(() => {
    if (isMounted.current) {
      // fetchData();
      isMounted.current = false;
    }
  }, []);

  const addNewCategory = () => {
    setAddCategory(true);
  };

  const resetAddButton = () => {
    setAddCategory(false);
  };

  return (
    <>
      <Navbar
        firstName={firstName}
        lastName={lastName}
        isAdmin={isAdmin}
        token={userToken}
        isCategories={true}
      />

      {addCategory && (
        <EditAddCategory
          resetButton={resetAddButton}
          adminToken={userToken}
          isEdit={false}
        />
      )}

      <div
        className="container categories-page-container"
        style={{ marginTop: "50px" }}
      >
        {isAdmin && (
          <>
            <button
              type="button"
              className="btn btn-primary"
              style={{ marginBottom: "20px" }}
              onClick={addNewCategory}
            >
              Add Category
            </button>
          </>
        )}
        <Slider {...SliderSettings}>
          {categories.map((category, index) => (
            <div key={index} className="category-slide">
              <CategoryCardComponent
                categoryName={category.name}
                imageLink={category.imageUrl}
                userToken={userToken}
                isAdmin={isAdmin}
              />
            </div>
          ))}
        </Slider>
      </div>
    </>
  );
};

export default Categories;
