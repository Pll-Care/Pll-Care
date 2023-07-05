export const getStringDate = (date) => {
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let day = date.getDate();

  if (month < 10) {
    month = `0${month}`;
  }

  if (day < 10) {
    day = `0${day}`;
  }

  return `${year}-${month}-${day}`;
};

// 오늘 날짜 영어로 표현하는 함수
export const getTodayDateEnglish = () => {
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
  return `${month} ${date}, ${dayOfWeek}`;
};

// 시작 일자와 종료 일자 기간을 표시하는 함수
// --월 --일 시간 ~ 시간 : type이 MEETING일 때
// --월 --일 ~ --월 --일 : type이 MILESTONE일 때
export const getDateTimeDuration = (startDate, endDate, type) => {
  const startDateTime = new Date(startDate);
  const endDateTime = new Date(endDate);
  if (type === "MILESTONE") {
    const startMonth = startDateTime.toLocaleString("default", {
      month: "short",
    });
    const startDay = startDateTime.getDate();

    const endMonth = endDateTime.toLocaleString("default", { month: "short" });
    const endDay = endDateTime.getDate();

    return `${startMonth} ${startDay}일 ~ ${endMonth} ${endDay}일`;
  }
  if (type === "MEETING") {
    const formattedStartDate = startDateTime.toLocaleString("default", {
      month: "short",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });

    const formattedEndDate = endDateTime.toLocaleString("default", {
      hour: "2-digit",
      minute: "2-digit",
    });

    return `${formattedStartDate} ~ ${formattedEndDate}`;
  }
};

export const getRemainDate = (date) => {
  const today = new Date();
  const end = new Date(date);

  // endDate의 시간을 0시 0분 0초로 설정하여 날짜만 비교
  const endDay = new Date(end.getFullYear(), end.getMonth(), end.getDate());
  const todayDay = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate()
  );

  const timeDiff = endDay.getTime() - todayDay.getTime();
  const remainingDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

  if (remainingDays < 0) {
    return "past";
  } else if (remainingDays === 0) {
    return "D-Day";
  } else {
    return `D-${remainingDays}`;
  }
};

export const getEnglishWeekdays = (date) => {
  const daysOfWeek = ["Sun", "Mon", "Tues", "Wednes", "Thurs", "Fri", "Satur"];
  const startDate = new Date(date);
  const dayOfWeek = daysOfWeek[startDate.getDay()];
  return `${dayOfWeek}`;
};
