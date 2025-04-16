import React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

export default function ReportTable({ ingresosPorMes, totalGeneral }) {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="Reporte de ingresos">
        <TableHead>
          <TableRow>
            <TableCell>Número de vueltas o tiempo máximo permitido</TableCell>
            <TableCell align="right">Enero</TableCell>
            <TableCell align="right">Febrero</TableCell>
            <TableCell align="right">Marzo</TableCell>
            <TableCell align="right">TOTAL</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {['10 vueltas o máx 10 min', '15 vueltas o máx 15 min', '20 vueltas o máx 20 min'].map((categoria) => (
            <TableRow key={categoria}>
              <TableCell component="th" scope="row">
                {categoria}
              </TableCell>
              <TableCell align="right">{ingresosPorMes['Enero 2024']?.[categoria] || 0}</TableCell>
              <TableCell align="right">{ingresosPorMes['Febrero 2024']?.[categoria] || 0}</TableCell>
              <TableCell align="right">{ingresosPorMes['Marzo 2024']?.[categoria] || 0}</TableCell>
              <TableCell align="right">
                {(ingresosPorMes['Enero 2024']?.[categoria] || 0) +
                  (ingresosPorMes['Febrero 2024']?.[categoria] || 0) +
                  (ingresosPorMes['Marzo 2024']?.[categoria] || 0)}
              </TableCell>
            </TableRow>
          ))}
          <TableRow>
            <TableCell component="th" scope="row" style={{ fontWeight: 'bold' }}>
              TOTAL
            </TableCell>
            <TableCell align="right" style={{ fontWeight: 'bold' }}>
              {ingresosPorMes['Enero 2024']?.total || 0}
            </TableCell>
            <TableCell align="right" style={{ fontWeight: 'bold' }}>
              {ingresosPorMes['Febrero 2024']?.total || 0}
            </TableCell>
            <TableCell align="right" style={{ fontWeight: 'bold' }}>
              {ingresosPorMes['Marzo 2024']?.total || 0}
            </TableCell>
            <TableCell align="right" style={{ fontWeight: 'bold' }}>
              {totalGeneral}
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
}