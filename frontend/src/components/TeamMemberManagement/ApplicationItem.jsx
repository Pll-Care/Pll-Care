import profile_default from "../../assets/profile-default-img.png";
import Button from "../common/Button";

const ApplicationItem = ({ name, userId, job }) => {
  const addMember = () => {
    console.log(userId);
  };
  return (
    <li className="application_item">
      <div className="application_item_imgbox">
        <img src={profile_default} alt={`${name}의 프로필`} />
      </div>
      <div className="application_item_infobox">
        <p className="application_item_infobox_name">{name}</p>
        <p className="application_item_infobox_job">{job}</p>
      </div>
      <div className="application_item_btnbox">
        <Button text="팀원 추가" size="small" onClick={addMember} />
      </div>
    </li>
  );
};

export default ApplicationItem;
