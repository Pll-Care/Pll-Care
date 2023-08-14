const ProfileInput = ({
  value,
  onChange,
  placeholder,
  readOnly = false,
  width = "100",
  onClick,
  position = "basic",
}) => {
  return (
    <input
      type="text"
      value={value}
      placeholder={placeholder}
      onChange={(event) => onChange(event.target.value)}
      readOnly={readOnly}
      className={
        position === "basic"
          ? `profileInput profileInput_width${width}`
          : `profileInput-header profileInput_width${width}`
      }
      onClick={!!onClick ? onClick : null}
    />
  );
};

export default ProfileInput;
