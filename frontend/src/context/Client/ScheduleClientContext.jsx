import { createContext, useContext } from "react";

const ScheduleContext = createContext(null);
export const useScheduleClient = () => useContext(ScheduleContext);

const ScheduleClientProvider = ({ children, scheduleService }) => {
  const getOverviewAllSchedule =
    scheduleService.getOverviewAllSchedule.bind(scheduleService);

  const getCalendarAllSchedule =
    scheduleService.getCalendarAllSchedule.bind(scheduleService);

  const createNewSchedule =
    scheduleService.createNewSchedule.bind(scheduleService);

  const getFilterSchedule =
    scheduleService.getFilterSchedule.bind(scheduleService);

  const getTodayAfterSchedule =
    scheduleService.getTodayAfterSchedule.bind(scheduleService);

  const getDetailSchedule =
    scheduleService.getDetailSchedule.bind(scheduleService);

  const modifySchedule = scheduleService.modifySchedule.bind(scheduleService);

  const deleteSchedule = scheduleService.deleteSchedule.bind(scheduleService);

  const updateDoneSchedule =
    scheduleService.updateDoneSchedule.bind(scheduleService);

  const updateDoneShcedule =
    scheduleService.updateDoneShcedule.bind(scheduleService);

  const makeNewMidEvaluation =
    scheduleService.makeNewMidEvaluation.bind(scheduleService);

  const getTeamMember = scheduleService.getTeamMember.bind(scheduleService);

  return (
    <ScheduleContext.Provider
      value={{
        getOverviewAllSchedule,
        getCalendarAllSchedule,
        createNewSchedule,
        getFilterSchedule,
        getTodayAfterSchedule,
        getDetailSchedule,
        modifySchedule,
        deleteSchedule,
        updateDoneSchedule,
        updateDoneShcedule,
        makeNewMidEvaluation,
        getTeamMember,
      }}
    >
      {children}
    </ScheduleContext.Provider>
  );
};
export default ScheduleClientProvider;
