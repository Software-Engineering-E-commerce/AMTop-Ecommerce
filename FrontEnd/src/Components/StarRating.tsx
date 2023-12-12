import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar, faStarHalf } from "@fortawesome/free-solid-svg-icons";

interface StarRatingProps {
  rating: number;
}

const StarRating: React.FC<StarRatingProps> = ({ rating }) => {
  const fullStars = Math.floor(rating);
  const hasHalfStar = rating - fullStars >= 0.5;

  return (
    <div className="d-inline-flex align-items-center">
      {Array.from({ length: fullStars }, (_, index) => (
        <FontAwesomeIcon
          key={index}
          icon={faStar}
          style={{ color: "#FFD700", fontSize: "1.5rem" }}
        />
      ))}
      {hasHalfStar && (
        <FontAwesomeIcon
          icon={faStarHalf}
          style={{ color: "#FFD700", fontSize: "1.5rem" }}
        />
      )}
      {Array.from({ length: 5 - Math.ceil(rating) }, (_, index) => (
        <FontAwesomeIcon
          key={index + fullStars}
          icon={faStar}
          style={{ color: "#CED4DA", fontSize: "1.5rem" }}
        />
      ))}
    </div>
  );
};

export default StarRating;