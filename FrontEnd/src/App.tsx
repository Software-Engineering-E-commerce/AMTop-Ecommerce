import "./App.css";
import EditAddProduct from "./Components/EditAddProduct";
import SignUp from "./Pages/SignUp";

function App() {
  let adminT:string = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbXRvcGVjb21tZXJjZTEyM0BnbWFpbC5jb20iLCJpYXQiOjE3MDI1MDYyMzEsImV4cCI6MTcwMjU5MjYzMX0.fO6_2IQFCv_h5f33syYBECdQSObjWVtGw_l8enUH26s"
  return (
    // <>
      <EditAddProduct adminToken={adminT} isEdit={false} show={true} />
    // </>
    // <SignUp/>
  );
}

export default App;
