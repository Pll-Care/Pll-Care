import Button from "../Button";
import Card from "../Card";
import ScheduleList from "./ScheduleList";

const ScheduleOption = () => {
  return (
    <>
      <div className="schedule-option">
        <Button text={"전체"} />
        <Button text={"Plan"} />
        <Button text={"Meeting"} />
        <Button text={"지난 plan"} />
      </div>
      <Card className="schedule-option-person">
        <h4>참여자</h4>
        <Button text={"김철수"} />
        <Button text={"박영수"} />
        <Button text={"최민수"} />
        <Button text={"김영희"} />
        <Button text={"김민지"} />
      </Card>

      <ScheduleList />
    </>
  );
};
export default ScheduleOption;
