import { useState } from "react";
import ModalContainer from "./ModalContainer";
import { positionSelect } from "../../../utils/optionData";
import Select from "../Select";
import { toast } from "react-toastify";

const ChagePositionModal = ({
  onClose,
  open,
  name,
  clickHandler,
  width,
  position,
}) => {
  const [chagePosition, setChangePosition] = useState("");

  const clickButton = () => {
    if (!chagePosition) {
      toast.error("변경하실 직무를 선택해주세요.");
      return;
    }
    clickHandler(chagePosition);
  };
  return (
    <ModalContainer open={open} onClose={onClose} type="dark" width={width}>
      <div className="modal-position">
        <p className="modal-position-text">
          '{name}'의 포지션을 '{position}'에서
        </p>
        <div className="modal-position-box">
          <Select
            onChange={(value) => setChangePosition(value.target.value)}
            options={positionSelect}
          />
          <p className="modal-position-text">로 변경합니다.</p>
        </div>
        <div>
          <button className="modal-position-button" onClick={clickButton}>
            확인
          </button>
        </div>
      </div>
    </ModalContainer>
  );
};
export default ChagePositionModal;
