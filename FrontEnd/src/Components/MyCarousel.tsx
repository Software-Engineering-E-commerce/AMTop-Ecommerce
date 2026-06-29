import React from "react";
import { Carousel } from "react-bootstrap";
import "./MyCarousel.css";

import carousel1 from "../assets/carouselPics/carousel1.jpg";
import carousel2 from "../assets/carouselPics/carousel2.png";
import carousel3 from "../assets/carouselPics/carousel3.png";
import carousel4 from "../assets/carouselPics/carousel4.png";
import carousel5 from "../assets/carouselPics/carousel5.png";
import carousel6 from "../assets/carouselPics/carousel6.png";

const MyCarousel: React.FC = () => {
  const carouselItems = [
    {
      imageSrc: carousel1,
      title: "Latest Laptops",
      description: "Explore high-performance laptops for work and entertainment.",
    },
    {
      imageSrc: carousel2,
      title: "Smartphones Galore",
      description: "Upgrade your smartphone and stay connected with the latest technology.",
    },
    {
      imageSrc: carousel3,
      title: "Smart Watches for You",
      description: "Track fitness, receive notifications, and enhance your daily activities with smartwatches.",
    },
    {
      imageSrc: carousel4,
      title: "Wireless Aeropods",
      description: "Experience freedom with our wireless Aeropods. Immerse yourself in high-quality audio with a sleek and comfortable design for all-day use.",
    },
    {
      imageSrc: carousel5,
      title: "Wireless Headsets",
      description: "Experience freedom with our wireless headsets. Immerse yourself in high-quality audio with a sleek and comfortable design for all-day use.",
    },
    {
      imageSrc: carousel6,
      title: "All is available",
      description: "Get the finest, best, costly-effective electronic devices ever made",
    }
  ];


  return (
    <Carousel id="carouselExampleCaptions">
      {carouselItems.map((item, index) => (
        <Carousel.Item key={index}>
          <img src={item.imageSrc} className="d-block w-100" alt={`Slide ${index + 1}`} />
          <div className="carousel-caption d-none d-md-block">
            <h5>{item.title}</h5>
            <p>{item.description}</p>
          </div>
        </Carousel.Item>
      ))}
    </Carousel>
  );
};

export default MyCarousel;
