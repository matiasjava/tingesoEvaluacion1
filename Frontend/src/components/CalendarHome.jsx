import React, { useEffect, useState } from 'react';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { TextField } from '@mui/material';
import dayjs from 'dayjs';
import { getReservas } from '../services/CalendarHomeService';

export default function CalendarHome({ selectedDate, setSelectedDate, setAvailableTimes }) {
  const [reservas, setReservas] = useState([]);

  useEffect(() => {
    const fetchReservas = async () => {
      try {
        const reservasData = await getReservas();
        setReservas(reservasData);
      } catch (error) {
        console.error('Error al cargar las reservas:', error);
      }
    };

    fetchReservas();
  }, []);

  const handleDateChange = (newDate) => {
    setSelectedDate(newDate);

    if (!newDate) {
      setAvailableTimes([]);
      return;
    }

    // Filtra las reservas para el día seleccionado
    const reservasDelDia = reservas.filter((reserva) =>
      dayjs(reserva.fecha_reserva).isSame(newDate, 'day')
    );

    // Define las reglas de horarios
    const isWeekend = [0, 6].includes(newDate.day()); // Domingo = 0, Sábado = 6
    const startHour = isWeekend ? 10 : 14; // Hora de inicio
    const endHour = 22; // Hora de fin

    // Genera todas las horas posibles
    const allTimes = [];
    for (let hour = startHour; hour < endHour; hour++) {
      for (let minutes = 0; minutes < 60; minutes += 30) {
        const time = dayjs().hour(hour).minute(minutes).format('HH:mm');
        allTimes.push(time);
      }
    }

    // Filtra las horas ocupadas
    const horasOcupadas = reservasDelDia.flatMap((reserva) => {
      const startTime = dayjs(`${reserva.fecha_reserva}T${reserva.hora_inicio}`, 'YYYY-MM-DDTHH:mm');
      const endTime = dayjs(`${reserva.fecha_reserva}T${reserva.hora_fin}`, 'YYYY-MM-DDTHH:mm');
      const occupiedTimes = [];

      for (let time = startTime; time.isBefore(endTime); time = time.add(30, 'minute')) {
        occupiedTimes.push(time.format('HH:mm'));
      }

      return occupiedTimes;
    });

    // Genera la lista de horarios con etiquetas de disponibilidad
    const horariosConDisponibilidad = allTimes.map((time) => ({
      time,
      disponible: !horasOcupadas.includes(time), // Si no está en horas ocupadas, está disponible
    }));

    setAvailableTimes(horariosConDisponibilidad); // Actualiza los horarios con disponibilidad
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DatePicker
        label="Selecciona un día"
        value={selectedDate}
        onChange={handleDateChange}
        renderInput={(params) => <TextField {...params} />}
      />
    </LocalizationProvider>
  );
}