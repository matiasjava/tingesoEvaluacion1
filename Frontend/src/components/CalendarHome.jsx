import React from 'react';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { TextField } from '@mui/material';
import dayjs from 'dayjs';

export default function CalendarHome({ selectedDate, setSelectedDate }) {
  const unavailableDates = [
    dayjs('2025-04-05'),
    dayjs('2025-04-10'),
    dayjs('2025-04-15'),
  ];

  const disableUnavailableDates = (date) => {
    return unavailableDates.some((unavailableDate) =>
      date.isSame(unavailableDate, 'day')
    );
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DatePicker
        label="Selecciona un día"
        value={selectedDate}
        onChange={(newValue) => setSelectedDate(newValue)}
        shouldDisableDate={disableUnavailableDates}
        renderInput={(params) => <TextField {...params} />}
      />
    </LocalizationProvider>
  );
}