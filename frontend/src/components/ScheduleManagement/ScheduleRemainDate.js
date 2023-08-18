import { getRemainDate } from "../../utils/date";

const ScheduleRemainDate = ({ startDate }) => {
  const remainDate = getRemainDate(startDate);
  return (
    <div className="schedule-list-content-time">
      {remainDate !== "past" && <h1>{remainDate}</h1>}
    </div>
  );
};
export default ScheduleRemainDate;
