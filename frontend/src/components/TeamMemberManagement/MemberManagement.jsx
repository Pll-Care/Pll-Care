import { useCallback, useState } from "react";
import Button from "../common/Button";
import MemberItem from "./MemberItem";

const dummy = [
  { memberId: 0, name: "김철수", job: "Front-End" },
  { memberId: 1, name: "김현학", job: "Front-End" },
  { memberId: 2, name: "김하영", job: "Back-End" },
  { memberId: 3, name: "김아름", job: "Back-End" },
  { memberId: 4, name: "김현수", job: "Front-End" },
  { memberId: 5, name: "김나영", job: "Back-End" },
];

const MemberManagement = () => {
  const [isEdit, setIsEdit] = useState(false);
  const editMember = useCallback(() => {
    setIsEdit(true);
  }, []);

  const editCompleted = useCallback(() => {
    setIsEdit(false);
  }, []);
  return (
    <section className="memberMangement">
      <div className="memberMangement_head">
        <span className="teamMember_title">팀원 관리</span>
        {isEdit ? (
          <Button text="수정완료" size="small" onClick={editCompleted} />
        ) : (
          <Button text="수정" size="small" onClick={editMember} />
        )}
      </div>
      <div>
        <ul className="memberMangement_memebers">
          {dummy.map((member) => (
            <MemberItem
              key={member.memberId}
              memberId={member.memberId}
              name={member.name}
              job={member.job}
              isEdit={isEdit}
            />
          ))}
        </ul>
      </div>
    </section>
  );
};
export default MemberManagement;
