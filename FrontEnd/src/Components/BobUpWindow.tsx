import "./BobUpWindow.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import React, { ReactNode, useState } from "react";

interface Props {
  children: ReactNode;
  setResponseStatus?: React.Dispatch<React.SetStateAction<string>>;
}

const BobUpWindow = ({ children, setResponseStatus }: Props) => {
  const [xClicked, setXclicked] = useState(true);

  return (
    <>
      <div
        className="light-blocker"
        id="light-blocker"
        style={{
          position: "absolute",
          width: "100vw",
          display: xClicked ? "block" : "none",
          height: "100vh",
          backgroundColor: "#000000",
          opacity: 0.3,
          top: 0,
          right: 0,
        }}
      ></div>

      <div
        className="bob-up-window"
        style={{ display: xClicked ? "block" : "none" }}
      >
        <div className="x-container">
          <button
            className="x-button"
            onClick={() => {
              setXclicked(false);
              setResponseStatus!("");
            }}
          >
            <FontAwesomeIcon icon={faXmark} />
          </button>
        </div>
        <div className="content">{children}</div>
      </div>
    </>
  );
};

export default BobUpWindow;
