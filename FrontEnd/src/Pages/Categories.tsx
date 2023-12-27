import React from "react";
import { useLocation } from "react-router";
import Navbar from "../Components/HomeNavbar";

const Categories = () => {
  const location = useLocation();
  var { userToken, isAdmin, firstName, lastName } = location.state || {};

  console.log(
    "🚀 ~ file: Categories.tsx:7 ~ Categories ~  userToken, isAdmin, firstName, lastName :",
    userToken,
    isAdmin,
    firstName,
    lastName
  );

  return (
    <>
      <Navbar
        firstName={firstName}
        lastName={lastName}
        isAdmin={isAdmin}
        token={userToken}
        isCategories={true}
      />
    </>
  );
};

export default Categories;
