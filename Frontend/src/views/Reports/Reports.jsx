import React, { useState } from 'react';
import { generarReporteIngresos } from '../../services/ReportsService';
import ReportTable from '../../components/ReportTable';
import { TextField, Button, Box } from '@mui/material';
import dayjs from 'dayjs';
import './Reports.css'; // Asegúrate de importar el archivo CSS

const ReportView = () => {
  const [mesInicio, setMesInicio] = useState('');
  const [mesFin, setMesFin] = useState('');
  const [reporte, setReporte] = useState(null);

  const handleGenerateReport = () => {
    const reservas = [
      { fecha_reserva: '2024-01-15', tipo_duracion: '10 vueltas o máx 10 min' },
      { fecha_reserva: '2024-01-20', tipo_duracion: '15 vueltas o máx 15 min' },
      { fecha_reserva: '2024-02-10', tipo_duracion: '20 vueltas o máx 20 min' },
      { fecha_reserva: '2024-02-15', tipo_duracion: '15 vueltas o máx 15 min' },
      { fecha_reserva: '2024-03-05', tipo_duracion: '10 vueltas o máx 10 min' },
    ];

    if (!mesInicio || !mesFin) {
      alert('Por favor, selecciona ambos meses.');
      return;
    }

    const fechaInicio = dayjs(mesInicio).startOf('month').format('YYYY-MM-DD');
    const fechaFin = dayjs(mesFin).endOf('month').format('YYYY-MM-DD');

    const reporteGenerado = generarReporteIngresos(reservas, fechaInicio, fechaFin);
    setReporte(reporteGenerado);
  };

  return (
    <div className="reports-page">
      <h1>Reporte de Ingresos</h1>

      <Box display="flex" gap={2} mb={4}>
        <TextField
          label="Mes de inicio"
          type="month"
          value={mesInicio}
          onChange={(e) => setMesInicio(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          label="Mes de fin"
          type="month"
          value={mesFin}
          onChange={(e) => setMesFin(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
        <Button variant="contained" color="primary" onClick={handleGenerateReport}>
          Generar Reporte
        </Button>
      </Box>

      {reporte ? (
        <ReportTable ingresosPorMes={reporte.ingresosPorMes} totalGeneral={reporte.totalGeneral} />
      ) : (
        <p>Selecciona un rango de meses y genera el reporte.</p>
      )}
    </div>
  );
};

export default ReportView;