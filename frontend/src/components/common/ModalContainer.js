import { Box, Modal, useMediaQuery } from "@mui/material";

const ModalContainer = ({ children, open, onClose, type, width, height }) => {
  const color = type === "dark" ? "#01e89e" : "white";
  const isMobile = useMediaQuery("(max-width:600px)");
  const containerWidth = isMobile ? "90%" : width;
  const containerHeight = isMobile ? "60%" : height;
  const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: containerWidth,
    height: containerHeight,
    bgcolor: color,
    borderRadius: "20px",
    p: 4,
    // 스크롤 추가
    overflow: "auto",
  };
  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>{children}</Box>
    </Modal>
  );
};

export default ModalContainer;
