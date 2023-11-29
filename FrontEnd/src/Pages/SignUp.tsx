import axios from "axios";
import Form from "../Components/Form";
import { useLocation, useNavigate } from "react-router-dom";
import BobUpWindow from "../Components/BobUpWindow";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";

const SignUp = () => {
  const navigate = useNavigate();

  //function to direct the flow to sign up using credentials
  const getSignUpCredentials = (customer: RegisterRequest) => {
    handelSignUpBasicCredentialsRequest(customer);
  };

  //-------------------------handeling sign up using basic credentials-----------------------------------------
  const handelSignUpBasicCredentialsRequest = async (
    customer: RegisterRequest
  ) => {
    //if success then the user will be added to the DB and then routed to his home page
    try {
      const response = await axios({
        method: "post",
        url: "http://localhost:9080/api/auth/registerC",
        data: customer,
      });
      console.log(response);
      let res: AuthenticationResponse = response.data;
      handelSignUpBasicCredentialsResponse(res);
    } catch (error) {
      console.error("Error:", error);
      alert(error);
    }
  };

  const handelSignUpBasicCredentialsResponse = (
    response: AuthenticationResponse
  ) => {
    let status = response.token;

    //here the userTok is "SUCCESS + token"
    if (status === "SUCCESS") {
      return (
        <>
          <BobUpWindow>
            <p>
              <FontAwesomeIcon
                icon={faCircleCheck}
                style={{
                  color: "green",
                  fontSize: "18px",
                  marginRight: "10px",
                }}
              />
              We've sent a verification email to you, please check it out to be
              able to log in.
            </p>
          </BobUpWindow>
          {navigate("/signup")}
        </>
      );

      //but here it is "Already Exist"
    } else if (status === "Already Exist") {
      return (
        <>
          <BobUpWindow>
            <p style={{ color: "black" }}>
              The email address you provided already exists so you might wanna
              log in
            </p>
            <button
              type="button"
              className="btn btn-primary"
              style={{ marginLeft: 0 }}
              onClick={() => navigate("/login")}
            >
              Log in
            </button>
          </BobUpWindow>
        </>
      );
    } else {
      return (
        <>
          <BobUpWindow>
            <p style={{ color: "red" }}>
              An error has occured while signing you in, please sign up
              properly!
            </p>
          </BobUpWindow>
          {navigate("/signup")}
        </>
      );
    }
  };

  //-------------------------end of handeling sign up using basic credentials-----------------------------------------

  return (
    <>
      <Form isLogin={false} getSignUpCredentials={getSignUpCredentials} />
    </>
  );
};

export default SignUp;
