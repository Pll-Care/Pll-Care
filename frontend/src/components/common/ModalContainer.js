import { Box, Modal, useMediaQuery } from "@mui/material";

const ModalContainer = ({ children, open, onClose, type, width }) => {
  const color = type === "dark" ? "#01e89e" : "white";
  const isMobile = useMediaQuery("(max-width:600px)");
  const containerWidth = isMobile ? "90%" : width;
  const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: containerWidth,
    bgcolor: color,
    borderRadius: "20px",
    p: 4,
  };
  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>{children}</Box>
    </Modal>
  );
};

export default ModalContainer;
