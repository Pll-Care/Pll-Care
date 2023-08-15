import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";

import { getDateTimeDuration } from "../../utils/date";

const CalendarItem = ({ data }) => {
  const location = data.address ? data.address : "";
  const type = location ? "MEETING" : "MILESTONE";
  const time = getDateTimeDuration(data.startDate, data.endDate, type);
  return (
    <div className="calendar-list-item">
      {data.address ? (
        <h5 style={{ color: "#00aa72" }}>Meeting</h5>
      ) : (
        <h5 style={{ color: "#00aa72" }}>Plan</h5>
      )}
      <h5>{time}</h5>
      <h4>{data.title}</h4>
      <div className="calendar-list-item-name">
        <h5>{location}</h5>
        <div className="calendar-list-item-name-avatar">
          {data.members.map((member, index) => (
            <Tooltip key={index} title={member.name}>
              <Avatar src={member.imageUrl} />
            </Tooltip>
          ))}
        </div>
      </div>
    </div>
  );
};
export default CalendarItem;
