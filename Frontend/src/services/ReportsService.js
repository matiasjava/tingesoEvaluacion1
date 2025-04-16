import dayjs from 'dayjs';
import axios from 'axios';
export const generarReporteIngresos = (reservas, fechaInicio, fechaFin) => {
  const reservasFiltradas = reservas.filter((reserva) => {
    const fechaReserva = dayjs(reserva.fecha_reserva);
    return fechaReserva.isBetween(fechaInicio, fechaFin, 'day', '[]');
  });

  // Inicializar ingresos por categoría
  const ingresosPorCategoria = {
    '10 vueltas o máx 10 min': 0,
    '15 vueltas o máx 15 min': 0,
    '20 vueltas o máx 20 min': 0,
  };

  // Tarifas por categoría (puedes ajustar estos valores según tu lógica)
  const tarifas = {
    '10 vueltas o máx 10 min': 4000,
    '15 vueltas o máx 15 min': 7000,
    '20 vueltas o máx 20 min': 2000,
  };

  // Calcular ingresos por categoría
  reservasFiltradas.forEach((reserva) => {
    const { tipo_duracion } = reserva; // Ejemplo: "10 vueltas o máx 10 min"
    if (ingresosPorCategoria[tipo_duracion] !== undefined) {
      ingresosPorCategoria[tipo_duracion] += tarifas[tipo_duracion];
    }
  });

  // Calcular totales por mes y general
  const ingresosPorMes = {};
  let totalGeneral = 0;

  reservasFiltradas.forEach((reserva) => {
    const mes = dayjs(reserva.fecha_reserva).format('MMMM YYYY'); // Ejemplo: "Enero 2024"
    const { tipo_duracion } = reserva;

    if (!ingresosPorMes[mes]) {
      ingresosPorMes[mes] = {
        '10 vueltas o máx 10 min': 0,
        '15 vueltas o máx 15 min': 0,
        '20 vueltas o máx 20 min': 0,
        total: 0,
      };
    }

    if (ingresosPorCategoria[tipo_duracion] !== undefined) {
      ingresosPorMes[mes][tipo_duracion] += tarifas[tipo_duracion];
      ingresosPorMes[mes].total += tarifas[tipo_duracion];
      totalGeneral += tarifas[tipo_duracion];
    }
  });

  return {
    ingresosPorCategoria,
    ingresosPorMes,
    totalGeneral,
  };
};