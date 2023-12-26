import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar as filledStar } from "@fortawesome/free-solid-svg-icons";
import { faStar as emptyStar } from "@fortawesome/free-regular-svg-icons";

interface StarRatingProps {
  rating: number;
  setRating?: (rating: number) => void;
}
const StarRating: React.FC<StarRatingProps> = ({ rating, setRating }) => {
  const [hoverRating, setHoverRating] = useState(0);

  const handleMouseOver = (newHoverRating: number) => {
    if (setRating) {
      setHoverRating(newHoverRating);
    }
  };

  const handleMouseLeave = () => {
    if (setRating) {
      setHoverRating(0);
    }
  };

  const handleClick = (newRating: number) => {
    if (setRating) {
      setRating(newRating);
    }
  };

  return (
    <div className="d-inline-flex align-items-center">
      {[1, 2, 3, 4, 5].map((star) => (
        <FontAwesomeIcon
          key={star}
          icon={star <= (hoverRating || rating) ? filledStar : emptyStar}
          onMouseOver={() => handleMouseOver(star)}
          onMouseLeave={handleMouseLeave}
          onClick={() => handleClick(star)}
          style={{
            color: star <= (hoverRating || rating) ? "#FFD700" : "#CED4DA",
            fontSize: "1.5rem",
            cursor: setRating ? "pointer" : "default",
          }}
        />
      ))}
    </div>
  );
};

export default StarRating;
