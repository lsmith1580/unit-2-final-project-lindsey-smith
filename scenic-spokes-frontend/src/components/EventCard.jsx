import { useState } from "react";
import Button from "./Button";
import ConfirmModal from "./ConfirmModal";

const EventCard = ({ event, onDelete, isUserEvent }) => {
  const [showConfirm, setShowConfirm] = useState(false); //state variable to keep track of whether to show confirm modal

  const handleDelete = () => {
    onDelete(event.id);
    setShowConfirm(false); //delete event by id and hide the confirm modal
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString(); //formats the date
  };

  return (
    <div className="event-card">
      <div className="event-image">
        <img src={event.image} alt={event.title} />
      </div>
      <div className="event-info">
        <h2>{event.title}</h2>
        <p>{formatDate(event.date)}</p>
        <p>{event.description}</p>
        {/*if user event allow the option to delete, if clicked show confirm modal*/}
        {isUserEvent && (
          <div className="button-group">
            <Button variant="danger" onClick={() => setShowConfirm(true)}>
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
