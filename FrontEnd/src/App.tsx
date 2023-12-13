import "./App.css";
import EditAddProduct from "./Components/EditAddProduct";
import SignUp from "./Pages/SignUp";

function App() {
  return (
    <>
      <EditAddProduct isEdit={false} show={true} />
    </>
  );
}

export default App;
