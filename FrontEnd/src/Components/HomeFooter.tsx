import React from "react";

import "./HomeFooter.css"; // Import your CSS file
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFacebook,
  faInstagram,
  faLinkedin,
  faTwitter,
} from "@fortawesome/free-brands-svg-icons";
import { faEnvelope, faPhoneVolume } from "@fortawesome/free-solid-svg-icons";

const HomeFooter: React.FC = () => {
  return (
    <footer className="footer homeFooter" style={{marginTop:"45px"}}>
      <div className="container">
        <div className="row" style={{padding:"40px 20px"}}>
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
              <li>
                <a href="#">smart watches</a>
              </li>
              <li>
                <a href="#">headphones</a>
              </li>
              <li>
                <a href="#">aeropods</a>
              </li>
              <li>
                <a href="#">laptops</a>
              </li>
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
        <div className="footerBottom"><p>Copyright &copy;2023; Designed by the <span>AMTOP team</span></p></div>

      </div>
    </footer>
  );
};

export default HomeFooter;
