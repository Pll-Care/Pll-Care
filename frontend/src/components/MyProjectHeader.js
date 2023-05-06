// components
import Button2 from "./Button2";

const MyProjectHeader = () => {
  return (
    <header className="myproject-header">
      <div className="myproject-header-left">
        <div className="myproject-header-left-participation-project">
          참여 프로젝트
        </div>
        <div className="myprojcet-header-left-new-project">
          <Button2
            text={"새 프로젝트 생성"}
            onClick={() => alert("새 프로젝트 버튼 클릭!")}
          />
        </div>
      </div>

      <div className="myproject-header-right">
        <div className="myproject-header-right-total-btn">
          <Button2 text={"전체"} onClick={() => alert("전체 버튼 클릭!")} />
        </div>
        <div className="myproject-header-right-ongoing-btn">
          <Button2 text={"진행중"} onClick={() => alert("진행 중 버튼 클릭")} />
        </div>
      </div>
    </header>
  );
};

export default MyProjectHeader;
