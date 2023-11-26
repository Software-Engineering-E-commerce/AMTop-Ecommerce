import React from "react";
import LightBlocker from "./LightBlocker";

const BobUpWindow = () => {
  return (
    <>
      <LightBlocker />
      <div
        style={{
          fontWeight: 500,
          position: "absolute",
          top: "50%",
          fontSize: "1.2em",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: "40vw",
          zIndex: 10000,
          borderRadius: "10px",
          backgroundColor: "red",
          height: "200px",
          display: "flex",
          flexDirection: "column",
        }}
      >
      </div>
    </>
  );
};

export default BobUpWindow;
