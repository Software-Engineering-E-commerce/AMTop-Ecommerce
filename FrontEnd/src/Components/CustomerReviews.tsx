import React, { useState } from "react";
import StarRating from "./StarRating";
import axios from 'axios';

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
  productID: number;
  firstName: string;
  lastName: string;
}
 
const CustomerReviews: React.FC<CustomerReviewsProps> = ({
  reviews: initialReviews,
  token,
  isAdmin,
  productID,
  firstName,
  lastName,
}) => {

  const [showReviewForm, setShowReviewForm] = useState(false);
  const [newReviewText, setNewReviewText] = useState("");
  const [newReviewRating, setNewReviewRating] = useState(0);
  const [errorMessage, setErrorMessage] = useState("");
  const [reviews, setReviews] = useState<Review[]>(initialReviews);
  
  const handleAddReviewClick = () => {
    setShowReviewForm(true);
  };

  const handleReviewTextChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setNewReviewText(event.target.value);
  };

  const handleReviewSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    
    // review is not empty and rating is chosen
    if (!newReviewText.trim() || newReviewRating === 0) {
      setErrorMessage("Please enter a review and select a rating before posting.");
      return;
    }
  
    // Create a new review object
    const newReview = {
      id: reviews.length > 0 ? Math.max(...reviews.map((r) => r.id)) + 1 : 1,
      customerName: firstName + " " +lastName, // Replace with actual customer name if available
      rating: newReviewRating,
      reviewText: newReviewText,
      date: new Date().toISOString(),
    };

    const reviewData = {
      comment: newReviewText,
      date: new Date().toISOString(),
      rating: newReviewRating,
      product_id: productID,
    };
    try {
      const response = await axios.post('http://localhost:9080/reviews', reviewData, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        setReviews((prevReviews) => [newReview, ...prevReviews]);
        // Clear the form fields and error message
        setShowReviewForm(false);
        setNewReviewText("");
        setNewReviewRating(0);
        setErrorMessage("");
      } else {
        setErrorMessage("Failed to post the review. Please try again.");
      }
    } catch (error) {
      setErrorMessage("An error occurred while posting the review. Please try again.");
    }
    setReviews((prevReviews) => [newReview, ...prevReviews]);
        // Clear the form fields and error message
        setShowReviewForm(false);
        setNewReviewText("");
        setNewReviewRating(0);
        setErrorMessage("");
  };


  return (
    <div>
      {reviews.length > 0 && <h3 className="mt-4 mb-3">Customer Reviews</h3>}
      
      {!isAdmin && (
        <div>
          <button className="btn btn-primary" onClick={handleAddReviewClick}>Add Review</button>
          {showReviewForm && (
            <form onSubmit={handleReviewSubmit} className="my-3">
              <StarRating rating={newReviewRating} setRating={setNewReviewRating} />
              <textarea style={{ resize: "none" }}
                className="form-control my-2"
                value={newReviewText}
                onChange={handleReviewTextChange}
                placeholder="Write your review here..."
                rows={3}
              />
              {errorMessage && <div className="text-danger mb-2">{errorMessage}</div>}
              <div className="mt-2">
                <button style={{ background: "red" }} type="button" className="btn btn-secondary me-2" onClick={() => {
                   setShowReviewForm(false); setErrorMessage(''); }}>Cancel</button>
                <button type="submit" className="btn btn-success">Post Review</button>
              </div>
            </form>
          )}
        </div>
      )}

      {reviews.map((review) => (
        <div key={review.id} className="card mb-3 mt-4">
          <div className="card-body">
            <div className="d-flex justify-content-between align-items-center">
              <h5 className="card-title">{review.customerName}</h5>
              <StarRating rating={review.rating}/>
            </div>
            <p className="card-text">{review.reviewText}</p>
            <p className="card-text">
              <small className="text-muted">Date: {new Date(review.date).toLocaleString()}</small>
            </p>
          </div>
        </div>
      ))}
    </div>
  );
};

export default CustomerReviews;