import CircularProgress from "@mui/material/CircularProgress";

export const Loading = () => {
  return (
    <div className="loading-indicator-container">
      <CircularProgress className="loading-indicator" />
    </div>
  );
};