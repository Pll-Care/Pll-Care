const makeDays = ({ week, year, month, firstDay, lastDate }) => {
  const dates = [];

  if (week === 1) {
    const prevLastDate = parseInt(new Date(year, month - 1, 0).getDate(), 10);
    for (let i = 1; i <= 7; i++) {
      if (i <= firstDay) {
        const now = prevLastDate - firstDay + i;
        dates.push({
          date: now,
          month: month - 1,
          thisMonth: false,
        });
      } else {
        const now = i - firstDay;
        dates.push({
          date: now,
          month: month,
          thisMonth: true,
        });
      }
    }
  } else {
    const startDate = (week - 1) * 7;
    for (let i = startDate; i <= week * 7 - 1; i++) {
      if (i - firstDay < lastDate) {
        const now = i - firstDay + 1;
        dates.push({
          date: now,
          month: month,
          thisMonth: true,
        });
      } else {
        const now = i - lastDate - firstDay + 1;
        dates.push({
          date: now,
          month: month + 1,
          thisMonth: false,
        });
      }
    }
  }
  return dates;
};

export default makeDays;
