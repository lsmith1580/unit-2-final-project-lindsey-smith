import { useEffect, useState } from "react";
import EventCard from "../components/EventCard";
import EventForm from "../components/EventForm";
import { useAuth } from "@clerk/clerk-react";
import { toast } from "react-toastify";
import "./CommunityPage.css";

// const defaultEvents = [
//   {
//     id: 1,
//     title: "85th Sturgis Motorcycle Rally",
//     date: "08-01-2025",
//     image: "/maxim-simonov-RUcDh47KhLk-unsplash.jpg",
//     description:
//       "Join one of the biggest motorcycle rallies in the United States!",
//   },
//   {
//     id: 2,
//     title: "Bike Night",
//     date: "06-17-2025",
//     image: "/dipankar-gogoi-ZxYIby8WSNI-unsplash.jpg",
//     description: "A fun evening meetup for bike enthusiasts.",
//   },
//   {
//     id: 3,
//     title: "Bike Show",
//     date: "07-10-2025",
//     image: "/ojo-toluwashe-_PcRWlbEqAE-unsplash.jpg",
//     description: "Show off your ride and check out others in the community.",
//   },
// ];

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

  const editEvent = (updatedEvent) => {
    setUserEvents((prevEvents) =>
      prevEvents.map((event) =>
        event.id === updatedEvent.id ? updatedEvent : event
      )
    );
  };

  const deleteEvent = (idToDelete) => {
    setUserEvents((prevEvents) =>
      prevEvents.filter((event) => event.id !== idToDelete)
    );
  };

  const combinedEvents = [...defaultEvents, ...userEvents];

  return (
    <div className="community-page">
      <h1>Community Events</h1>
      <div className="event-grid">
        {combinedEvents.map((event) => (
          <EventCard
            key={event.id}
            event={event}
            onEdit={editEvent}
            onDelete={deleteEvent}
            isUserEvent={userEvents.some((e) => e.id === event.id)} // for controlling edit/delete availability
          />
        ))}
      </div>
      <EventForm addEvent={addEvent} />
    </div>
  );
};

export default CommunityPage;
