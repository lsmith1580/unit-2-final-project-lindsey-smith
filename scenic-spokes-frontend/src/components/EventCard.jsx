import { useState } from "react";
import Button from "./Button";
import ConfirmModal from "./ConfirmModal";
import axios from "axios";
import { toast } from "react-toastify";
import { useAuth } from "@clerk/clerk-react";

const EventCard = ({ event, onEdit, onDeleteComplete }) => {
  const { getToken } = useAuth();
  const [showConfirm, setShowConfirm] = useState(false); //state variable to keep track of whether to show confirm modal

  const owned = event.isUserEvent ?? event.userEvent;

  const handleDelete = async () => {
    try {
      const token = await getToken();
      await axios.delete(`/api/events/${event.id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      toast.success("Event deleted.");
      setShowConfirm(false);
      if (onDeleteComplete) onDeleteComplete(event.id);
    } catch (error) {
      console.error("Delete failed:", error);
      toast.error("Failed to delete event.");
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "";

    const date = new Date(dateString);
    return date.toLocaleDateString("en-US"); //formats the date
  };

  return (
    <div className="event-card">
      <div className="event-image">
        <img
          src={event.image || "/maxim-simonov-RUcDh47KhLk-unsplash.jpg"}
          alt={event.title}
        />
      </div>
      <div className="event-info">
        <h2>{event.title}</h2>
        <p>{formatDate(event.date)}</p>
        <p>{event.description}</p>
        {/*if user event allow the option to delete, if clicked show confirm modal*/}
        {owned && (
          <div className="button-group">
            <Button variant="secondary" onClick={() => onEdit(event)}>
              Edit
            </Button>
            <Button variant="secondary" onClick={() => setShowConfirm(true)}>
              Delete
            </Button>
          </div>
        )}
      </div>
      {/*if show confirm is true, show confirm modal with these values for the props*/}
      {showConfirm && (
        <ConfirmModal
          message="Are you sure you want to delete this event?"
          onConfirm={handleDelete}
          onCancel={() => setShowConfirm(false)}
        />
      )}
    </div>
  );
};

export default EventCard;
