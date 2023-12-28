import { useLocation } from "react-router-dom";
import ProductsList from "../Components/ProductsList";
import axios from "axios";
import Navbar from "../Components/HomeNavbar";
import HomeFooter from "../Components/HomeFooter";

const Catalog = () => {
  const location = useLocation();
  var { userToken, isAdmin, firstName, lastName, passedProducts } = location.state || {};

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

  const getSortedProducts = async (sortBy: any, sortOrder: any) => {
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

  const getFilteredProducts = async (filter: FilterProductDto) => {
    console.log("In get filtered products");
    console.log(filter);
    let url = `http://localhost:9080/api/filter/Product`;
    try {
      const response = await axios(url, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
        data: filter
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
        isProducts={true}
      />
      <ProductsList
        passedProducts={passedProducts}
        firstName={firstName}
        lastName={lastName}
        userToken={userToken}
        isAdmin={isAdmin}
        getProducts={getProducts}
        getSortedProducts={getSortedProducts}
        getFilteredProducts={getFilteredProducts}
      />
    </>
  );
};

export default Catalog;
