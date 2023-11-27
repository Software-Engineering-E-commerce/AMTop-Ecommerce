import React, { ChangeEvent, FormEvent, useState } from "react";
import "./Form.css";
import GoogleAuth from "../Components/GoogleAuth";
import { Link } from "react-router-dom";

interface Props {
  isLogin: boolean;
}

const Form = ({ isLogin }: Props) => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

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
      if (checkFirstName())
        setFormErrors((prvious) => {
          return {
            ...prvious,
            firstName: "",
          };
        });
    }

    if (name === "lastName") {
      if (checkLastName())
        setFormErrors((prvious) => {
          return {
            ...prvious,
            lastName: "",
          };
        });
    }
    if (name === "email") {
      if (checkEmail())
        setFormErrors((prvious) => {
          return {
            ...prvious,
            email: "",
          };
        });
    }
    if (name === "password") {
      if (checkPassword())
        setFormErrors((prvious) => {
          return {
            ...prvious,
            password: "",
          };
        });
    }
  };

  const handleFormSubmit = (e: FormEvent<HTMLFormElement>) => {
    console.log("Submitt")
    e.preventDefault();
    let isValid = CheckFields();
    if (isValid) {
       console.log("22")
      console.log("All our credentials are set");
      console.log(formData);
    }
  };

  function CheckFields() {
    let isValid: boolean = true; //to check the overall validity

    let errorMessages = {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      confirmPassword: "",
    };

    //here we need to check the validity of all of our fields
    if (!checkFirstName()) {
      errorMessages.firstName = " Enter a valid name please!";
      if (formData.firstName.length == 0)
        errorMessages.firstName = "This is a mandatory field to fill !";
      isValid = false;
    }
    if (!checkLastName()) {
      errorMessages.lastName = " Enter a valid name please!";
      if (formData.lastName.length == 0)
        errorMessages.lastName = "This is a mandatory field to fill !";
      isValid = false;
    }
    if (!checkEmail()) {
      errorMessages.email =
        "Please enter a valid email of the format username@domain";
      if (formData.email.length == 0)
        errorMessages.email = "This is a mandatory field to fill !";
      isValid = false;
    }
    if (!checkPassword()) {
      errorMessages.password =
        "Please enter a valid password of at least 8 characters";
      if (formData.password.length == 0)
        errorMessages.password = "This is a mandatory field to fill !";
      isValid = false;
    }
    if (!checkConfirmPassword()) {
      errorMessages.confirmPassword = "Passwords do not match";
      if (formData.confirmPassword.length == 0)
        errorMessages.confirmPassword = "This is a mandatory field to fill !";
      isValid = false;
    }
    setFormErrors(errorMessages);
    console.log(formErrors);
    return isValid;
  }

  function checkFirstName() {
    const validNameRegex = /^[A-Za-z\s]+$/;
    let isValid = true;
    if (
      !validNameRegex.test(formData.firstName) ||
      formData.firstName.length < 3
    ) {
      isValid = false;
    }
    return isValid;
  }

  function checkLastName() {
    const validNameRegex = /^[A-Za-z\s]+$/;
    let isValid = true;
    if (
      !validNameRegex.test(formData.lastName) ||
      formData.lastName.length < 3
    ) {
      isValid = false;
    }
    return isValid;
  }

  function checkEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    let isValid = true;
    if (!emailRegex.test(formData.email) || formData.email.length < 6) {
      isValid = false;
    }
    return isValid;
  }

  function checkPassword() {
    let isValid = true;
    if (formData.password.length < 8) {
      console.log("Password not valid");
      isValid = false;
    }
    return isValid;
  }

  function checkConfirmPassword() {
    let isValid = true;
    if (
      formData.password !== formData.confirmPassword ||
      formData.confirmPassword.length == 0
    ) {
      isValid = false;
    }
    return isValid;
  }

  function getGoogleAuthData(
    firstName: string,
    lastName: string,
    email: string
  ) {
    console.log("get data called");
    console.log(firstName);
    console.log(lastName);
    console.log(email);
  }

  return (
    <>
      <div className="container">
        <form className="row g-3" onSubmit={handleFormSubmit} noValidate>
          <header style={{ marginBottom: "0px" }}>
            <h3 style={{ marginBottom: "10px", color: "#007bff" }}>
              {isLogin ? "Log in" : "Sign Up"}
            </h3>
            <h6 style={{ color: "gray" }}>
              Please fill in this form in order to{" "}
              {isLogin ? "Log in" : "Sign Up"}
            </h6>
            <hr style={{ marginBottom: "5px" }} />
          </header>

          {!isLogin && (
            <>
              <div className="col-md-6">
                <label className="form-label">Frist name</label>
                <input
                  type="text"
                  className={`form-control ${
                    formErrors.firstName ? "is-invalid" : ""
                  }`}
                  placeholder="First name"
                  name="firstName"
                  aria-label="First name"
                  value={formData.firstName}
                  onChange={handleInputChange}
                  required
                />
                {formErrors.firstName && (
                  <div className="invalid-feedback">
                    {isLogin ? "Log in" : "Sign Up"}{" "}
                  </div>
                )}
              </div>

              <div className="col-md-6">
                <label className="form-label">Last name</label>
                <input
                  type="text"
                  className={`form-control ${
                    formErrors.lastName ? "is-invalid" : ""
                  }`}
                  placeholder="Last name"
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

          <div className="col-12">
            <label className="form-label">Email</label>
            <input
              type="email"
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
          <div className="col-12">
            <label className="form-label">Password</label>
            <input
              type="password"
              className={`form-control ${
                formErrors.password ? "is-invalid" : ""
              }`}
              id="password"
              name="password"
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
              <div className="col-12">
                <label className="form-label">confirm password</label>
                <input
                  type="password"
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

           {!isLogin ? (<Link to="/login">Already have an account? Log-in</Link>) : <Link to="/">Don't have an account? Sign-Up</Link>} 
          
        </form>
      </div>
    </>
  );
};

export default Form;
