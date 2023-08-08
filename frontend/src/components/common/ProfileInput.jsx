const ProfileInput = ({
  value,
  onChange,
  placeholder,
  readOnly = false,
  width = "100",
}) => {
  return (
    <input
      type="text"
      value={value}
      placeholder={placeholder}
      onChange={(event) => onChange(event.target.value)}
      readOnly={readOnly}
      className={`profileInput profileInput_width${width}`}
    />
  );
};

export default ProfileInput;
