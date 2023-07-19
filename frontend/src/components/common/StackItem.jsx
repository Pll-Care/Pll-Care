const StackItem = ({ imageUrl, name, onClick }) => {
  return (
    <li className="stackItem">
      <div className="stackItem_image">
        <img src={imageUrl} alt={name} />
      </div>
      <div>
        <span>{name}</span>
      </div>
      <div>
        <button className="stackItem_button" name={name} onClick={onClick}>
          x
        </button>
      </div>
    </li>
  );
};

export default StackItem;
