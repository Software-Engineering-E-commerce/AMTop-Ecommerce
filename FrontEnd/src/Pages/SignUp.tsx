import axios from "axios";
import Form from "../Components/Form";
import { useNavigate } from "react-router-dom";

const SignUp = () => {
  const navigate = useNavigate();

  const getSignUpCredentials = (customer: Customer, isGoogle: boolean) => {
    if (isGoogle) {
      //TODO send the request for google signup
      handelSignUpBasicCredentials(customer);
    } else {
      //TODO send the request for basic credentials sign up
    }
  };

  const handelSignUpBasicCredentials = async (customer: Customer) => {
    //if success then the user will be added to the DB and then routed to his home page
    
    // const requestPath = "";
    // try {
    //   const response = await axios.post(requestPath, customer);
    //   if (response.data === "SUCCESS") {
    //     //TODO display a pop up window that says that the user's got an email with a verify link
    //     // navigate("/home");
    //   } else {
    //     //this account already exists
    //     // alert(response.data);
    //   }
    // } catch (error) {
    //   console.error("Error:", error);
    //   alert(error);
    // }
  };

  const handelSignUpBasicGoogle = async (customer: Customer) => {
    //if success then the user will be addel to the DB and then routed to his home page
    //if the user already exists then we'll just log him in.
  };

  return (
    <>
      <Form isLogin={false} getSignUpCredentials={getSignUpCredentials} />
    </>
  );
};

export default SignUp;

