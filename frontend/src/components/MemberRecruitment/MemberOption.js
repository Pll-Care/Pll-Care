import { Link } from "react-router-dom";

import Button from "../common/Button";
import MemberFilterOption from "./MemberFilterOption";

const MemberOption = () => {
  return (
    <>
      <div className="member-option">
        <div className="member-option-title">
          <h2>모집 중인 프로젝트 목록</h2>
          <h3>Currently Recruiting</h3>
        </div>
      </div>
      <div className="member-button">
        <Link to="/recruitment/post">
          <Button text="작성하기" type="positive" />
        </Link>
        <MemberFilterOption />
      </div>
    </>
  );
};
export default MemberOption;
