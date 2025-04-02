import { Route, Routes } from 'react-router-dom'
import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import Home from './views/Home'

function App() {

  return (
    <div className="App">
      <Routes>
        <Route path="/home" element={<Home />} />  
      </Routes>
      </div>
  )
}

export default App