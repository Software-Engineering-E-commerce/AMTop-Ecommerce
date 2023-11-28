import { useNavigate } from "react-router";
import "./App.css";
import SignUp from "./Pages/SignUp";

function App() {
  const navigate = useNavigate();
  return (
    <>
      <SignUp />
    </>
  );
}

export default App;
