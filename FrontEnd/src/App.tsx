import { useNavigate } from "react-router";
import "./App.css";
import BobUpWindow from "./Components/BobUpWindow";
import SignUp from "./Pages/SignUp";

function App() {
  const navigate = useNavigate();
  return (
    <>
      {/* <SignUp /> */}
      <BobUpWindow>
        <p>
          We've sent a verification email to you, please check it out to be able
          to sign in
        </p>
        <button
          type="button"
          className="btn btn-primary"
          style={{ marginLeft: 0 }}
          onClick={() => navigate("/signup")}
        >
          Ok
        </button>
      </BobUpWindow>
    </>
  );
}

export default App;
