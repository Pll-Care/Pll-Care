// 오늘 이전 일정을 return
export const getPastScheduleData = (data) => {
  const today = new Date();
  const filteredPastData = data.filter((item) => {
    const endDate = new Date(item.endDate);
    return endDate < today;
  });
  return filteredPastData;
};

// 오늘 이후 일정을 return
export const getAfterScheduleData = (data) => {
  const today = new Date();
  const filteredAfterData = data.filter((item) => {
    const endDate = new Date(item.endDate);
    return endDate >= today;
  });
  return filteredAfterData;
};

// 계획과 회의를 시간 순으로 정렬하는 함수
export const getCombineSortedPlanMeeting = (plan, meeting) => {
  const sortedSchedules = [...plan, ...meeting].sort((a, b) => {
    const startDateA = new Date(a.startDate);
    const startDateB = new Date(b.startDate);
    return startDateA - startDateB;
  });
  return sortedSchedules;
};
