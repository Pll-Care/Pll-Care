import profile_default from "../../assets/profile-default-img.png";

const MemberItem = ({ name, memberId, job, isEdit }) => {
  const deleteMember = () => {
    console.log(memberId);
  };
  return (
    <li className="member_item">
      <div className="member_item_imgbox">
        <div>
          <img
            src={profile_default}
            alt={`${name}의 프로필`}
            className="member_item_imgbox_img"
          />
          {isEdit ? (
            <div onClick={deleteMember} className="member_item_delete">
              팀원 삭제
            </div>
          ) : null}
        </div>
      </div>
      <div className="member_item_infobox">
        <p className="member_item_infobox_name">{name}</p>
        <p className="member_item_infobox_job">{job}</p>
      </div>
    </li>
  );
};

export default MemberItem;

// Todo 팀원의 프로필 이미지와 직무, 유저 아이디에 대한 데이터 필요
