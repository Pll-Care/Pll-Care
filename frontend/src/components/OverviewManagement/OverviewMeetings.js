import Card from '../shared/Card';
import OverviewMeetingList from "./OverviewMeetingList";

const bookmarks = [
  {
    id: 1,
    time: "2023.5.2",
    title: "즐겨찾기 회의록1",
    writer: "김철수",
  },
  {
    id: 2,
    time: "2023.3.27",
    title: "즐겨찾기 회의록22222222222222",
    writer: "김철수",
  },
  {
    id: 3,
    time: "2023.4.10",
    title: "즐겨찾기 회의록3",
    writer: "김철수",
  },
];

const writes = [
  {
    id: 1,
    time: "2023.5.2",
    title: "즐겨찾기 회의록1",
    writer: "김철수",
  },
  {
    id: 2,
    time: "2023.3.27",
    title: "즐겨찾기 회의록22222222222222",
    writer: "김철수",
  },
  {
    id: 3,
    time: "2023.4.10",
    title: "즐겨찾기 회의록3",
    writer: "김철수",
  },
];

const OverviewMeetings = () => {
  return (
    <Card className="overview-meetings">
      <h1>회의록</h1>
      <div className="overview-meetings-list">
        <div className="overview-meetings-list-bookmark">
          <h2>즐겨찾기</h2>
          <OverviewMeetingList datas={bookmarks} />
        </div>
        <div className="overview-meetings-list-current">
          <h2>최근 작성</h2>
          <OverviewMeetingList datas={writes} />
        </div>
      </div>
    </Card>
  );
};
export default OverviewMeetings;
