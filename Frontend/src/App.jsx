import { Route, Routes } from 'react-router-dom'
import { Navbar } from './components/NavBar/Navbar'
import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import Home from './views/Home/Home'
import Contact from './views/Contact/Contact'

function App() {

  return (
    <div className="App">
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} /> 
        <Route path="/contact" element={<Contact />} /> 
      </Routes>
      </div>
  )
}

export default App