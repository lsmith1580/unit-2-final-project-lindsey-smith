import "./Button.css";

const Button = ({
  onClick,
  children, //whatever is placed between opening and closing tags when used
  type = "button", //default type
  variant = "primary", //default style
  ...props //accounts for any other props passed to button
}) => {
  return (
    <button
      type={type}
      className={`btn btn-${variant}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;
