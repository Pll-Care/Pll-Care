import { useQuery } from "react-query";
import { useParams } from "react-router";

import Card from "../common/Card";
import { getFilterSchedule } from "../../lib/apis/scheduleManagementApi";
import {
  getAfterScheduleData,
  getCombineSortedPlanMeeting,
  getPastScheduleData,
} from "../../utils/schedule";
import ScheduleItem from "./ScheduleItem";

// 더미 데이터
const data1 = [
  {
    scheduleId: 1,
    title: "string1",
    startDate: "2023-04-28T13:33:35.935Z",
    endDate: "2023-04-30T13:33:35.935Z",
    scheduleCategory: "MILESTONE",
    members: [
      {
        id: 1,
        name: "string1",
        imageUrl: "string1",
      },
      {
        id: 2,
        name: "string2",
        imageUrl: "string1",
      },
      {
        id: 3,
        name: "string3",
        imageUrl: "string1",
      },
      {
        id: 4,
        name: "string4",
        imageUrl: "string1",
      },
    ],
    state: "TBD",
    modifyDate: "2023-06-28",
    check: true,
    evaluationRequired: true,
  },
  {
    scheduleId: 2,
    title: "string1",
    startDate: "2023-05-28T13:33:35.935Z",
    endDate: "2023-05-30T13:33:35.935Z",
    scheduleCategory: "MILESTONE",
    members: [
      {
        id: 1,
        name: "string1",
        imageUrl: "string1",
      },
      {
        id: 3,
        name: "string3",
        imageUrl: "string1",
      },
    ],
    state: "ONGOING",
    modifyDate: "2023-06-28",
    check: true,
    evaluationRequired: true,
  },
  {
    scheduleId: 3,
    title: "string1",
    startDate: "2023-06-28T13:33:35.935Z",
    endDate: "2023-07-30T13:33:35.935Z",
    scheduleCategory: "MILESTONE",
    members: [
      {
        id: 1,
        name: "string1",
        imageUrl: "string1",
      },
      {
        id: 2,
        name: "string2",
        imageUrl: "string1",
      },
      {
        id: 3,
        name: "string3",
        imageUrl: "string1",
      },
    ],
    state: "ONGOING",
    modifyDate: "2023-06-28",
    check: true,
    evaluationRequired: true,
  },
  {
    scheduleId: 4,
    title: "string2",
    startDate: "2023-06-29T13:33:35.935Z",
    endDate: "2023-06-30T13:33:35.935Z",
    scheduleCategory: "MILESTONE",
    members: [
      {
        id: 1,
        name: "string1",
        imageUrl: "string1",
      },
      {
        id: 2,
        name: "string2",
        imageUrl: "string1",
      },
    ],
    state: "TBD",
    modifyDate: "2023-06-29",
    check: true,
    evaluationRequired: true,
  },
  {
    scheduleId: 5,
    title: "string3",
    startDate: "2023-07-08T13:33:35.935Z",
    endDate: "2023-07-28T13:33:35.935Z",
    scheduleCategory: "MILESTONE",
    members: [
      {
        id: 2,
        name: "string2",
        imageUrl: "string2",
      },
      {
        id: 3,
        name: "string3",
        imageUrl: "string3",
      },
      {
        id: 4,
        name: "string4",
        imageUrl: "string4",
      },
    ],
    state: "ONGOING",
    modifyDate: "2023-06-28",
    check: true,
    evaluationRequired: true,
  },
];
const ScheduleList = ({ nameId, option }) => {
  // 통신해서 리스트 가져오기
  let type;
  type = (option === "Plan" || option === "지난 Plan") && "MEETING";
  type = (option === "Meeting" || option === "지난 Meeting") && "MILESTONE";

  const { id } = useParams();

  const { data } = useQuery("FilterSchedule", async () => {
    if (option === "ALL") {
      const milestones = await getFilterSchedule(id, nameId, "MILESTONE");
      const meetings = await getFilterSchedule(id, nameId, "MEETING");
      return getCombineSortedPlanMeeting(milestones, meetings);
    } else {
      return getFilterSchedule(id, nameId, type);
    }
  });
  console.log(data);

  // 과거 일정 필터
  const filteredPastData = getPastScheduleData(data1);

  // 오늘 이후 일정 필터
  const filteredAfterData = getAfterScheduleData(data1);

  return (
    <Card>
      {/*{option === "ALL" &&
        data &&
        data?.map((schedule, index) => (
          <ScheduleItem key={index} data={schedule} />
        ))}*/}
      {(option === "Plan" || option === "Meeting") &&
        filteredAfterData?.map((schedule, index) => (
          <ScheduleItem key={index} data={schedule} />
        ))}
      {(option === "지난 Plan" || option === "지난 Meeting") &&
        filteredPastData?.map((schedule, index) => (
          <ScheduleItem key={index} data={schedule} />
        ))}
    </Card>
  );
};
export default ScheduleList;
