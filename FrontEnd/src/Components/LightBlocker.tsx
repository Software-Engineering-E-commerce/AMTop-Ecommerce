import React from "react";

const LightBlocker = () => {
  return (
    <div
      className="light-blocker"
      id="light-blocker"
      style={{
        position: "absolute",
        width: "100vw",
        height: "100vh",
        backgroundColor: "#000000",
        opacity: 0.3,
        top: 0,
        right: 0,
      }}
    ></div>
  );
};

export default LightBlocker;
