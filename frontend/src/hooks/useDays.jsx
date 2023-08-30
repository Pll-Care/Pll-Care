import { useEffect, useState } from "react";
import makeDays from "../utils/calendar/makeDays";

const useDays = ({ year, month, firstDay, lastDate }) => {
  const [days, setDays] = useState([]);

  useEffect(() => {
    const calendarDate = [];
    const weeks = Math.ceil((firstDay + lastDate) / 7);

    for (let i = 1; i <= weeks; i++) {
      calendarDate.push(makeDays({ week: i, year, month, firstDay, lastDate }));
    }

    setDays([...calendarDate]);
  }, [year, month, firstDay, lastDate]);

  return { days };
};

export default useDays;
