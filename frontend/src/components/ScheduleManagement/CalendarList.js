import { Avatar } from "@mui/material";

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
  {
    id: 3,
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
  {
    id: 4,
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
  {
    id: 5,
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
  {
    id: 6,
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
  {
    id: 7,
    time: "13:00 ~ 16:00",
    title: "디자인팀 중간 회의",
  },
];

const CalendarList = () => {
  const today = new Date();

  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  const month = months[today.getMonth()];

  const daysOfWeek = ["Sun", "Mon", "Tues", "Wednes", "Thurs", "Fri", "Satur"];
  const dayOfWeek = daysOfWeek[today.getDay()];

  const date = today.getDate();

  const calendar = `${month} ${date}, ${dayOfWeek}`;

  return (
    <div className="calendar-list">
      <div className="calendar-list-time">
        <h5>오늘</h5>
        <h1>{calendar}</h1>
      </div>
      {datas.map((data) => (
        <div className="calendar-list-item">
          <h5>{data.time}</h5>
          <h4>{data.title}</h4>
          <div className="calendar-list-item-name">
            <h5>스타벅스</h5>
            <div className="calendar-list-item-name-avatar">
              <Avatar />
              <Avatar />
              <Avatar />
              <Avatar />
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
export default CalendarList;
