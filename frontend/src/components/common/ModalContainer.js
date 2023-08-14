import { Box, Modal } from "@mui/material";

const ModalContainer = ({
  children,
  open,
  onClose,
  type,
  width,
  height = "auto",
}) => {
  return (
    <Modal open={open} onClose={onClose}>
      <Box className={["modal", `modal_${type}`]} style={{ width, height }}>
        {children}
      </Box>
    </Modal>
  );
};

export default ModalContainer;
