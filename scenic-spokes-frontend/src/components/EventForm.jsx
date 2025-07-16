import { useState } from "react";
import "./EventForm.css";
import Button from "./Button";

const EventForm = ({ addEvent }) => {
  const [formData, setFormData] = useState({
    title: "",
    date: "",
    image: "",
    description: "",
  }); //state variable for form data with default values of empty strings

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  }; //shows any changes in the values of the form while a user is updating it

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setFormData((prevData) => ({
          ...prevData,
          image: reader.result,
        }));
      };
      reader.readAsDataURL(file);
    }
  }; //function to handle image uploads, realized later that encoding images this way takes
  // up way too much storage space, need to find a better way to handle uploading images

  const handleSubmit = (e) => {
    e.preventDefault();

    addEvent(formData);

    setFormData({
      title: "",
      date: "",
      image: "",
      description: "",
    });
  };

  return (
    <div className="event-form-container">
      <h2>Create a New Event</h2>
      <form onSubmit={handleSubmit} className="event-form">
        <input
          type="text"
          name="title"
          placeholder="Event Title"
          value={formData.title}
          onChange={handleChange}
          required
        />
        <input
          type="date"
          name="date"
          value={formData.date}
          onChange={handleChange}
          required
        />
        <input
          className="file-text"
          type="file"
          accept="image/*"
          onChange={handleImageUpload}
          required
        />
        <textarea
          name="description"
          placeholder="Event Description"
          value={formData.description}
          onChange={handleChange}
          required
        />
        <Button type="submit">Add Event</Button>
      </form>
    </div>
  );
};

export default EventForm;
