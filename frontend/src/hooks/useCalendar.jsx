import { useEffect, useState } from "react";

const useCalendar = ({ date = "", onChangeDate }) => {
  const [dateInfo, setDateInfo] = useState({});
  const [isFocuse, setIsFocuse] = useState(false);
  const changeDate = (info) => {
    setDateInfo((prev) => ({ ...prev, ...info }));
  };

  const onIncrease = () => {
    if (dateInfo.month < 11) {
      setDateInfo((prev) => ({ ...prev, month: prev.month + 1 }));
    } else {
      setDateInfo((prev) => ({ ...prev, month: 1, year: prev.year + 1 }));
    }
  };

  const onDecrease = () => {
    if (dateInfo.month > 1) {
      setDateInfo((prev) => ({ ...prev, month: prev.month - 1 }));
    } else {
      setDateInfo((prev) => ({ ...prev, month: 12, year: prev.year - 1 }));
    }
  };

  const changeFocuse = () => {
    setIsFocuse((prev) => !prev);
  };

  useEffect(() => {
    const newDate = !!date ? new Date(date) : new Date();
    const year = newDate.getFullYear();
    const month = newDate.getMonth() + 1;
    const day = newDate.getDate();

    const lastDate = parseInt(new Date(year, month, 0).getDate(), 10);
    const firstDay = parseInt(new Date(year, month - 1, 1).getDay(), 10);

    setDateInfo({ year, month, day, lastDate, firstDay });
  }, [date]);

  useEffect(() => {
    const lastDate = parseInt(
      new Date(dateInfo.year, dateInfo.month, 0).getDate(),
      10
    );
    const firstDay = parseInt(
      new Date(dateInfo.year, dateInfo.month - 1, 1).getDay(),
      10
    );

    if (lastDate && firstDay)
      setDateInfo((prev) => ({ ...prev, lastDate, firstDay }));
  }, [dateInfo.month, dateInfo.year]);

  useEffect(() => {
    const newDate = {
      year: String(dateInfo.year),
      month:
        dateInfo.month < 10
          ? "0" + String(dateInfo.month)
          : String(dateInfo.month),
      day:
        dateInfo.day < 10 ? "0" + String(dateInfo.day) : String(dateInfo.day),
    };

    onChangeDate(newDate);
  }, [dateInfo, onChangeDate]);

  return {
    dateInfo,
    changeDate,
    onIncrease,
    onDecrease,
    changeFocuse,
    isFocuse,
  };
};

export default useCalendar;
