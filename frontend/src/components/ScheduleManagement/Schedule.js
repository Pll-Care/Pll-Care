import Button from "../Button";

const Schedule = (props) => {
  return (
    <div className="schedule-list">
      <div className="schedule-list-time">
        <h1>{props.data.day}</h1>
        <h2>{props.data.week}</h2>
        <Button text={"평가 작성"} />
      </div>
      <div className="schedule-list-content">
        <div className="schedule-list-content-time">
          <h5>{props.data.time}</h5>
          <h1>D-day</h1>
        </div>
        <h4>{props.data.title}</h4>
      </div>
    </div>
  );
};
export default Schedule;
