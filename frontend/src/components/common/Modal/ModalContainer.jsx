import { Box, Modal } from "@mui/material";

const ModalContainer = ({
  children,
  open,
  onClose,
  type,
  width,
  height = "auto",
  padding = "auto",
  border = "auto",
}) => {
  return (
    <Modal open={open} onClose={onClose}>
      <Box
        className={["modal", `modal_${type}`]}
        style={{ width, height, padding, borderRadius: border }}
      >
        {children}
      </Box>
    </Modal>
  );
};

export default ModalContainer;
