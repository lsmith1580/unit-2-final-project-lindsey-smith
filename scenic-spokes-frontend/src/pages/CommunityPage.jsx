import { useEffect, useState } from "react";
import EventCard from "../components/EventCard";
import EventForm from "../components/EventForm";
import { useAuth } from "@clerk/clerk-react";
import { toast } from "react-toastify";
import "./CommunityPage.css";

const CommunityPage = () => {
  const { getToken, isSignedIn } = useAuth();
  const [events, setEvents] = useState([]);
  const [editingEvent, setEditingEvent] = useState(null);

  const getEvents = async () => {
    try {
      const token = isSignedIn ? await getToken() : null;
      const response = await axios.get("/api/events", {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      });

      setEvents(response.data);
    } catch (error) {
      console.error("Failed to get events: ", error);
      toast.error("Could not load events.");
    }
  };

  useEffect(() => {
    getEvents();
  }, []);

  const handleDelete = (id) => {
    setEvents((prev) => prev.filter((e) => e.id !== id));
  };

  const handleEdit = (event) => {
    setEditingEvent(event);
  };

  const handleFormComplete = () => {
    setEditingEvent(null);
    getEvents();
  };

  return (
    <div className="community-page">
      <h1>Community Events</h1>
      <div className="event-grid">
        {events.map((event) => (
          <EventCard
            key={event.id}
            event={event}
            onEdit={handleEdit}
            onDeleteComplete={handleDelete}
          />
        ))}
      </div>
      <EventForm editingEvent={editingEvent} onComplete={handleFormComplete} />
    </div>
  );
};

export default CommunityPage;
