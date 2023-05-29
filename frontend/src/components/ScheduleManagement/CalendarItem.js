import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";

const CalendarItem = (props) => {
  const members = ["김철수", "박영수", "최민수", "김영희", "김민지"];
  return (
    <div className="calendar-list-item">
      <h5>{props.data.time}</h5>
      <h4>{props.data.title}</h4>
      <div className="calendar-list-item-name">
        <h5>스타벅스</h5>
        <div className="calendar-list-item-name-avatar">
          {members.map((member, index) => (
            <Tooltip key={index} title={member}>
              <Avatar />
            </Tooltip>
          ))}
        </div>
      </div>
    </div>
  );
};
export default CalendarItem;
