import React, { useState } from 'react';
import { Box, TextField, Button, MenuItem, Typography } from '@mui/material';
import CalendarHome from '../../components/CalendarHome';
import './Home.css';

const Home = () => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState('');
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    rut: '',
    correo: '',
    telefono: '',
  });

  const horariosDisponibles = ['10:00 AM', '12:00 PM', '2:00 PM', '4:00 PM'];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleConfirm = () => {
    console.log('Datos confirmados:', { selectedDate, selectedTime, ...formData });
    alert('Reserva confirmada');
  };

  return (
    <Box className="home-container">
      <Typography variant="h4" className="title">
        Disfruta de la mejor experiencia
      </Typography>

      <Box className="calendar-container">
        <Typography variant="h6">Selecciona una fecha:</Typography>
        <CalendarHome
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
        />
      </Box>

      {selectedDate && (
        <Box className="time-container">
          <Typography variant="h6">Selecciona un horario:</Typography>
          <TextField
            select
            label="Horario"
            value={selectedTime}
            onChange={(e) => setSelectedTime(e.target.value)}
            fullWidth
          >
            {horariosDisponibles.map((horario) => (
              <MenuItem key={horario} value={horario}>
                {horario}
              </MenuItem>
            ))}
          </TextField>
        </Box>
      )}

      {selectedTime && (
        <Box className="form-container">
          <Typography variant="h6">Ingresa tus datos personales:</Typography>
          <TextField
            label="Nombre"
            name="nombre"
            value={formData.nombre}
            onChange={handleInputChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="Apellido"
            name="apellido"
            value={formData.apellido}
            onChange={handleInputChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="RUT"
            name="rut"
            value={formData.rut}
            onChange={handleInputChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="Correo"
            name="correo"
            value={formData.correo}
            onChange={handleInputChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="Teléfono"
            name="telefono"
            value={formData.telefono}
            onChange={handleInputChange}
            fullWidth
            margin="normal"
          />
          <Button
            variant="contained"
            color="primary"
            onClick={handleConfirm}
            className="confirm-button"
          >
            Confirmar Reserva
          </Button>
        </Box>
      )}
    </Box>
  );
};

export default Home;