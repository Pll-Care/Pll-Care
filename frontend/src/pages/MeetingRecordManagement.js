import { createContext, useReducer, useRef, useState } from 'react';

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
      return state.filter((item) => parseInt(item.id) !== parseInt(action.targetId));
    }
    case 'EDIT': {
      return state.map((item) => parseInt(item.id) === parseInt(action.data.id) ? { ...action.data } : item);
    }
    case 'BOOKMARK': {
      return state.map((item) => parseInt(item.id) === parseInt(action.data.id) ? { ...action.data } : item);
    }
    default: {
      return state;
    }
  }
}

export const MeetingRecordStateContext = createContext();
export const MeetingRecordDispatchContext = createContext();
export const SelectedMeetingRecordStateContext = createContext();
export const SelectedMeetingRecordDispatchContext = createContext();
export const InitialStateContext = createContext();
export const InitialStateDispatchContext = createContext();
export const EditStateContext = createContext();
export const EditStateDispatchContext = createContext();

const MeetingRecordManagement = () => {
  const [meetingRecordList, meetingRecordDispatch] = useReducer(meetingRecordReducer, [
    {
      id: 1,
      writer: '홍서현',
      date: 1683162856400,
      title: '제목1',
      content: '안녕1',
      bookMarked: false,
    },
    {
      id: 2,
      writer: '김도연',
      date: 1683062857459,
      title: '제목2',
      content: '안녕2',
      bookMarked: false,
    },
    {
      id: 3,
      writer: '이연제',
      date: 1683292857459,
      title: '제목3',
      content: '안녕3',
      bookMarked: false,
    },
    {
      id: 4,
      writer: '조상욱',
      date: 1682962857459,
      title: '제목4',
      content: '안녕4',
      bookMarked: false,
    },
  ]);

  let meetingRecordId = useRef(5);

  const [selectedMeetingRecord, setSelectedMeetingRecord] = useState();

  const [initialState, setInitialState] = useState(true);

  const [editState, setEditState] = useState('beforeEdit');

  const [title, setTitle] = useState('');

  const [content, setContent] = useState('');

  // INITIALIZE 기능은 백엔드와 연결 시 추가 (useEffect 사용)

  const onCreateMeetingRecord = (writer, date, title, content) => {
    meetingRecordDispatch({
      type: 'CREATE',
      data: {
        id: meetingRecordId.current,
        writer,
        date: new Date(date).getTime(),
        title,
        content,
        bookMarked: false
      }
    });

    meetingRecordId.current += 1;
  }
  
  const onRemoveMeetingRecord = (targetId) => {
    meetingRecordDispatch({
      type: 'REMOVE', 
      targetId
    })
  }
  
  const onEditMeetingRecord = (targetId, writer, date, title, content, bookMarked) => {
    meetingRecordDispatch({
      type: 'EDIT',
      data: {
        id: targetId,
        writer,
        date: new Date(date).getTime(),
        title,
        content,
        bookMarked
      }
    })
  }

  const onBookMarkMeetingRecord = (targetId, writer, date, title, content, bookMarked) => {
    meetingRecordDispatch({
      type: 'BOOKMARK',
      data: {
        id: targetId,
        writer,
        date: new Date(date).getTime(),
        title,
        content,
        bookMarked
      }
    });
  }

  return (
    <div className='meeting-record-management'>
      <MeetingRecordStateContext.Provider value={meetingRecordList}>
        <MeetingRecordDispatchContext.Provider value={{
          onCreateMeetingRecord,
          onEditMeetingRecord,
          onRemoveMeetingRecord,
          onBookMarkMeetingRecord
        }}>
          <SelectedMeetingRecordStateContext.Provider value={selectedMeetingRecord}>
            <SelectedMeetingRecordDispatchContext.Provider value={{ setSelectedMeetingRecord }}>
              <InitialStateContext.Provider value={initialState}>
                <InitialStateDispatchContext.Provider value={{ setInitialState }}>
                  <EditStateContext.Provider value={{ editState, title, content }}>
                    <EditStateDispatchContext.Provider value={{ setEditState, setTitle, setContent }}>
                      <MeetingRecordList />
                      <MeetingRecordListEditor />
                    </EditStateDispatchContext.Provider>
                  </EditStateContext.Provider>
                </InitialStateDispatchContext.Provider>
              </InitialStateContext.Provider>
            </SelectedMeetingRecordDispatchContext.Provider>
          </SelectedMeetingRecordStateContext.Provider>
        </MeetingRecordDispatchContext.Provider>
      </MeetingRecordStateContext.Provider>
    </div>
  )
};

export default MeetingRecordManagement;
