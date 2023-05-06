const AllMeetingRecordListData = ({sortedMeetingRecordList, isLoading}) => {
    return (
        <div className='meeting-record-all-meeting-record-list-data'>
            {sortedMeetingRecordList().map((record) => (
                <div
                    className='meeting-record-all-meeting-record-list-record-item meeting-record-item'
                    key={record.id}
                >
                    <div className='meeting-record-item-date'>
                        {new Date(record.date).toLocaleDateString()}
                    </div>
                    <div className='meeting-record-item-title'>
                        {record.title}
                    </div>
                    <div className='meeting-record-item-writer'>
                        {record.writer}
                    </div>
                </div>
            ))}
        </div>
    )
}

export default AllMeetingRecordListData;