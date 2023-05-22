import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import CalendarList from "./CalendarList";

const MyCalendar = () => {
  const startDate = new Date(2023, 4, 10);
  const endDate = new Date(2023, 4, 15);

  const datePickerClasses = {
    start: "start",
    end: "end",
  };

  const customDatePickerStyles = `
      .start {
        background-color: #00aa72 !important;
        border-radius: 20% !important;
      }
      .end {
        background-color: #00aa72 !important;
        border-radius: 20% !important;
      }
    `;
  return (
    <div className="my-datepicker">
      <style>{customDatePickerStyles}</style>
      <DatePicker
        calendarClassName="custom-calendar"
        showDisabledMonthNavigation={true}
        formatWeekDay={(nameOfDay) => nameOfDay.substring(0, 1)}
        selected={null}
        inline
        fixedHeight
        dayClassName={(date) => {
          if (date.getTime() === startDate.getTime()) {
            return datePickerClasses.start;
          }
          if (date.getTime() === endDate.getTime()) {
            return datePickerClasses.end;
          }
          return "";
        }}
      />
      <div className="my-calendar-list">
        <CalendarList />
      </div>
    </div>
  );
};
export default MyCalendar;
