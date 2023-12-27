import axios from "axios";
import React, { useState, ChangeEvent, FormEvent } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import GenericAlertModal from "./GenericAlertModal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";
import Loading from "./Loading";

interface AddAdminPopupProps {
  show: boolean;
  handleClose: () => void;
  token: string;
}

interface addAdminRequest {
  newAdminEmail: string;
}

const AddAdminPopup: React.FC<AddAdminPopupProps> = ({
  show,
  handleClose,
  token,
}) => {
  const [email, setEmail] = useState("");
  const [isValidEmail, setIsValidEmail] = useState(true);
  const [responseData, setResponseData] = useState("");
  const [loading, setLoading] = useState(false);

  const handleEmailChange = (event: ChangeEvent<HTMLInputElement>) => {
    const newEmail = event.target.value;
    setEmail(newEmail);
    validateEmail(newEmail);
  };

  const validateEmail = (value: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    setIsValidEmail(emailRegex.test(value));
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setLoading(true);
    setEmail("");
    setIsValidEmail(true);
    handleClose();
    try {
      let request: addAdminRequest = { newAdminEmail: email };
      let url: string = "http://localhost:9080/api/admin/addAdmin";
      const response = await axios.post(url, request, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      console.log("Response:", response.data);
      setResponseData(response.data);
    } catch (error) {
      //   Handle errors here
      console.log(error);
      if (axios.isAxiosError(error)) {
        // This type assertion tells TypeScript that error is an AxiosError
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          console.error("Response data:", axiosError.response.data);
          console.error("Response status:", axiosError.response.status);
          setResponseData(axiosError.response.data as string);
        } else if (axiosError.request) {
          // The request was made but no response was received
          console.error("No response received:", axiosError.request);
        } else {
          // Something happened in setting up the request that triggered an Error
          console.error("Error:", axiosError.message);
        }
      } else {
        // Handle non-Axios errors
        console.error("Non-Axios error:", error);
      }
    }
    setLoading(false);
  };

  const handleModalClose = () => {
    // Reset form state
    setEmail("");
    setIsValidEmail(true);

    // Close the modal
    handleClose();
  };
  const resetResponseData = () => {
    setResponseData("");
  };

  return (
    <>
      {loading && <Loading isLoading={loading} />}
      <Modal show={show} onHide={handleModalClose} centered>
        <Modal.Header closeButton>
          <Modal.Title>Add Admin</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter new admin email"
                value={email}
                onChange={handleEmailChange}
                isInvalid={!isValidEmail}
                required
                autoComplete="email"
                name="email"
              />
              <Form.Control.Feedback type="invalid">
                Please enter a valid email address.
              </Form.Control.Feedback>
            </Form.Group>
            <div className="text-center">
              <Button
                variant="primary"
                type="submit"
                disabled={!isValidEmail}
                style={{ marginTop: "20px" }}
              >
                Add Admin
              </Button>
            </div>
          </Form>
        </Modal.Body>
      </Modal>
      <>
        {responseData ===
          "Admin has been added and a Verification Email sent to him" && (
          <GenericAlertModal
            onClose={handleModalClose}
            resetResponseData={resetResponseData}
            show={true}
            body={
              <>
                <h5 style={{ color: "green" }}>
                  <FontAwesomeIcon
                    icon={faCircleCheck}
                    style={{
                      color: "green",
                      fontSize: "18px",
                      marginRight: "10px",
                    }}
                  />
                  {responseData}
                </h5>
              </>
            }
          />
        )}
        {responseData !== "" &&
          responseData !==
            "Admin has been added and a Verification Email sent to him" && (
            <GenericAlertModal
              onClose={handleModalClose}
              resetResponseData={resetResponseData}
              show={true}
              body={
                <>
                  <h5 style={{ color: "red" }}>{responseData}</h5>
                </>
              }
            />
          )}
      </>
    </>
  );
};

export default AddAdminPopup;
