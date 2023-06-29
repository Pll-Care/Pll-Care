const EvaluationRanking = ({ rankingData }) => {
  const sortedTop3RankingData = rankingData.filter((dataItem) => dataItem.rank < 4).sort((a, b) => a.rank - b.rank);

  return (
    <div className="evaluation-management-evaluation-ranking">
      <h1 className="evaluation-management-evaluation-ranking-header">
        배지 랭킹
      </h1>
      <div className="evaluation-management-evaluation-ranking-body">
        {sortedTop3RankingData.map((item) => {
          return (
            <div
              className={[
                `evaluation-management-evaluation-ranking-item`,
                `evaluation-management-evaluation-ranking-item_${item.rank}`,
              ].join(" ")}
              key={item.rank}
            >
              <div className="evaluation-management-left-col">
                <figure />
                <div className="ranking">{item.rank}위</div>
              </div>
              <div className="evaluation-management-right-col">
                <div className="name">{item.name}</div>
                <div className="score">
                  총 배지 개수: {item.quantity}개
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default EvaluationRanking;
