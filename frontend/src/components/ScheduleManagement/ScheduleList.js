import Card from "../Card";
import Schedule from "./Schedule";

const datas = [
  {
    id: 1,
    day: 24,
    week: "Fri",
    time: "13:00 ~ 16:00",
    title: "프로젝트 전체 개발 회의",
  },
  {
    id: 2,
    day: 26,
    week: "Sat",
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
];
const ScheduleList = () => {
  return (
    <Card>
      {datas.map((data) => (
        <div>
          <Schedule data={data} />
        </div>
      ))}
    </Card>
  );
};
export default ScheduleList;
