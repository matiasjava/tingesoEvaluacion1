import React, { useState } from "react";
import "./Navbar.css";
import { Link, NavLink } from "react-router-dom";


export const Navbar = () => {
    const [menuOpen, setMenuOpen] = useState(false);
  
    return (
      <nav>
        <Link to="/" className="title">
          KartingRM
        </Link>
        <div className="menu" onClick={() => setMenuOpen(!menuOpen)}>
          <span></span>
          <span></span>
          <span></span>
        </div>
        <ul className={menuOpen ? "open" : ""}>
          <li>
            <NavLink to="/karts">Carritos</NavLink>
          </li>
          <li>
            <NavLink to="/contact">Contacto</NavLink>
          </li>
        </ul>
      </nav>
    );
  };