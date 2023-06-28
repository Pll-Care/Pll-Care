import Button from "../../components/common/Button";
import useManagementMutation from "../../hooks/useManagementMutation";
import ModalContainer from "../common/ModalContainer";

const ProjectDeleteModal = ({
  projectId,
  deleteModalVisible,
  setDeleteModalVisible,
}) => {
  const { deleteMutate } = useManagementMutation();

  const handleModalClose = () => {
    setDeleteModalVisible(false);
  };

  const handleDeleteClick = (e) => {
    e.preventDefault();

    deleteMutate(projectId);
    setDeleteModalVisible(false);
  };

  return (
    <ModalContainer
      open={deleteModalVisible}
      onClose={handleModalClose}
      type={"dark"}
      width={"50%"}
      height={"60%"}
    >
      <div className="delete-project">
        <h1 className="delete-project-heading">
          정말 삭제하시겠습니까?
          <br />
          삭제 버튼을 누르시면 절대 복구하실 수 없습니다.
        </h1>
        <Button text={"삭제하기"} onClick={handleDeleteClick} />
      </div>
    </ModalContainer>
  );
};

export default ProjectDeleteModal;
