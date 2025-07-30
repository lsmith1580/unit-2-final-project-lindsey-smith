import { Routes, Route } from "react-router-dom";
import "./App.css";
import HomePage from "./pages/HomePage";
import AboutPage from "./pages/AboutPage";
import CommunityPage from "./pages/CommunityPage";
import NotFoundPage from "./pages/NotFoundPage";
import RouteMap from "./components/RouteMap";
import Footer from "./components/Footer";
import Header from "./components/Header";
import SignInPage from "./pages/SignInPage";
import SignUpPage from "./pages/SignUpPage";

function App() {
  return (
    <div id="window">
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/sign-in" element={<SignInPage />} />
        <Route path="/sign-up" element={<SignUpPage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/community" element={<CommunityPage />} />
        <Route path="/route-map" element={<RouteMap />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
