import Button from "../common/Button";
import useManagementMutation from "../../hooks/useManagementMutation";
import ModalContainer from "../common/ModalContainer";

const ProjectButtonModal = ({
  type,
  projectId,
  modalVisible,
  setModalVisible,
}) => {
  const { deleteMutate } = useManagementMutation();

  const handleModalClose = () => {
    setModalVisible(false);
  };

  const handleDeleteClick = () => {
    deleteMutate(projectId);

    setModalVisible(false);
  };

  const handleLeaveClick = () => {};

  return (
    <ModalContainer
      open={modalVisible}
      onClose={handleModalClose}
      type={"dark"}
      width={"50%"}
      height={"60%"}
    >
      <div className="delete-project">
        <h1 className="delete-project-heading">
          정말 {type}하시겠습니까?
          <br />
          {type} 버튼을 누르시면 절대 복구하실 수 없습니다.
        </h1>
        <Button
          text={`${type}하기`}
          onClick={type === "삭제" ? handleDeleteClick : handleLeaveClick}
        />
      </div>
    </ModalContainer>
  );
};

export default ProjectButtonModal;
