import React from "react";
import StarRating from "./StarRating";

export interface Review {
  id: number;
  customerName: string;
  rating: number; //from 0 to 5
  reviewText: string;
  date: string;
}

interface CustomerReviewsProps {
  reviews: Review[];
  token: string;
  isAdmin: boolean;
}

const CustomerReviews: React.FC<CustomerReviewsProps> = ({
  reviews,
  token,
  isAdmin,
}) => {
  console.log("isAdmin in reviews:" , isAdmin)
  return (
    <div>
      {reviews.length > 0 && <h3 className="mt-4 mb-3">Customer Reviews</h3>}
      {reviews.map((review) => (
        <div key={review.id} className="card mb-3">
          <div className="card-body">
            <h5 className="card-title">{review.customerName}</h5>
            <div className="d-flex align-items-center">
              <StarRating rating={Math.round(review.rating)} />
            </div>
            <p className="card-text">{review.reviewText}</p>
            <p className="card-text">
              <small className="text-muted">Date: {review.date}</small>
            </p>
          </div>
        </div>
      ))}
      {!isAdmin && <button className="btn btn-primary">Add Review</button>}
    </div>
  );
};

export default CustomerReviews;
