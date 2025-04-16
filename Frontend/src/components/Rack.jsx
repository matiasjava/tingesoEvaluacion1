import React from 'react';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';

const WeeklyCalendar = ({ events }) => {
    const renderEventContent = (eventInfo) => {
        console.log('Contenido del evento:', eventInfo.event.extendedProps); // Verifica los datos aquí
        return (
          <div>
            <b>{eventInfo.timeText}</b> {/* Muestra la hora del evento */}
            <div>{eventInfo.event.extendedProps.codigo_reserva}</div> {/* Muestra el código de reserva */}
          </div>
        );
      };

  return (
    <div style={{ height: '800px', width: '100%' }}>
        <FullCalendar
        plugins={[timeGridPlugin, interactionPlugin]}
        initialView="timeGridWeek"
        events={events} // Asegúrate de que los eventos incluyan codigo_reserva
        editable={false}
        selectable={false}
        eventContent={renderEventContent} // Personaliza el contenido del evento
        headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'timeGridWeek,timeGridDay',
        }}
        slotMinTime="10:00:00" 
        slotMaxTime="22:00:00"
        />
    </div>
  );
};

export default WeeklyCalendar;