import { useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import Button from '../../components/shared/Button';
import { managementActions } from "../../redux/managementSlice";

import { getStringDate } from "../../utils/date";

import axios from 'axios';

const NewProject = ({setIsModalVisible}) => {
    const modalOutside = useRef();

    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [startDate, setStartDate] = useState(getStringDate(new Date()));
    const [endDate, setEndDate] = useState(getStringDate(new Date()));

    const accessToken = useSelector(state => state.auth.accessToken);

    const getOtherMembers = (teamObj, name) => {
        const otherMembersObj = teamObj
            .filter((member) => member.name !== name)
            .reduce((obj, member) => {
                obj[member.name] = false;

                return obj;
            }, {});
        
        return otherMembersObj;
    }

    let teamMembers = [
        {
            name: '조상욱',
            role: 'projectLeader',
        },
        {
            name: '홍서현',
            role: 'member',
        },
        {
            name: '이연제',
            role: 'member',
        },
        {
            name: '김도연',
            role: 'member',
        },

    ]
        
    teamMembers = teamMembers.map((member) => ({
        ...member,
        isEvaluateCompleted: getOtherMembers(teamMembers, member.name)
    }))

    const descriptionRef = useRef();

    const dispatch = useDispatch();

    const handleModalClose = (e) => {
        if (e.target === modalOutside.current) {
            setIsModalVisible(false);
        }
    }

    const handleChangeTitle = (e) => {
        setTitle(e.target.value);
    }

    const handleChangeStartDate = (e) => {
        setStartDate(e.target.value);
    }

    const handleChangeEndDate = (e) => {
        setEndDate(e.target.value);
    }

    const handleChangeContent = (e) => {
        setDescription(e.target.value);
    }

    const handleSubmitNewProject = async () => {
        if (description.length < 5) {
            alert('프로젝트 설명은 다섯 글자 이상 작성해주세요.');
            descriptionRef.current.focus();
            return;
        }

        if (startDate > endDate) {
            alert('시작 일자는 종료 일자보다 늦을 수 없습니다. 다시 설정해주세요.');
            return;
        }

        let ongoingData = 'complete';

        if (new Date().getDate() >= new Date(startDate).getDate() && new Date().getDate() <= new Date(endDate).getDate()) {
            ongoingData = 'ongoing';
        }

        dispatch(managementActions.onCreate({
            startDate: new Date(startDate).getTime(),
            endDate: new Date(endDate).getTime(),
            title,
            description,
            state: ongoingData,
            members: teamMembers
        }));

        const apiUrl = `http://localhost:8080/api/auth/project`;

        const response = await axios.post(apiUrl, {
            "title": title,
            "description": description,
            "startDate": getStringDate(new Date(startDate)),
            "endDate": getStringDate(new Date(endDate))
        }, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            },
        })

        setIsModalVisible(false);
    }

    return (
        <div
            className='new-project-modal-wrapper'
            ref={modalOutside}
            onClick={handleModalClose}
        >
            <div className="new-project">
                <div className="new-project-first-row">
                    <div className='new-project-left-col'>
                        <figure />
                        <input
                            className='new-project-heading-input'
                            type='text'
                            required
                            value={title}
                            onChange={handleChangeTitle}
                            placeholder='프로젝트 제목을 입력하세요'
                        />
                    </div>
                    <div className='new-project-right-col'>
                        <Button
                            text='작성 완료'
                            onClick={handleSubmitNewProject}
                        />
                    </div>
                </div>
                <div className="new-project-second-row">
                    <div className='new-project-period'>
                        <div className='new-project-period-left-col'>
                            <h1>진행 기간:</h1>
                        </div>
                        <div className='new-project-period-right-col'>
                            <input
                                className="new-project-period-start-date"
                                type='date'
                                required
                                value={startDate}
                                onChange={handleChangeStartDate}
                                data-placeholder='시작 일자'
                            />
                            -
                            <input
                                className="new-project-period-end-date"
                                type='date'
                                required
                                value={endDate}
                                onChange={handleChangeEndDate}
                                data-placeholder='종료 일자'
                            />
                        </div>
                    </div>
                    <textarea
                        value={description}
                        onChange={handleChangeContent}
                        ref={descriptionRef}
                        placeholder='프로젝트 설명을 작성하세요'
                        required
                    />
                </div>
            </div>
        </div>
    )
}

export default NewProject;