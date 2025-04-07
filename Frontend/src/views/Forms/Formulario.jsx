import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { confirmReserve, getUserByRut, createUser } from '../../services/ReserveService'; // Importar servicios
import './Formulario.css';

const Formulario = () => {
  const location = useLocation();
  const { dia, horaInicio, horaTermino, tipoDuracion } = location.state || {};

  const [cantidadPersonas, setCantidadPersonas] = useState(1);
  const [personas, setPersonas] = useState([]); 
  const [codigoReserva, setCodigoReserva] = useState(''); 

  const handleCantidadChange = (e) => {
    const cantidad = parseInt(e.target.value);
    setCantidadPersonas(cantidad);

    const nuevasPersonas = Array.from({ length: cantidad }, (_, index) => ({
      nombre: personas[index]?.nombre || '',
      rut: personas[index]?.rut || '',
      fechaCumpleanos: personas[index]?.fechaCumpleanos || '',
    }));
    setPersonas(nuevasPersonas);
  };

  const handlePersonaChange = (index, field, value) => {
    const nuevasPersonas = [...personas];
    nuevasPersonas[index][field] = value;
    setPersonas(nuevasPersonas);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    try {
      // Verificar si hay al menos una persona en el array
      if (personas.length === 0) {
        alert('Debe ingresar al menos una persona.');
        return;
      }
  
      // Usar los datos de la primera persona para verificar o crear el usuario
      const personaPrincipal = personas[0];
      if (!personaPrincipal.nombre || !personaPrincipal.rut || !personaPrincipal.email || !personaPrincipal.telefono) {
        alert('Debe completar los datos del cliente principal (nombre, RUT, email y teléfono).');
        return;
      }
  
      let cliente = await getUserByRut(personaPrincipal.rut);
  
      if (!cliente) {
        // Crear el cliente si no existe
        cliente = await createUser({
          nombre: personaPrincipal.nombre,
          rut: personaPrincipal.rut,
          email: personaPrincipal.email,
          telefono: personaPrincipal.telefono,
        });
      }
  
      // Crear la reserva con el cliente asociado
      const reserva = {
        codigo_reserva: codigoReserva,
        fecha_uso: dia,
        hora_inicio: horaInicio,
        hora_fin: horaTermino,
        vueltas_o_tiempo: tipoDuracion,
        cliente: { id: cliente.id }, // Asociar el cliente a la reserva
        cantidad_personas: cantidadPersonas,
        detalles: personas.map((persona) => ({
          memberName: persona.nombre,
          rut: persona.rut,
          dateBirthday: persona.fechaCumpleanos,
        })),
      };
  
      const response = await confirmReserve(reserva); // Confirmar la reserva
      console.log('Reserva confirmada:', response);
      alert('Reserva confirmada exitosamente');
    } catch (error) {
      console.error('Error al confirmar la reserva:', error);
      alert('Hubo un error al confirmar la reserva');
    }
  };

  return (
    <div>
      <div className="container">
        <h1>Datos de reserva</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="codigoReserva">Código de Reserva:</label>
            <input
              type="text"
              id="codigoReserva"
              value={codigoReserva}
              onChange={(e) => setCodigoReserva(e.target.value)}
              required
            />
          </div>
          <p><strong>Día:</strong> {dia}</p>
          <p><strong>Hora de Inicio:</strong> {horaInicio}</p>
          <p><strong>Hora de Término:</strong> {horaTermino}</p>
          <p><strong>Tipo de Duración:</strong> {tipoDuracion}</p>

          <div>
            <label htmlFor="cantidadPersonas">Cantidad de Personas (1-15):</label>
            <input
              type="number"
              id="cantidadPersonas"
              min="1"
              max="15"
              value={cantidadPersonas}
              onChange={handleCantidadChange}
              required
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
            required
          />
        </div>
        <div>
          <label htmlFor={`rut-${index}`}>RUT:</label>
          <input
            type="text"
            id={`rut-${index}`}
            value={persona.rut}
            onChange={(e) => handlePersonaChange(index, 'rut', e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor={`fechaCumpleanos-${index}`}>Fecha de Cumpleaños:</label>
          <input
            type="date"
            id={`fechaCumpleanos-${index}`}
            value={persona.fechaCumpleanos}
            onChange={(e) => handlePersonaChange(index, 'fechaCumpleanos', e.target.value)}
            required
          />
        </div>
        {index === 0 && (
          <>
            <div>
              <label htmlFor={`email-${index}`}>Email:</label>
              <input
                type="email"
                id={`email-${index}`}
                value={persona.email || ''}
                onChange={(e) => handlePersonaChange(index, 'email', e.target.value)}
                required
              />
            </div>
            <div>
              <label htmlFor={`telefono-${index}`}>Teléfono:</label>
              <input
                type="text"
                id={`telefono-${index}`}
                value={persona.telefono || ''}
                onChange={(e) => handlePersonaChange(index, 'telefono', e.target.value)}
                required
              />
            </div>
          </>
        )}
      </div>
    ))}

          <button type="submit">Confirmar Reserva</button>
        </form>
      </div>
    </div>
  );
};

export default Formulario;