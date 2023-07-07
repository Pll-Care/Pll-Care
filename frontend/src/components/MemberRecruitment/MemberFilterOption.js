import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { useState } from "react";

const MemberFilterOption = () => {
  const [backend, setBackend] = useState("");

  const handleBackendChange = (e) => {
    setBackend(e.target.value);
  };
  return (
    <div className="filter-options">
      <FormControl
        sx={{ m: 1, minWidth: 120 }}
        size="small"
        className="filter-options-option"
      >
        <InputLabel id="demo-select-small-label">백엔드</InputLabel>
        <Select
          labelId="demo-select-small-label"
          id="demo-select-small"
          value={backend}
          label="백엔드"
          onChange={handleBackendChange}
        >
          <MenuItem value={10}>Ten</MenuItem>
          <MenuItem value={20}>Twenty</MenuItem>
          <MenuItem value={30}>Thirty</MenuItem>
        </Select>
      </FormControl>
    </div>
  );
};
export default MemberFilterOption;
