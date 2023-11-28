import BobUpWindow from "../Components/BobUpWindow";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";
import { useLocation } from "react-router-dom";

const Home = () => {
  const location = useLocation();
  var {userToken} = location.state || {};
  
  return (
    <>
      <BobUpWindow>
        <p style={{ color: "green" }}>
          <FontAwesomeIcon
            icon={faCircleCheck}
            style={{
              color: "green",
              fontSize: "18px",
              marginRight: "10px",
            }}
          />
          Congrats ! you've successfully logged in
        </p>
      </BobUpWindow>
      <h1>Welcome to Your Home page {userToken}!</h1>
    </>
  );
};

export default Home;