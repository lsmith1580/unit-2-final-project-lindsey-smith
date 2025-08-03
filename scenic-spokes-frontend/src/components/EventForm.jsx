import { useState } from "react";
import Button from "./Button";
import axios from "axios";
import { toast } from "react-toastify";
import { useAuth } from "@clerk/clerk-react";
import "react-toastify/dist/ReactToastify.css";
import "./EventForm.css";

const EventForm = ({ addEvent }) => {
  const { getToken } = useAuth();

  const [formData, setFormData] = useState({
    title: "",
    date: "",
    description: "",
  }); //state variable for form data with default values of empty strings

  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  }; //shows any changes in the values of the form while a user is updating it

  // const handleImageUpload = async (e) => {
  //   const file = e.target.files[0];
  //   if (!file) return;

  //   const formDataObj = new FormData();
  //   formDataObj.append("file", file);

  //   try {
  //     const response = await axios.post("/api/uploads/image", formDataObj, {
  //       headers: {
  //         "Content-Type": "multipart/form-data",
  //       },
  //     });

  //     const imageUrl = response.data;
  //     setFormData((prev) => ({ ...prev, image: imageUrl }));
  //     setImagePreview(imageUrl);
  //     toast.success("Image uploaded!");
  //   } catch (error) {
  //     console.error("Image upload failed:", error);
  //     toast.error("Image upload failed. Please try again.");
  //   }
  // };

  const handleImageSelection = (e) => {
    const file = e.target.files[0];
    setImageFile(file);

    if (file) {
      const previewUrl = URL.createObjectURL(file);
      setImagePreview(previewUrl);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = await getToken();
      const formDataObj = new FormData();

      formDataObj.append("title", formData.title);
      formDataObj.append("description", formData.description);
      formDataObj.append("date", formData.date);

      if (imageFile) {
        formDataObj.append("file", imageFile);
      }

      await axios.post("/api/events", formDataObj, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });

      toast.success("Event created!");
      setFormData({ title: "", date: "", description: "" });
      setImageFile(null);
      setImagePreview(null);
    } catch (error) {
      console.error("Error submitting event: ", error);
      toast.error("Failed to create event.");
    }
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
          onChange={handleImageSelection}
        />
        {imagePreview && (
          <img
            src={imagePreview}
            alt="Preview"
            className="image-preview"
            style={{ width: "100%", maxHeight: "200px", objectFit: "cover" }}
          />
        )}
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
