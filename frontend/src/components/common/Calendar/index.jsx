import Days from "./Days";
import ProfileInput from "../ProfileInput";
import CalendarHeader from "./CalendarHeader";
import useCalendar from "../../../hooks/useCalendar";
import React from "react";

const Calendar = ({ date = "", onChangeDate, modifyDate }) => {
  const {
    dateInfo,
    changeDate,
    onIncrease,
    onDecrease,
    isFocuse,
    changeFocuse,
  } = useCalendar({ date, onChangeDate });

  return (
    <div className="content-form-container">
      <div>
        <ProfileInput
          value={
            !modifyDate
              ? modifyDate
              : `${dateInfo?.year}.${dateInfo?.month}.${dateInfo?.day}`
          }
          readOnly={true}
          placeholder="yyyy.mm.dd"
          onClick={changeFocuse}
        />
        {isFocuse ? (
          <div className="calendar">
            <div>
              <CalendarHeader
                dateInfo={dateInfo}
                changeDate={changeDate}
                onIncrease={onIncrease}
                onDecrease={onDecrease}
              />
              <Days
                dateInfo={dateInfo}
                changeFocuse={changeFocuse}
                changeDate={changeDate}
                onIncrease={onIncrease}
                onDecrease={onDecrease}
              />
            </div>
          </div>
        ) : null}
      </div>
    </div>
  );
};

export default React.memo(Calendar);
