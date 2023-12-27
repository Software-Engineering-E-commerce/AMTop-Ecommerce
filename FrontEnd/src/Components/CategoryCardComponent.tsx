import React from "react";
import "../Pages/Home.css";

interface CategoryCardComponentProps {
  categoryName: string;
  imageLink: string;
  isAdmin: boolean;
}

// Name, link
const CategoryCardComponent = ({
  categoryName,
  imageLink,
  isAdmin,
}: CategoryCardComponentProps) => {
  return (
    <>
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
                style={{ width:"100%"}}
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
