import "./App.css";
import EditAddProduct, { Product } from "./Components/EditAddProduct";
import SignUp from "./Pages/SignUp";

function App() {
  let adminT: string =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbXRvcGVjb21tZXJjZTEyM0BnbWFpbC5jb20iLCJpYXQiOjE3MDI1MDYyMzEsImV4cCI6MTcwMjU5MjYzMX0.fO6_2IQFCv_h5f33syYBECdQSObjWVtGw_l8enUH26s";

  const prod: Product = {
    // Use the Product type as the value for prod
    id: "9",
    name: "Adel",
    price: "500",
    description: "hefhjlh",
    countAvailable: "10",
    brand: "DELL",
    discountPercentage: "20",
    category: "Electronics",
  };

  return (
    // <>
    <EditAddProduct adminToken={adminT} isEdit={true} product={prod} />
    // </>
    // <SignUp/>
  );
}

export default App;
