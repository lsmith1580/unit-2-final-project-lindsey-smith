import { stops } from "../shared/stops";
import { useParams, Link } from "react-router-dom";
import "./StopInfo.css";

const StopInfo = () => {
  const { id } = useParams();
  const stop = stops.find((stop) => stop.id === parseInt(id));

  if (!stop) {
    return (
      <div className="stop-info-container">
        <h2>Stop not found</h2>
        <Link to="/route-map" className="back-link">
          Back to Route Map
        </Link>
      </div>
    );
  }

  return (
    <div className="stop-info-body">
      <div className="stop-info-container">
        <h1>{stop.name}</h1>
        <img src={stop.image} alt={stop.name} className="stop-image" />
        <p>{stop.info}</p>
        <Link to="/route-map" className="back-link">
          ← Back to Route Map
        </Link>
      </div>
    </div>
  );
};

export default StopInfo;
