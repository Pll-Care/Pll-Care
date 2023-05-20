const datas = [
  {
    id: 1,
    time: "13:00 ~ 16:00",
    title: "프로젝트 전체 개발 회의",
  },
  {
    id: 2,
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
];

const CalendarList = () => {
  return (
    <div className="calendar-list">
      <div className="calendar-list-time">
        <h5>오늘</h5>
        <h1>March 24,Fri</h1>
      </div>
      {datas.map((data) => (
        <div className="calendar-list-item">
          <h5>{data.time}</h5>
          <h4>{data.title}</h4>
        </div>
      ))}
    </div>
  );
};
export default CalendarList;
