import BobUpWindow from "../Components/BobUpWindow";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";
import { useLocation } from "react-router";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const location = useLocation();
  var {userToken, from} = location.state || {};

  const navigate = useNavigate();

  const goToProfile = () => {
    navigate("/home/profile", { state: { userToken: userToken } });
  };

  console.log(from);
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
          Congrats ! you've successfully {from}
        </p>
      </BobUpWindow>
      <h1>Welcome to Your Home page!</h1>
      <h2>You're token is: </h2>
      <p style={{width:"200px"}}>{userToken}</p>
      <button onClick={goToProfile}>Go to Profile</button>
    </>
  );
};

export default Home;