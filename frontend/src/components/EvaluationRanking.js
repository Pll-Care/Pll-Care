const EvaluationRanking = ({ getSortedAverageEvaluationList }) => {
    let i = 0;

    return (
        <div className='evaluation-management-evaluation-ranking'>
            <h1 className='evaluation-management-evaluation-ranking-header'>기여도 랭킹</h1>
            <div className='evaluation-management-evaluation-ranking-body'>
                {getSortedAverageEvaluationList().map((item) => {
                    i++; 

                    return (
                        <div
                            className={[`evaluation-management-evaluation-ranking-item`,
                                `evaluation-management-evaluation-ranking-item_${i}`].join(' ')}
                            key={i}
                        >
                            <div className='evaluation-management-left-col'>
                                <figure />
                                <div className='ranking'>{i}위</div>
                            </div>
                            <div className='evaluation-management-right-col'>
                                <div className='name'>{item.name}</div>
                                <div className='score'>전체 평점: {item.averageEvaluation} / 5.0</div>
                            </div>
                        </div>
                    )
                })}
            </div>
        </div>
    )
}

export default EvaluationRanking;