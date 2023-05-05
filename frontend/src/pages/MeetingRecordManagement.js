import { createContext, useReducer, useRef } from 'react';

import MeetingRecordList from "../components/MeetingRecordList";
import MeetingRecordListEditor from "../components/MeetingRecordListEditor";

const meetingRecordReducer = (state, action) => {
  switch (action.type) {
    case 'INIT': {
      return action.data;
    }
    case 'CREATE': {
      return [...state, action.data];
    }
    case 'REMOVE': {
      return state.filter((item) => item.id !== action.targetId);
    }
    case 'EDIT': {
      return state.map((item) => item.id === action.data.id ? { ...action.data } : item);
    }
    default: {
      return state;
    }
  }
}

export const MeetingRecordStateContext = createContext();
export const MeetingRecordDispatchContext = createContext();

const MeetingRecordManagement = () => {
  const [meetingRecordList, meetingRecordDispatch] = useReducer(meetingRecordReducer, [
    {
      id: 0,
      writer: '홍서현',
      date: 1683162856400,
      title: '제목1',
      content: '안녕1'
    },
    {
      id: 1,
      writer: '김도연',
      date: 1683062857459,
      title: '제목2',
      content: '안녕2'
    },
    {
      id: 2,
      writer: '이연제',
      date: 1683292857459,
      title: '제목3',
      content: '안녕3'
    },
    {
      id: 3,
      writer: '조상욱',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 4,
      writer: '조상욱2',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 5,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 6,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 7,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 8,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 9,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 10,
      writer: '홍서현',
      date: 1683162856400,
      title: '제목1',
      content: '안녕1'
    },
    {
      id: 11,
      writer: '김도연',
      date: 1683062857459,
      title: '제목2',
      content: '안녕2'
    },
    {
      id: 12,
      writer: '이연제',
      date: 1683292857459,
      title: '제목3',
      content: '안녕3'
    },
    {
      id: 13,
      writer: '조상욱',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 14,
      writer: '조상욱2',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 15,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 16,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 17,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 18,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 19,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 20,
      writer: '홍서현',
      date: 1683162856400,
      title: '제목1',
      content: '안녕1'
    },
    {
      id: 21,
      writer: '김도연',
      date: 1683062857459,
      title: '제목2',
      content: '안녕2'
    },
    {
      id: 22,
      writer: '이연제',
      date: 1683292857459,
      title: '제목3',
      content: '안녕3'
    },
    {
      id: 23,
      writer: '조상욱',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 24,
      writer: '조상욱2',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 25,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 26,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 27,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 28,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
    {
      id: 29,
      writer: '조상욱3',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4'
    },
  ]);

  const meetingRecordId = useRef(0);

  // INITIALIZE 기능은 백엔드와 연결 시 추가 (useEffect 사용)

  const onCreateMeetingRecord = (writer, date, title, content) => {
    meetingRecordDispatch({
      type: 'CREATE',
      data: {
        id: meetingRecordId.current,
        writer,
        date: new Date(date).getTime(),
        title,
        content
      }
    });

    meetingRecordId += 1;
  }
  
  const onRemoveMeetingRecord = (targetId) => {
    meetingRecordDispatch({
      type: 'REMOVE', 
      targetId
    })
  }
  
  const onEditMeetingRecord = (targetId, writer, date, title, content) => {
    meetingRecordDispatch({
      type: 'EDIT',
      data: {
        id: targetId,
        writer,
        date: new Date(date).getTime(),
        title,
        content
      }
    })
  }

  return (
    <div className='meeting-record-management'>
      <MeetingRecordStateContext.Provider value={meetingRecordList}>
        <MeetingRecordDispatchContext.Provider value={{
          onCreateMeetingRecord,
          onEditMeetingRecord,
          onRemoveMeetingRecord
        }}> 
          <MeetingRecordList />
          <MeetingRecordListEditor />
        </MeetingRecordDispatchContext.Provider>
      </MeetingRecordStateContext.Provider>
    </div>
  )
};

export default MeetingRecordManagement;
