import React from "react";
import { Carousel } from "react-bootstrap";
import "./MyCarousel.css";

const MyCarousel: React.FC = () => {
  const carouselItems = [
    {
      imageSrc: "src/assets/carouselPics/carousel1.jpg",
      title: "Latest Laptops",
      description: "Explore high-performance laptops for work and entertainment.",
    },
    {
      imageSrc: "src/assets/carouselPics/carousel2.png",
      title: "Smartphones Galore",
      description: "Upgrade your smartphone and stay connected with the latest technology.",
    },
    {
      imageSrc: "src/assets/carouselPics/gamda.png",
      title: "Smart Watches for You",
      description: "Track fitness, receive notifications, and enhance your daily activities with smartwatches.",
    },
    {
      imageSrc: "src/assets/carouselPics/carousel4444.png",
      title: "Wireless Aeropods",
      description: "Experience freedom with our wireless Aeropods. Immerse yourself in high-quality audio with a sleek and comfortable design for all-day use.",
    },
    {
      imageSrc: "src/assets/carouselPics/carousel5.png",
      title: "Wireless Headsets",
      description: "Experience freedom with our wireless headsets. Immerse yourself in high-quality audio with a sleek and comfortable design for all-day use.",
    },
    {
      imageSrc: "src/assets/carouselPics/carousel6.png",
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
