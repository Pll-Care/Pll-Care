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
  {
    id: 3,
    day: 26,
    week: "Sat",
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
  {
    id: 4,
    day: 26,
    week: "Sat",
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
  {
    id: 5,
    day: 26,
    week: "Sat",
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
  {
    id: 6,
    day: 26,
    week: "Sat",
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
];
const ScheduleList = (props) => {
  console.log(props.name);
  console.log(props.option);
  // 통신해서 리스트 가져오기

  return (
    <Card>
      <div className="schedule">
        {datas.map((data) => (
          <Schedule data={data} />
        ))}
      </div>
    </Card>
  );
};
export default ScheduleList;
