import { useRef, useState } from "react";
import Button from "../Button";

const middleEvaluationBadges = [
    {
        name: '열정적인 참여자',
        numOfBadges: 3
    },
    {
        name: '아이디어 뱅크',
        numOfBadges: 2
    },
    {
        name: '탁월한 리더',
        numOfBadges: 4
    },
    {
        name: '최고의 서포터',
        numOfBadges: 1
    },
];

const evaluationCriterion = [
    {
        name: '성실도',
        value: 'diligence'
    },
    {
        name: '시간 엄수',
        value: 'punctuality'
    },
    {
        name: '업무 수행 능력',
        value: 'performance'
    },
    {
        name: '의사 소통',
        value: 'communication'
    },
];

const evaluationOptionList = [
    {
        id: 1,
        value: 1,
        name: '1'
    },
    {
        id: 2,
        value: 2,
        name: '2'
    },
    {
        id: 3,
        value: 3,
        name: '3'
    },
    {
        id: 4,
        value: 4,
        name: '4'
    },
    {
        id: 5,
        value: 5,
        name: '5'
    },
]

const FinalEvaluation = ({ member, setIsFinalEvaluationVisible }) => {
    const [evaluationScore, setEvaluationScore] = useState([
        {
            name: 'diligence',
            value: 1
        },
        {
            name: 'punctuality',
            value: 1
        },
        {
            name: 'performance',
            value: 1
        },
        {
            name: 'communication',
            value: 1
        },
    ])

    const [content, setContent] = useState('');

    const modalOutside = useRef();

    const handleFinalEvaluationVisible = (e) => {
        if (e.target === modalOutside.current) {
            setIsFinalEvaluationVisible('');
        }
    }

    const handleChangeContent = (e) => {
        setContent(e.target.value);
    }

    const handleChangeScore = (e, idx) => {
        setEvaluationScore((prevScore) => {
            return prevScore.map((score, index) => ( idx === index ? {...score, value: parseInt(e.target.value)} : score))
        })
    }

    return (
        <div
            className='final-evaluation-wrapper'
            ref={modalOutside}
            onClick={handleFinalEvaluationVisible}
        >
            <div className='evaluation-management-final-evaluation'>
                <div className='final-evaluation-heading'>
                    '{member}' 최종 평가하기
                </div>
                <div className='final-evaluation-body'>
                    <div className='evaluation'>
                        <h1>평가하기</h1>
                        <div className='evaluation-criterion'>
                            {evaluationCriterion.map((criterion, idx) => (
                                <div key={idx}>
                                    {criterion.name}
                                    <select
                                        className='evaluation-score'
                                        onChange={(e) => handleChangeScore(e, idx)}
                                        value={evaluationScore[idx].value}
                                    >
                                        {evaluationOptionList.map((opt) => (
                                            <option
                                                key={opt.value}
                                                value={opt.value}
                                            >
                                                {opt.name}
                                                <figure />
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className='badges'>
                        <h1>누적 뱃지</h1>
                        <div className='badges-body'>
                            {middleEvaluationBadges.map((item) => (
                                <div className='badge'>
                                    <figure />
                                    <div>{item.name}</div>
                                    <div>{item.numOfBadges}개</div>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className='content'>
                        <h1>최종 의견 작성하기</h1>
                        <div>
                            <div>'{member}'에 대한 최종 의견입니다.</div>
                            <textarea
                                value={content}
                                onChange={handleChangeContent}
                                placeholder={`${member}에 대한 최종 평가를 글로 써보세요!`}
                            />
                        </div>
                    </div>
                </div>
                <div className='final-evaluation-button-wrapper'>
                    <Button
                        text={'평가 완료하기'}
                    />
                </div>
            </div>
        </div>
    )
}

export default FinalEvaluation;