import React, { useState } from "react";
import "../Pages/Home.css";
import EditAddCategory, { EditedCategory } from "./EditAddCategory";

interface CategoryCardComponentProps {
  categoryName: string;
  imageLink: string;
  userToken: string;
  isAdmin: boolean;
}

// Name, link
const CategoryCardComponent = ({
  categoryName,
  imageLink,
  userToken,
  isAdmin,
}: CategoryCardComponentProps) => {
  const [isEdit, setIsEdit] = useState(false);
  const [editedCategory, setEditedCategory] = useState<EditedCategory>();

  const resetIsEdit = () => {
    setIsEdit(false);
  };

  const editTheCategory = () => {
    setEditedCategory({ categoryName: categoryName });
    setIsEdit(true);
  };

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

      
      <div className="subSlider">
        <div className="subSliderWrap">
          <div className="categoryImage">
            <img
              style={{ maxHeight: "10rem" }}
              src={imageLink}
              alt={categoryName}
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
