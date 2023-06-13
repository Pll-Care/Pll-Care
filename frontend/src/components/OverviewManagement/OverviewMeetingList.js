const OverviewMeetingList = (props) => {
  return (
    <div className="meeting-list-wrapper">
      {props.datas.map((data, index) => (
        <div className="meeting-item" key={index}>
          <h5>{data.time}</h5>
          <h3>{data.title}</h3>
          <h4>{data.writer}</h4>
        </div>
      ))}
    </div>
  );
};
export default OverviewMeetingList;
