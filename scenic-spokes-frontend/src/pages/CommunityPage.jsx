import { useEffect, useState, useCallback } from "react";
import EventCard from "../components/EventCard";
import EventForm from "../components/EventForm";
import { useAuth } from "@clerk/clerk-react";
import { toast } from "react-toastify";
import axios from "axios";
import "./CommunityPage.css";

const CommunityPage = () => {
  const { getToken, isSignedIn } = useAuth(); //hooks provided by clerk to retrieve jwt token and check if a user is signed in
  const [events, setEvents] = useState([]);
  const [editingEvent, setEditingEvent] = useState(null);

  const getEvents = useCallback(async () => {
    //usecallback prevents stale closure
    try {
      const token = isSignedIn ? await getToken() : null;
      const response = await axios.get("/api/events", {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      });
      setEvents(response.data);
    } catch (error) {
      console.error(error);
      toast.error("Could not load events.");
    }
  }, [getToken, isSignedIn]);

  useEffect(() => {
    getEvents();
  }, [getEvents]); //runs the effect again if the user signs in since the dependencies of usecallback change

  const handleDelete = (id) => {
    //receives id of deleted event from EventCard and removes deleted event immediately
    setEvents((prev) => prev.filter((e) => e.id !== id));
  };

  const handleFormComplete = () => {
    setEditingEvent(null); //closes the edit mode and displays updated events
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
            onEdit={setEditingEvent}
            onDeleteComplete={handleDelete}
          />
        ))}
      </div>
      <EventForm editingEvent={editingEvent} onComplete={handleFormComplete} />
    </div>
  );
};

export default CommunityPage;
