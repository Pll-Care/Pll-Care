import ReactQuill from "react-quill";
import 'react-quill/dist/quill.snow.css';
import Quill from 'quill';
import ImageResize from 'quill-image-resize';
Quill.register('modules/ImageResize', ImageResize);

const NewMeetingRecord = () => {
    return (
        <div className='meeting-record-new-meeting-record-editor'>
            <ReactQuill
                className='react-quill'
                modules={{
                toolbar: [
                    [{ 'header': [1, 2, 3, false] }],
                    [{ 'size': ['small', false, 'large', 'huge'] }],
                    ['bold', 'italic', 'underline', 'strike'],
                    [{ 'align': [] }],
                    [{ 'color': [] }, { 'background': [] }],
                    ['link', 'image'],
                ],
                ImageResize: {
                    parchment: Quill.import('parchment')
                }
            }}/>
        </div>
    )
}

export default NewMeetingRecord;