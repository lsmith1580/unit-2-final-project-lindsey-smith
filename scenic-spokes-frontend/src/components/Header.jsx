import { Link, NavLink } from "react-router-dom";
import { useState } from "react";
import {
  SignedIn,
  SignedOut,
  SignInButton,
  SignUpButton,
  UserButton,
} from "@clerk/react-router";
import "./Header.css";

const Header = () => {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header className="header">
      <nav className="navbar">
        <div className="title-container">
          <img
            src="/scenic-spokes-logo.png"
            className="logo-image"
            alt="scenic spokes logo"
            height={"80px"}
            width={"80px"}
          />
          <Link to="/" className="title">
            Scenic Spokes
          </Link>
          <div
            className="mobile-menu-toggle"
            onClick={() => setMenuOpen(!menuOpen)}
          >
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
        <div className="nav-links">
          <ul className={menuOpen ? "open" : ""}>
            <li>
              <NavLink to="/about">About</NavLink>
            </li>
            <li>
              <NavLink to="/route-map">Plan Route</NavLink>
            </li>
            <li>
              <NavLink to="/community">Community Events</NavLink>
            </li>
            <li>
              <SignedOut>
                <div className="auth-buttons">
                  <SignInButton>
                    <span className="sign-in-button">Sign In</span>
                  </SignInButton>
                  <SignUpButton>
                    <span className="sign-up-button">Sign Up</span>
                  </SignUpButton>
                </div>
              </SignedOut>
              <SignedIn>
                <UserButton />
              </SignedIn>
            </li>
          </ul>
        </div>
      </nav>
    </header>
  );
};

export default Header;
