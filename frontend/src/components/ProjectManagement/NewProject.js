import { useRef, useState } from "react";

import Button from '../../components/shared/Button';

import { getStringDate } from "../../utils/date";
import useManagementMutation from "./hooks/useManagementMutation";


const NewProject = ({setIsModalVisible}) => {
    const modalOutside = useRef();

    const { createMutate } = useManagementMutation();

    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [startDate, setStartDate] = useState(getStringDate(new Date()));
    const [endDate, setEndDate] = useState(getStringDate(new Date()));

    const descriptionRef = useRef();

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

        createMutate({
            "title": title,
            "description": description,
            "startDate": getStringDate(new Date(startDate)),
            "endDate": getStringDate(new Date(endDate))
        });

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