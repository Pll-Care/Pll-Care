import { Avatar } from "@mui/material";
import Button from "../Button";

const Schedule = (props) => {
  return (
    <div className="schedule-list">
      <div className="schedule-list-time">
        <h1>{props.data.day}</h1>
        <h2>{props.data.week}</h2>
        <Button text={"평가 작성"} type="positive" />
      </div>

      <div className="schedule-list-content">
        <h5>{props.data.time}</h5>
        <div className="schedule-list-content-time">
          <h4>{props.data.title}</h4>
          <h1>D-day</h1>
        </div>

        <div className="schedule-list-content-option">
          <h5>2023.04.23 수정</h5>
          <Avatar />
          <Avatar />
          <Avatar />
          <Avatar />
          <Avatar />
        </div>
      </div>
    </div>
  );
};
export default Schedule;
