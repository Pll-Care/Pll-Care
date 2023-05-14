const AllParticipants = ({ getSortedAverageEvaluationList }) => {
    return (
        <div className='evaluation-management-all-participants'>
            <h1>참여자 보기</h1>
            <div className='evaluation-management-participants'>
                {getSortedAverageEvaluationList().map((item) => (
                    <div className='evaluation-management-participant'>
                        <div className='evaluation-management-participant-left-col'>
                            <figure className='evaluation-management-user-profile' />
                        </div>
                        <div className='evaluation-management-participant-right-col'>
                            <div>{item.name}</div>
                            <div className='evaluation-management-badges'>
                                <figure />
                                <figure />
                                <figure />
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default AllParticipants;