import PositionImage from "../../assets/exchange.png";
import DeleteImage from "../../assets/ban-user.png";
import DelegateImage from "../../assets/social-network.png";
import { useParams } from "react-router";
import { useState } from "react";
import useModalIsOpen from "../../hooks/useModalIsOpen";
import ChagePositionModal from "../common/Modal/ChangePositionModal";
import { useRouter } from "../../hooks/useRouter";
import { toast } from "react-toastify";
import AlertCheckModal from "../common/Modal/AlertCheckModal";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const EditTeamMember = ({ memberId, refetch, name, position }) => {
  const { id: projectId } = useParams();
  const [message, setMessage] = useState("");
  const { isOpen: openPositionModal, chageModalOpen: chagePositionModal } =
    useModalIsOpen();
  const { isOpen, chageModalOpen } = useModalIsOpen();
  const { deleteKickoutAPI, putLeaderChangeAPI, putPositionChangeAPI } =
    useManagementClient();
  const { routeTo } = useRouter();

  const openDeleteModal = () => {
    setMessage("삭제");
    chageModalOpen(true);
  };
  const openLeaderChangeModal = () => {
    setMessage("위임");
    chageModalOpen(true);
  };

  const chagePosition = async (position) => {
    try {
      const response = await putPositionChangeAPI(
        projectId,
        memberId,
        position
      );
      if (response.status === 200) {
        refetch();
        chagePositionModal(false);
        toast.success("변경되었습니다.");
      }
    } catch (error) {
      console.log(error);
    }
  };

  const deleteMember = async () => {
    try {
      const response = await deleteKickoutAPI(projectId, memberId);
      if (response.status === 200) {
        refetch();
        chagePositionModal(false);
        toast.success("삭제되었습니다.");
      }
    } catch (error) {
      console.log(error);
    }
  };

  const chageLeader = async () => {
    try {
      const response = await putLeaderChangeAPI(projectId, memberId);
      if (response.status === 200) {
        routeTo(`/management/${projectId}`);
        toast.success("변경되었습니다.");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <ChagePositionModal
        open={openPositionModal}
        onClose={() => {
          chagePositionModal(false);
        }}
        clickHandler={chagePosition}
        name={name}
        position={position}
      />
      <AlertCheckModal
        open={isOpen}
        onClose={() => {
          chageModalOpen(false);
          setMessage("");
        }}
        text={
          message === "삭제"
            ? `${name}님을 팀원에서 삭제하시겠습니까?`
            : `${name}님을 새로운 리더로 임명하시겠습니까?`
        }
        clickHandler={message === "삭제" ? deleteMember : chageLeader}
      />
      <div className="member_item_delete">
        <ul className="edit_option">
          {EditOptionData.map((option) => (
            <EditOption
              key={option.title}
              imageUrl={option.imageUrl}
              title={option.title}
              chagePositionModal={chagePositionModal}
              openDeleteModal={openDeleteModal}
              openLeaderChangeModal={openLeaderChangeModal}
            />
          ))}
        </ul>
      </div>
    </>
  );
};

export default EditTeamMember;

const EditOption = ({
  imageUrl,
  title,
  chagePositionModal,
  openLeaderChangeModal,
  openDeleteModal,
}) => {
  return (
    <li
      className="edit_option_item"
      onClick={() => {
        title === "포지션"
          ? chagePositionModal(true)
          : title === "삭제"
          ? openDeleteModal()
          : openLeaderChangeModal();
      }}
    >
      <div>
        <img src={imageUrl} alt={title} className="edit_option_item_image" />
      </div>
      <p className="edit_option_item_title">{title}</p>
    </li>
  );
};

const EditOptionData = [
  {
    imageUrl: PositionImage,
    title: "포지션",
  },
  {
    imageUrl: DeleteImage,
    title: "삭제",
  },
  {
    imageUrl: DelegateImage,
    title: "위임",
  },
];
