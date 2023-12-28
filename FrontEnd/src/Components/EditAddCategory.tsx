import axios from "axios";
import React, { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { useNavigate } from "react-router";
import CategoryAlertModal from "./CategoryAlertModal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";

export interface EditedCategory {
  categoryName: string;
}

interface EditAddCategoryProps {
  resetButton: () => void;
  adminToken: string;
  isEdit: Boolean;
  category?: EditedCategory;
}

const EditAddCategory = ({
  resetButton,
  adminToken,
  isEdit,
  category,
}: EditAddCategoryProps) => {
  const [formData, setFormData] = useState<EditedCategory>({
    categoryName: "",
  });
  const navigate = useNavigate();
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [responseData, setResponseData] = useState("");
  const [show, setShow] = useState<boolean>(true);

  useEffect(() => {
    if (category) {
      // If product prop is passed, set the initial values from the product
      setFormData({
        categoryName: category.categoryName,
      });
    } else {
      // If product prop is not passed, set the initial values to ""
      setFormData({
        categoryName: "",
      });
    }
  }, [category]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    // Value should be less than 30 chars
    if (name === "categoryName") {
      if (value.length > 30) {
        // Invalid input, do not update the state
        return;
      }
    }
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null;
    let fileSize: number = file?.size as number;
    if (fileSize >= 1024 * 1024) {
      setSelectedFile(null);
      return;
    }
    setSelectedFile(file);
  };

  const handleFormSubmit = () => {
    setFormSubmitted(true);
    // Check if all fields are filled before processing the form
    if (
      formData.categoryName.length >= 4 &&
      (isEdit || (!isEdit && selectedFile !== null))
    ) {
      console.log(
        "🚀 ~ file: EditAddCategory.tsx:79 ~ handleFormSubmit ~ formData:",
        formData
      );
      console.log(selectedFile);
      // Process the form data
      if (isEdit) {
        handleEditRequest();
      } else {
        handleAddRequest();
      }
    }
  };

  const handleEditRequest = async () => {
    console.log("From editing a category");

    let tmp: categoryDTO = {
      name: formData.categoryName,
      imageUrl: "Abc",
    };

    try {
      let url: string = `http://localhost:9080/api/editCategory?categoryDTO=${encodeURIComponent(
        JSON.stringify(tmp)
      )}`;

      const formData = new FormData();
      // Remove the line formData.append("image", null);
      if (selectedFile) {
        formData.append("image", selectedFile);
      } else {
        const dummyBlob = new Blob([""], { type: "application/octet-stream" });
        const dummyFile = new File([dummyBlob], "");
        formData.append("image", dummyFile);
      }

      const response = await axios.post(url, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${adminToken}`,
        },
      });
      console.log(response);

      // Here means that the response is Ok and the product is added successfully
      setResponseData(response.data);
    } catch (error) {
      // Handle errors here
      if (axios.isAxiosError(error)) {
        // This type assertion tells TypeScript that error is an AxiosError
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
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
  };

  const handleAddRequest = async () => {
    console.log("From adding a new category");
    let tmp: categoryDTO = {
      name: formData.categoryName,
      imageUrl: "Abc",
    };

    try {
      let url: string = `http://localhost:9080/api/addCategory?categoryDTO=${encodeURIComponent(
        JSON.stringify(tmp)
      )}`;

      const formData = new FormData();
      if (selectedFile) {
        formData.append("image", selectedFile);
      }

      const response = await axios.post(url, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${adminToken}`,
        },
      });
      console.log(response);

      // Here means that the response is Ok and the product is added successfully
      setResponseData(response.data);
    } catch (error) {
      // Handle errors here
      if (axios.isAxiosError(error)) {
        // This type assertion tells TypeScript that error is an AxiosError
        const axiosError = error as import("axios").AxiosError;
        if (axiosError.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
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
  };

  const onCancel = () => {
    setShow(false);
    resetButton();
  };

  const resetResponseData = () => {
    setResponseData("");
  };

  return (
    <>
      {responseData !== "" &&
        (responseData === "Category added successfully" ||
          responseData === "Category edited successfully") && (
          <>
            <CategoryAlertModal
              onClose={resetResponseData}
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
          </>
        )}

      {responseData !== "" &&
        responseData !== "Category added successfully" &&
        responseData !== "Category edited successfully" && (
          <>
            <CategoryAlertModal
              onClose={resetResponseData}
              show={true}
              body={
                <>
                  <h5 style={{ color: "red" }}>{responseData}</h5>
                </>
              }
            />
          </>
        )}

      <Modal
        id="exampleModalCenter"
        show={show}
        onHide={onCancel}
        backdrop="static"
        keyboard={false}
        tabIndex={-1}
        centered // Add the centered prop
        aria-labelledby="exampleModalCenterTitle"
        aria-hidden="true"
      >
        <Modal.Header
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            color: "#007bff",
          }}
        >
          {isEdit && <h3>Edit The Category</h3>}
          {!isEdit && <h3>Add A Category</h3>}
        </Modal.Header>

        <Modal.Body style={{ padding: "0px" }}>
          <form noValidate>
            <div className="productFormField">
              <div
                className="productNameField"
                style={{
                  width: "100%",
                  display: "flex",
                  flexDirection: "column",
                  marginBottom: "50px",
                }}
              >
                <div style={{ width: "30%", margin: "0" }}>
                  <label className="form-label" style={{ fontWeight: "550" }}>
                    Category Name*:
                  </label>
                </div>

                <div style={{ width: "90%" }}>
                  <input
                    type="text"
                    className="form-control"
                    name="categoryName"
                    aria-label="categoryName"
                    value={formData.categoryName}
                    onChange={handleInputChange}
                    style={{ fontWeight: "600" }}
                    required
                  />
                  {formSubmitted && !formData.categoryName && (
                    <div className="text-danger">
                      *Category name is required
                    </div>
                  )}
                  {formSubmitted &&
                    formData.categoryName.length < 4 &&
                    formData.categoryName && (
                      <div className="text-danger">
                        *Category name must be at least 4 characters
                      </div>
                    )}
                </div>
              </div>
            </div>

            <div className="mb-3">
              <label className="form-label" style={{ fontWeight: "550" }}>
                Upload Category image{!isEdit && <>*</>}:
              </label>
              <input
                className="form-control"
                type="file"
                id="formFile"
                accept="image/*"
                style={{ fontWeight: "500" }}
                onChange={handleFileChange}
              />
              {formSubmitted && selectedFile === null && isEdit == false && (
                <div className="text-danger">*Image is required</div>
              )}
            </div>
          </form>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="danger" onClick={onCancel}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleFormSubmit} type="submit">
            Confirm
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default EditAddCategory;
