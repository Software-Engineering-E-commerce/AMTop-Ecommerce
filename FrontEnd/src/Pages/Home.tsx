import BobUpWindow from "../Components/BobUpWindow";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";
// import { useLocation } from "react-router";
import Navbar from "../Components/HomeNavbar";
import { useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";

interface Categorie {
  categoryName: string;
  imageLink: string;
}

interface HomeInfo {
  firstName: string;
  lastName: string;
  admin: boolean;
  categoryList: Categorie[];
}

const Home = () => {
  const location = useLocation();
  var { userToken, from } = location.state || {};
  const [homeInfo, setHomeInfo] = useState<HomeInfo | null>(null);
  const isMounted = useRef<boolean>(true);
  // const [registerStatus, setRegisterStatus] = useState<string>;

  const fetchData = async () => {
    try {
      const response = await axios.get(
        "http://localhost:9080/Home/startup",
        {
          headers: {
            Authorization: `Bearer ${userToken}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("response")
      console.log(response.data)
      setHomeInfo(response.data);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
        } else if (axiosError.request) {
          console.error("No response received:", axiosError.request);
        } else {
          console.error("Error:", axiosError.message);
        }
      } else {
        // Handle non-Axios errors
        console.error("Non-Axios error:", error);
      }
    }
  };
  useEffect(() => {
    // Check if the request has not been made
    if (isMounted.current) {
      fetchData();
      isMounted.current = false;

      // Make the GET request here
      // fetch("http://localhost:9080/Home/startup", {
      //   method: "GET",
      //   headers: {
      //     Authorization: `Bearer ${userToken}`,
      //   },
      // })
      //   .then((response) => response.json())
      //   .then((data) => {
      //     // Update state with the fetched HomeInfo
      //     setHomeInfo(data);
      //   })
      //   .catch((error) => {
      //     console.error("Error fetching HomeInfo:", error);
      //   });
    }
  }, []);

  console.log("from: ", from);
  console.log(homeInfo);
  return (
    <>
      <Navbar
        firstName={homeInfo?.firstName || ""}
        lastName={homeInfo?.lastName || ""}
        isAdmin={homeInfo?.admin || false}
        token={userToken}
      />
      {/* {(from === "logged-in" || from === "Signed-up") && (
       
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
      )} */}
    </>
  );
};

export default Home;
