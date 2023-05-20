const OverviewMeetingList = (props) => {
  return (
    <div className="meeting-list-wrapper">
      {props.datas.map((data) => (
        <div className="meeting-item">
          <h5>{data.time}</h5>
          <h3>{data.title}</h3>
          <h4>{data.writer}</h4>
        </div>
      ))}
    </div>
  );
};
export default OverviewMeetingList;
