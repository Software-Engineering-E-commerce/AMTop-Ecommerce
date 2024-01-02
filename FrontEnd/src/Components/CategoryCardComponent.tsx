import React, { useState } from "react";
import "../Pages/Home.css";
import EditAddCategory, { EditedCategory } from "./EditAddCategory";
import { useNavigate } from "react-router-dom";
import axios from "axios";

interface CategoryCardComponentProps {
  firstName: string;
  lastName: string;
  categoryName: string;
  imageLink: string;
  userToken: string;
  isAdmin: boolean;
}

// Name, link
const CategoryCardComponent = ({
  firstName,
  lastName,
  categoryName,
  imageLink,
  userToken,
  isAdmin,
}: CategoryCardComponentProps) => {
  const [isEdit, setIsEdit] = useState(false);
  const [editedCategory, setEditedCategory] = useState<EditedCategory>();
  const navigate = useNavigate();


  const resetIsEdit = () => {
    setIsEdit(false);
  };

  const editTheCategory = () => {
    setEditedCategory({ categoryName: categoryName });
    setIsEdit(true);
  };

  const getSearchedProducts = async () => {
    console.log("In get searched products");
    let url = `http://localhost:9080/api/search/${categoryName}`;
    try {
      const response = await axios(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
      });
      console.log(response.data);
      const products: Product[] = response.data;
      return products;
    } catch (error) {
      console.log("Error:", error);
      const products: Product[] = [];
      return products;
    }
  }

  const handleCategoryClick = async () => {
    const returnedProducts = await getSearchedProducts();
    navigate("/catalog", {
      state: {
        userToken: userToken,
        isAdmin: isAdmin,
        firstName: firstName,
        lastName: lastName,
        passedProducts: returnedProducts
      },
    });
  }

  return (
    <>
      {isEdit && (
        <>
          <EditAddCategory
            resetButton={resetIsEdit}
            category={editedCategory}
            isEdit={true}
            adminToken={userToken}
          />
        </>
      )}
      <div className="subSlider" >
        <div className="subSliderWrap" style={{minHeight:"300px"}}>
          <div className="categoryImage">
            <img
              style={{ maxHeight: "10rem" }}
              src={imageLink}
              alt={categoryName}
              onClick={handleCategoryClick}
            />
          </div>
          <div className="categoryName">
            <h5 style={{ marginTop: "30px" }}>{categoryName}</h5>
          </div>
          {isAdmin && (
            <>
              <button
                type="button"
                className="btn btn-primary"
                style={{ width: "100%" }}
                onClick={editTheCategory}
              >
                Edit
              </button>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default CategoryCardComponent;
