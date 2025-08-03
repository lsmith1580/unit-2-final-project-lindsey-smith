import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import AboutPage from "./pages/AboutPage";
import CommunityPage from "./pages/CommunityPage";
import NotFoundPage from "./pages/NotFoundPage";
import RouteMap from "./components/RouteMap";
import Footer from "./components/Footer";
import Header from "./components/Header";
import SignInPage from "./pages/SignInPage";
import { ToastContainer } from "react-toastify";
import "./App.css";

function App() {
  return (
    <div id="window">
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/sign-in" element={<SignInPage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/community" element={<CommunityPage />} />
        <Route path="/route-map" element={<RouteMap />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <Footer />
      <ToastContainer />
    </div>
  );
}

export default App;
