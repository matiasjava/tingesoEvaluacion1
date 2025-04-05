import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import "./Formulario.css";

const Formulario = () => {
  const location = useLocation();
  const { dia, horaInicio, horaTermino, tipoDuracion } = location.state || {};

  const [cantidadPersonas, setCantidadPersonas] = useState(1); // Número de personas
  const [personas, setPersonas] = useState([]); // Datos de las personas

  
  const handleCantidadChange = (e) => {
    const cantidad = parseInt(e.target.value);
    setCantidadPersonas(cantidad);

    
    const nuevasPersonas = Array.from({ length: cantidad }, (_, index) => ({
      nombre: personas[index]?.nombre || '',
      rut: personas[index]?.rut || '',
    }));
    setPersonas(nuevasPersonas);
  };

  
  const handlePersonaChange = (index, field, value) => {
    const nuevasPersonas = [...personas];
    nuevasPersonas[index][field] = value;
    setPersonas(nuevasPersonas);
  };

  
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Datos de la reserva:', { dia, horaInicio, horaTermino, tipoDuracion, personas });
    alert('Reserva guardada exitosamente');
  };

  return (
    <div>
      <div className="container">
        <h1>Datos de reserva</h1>
        <p><strong>Día:</strong> {dia}</p>
        <p><strong>Hora de Inicio:</strong> {horaInicio}</p>
        <p><strong>Hora de Término:</strong> {horaTermino}</p>
        <p><strong>Tipo de Duración:</strong> {tipoDuracion}</p>

        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="cantidadPersonas">Cantidad de Personas (1-15):</label>
            <input
              type="number"
              id="cantidadPersonas"
              min="1"
              max="15"
              value={cantidadPersonas}
              onChange={handleCantidadChange}
            />
          </div>

          {personas.map((persona, index) => (
            <div key={index} className="persona-form">
              <h3>Persona {index + 1}</h3>
              <div>
                <label htmlFor={`nombre-${index}`}>Nombre:</label>
                <input
                  type="text"
                  id={`nombre-${index}`}
                  value={persona.nombre}
                  onChange={(e) => handlePersonaChange(index, 'nombre', e.target.value)}
                />
              </div>
              <div>
                <label htmlFor={`rut-${index}`}>RUT:</label>
                <input
                  type="text"
                  id={`rut-${index}`}
                  value={persona.rut}
                  onChange={(e) => handlePersonaChange(index, 'rut', e.target.value)}
                  
                />
              </div>
            </div>
          ))}

          <button type="submit">Continuar</button>
        </form>
      </div>
    </div>
  );
};

export default Formulario;