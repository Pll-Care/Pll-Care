import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";

const CalendarItem = ({ data }) => {
  const location = data.address
    ? data.address.city + " " + data.address.street
    : "";

  const time = data.address
    ? data.startDate.slice(5, 7) +
      "월 " +
      data.startDate.slice(8, 10) +
      "일 " +
      data.startDate.slice(11, 16) +
      " ~ " +
      data.endDate.slice(11, 16)
    : data.startDate.slice(5, 7) +
      "월 " +
      data.startDate.slice(8, 10) +
      "일 " +
      " ~ " +
      data.endDate.slice(5, 7) +
      "월 " +
      data.endDate.slice(8, 10) +
      "일 ";
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
