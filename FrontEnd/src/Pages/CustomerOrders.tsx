import axios from "axios";
import OrdersList from "../Components/OrdersList";
import { useLocation } from "react-router";
import Navbar from "../Components/HomeNavbar";

const CustomerOrders = () => {

  const location = useLocation();
  var {userToken, isAdmin, firstName, lastName} = location.state || {};

  const getOrders = async () => {
    console.log("In get orders");
    try {
      const response = await axios(
        `http://localhost:9080/api/customerOrders/getOrders`,
        {
          method: "GET",
          headers: {
            'Authorization': `Bearer ${userToken}`,
          },
        }
      );
      
      const orders: Order[] = response.data;
      console.log(orders)
      return orders;
    } catch (error) {
      console.log("Error:", error);
      const orders: Order[] = [];
      return orders;
    }
  };

  return (
    <div>
      <Navbar 
          firstName = {firstName}
          lastName = {lastName}
          isAdmin = {isAdmin}
          token = {userToken}
      />
      <OrdersList 
        getOrders={getOrders}
        deleteOrder={async () => "Not Required"}
        deleteOrderItem={async () => "Not Required"}
        updateOrderStatus={async () => "Not Required"}
        isAdmin={isAdmin}
        firstName={firstName}
        lastName={lastName}
        userToken={userToken} getSortedOrders={function (sortBy: any, sortOrder: any): Promise<Order[]> {
          throw new Error("Function not implemented.");
        } } getFilteredOrders={function (filter: FilterOrderDto): Promise<Order[]> {
          throw new Error("Function not implemented.");
        } }      
        />
    </div>
  );
};

export default CustomerOrders;
