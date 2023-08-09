import { useRef, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useQuery } from "react-query";
import { toast } from "react-toastify";

import Button from "../common/Button";
import Card from "../common/Card";
import RecruitmentProjectWrite from "./RecruitmentProjectWrite";
import RecruitmentTitleWrite from "./RecruitmentTitleWrite";
import RecruitmentPostionWrite from "./RecruitmentPositionWrite";
import RecruitmentContentWrite from "./RecruitmentContentWrite";
import SearchStack from "../Profile/Introduce/PositionBox/SearchStack";

import { useAddRecruitmentPostMutation } from "../../hooks/useRecruitmentMutation";
import { getRecruitmentProject } from "../../lib/apis/memberRecruitmentApi";

const MemberRecruitmentWrite = () => {
  const navigate = useNavigate();
  const initialDate = new Date().toISOString().split("T")[0];

  // 모집글 생성 입력값들
  const [formValues, setFormValues] = useState({
    projectId: "",
    title: "",
    concept: "IOS",
    description: "",
    recruitStartDate: initialDate,
    recruitEndDate: initialDate,
    reference: "",
    contact: "",
    region: "서울",
    backendCnt: 0,
    frontendCnt: 0,
    managerCnt: 0,
    designCnt: 0,
    techStack: [],
  });

  // 프로젝트 이미지
  const [imageUrl, setImageUrl] = useState();

  const { title, description, recruitStartDate, recruitEndDate } = formValues;

  const inputRefs = {
    title: useRef(),
    description: useRef(),
    startDate: useRef(),
    endDate: useRef(),
    reference: useRef(),
    region: useRef(),
    techStack: useRef(),
    contact: useRef(),
    backendCnt: useRef(),
  };

  // 프로젝트 관리 리스트 react query
  const { data, isLoading } = useQuery(
    ["allProject"],
    async () => await getRecruitmentProject(),
    {
      onSuccess: (data) => {
        if (data && data.length > 0) {
          setFormValues((prevState) => ({
            ...prevState,
            projectId: data[0].projectId || "",
          }));
          setImageUrl(data[0].imageUrl);
        }
      },
    }
  );

  // 입력하는 모든 상태값 update
  const handleChange = useCallback((e) => {
    const { name, value } = e.target;
    setFormValues((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  }, []);

  // 모집글 생성 react query문
  const { mutate: addPostMutate } = useAddRecruitmentPostMutation(formValues);

  // 생성하기
  const handleAddRecruitmetPost = () => {
    const { backendCnt, frontendCnt, designCnt, managerCnt, ...allData } =
      formValues;

    if (title.length < 2) {
      toast.error("모집글 제목을 입력해주세요");
      inputRefs.title.current.focus();
      return;
    }
    const start = new Date(recruitStartDate);
    const end = new Date(recruitEndDate);
    if (start > end) {
      toast.error("모집 기간 수정해주세요");
      inputRefs.startDate.current.focus();
      return;
    }
    if (description.length < 2) {
      toast.error("모집글 설명을 입력해주세요");
      inputRefs.description.current.focus();
      return;
    }
    if (
      parseInt(backendCnt, 10) +
        parseInt(frontendCnt, 10) +
        parseInt(managerCnt, 10) +
        parseInt(designCnt, 10) ===
      0
    ) {
      toast.error("포지션 인원 1명 이상이어야 합니다");
      inputRefs.backendCnt.current.focus();
      return;
    }

    const recruitCnt = [
      {
        position: "백엔드",
        currentCnt: 0,
        totalCnt: parseInt(backendCnt, 10),
      },
      {
        position: "프론트엔드",
        currentCnt: 0,
        totalCnt: parseInt(frontendCnt, 10),
      },
      {
        position: "기획",
        currentCnt: 0,
        totalCnt: parseInt(managerCnt, 10),
      },
      {
        position: "디자인",
        currentCnt: 0,
        totalCnt: parseInt(designCnt, 10),
      },
    ];
    const body = {
      ...allData,
      recruitInfo: recruitCnt,
    };
    console.log(body);
    addPostMutate(body);

    setFormValues({
      projectId: "",
      title: "",
      concept: "IOS",
      description: "",
      recruitStartDate: initialDate,
      recruitEndDate: initialDate,
      reference: "",
      contact: "",
      region: "서울",
      backendCnt: 0,
      frontendCnt: 0,
      managerCnt: 0,
      designCnt: 0,
      techStack: [],
    });

    navigate("/recruitment");
  };

  const changeStack = (response) => {
    setFormValues((prevValues) => ({
      ...prevValues,
      techStack: [...prevValues.techStack, response.name],
    }));
  };

  // techStack 삭제 함수
  const deleteStack = (stackName) => {
    setFormValues((prevValues) => ({
      ...prevValues,
      techStack: prevValues.techStack.filter((stack) => stack !== stackName),
    }));
  };

  return (
    <div className="member-write">
      {/*모집글 제목 입력*/}
      <RecruitmentTitleWrite
        imageUrl={imageUrl}
        handleChange={handleChange}
        formValues={formValues}
        inputRefs={inputRefs}
      />

      <Card className="member-write-card">
        <div className="member-content">
          {/*모집글 프로젝트 선택*/}
          {data && !isLoading && (
            <RecruitmentProjectWrite
              formValues={formValues}
              setFormValues={setFormValues}
              setImageUrl={setImageUrl}
              projectData={data}
              handleChange={handleChange}
              inputRefs={inputRefs}
            />
          )}

          <div className="member-content-position">
            <h3>포지션</h3>
            {/*모집글 포지션 인원 수 선택*/}
            <RecruitmentPostionWrite
              formValues={formValues}
              handleChange={handleChange}
              inputRefs={inputRefs}
            />

            <div className="member-content-position-stack">
              <h5>기술 스택</h5>
              <SearchStack
                stackList={formValues.techStack}
                changeStack={changeStack}
                deleteStack={deleteStack}
              />
            </div>
          </div>

          {/*모집글 컨탠츠 입력*/}
          <RecruitmentContentWrite
            formValues={formValues}
            handleChange={handleChange}
            inputRefs={inputRefs}
            setFormValues={setFormValues}
          />
        </div>
      </Card>
      <Button text="생성 완료" onClick={handleAddRecruitmetPost} />
    </div>
  );
};
export default MemberRecruitmentWrite;
