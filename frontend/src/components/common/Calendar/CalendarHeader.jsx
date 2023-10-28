import { months, years } from "../../../utils/calendar/monthsAndYearsData";
import YearAndMonth from "./YearAndMonth";

const CalendarHeader = ({ dateInfo, changeDate, onIncrease, onDecrease }) => {
  return (
    <div className="year-month">
      <div>
        <div className="year-month-button" onClick={onDecrease}>
          {"<"}
        </div>
      </div>
      <div className="selectors">
        <YearAndMonth
          type="year"
          defaultValue={dateInfo.year}
          options={years}
          changeDate={changeDate}
        />
        <YearAndMonth
          type="month"
          defaultValue={dateInfo.month}
          options={months}
          changeDate={changeDate}
        />
      </div>
      <div className="year-month-button" onClick={onIncrease}>
        {">"}
      </div>
    </div>
  );
};

export default CalendarHeader;
