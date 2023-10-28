import React, { useState } from "react";
import profile_default from "../../assets/profile-default-img.png";
import Button from "../common/Button";
import { Link } from "react-router-dom";
import useModalIsOpen from "../../hooks/useModalIsOpen";
import AlertCheckModal from "../common/Modal/AlertCheckModal";

import { useProjectDetail } from "../../context/ProjectDetailContext";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const ApplicationItem = ({
  name,
  memberId,
  position,
  imageUrl,
  postId,
  refetch,
}) => {
  const [message, setMessage] = useState("");
  const { isOpen, chageModalOpen } = useModalIsOpen();
  const { projectId, isLeader } = useProjectDetail();
  const { postApplyAcceptAPI, postApplyRejectAPI } = useManagementClient();

  const openAcceptModal = () => {
    setMessage("수락");
    chageModalOpen(true);
  };
  const openRejectModal = () => {
    setMessage("거절");
    chageModalOpen(true);
  };

  const applyAccept = async () => {
    try {
      const response = await postApplyAcceptAPI(projectId, memberId, postId);
      if (response.status === 200) refetch();
    } catch (error) {
      console.log(error);
    }
  };
  const applyReject = async () => {
    try {
      const response = await postApplyRejectAPI(projectId, memberId, postId);
      if (response.status === 200) refetch();
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <AlertCheckModal
        open={isOpen}
        onClose={() => {
          chageModalOpen(false);
          setMessage("");
        }}
        text={`${name}님의 지원을 ${message}하시겠습니까?`}
        clickHandler={message === "수락" ? applyAccept : applyReject}
      />
      <li className="application_item">
        <Link
          to={`/profile/${memberId}/introduce`}
          className="application_item_link"
        >
          <div className="application_item_imgbox">
            <img
              src={!!imageUrl ? imageUrl : profile_default}
              alt={`${name}의 프로필`}
            />
          </div>
          <div className="application_item_infobox">
            <p className="application_item_infobox_name">{name}</p>
            <p className="application_item_infobox_job">{position}</p>
          </div>
        </Link>
        {isLeader && (
          <div className="application_item_btnbox">
            <Button
              text="수락"
              type="positive"
              color="white"
              size="small"
              onClick={openAcceptModal}
            />
            <Button
              text="거절"
              type="positive"
              color="white"
              size="small"
              onClick={openRejectModal}
            />
          </div>
        )}
      </li>
    </>
  );
};

export default React.memo(ApplicationItem);
