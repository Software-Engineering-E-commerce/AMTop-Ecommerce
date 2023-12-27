import { useLocation } from "react-router-dom";
import ProductsList from "../Components/ProductsList";
import axios from "axios";
import Navbar from "../Components/HomeNavbar";

const Catalog = () => {
  const location = useLocation();
  var { userToken, isAdmin, firstName, lastName } = location.state || {};

  const getProducts = async () => {
    console.log("In get products");
    let url = `http://localhost:9080/api/products/${
      isAdmin ? "getAdminProducts" : "getCustomerProducts"
    }`;
    try {
      const response = await axios(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
      });
      const products: Product[] = response.data;
      console.log(products);
      return products;
    } catch (error) {
      console.log("Error:", error);
      const products: Product[] = [];
      return products;
    }
  };

  const getSortedProducts = async(sortBy: any, sortOrder: any) => {
    console.log("In get sorted products");
    let url = `http://localhost:9080/api/sort/Product/${sortBy}/${sortOrder}`;
    try {
      const response = await axios(url, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
      });
      console.log(response.data);
      const products: Product[] = response.data;
      return products;
    } catch (error) {
      console.log("Error:", error);
      const products: Product[] = [];
      return products;
    }
  }

  return (
    <>
      <Navbar
        firstName={firstName}
        lastName={lastName}
        isAdmin={isAdmin}
        token={userToken}
      />
      <ProductsList
        firstName={firstName}
        lastName={lastName}
        userToken={userToken}
        isAdmin={isAdmin}
        getProducts={getProducts}
        getSortedProducts={getSortedProducts}
      />
    </>
  );
};

export default Catalog;
