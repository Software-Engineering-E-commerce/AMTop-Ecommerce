import React, { useEffect, useRef, useState } from "react";

import "./HomeFooter.css"; // Import your CSS file
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFacebook,
  faInstagram,
  faLinkedin,
  faTwitter,
} from "@fortawesome/free-brands-svg-icons";
import { faEnvelope, faPhoneVolume } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";

interface HomeFooterProps {
  token: string;
}
const HomeFooter = ({ token }: HomeFooterProps) => {
  const [Categories, setCategories] = useState<categoryDTO[]>([]);
  const isMounted = useRef(true);

  const fetchCategories = async () => {
    try {
      const response = await axios.get(
        "http://localhost:9080/api/categoryDetails/getAllCategories",
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      setCategories(response.data);
    } catch (error) {
      console.log("Error:", error);
    }
  };

  // useEffect runs on component mount
  useEffect(() => {
    if (isMounted.current) {
      fetchCategories();
      isMounted.current = false;
    }
  }, []);

  return (
    <footer className="footer homeFooter" style={{ marginTop: "45px" }}>
      <div className="container-fluid">
        <div className="row" style={{ padding: "40px 20px" }}>
          <div className="footer-col">
            <h4>Contact Us</h4>
            <ul>
              <li>
                <a
                  style={{
                    color: "white",
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
                  }}
                >
                  <FontAwesomeIcon icon={faPhoneVolume} />{" "}
                  <a style={{ marginLeft: "5px" }}> 01098327484</a>
                </a>
              </li>
              <li>
                <a
                  style={{
                    color: "white",
                    marginTop: "30px",
                    textTransform: "lowercase",
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
                  }}
                >
                  <FontAwesomeIcon icon={faEnvelope} />
                  <a style={{ marginLeft: "5px" }}>amtop02@outlook.com</a>
                </a>
              </li>
            </ul>
          </div>
          <div className="footer-col">
            <h4>online shop</h4>
            <ul>
              {Categories.map((category, index) => (
                <>
                  <li>
                    <a href="#">{category.name}</a>
                  </li>
                </>
              ))}
            </ul>
          </div>
          <div className="footer-col">
            <h4>follow us</h4>
            <div className="social-links">
              <a href="#">
                <FontAwesomeIcon icon={faFacebook} />
              </a>
              <a href="#">
                <FontAwesomeIcon icon={faTwitter} />
              </a>
              <a href="#">
                <FontAwesomeIcon icon={faInstagram} />
              </a>
              <a href="#">
                <FontAwesomeIcon icon={faLinkedin} />
              </a>
            </div>
          </div>
        </div>
        <div className="footerBottom">
          <p>
            Copyright &copy;2023, Designed by the <span>AMTOP team</span>
          </p>
        </div>
      </div>
    </footer>
  );
};

export default HomeFooter;
