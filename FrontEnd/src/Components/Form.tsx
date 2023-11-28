import React, { ChangeEvent, FormEvent, useState } from "react";
import "./Form.css";
import GoogleAuth from "../Components/GoogleAuth";
import { Link } from "react-router-dom";

// to define whether it is a login or signup form


interface Props {
  isLogin: boolean;

  getSignUpCredentials?: (Customer: RegisterRequest) => void;
  getSignUpGoogleTok?: (googleTok:string) => void;
  
  getLogInCredentials?: (customer: LoginRequest) => void
  getLogInGoogleTok?: (googleTok:string) => void
}

//this is the main component of the form
const Form = ({ isLogin, getSignUpCredentials, getSignUpGoogleTok, getLogInCredentials, getLogInGoogleTok}: Props) => {

  //one use state for all of the form fields (sign up or login)
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  //here's states to show whether there's error for a specific field
  const [formErrors, setFormErrors] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  // Function to handle input changes
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
    console.log(name);
    if (name === "firstName") {
      if (checkFirstName().length == 0)
        setFormErrors((prvious) => {
          return {
            ...prvious,
            firstName: "",
          };
        });
    }

    if (name === "lastName") {
      if (checkLastName().length == 0)
        setFormErrors((prvious) => {
          return {
            ...prvious,
            lastName: "",
          };
        });
    }
    if (name === "email") {
      if (checkEmail().length == 0)
        setFormErrors((prvious) => {
          return {
            ...prvious,
            email: "",
          };
        });
    }
    if (name === "password") {
      if (checkPassword().length == 0)
        setFormErrors((prvious) => {
          return {
            ...prvious,
            password: "",
          };
        });
    }
  };

  //function to handel the contimue  button and see whether our fields are all valid or not
  const handleFormSubmit = (e: FormEvent<HTMLFormElement>) => {
    console.log("Submitt");
    e.preventDefault();
    let validFields = CheckFields();

    if (isLogin) {
      if (validFields.email && validFields.password) {
        console.log("All our credentials are set for the login");
        const customer:LoginRequest = {
          email: formData.email,
          password: formData.password
        }
        getLogInCredentials!(customer);
      }
    
    } else {
      if (
        validFields.firstName &&
        validFields.lastName &&
        validFields.email &&
        validFields.password &&
        validFields.confirmPassword
      ) {
        console.log("All our credentials are set for the Signup");
        const customer: RegisterRequest = {
          firstName: formData.firstName,
          lastName: formData.lastName,
          email: formData.email,
          password: formData.password,
        };
        getSignUpCredentials!(customer);
      }
    }
  };

  //here's a function to get the google authenticator data if the user has clicked the "sign in with google" button
  function getGoogleAuthData(googleTok:string) {
      isLogin? getLogInGoogleTok!(googleTok) :getSignUpGoogleTok!(googleTok) 
  }

  //function to check the validity of all the fields and change the errors states using comments
  //associated with each violation
  function CheckFields() {
    let errorMessages = {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      confirmPassword: "",
    };

    let validFields = {
      firstName: true,
      lastName: true,
      email: true,
      password: true,
      confirmPassword: true,
    };

    //here we need to check the validity of all of our fields
    let firstNameComment = checkFirstName();
    if (firstNameComment.length != 0) {
      errorMessages.firstName = firstNameComment;
      if (formData.firstName.length == 0)
        errorMessages.firstName = "This is a mandatory field to fill !";
      validFields.firstName = false;
    }

    let lastNameComment = checkLastName();
    if (lastNameComment.length != 0) {
      errorMessages.lastName = lastNameComment;
      if (formData.lastName.length == 0)
        errorMessages.lastName = "This is a mandatory field to fill !";
      validFields.lastName = false;
    }
    let emailComment = checkEmail();
    if (emailComment.length != 0) {
      errorMessages.email = emailComment;
      if (formData.email.length == 0)
        errorMessages.email = "This is a mandatory field to fill !";
      validFields.email = false;
    }

    let passwordComment = checkPassword();
    if (passwordComment.length != 0) {
      errorMessages.password = passwordComment;
      if (formData.password.length == 0)
        errorMessages.password = "This is a mandatory field to fill !";
      validFields.password = false;
    }

    let confirmPasswordComment = checkConfirmPassword();
    if (confirmPasswordComment.length != 0) {
      errorMessages.confirmPassword = confirmPasswordComment;
      if (formData.confirmPassword.length == 0)
        errorMessages.confirmPassword = "This is a mandatory field to fill!";
      validFields.confirmPassword = false;
    }

    setFormErrors(errorMessages);
    console.log(formErrors);
    return validFields;
  }

  //--------------------------------Here are some individual checking functions for each field----------------
  function checkFirstName() {
    formData.firstName.trim();
    const validNameRegex = /^[A-Za-z\s]+$/;
    let comment = "";
    if (!validNameRegex.test(formData.firstName.trim())) {
      comment = "Please use valid Characters for a name !";
    }
    if (formData.firstName.trim().length < 3) {
      comment = "Name is too short !";
    }
    return comment;
  }

  function checkLastName() {
    const validNameRegex = /^[A-Za-z\s]+$/;
    let comment = "";
    if (!validNameRegex.test(formData.lastName.trim())) {
      comment = "Please use valid Characters for a name !";
    }
    if (formData.lastName.trim().length < 3) {
      comment = "Name is too short !";
    }
    return comment;
  }

  function checkEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    let comment = "";
    if (!emailRegex.test(formData.email.trim())) {
      comment = "Please enter a valid email of the format username@domain";
    }
    if (formData.email.trim().length < 6) {
      comment = "Email is too short !";
    }
    return comment;
  }

  function checkPassword() {
    const passwordRegex =
      /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?~\\-])[A-Za-z\d!@#$%^&*()_+{}\[\]:;<>,.?~\\-]{8,}$/;

    let comment = "";
    if (!passwordRegex.test(formData.password)) {
      comment = "Please use a stronger password and Don't use white spaces!";
    }
    if (formData.password.length < 8) {
      comment = "Password must be at least 8 characters long";
    }
    return comment;
  }

  function checkConfirmPassword() {
    let comment = "";
    if (
      formData.password !== formData.confirmPassword ||
      formData.confirmPassword.length == 0
    ) {
      comment = "Passwords do not match !";
    }
    return comment;
  }
  //--------------------------------the end of some individual checking functions for each field----------------

  //and here's the overall return HTML document for the form
  //note that there's conditional rendering for some fields
  return (
    <>
      <div className="col-lg-5 col-sm-12 container">
        <form className="row g-3" onSubmit={handleFormSubmit} noValidate>
          <header style={{ marginBottom: "0px" }}>
            <h3 style={{ marginBottom: "10px", color: "#007bff" }}>
              {isLogin ? "Log in" : "Sign Up"}
            </h3>
            <h6 style={{ color: "gray" }}>
              Please fill in this form in order to{" "}
              {isLogin ? "Log in" : "Sign Up"}
            </h6>
            <hr style={{ marginBottom: "0px" }} />
          </header>

          {!isLogin && (
            <>
              <div className="col-md-6 input-cont">
                <label className="form-label">Frist name</label>
                <input
                  type="text"
                  placeholder="At least 3 characters"
                  style={{ padding: "0.8rem 0.75rem" }}
                  className={`form-control ${
                    formErrors.firstName ? "is-invalid" : ""
                  }`}
                  name="firstName"
                  aria-label="First name"
                  value={formData.firstName}
                  onChange={handleInputChange}
                  required
                />
                {formErrors.firstName && (
                  <div className="invalid-feedback">{formErrors.firstName}</div>
                )}
              </div>

              <div className="col-md-6 input-cont">
                <label className="form-label">Last name</label>
                <input
                  type="text"
                  placeholder="At least 3 characters"
                  style={{ padding: "0.8rem 0.75rem" }}
                  className={`form-control ${
                    formErrors.lastName ? "is-invalid" : ""
                  }`}
                  aria-label="Last name"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleInputChange}
                  required
                />
                {formErrors.lastName && (
                  <div className="invalid-feedback">{formErrors.lastName}</div>
                )}
              </div>
            </>
          )}

          <div className="col-12 input-cont">
            <label className="form-label">Email</label>
            <input
              type="email"
              style={{ padding: "0.8rem 0.75rem" }}
              className={`form-control ${formErrors.email ? "is-invalid" : ""}`}
              id="inputEmail4"
              placeholder="Username@domain"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              required
            />
            {formErrors.email && (
              <div className="invalid-feedback">{formErrors.email}</div>
            )}
          </div>
          <div className="col-12 input-cont">
            <label className="form-label">Password</label>
            <input
              type="password"
              className={`form-control ${
                formErrors.password ? "is-invalid" : ""
              }`}
              id="password"
              name="password"
              style={{ padding: "0.8rem 0.75rem" }}
              value={formData.password}
              onChange={handleInputChange}
              placeholder="At least 8 characters"
              required
            />
            {formErrors.password && (
              <div className="invalid-feedback">{formErrors.password}</div>
            )}
          </div>
          {!isLogin && (
            <>
              <div className="col-12 input-cont">
                <label className="form-label">Confirm password</label>
                <input
                  type="password"
                  style={{ padding: "0.8rem 0.75rem" }}
                  className={`form-control ${
                    formErrors.confirmPassword ? "is-invalid" : ""
                  }`}
                  id="Password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleInputChange}
                  placeholder="Re-enter your password"
                  required
                />
                {formErrors.confirmPassword && (
                  <div className="invalid-feedback">
                    {formErrors.confirmPassword}
                  </div>
                )}
              </div>
            </>
          )}
          <button type="submit" className="btn btn-primary col">
            Continue
          </button>
          <div className="line-container">
            <div className="line"></div>
            <div className="or">OR</div>
            <div className="line"></div>
          </div>
          <GoogleAuth getUserData={getGoogleAuthData} />

          {!isLogin ? (
            <Link to="/login">Already have an account? Log-in</Link>
          ) : (
            <Link to="/">Don't have an account? Sign-Up</Link>
          )}
        </form>
      </div>
    </>
  );
};

export default Form;
