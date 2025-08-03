import { useState } from "react";
import Button from "./Button";
import axios from "axios";
import { toast } from "react-toastify";
import { useAuth, useSignIn } from "@clerk/clerk-react";
import "react-toastify/dist/ReactToastify.css";
import "./EventForm.css";

const EventForm = ({ editingEvent = null, onComplete }) => {
  const { getToken, isSignedIn } = useAuth();
  const { openSignIn } = useSignIn();

  const [formData, setFormData] = useState({
    title: editingEvent?.title || "",
    date: editingEvent?.date || "",
    description: editingEvent?.description || "",
  }); //state variable for form data with default values of empty strings
  //editingEvent is used to determine whether the form is creating a new event or editing an existing one
  //The ?. operator is used to access properties of editingEvent without throwing an error if null

  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState(editingEvent?.image || null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  }; //shows any changes in the values of the form while a user is updating it

  const handleImageSelection = (e) => {
    //function that gets and stores selected image file for use later in handleSubmit function
    const file = e.target.files[0]; //and creates preview image url
    setImageFile(file);

    if (file) {
      const previewUrl = URL.createObjectURL(file);
      setImagePreview(previewUrl);
    } else {
      setImagePreview(null);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isSignedIn) {
      toast.info("Please sign in to create or update an event.");
      openSignIn();
      return;
    }

    try {
      const token = await getToken(); //need to get token from clerk to store clerk user id with event being created
      const formDataObj = new FormData(); //need to use formData object since sending an image file and not just JSON

      formDataObj.append("title", formData.title);
      formDataObj.append("description", formData.description);
      formDataObj.append("date", formData.date);

      if (imageFile) {
        formDataObj.append("file", imageFile);
      }

      if (editingEvent) {
        await axios.put(`/api/events/${editingEvent.id}`, formDataObj, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        });
        toast.success("Event updated!");
      } else {
        await axios.post("/api/events", formDataObj, {
          //using axios library for requests to the backend
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        });

        toast.success("Event created!");
      } //using the toastify library to create pop-ups to notify the user of successful form submission
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
      <h2>{editingEvent ? "Edit Event" : "Create a New Event"}</h2>
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
        <Button type="submit">
          {editingEvent ? "Update Event" : "Add Event"}
        </Button>
      </form>
    </div>
  );
};

export default EventForm;
