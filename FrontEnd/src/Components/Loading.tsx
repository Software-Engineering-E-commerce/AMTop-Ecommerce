import { PulseLoader } from "react-spinners";

interface Props{
    isLoading: boolean;
}

const Loading = ({isLoading}:Props) => {
  return (
    <>
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
      <div
        className="loadingDiv"
        style={{
          position: "fixed",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          zIndex: 12,
        }}
      >
        <PulseLoader
          color={"black"}
          loading={isLoading}
          size={30}
          aria-label="Loading Spinner"
          data-testid="loader"
        />
      </div>
    </>
  );
};

export default Loading;
