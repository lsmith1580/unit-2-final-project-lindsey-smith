import { useState, useRef, useEffect } from "react";
import { MapContainer, TileLayer, Polyline, useMap } from "react-leaflet";
import L from "leaflet";
import {
  GoogleMap,
  useJsApiLoader,
  Autocomplete,
} from "@react-google-maps/api";
import axios from "axios";
import haversine from "haversine-distance";
import { toast } from "react-toastify";
import Button from "./Button";
import "leaflet/dist/leaflet.css";
import "./RouteMap.css";

const GOOGLE_KEY = import.meta.env.VITE_GOOGLE_API_KEY;
const LINE_STYLE = { color: "#0077ff", weight: 5 };
const GOOGLE_LIBS = ["places"];

function FitToRoute({ coords }) {
  const map = useMap();
  useEffect(() => {
    if (!map || !Array.isArray(coords) || coords.length < 2) return;
    const bounds = L.latLngBounds(coords.map(([lat, lng]) => [lat, lng]));
    // wait a tick so the polyline is painted
    requestAnimationFrame(() => {
      map.invalidateSize();
      map.fitBounds(bounds, { padding: [40, 40], maxZoom: 15, animate: true });
    });
  }, [map, coords]);
  return null;
}

const RouteMap = () => {
  const startRef = useRef(null); //google autocomplete refs
  const endRef = useRef(null);
  const [coords, setCoords] = useState([]); //coordinates for leaflet
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);

  const { isLoaded } = useJsApiLoader({
    //for loading google places
    googleMapsApiKey: GOOGLE_KEY,
    libraries: GOOGLE_LIBS,
  });

  const handleGenerate = async () => {
    if (loading) return;
    if (!startRef.current || !endRef.current) return;

    const start = startRef.current.getPlace();
    const end = endRef.current.getPlace();
    if (!start?.geometry || !end?.geometry) {
      return toast.error("Select both start & end");
    }

    const [startLat, startLng] = [
      start.geometry.location.lat(),
      start.geometry.location.lng(),
    ];
    const [endLat, endLng] = [
      end.geometry.location.lat(),
      end.geometry.location.lng(),
    ];

    const meters = haversine(
      //added haversine-distance so don't have to calculate distance manually
      { lat: startLat, lng: startLng },
      { lat: endLat, lng: endLng }
    );
    const km = meters / 1000;

    if (km > 900) {
      toast.error("TomTom 'thrilling' routes are limited to 900 km.");
      return; // validation since tomtom only allows up to 900 km on thrilling route generation
    }

    setLoading(true);
    try {
      const { data } = await axios.get("/api/routes/generate", {
        params: { startLat, startLng, endLat, endLng },
      });
      setCoords(data.coordinates);
      setStats({
        km: Number(data.distanceKm),
        min: Number(data.estimatedTimeMin),
      });
    } catch (err) {
      console.error(err);
      toast.error("Route generation failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="route-map-wrapper">
      {/* {autocomplete component from google maps api} */}
      {isLoaded && (
        <div className="route-header">
          <div className="controls">
            <Autocomplete onLoad={(a) => (startRef.current = a)}>
              <input placeholder="Start" />
            </Autocomplete>
            <Autocomplete onLoad={(a) => (endRef.current = a)}>
              <input placeholder="Destination" />
            </Autocomplete>
            <Button onClick={handleGenerate} disabled={loading}>
              {loading ? (
                <>
                  Generating… <span className="spinner" />
                </>
              ) : (
                "Generate Thrilling Route"
              )}
            </Button>
          </div>
          {/* renders route info after it's been received from the backend */}
          {stats && Number.isFinite(stats.min) && (
            <p className="stats">
              {(stats.km * 0.621371).toFixed(1)} mi&nbsp;•&nbsp;
              {Math.floor(stats.min / 60)} h&nbsp;
              {Math.round(stats.min % 60)} min
            </p>
          )}
        </div>
      )}

      {/* map container, tile layer, and polyline components from leaflet */}
      <MapContainer className="route-map" center={[39.1, -94.57]} zoom={7}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="&copy; OpenStreetMap contributors"
        />
        {coords.length > 0 && (
          <Polyline positions={coords} pathOptions={LINE_STYLE} />
        )}
        <FitToRoute coords={coords} />
      </MapContainer>
    </div>
  );
};

export default RouteMap;

// import { useState, useRef } from "react";
// import { MapContainer, TileLayer, Polyline } from "react-leaflet";
// import { useJsApiLoader, Autocomplete } from "@react-google-maps/api";
// import axios from "axios";
// import { toast } from "react-toastify";
// import Button from "./Button";
// import "leaflet/dist/leaflet.css";
// import "./RouteMap.css";

// // Get Google Maps key and set route line color
// const GOOGLE_KEY = import.meta.env.VITE_GOOGLE_API_KEY;
// const LINE_STYLE = { color: "#ff5722", weight: 5 };

// const RouteMap = () => {
//   // References to get data from Google autocomplete
//   const startRef = useRef(null);
//   const endRef = useRef(null);

//   // Component state
//   const [routeCoords, setRouteCoords] = useState([]); // Route points for map
//   const [routeInfo, setRouteInfo] = useState(null); // Distance and time
//   const [isLoading, setIsLoading] = useState(false); // Show loading?

//   // Load Google Maps
//   const { isLoaded } = useJsApiLoader({
//     googleMapsApiKey: GOOGLE_KEY,
//     libraries: ["places"],
//   });

//   // Generate route when button is clicked
//   const generateRoute = async () => {
//     // Get places from autocomplete
//     const startPlace = startRef.current?.getPlace();
//     const endPlace = endRef.current?.getPlace();

//     // Check if both places are selected
//     if (!startPlace?.geometry || !endPlace?.geometry) {
//       toast.error("Please select both start and destination");
//       return;
//     }

//     // Get coordinates
//     const startLat = startPlace.geometry.location.lat();
//     const startLng = startPlace.geometry.location.lng();
//     const endLat = endPlace.geometry.location.lat();
//     const endLng = endPlace.geometry.location.lng();

//     setIsLoading(true);

//     try {
//       // Call backend API
//       const response = await axios.get("/api/routes/generate", {
//         params: { sLat: startLat, sLng: startLng, eLat: endLat, eLng: endLng },
//       });

//       // Update map with route
//       setRouteCoords(response.data.coordinates);
//       setRouteInfo({
//         distance: response.data.distanceKm,
//         time: response.data.timeMin,
//       });

//       toast.success("Route generated!");
//     } catch (error) {
//       console.error(error);
//       toast.error("Failed to generate route");
//     } finally {
//       setIsLoading(false);
//     }
//   };

//   // Clear everything
//   const clearRoute = () => {
//     setRouteCoords([]);
//     setRouteInfo(null);
//     // Note: inputs will need manual clearing by user
//     toast.info("Route cleared");
//   };

//   return (
//     <div className="route-map-wrapper">
//       {/* Input controls */}
//       {isLoaded && (
//         <div className="controls">
//           <Autocomplete onLoad={(a) => (startRef.current = a)}>
//             <input placeholder="Start location" disabled={isLoading} />
//           </Autocomplete>

//           <Autocomplete onLoad={(a) => (endRef.current = a)}>
//             <input placeholder="Destination" disabled={isLoading} />
//           </Autocomplete>

//           <Button onClick={generateRoute} disabled={isLoading}>
//             {isLoading ? "Generating..." : "Generate Route"}
//           </Button>

//           {routeCoords.length > 0 && (
//             <Button onClick={clearRoute} disabled={isLoading}>
//               Clear
//             </Button>
//           )}
//         </div>
//       )}

//       {/* Map */}
//       <MapContainer className="route-map" center={[39.1, -94.57]} zoom={7}>
//         <TileLayer
//           url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
//           attribution="&copy; OpenStreetMap"
//         />

//         {routeCoords.length > 0 && (
//           <Polyline positions={routeCoords} pathOptions={LINE_STYLE} />
//         )}
//       </MapContainer>

//       {/* Route info */}
//       {routeInfo && (
//         <div className="stats">
//           {routeInfo.distance.toFixed(1)} km •{" "}
//           {(routeInfo.time / 60).toFixed(1)} hours
//         </div>
//       )}
//     </div>
//   );
// };

// export default RouteMap;
