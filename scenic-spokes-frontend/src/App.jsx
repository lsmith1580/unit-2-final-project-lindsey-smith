import { Routes, Route } from "react-router-dom";
import "./App.css";
import HomePage from "./pages/HomePage";
import AboutPage from "./pages/AboutPage";
import CommunityPage from "./pages/CommunityPage";
import NotFoundPage from "./pages/NotFoundPage";
import RouteMap from "./components/RouteMap";
import Footer from "./components/Footer";
import Header from "./components/Header";
import StopInfo from "./components/StopInfo";

function App() {
  return (
    <div id="window">
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="about" element={<AboutPage />} />
        <Route path="community" element={<CommunityPage />} />
        <Route path="route-map" element={<RouteMap />} />
        <Route path="/routes/:id" element={<StopInfo />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
