import React, { useState } from 'react';
import {
  Typography,
  Paper,
  Grid,
  TextField,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  MenuItem,
} from '@mui/material';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

import { getSalesReport, getEventReport, getEvents } from '../services/supabase';

export default function Reports() {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [selectedEvent, setSelectedEvent] = useState('');
  const [events, setEvents] = useState([]);
  const [reportData, setReportData] = useState([]);
  const [reportType, setReportType] = useState('sales'); // 'sales' ou 'event'
  const [loading, setLoading] = useState(false);

  const handleGenerateReport = async () => {
    setLoading(true);
    try {
      if (reportType === 'sales') {
        const { data } = await getSalesReport(startDate, endDate);
        setReportData(data || []);
      } else {
        const { data } = await getEventReport(selectedEvent);
        setReportData(data || []);
      }
    } catch (error) {
      console.error('Erro ao gerar relatório:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadEvents = async () => {
    try {
      const { data } = await getEvents();
      setEvents(data || []);
    } catch (error) {
      console.error('Erro ao carregar eventos:', error);
    }
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value / 100);
  };

  const renderSalesReport = () => (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Data</TableCell>
            <TableCell>Terminal</TableCell>
            <TableCell>Forma de Pagamento</TableCell>
            <TableCell align="right">Valor</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reportData.map((row) => (
            <TableRow key={row.id}>
              <TableCell>
                {format(new Date(row.created_at), 'dd/MM/yyyy HH:mm', {
                  locale: ptBR,
                })}
              </TableCell>
              <TableCell>{row.terminals?.serial || 'N/A'}</TableCell>
              <TableCell>{row.payment_type}</TableCell>
              <TableCell align="right">{formatCurrency(row.amount)}</TableCell>
              <TableCell>{row.status}</TableCell>
            </TableRow>
          ))}
          {reportData.length > 0 && (
            <TableRow>
              <TableCell colSpan={3}>
                <strong>Total</strong>
              </TableCell>
              <TableCell align="right">
                <strong>
                  {formatCurrency(
                    reportData.reduce((acc, curr) => acc + curr.amount, 0)
                  )}
                </strong>
              </TableCell>
              <TableCell />
            </TableRow>
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );

  const renderEventReport = () => (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Data</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Data de Validação</TableCell>
            <TableCell align="right">Valor</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reportData.map((row) => (
            <TableRow key={row.id}>
              <TableCell>
                {format(new Date(row.created_at), 'dd/MM/yyyy HH:mm', {
                  locale: ptBR,
                })}
              </TableCell>
              <TableCell>{row.status}</TableCell>
              <TableCell>
                {row.validated_at
                  ? format(new Date(row.validated_at), 'dd/MM/yyyy HH:mm', {
                      locale: ptBR,
                    })
                  : 'Não validado'}
              </TableCell>
              <TableCell align="right">
                {formatCurrency(row.transactions?.amount || 0)}
              </TableCell>
            </TableRow>
          ))}
          {reportData.length > 0 && (
            <TableRow>
              <TableCell colSpan={3}>
                <strong>Total</strong>
              </TableCell>
              <TableCell align="right">
                <strong>
                  {formatCurrency(
                    reportData.reduce(
                      (acc, curr) => acc + (curr.transactions?.amount || 0),
                      0
                    )
                  )}
                </strong>
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Relatórios
      </Typography>

      <Paper sx={{ p: 2, mb: 2 }}>
        <Grid container spacing={2}>
          <Grid item xs={12} md={3}>
            <TextField
              select
              fullWidth
              label="Tipo de Relatório"
              value={reportType}
              onChange={(e) => setReportType(e.target.value)}
            >
              <MenuItem value="sales">Vendas</MenuItem>
              <MenuItem value="event">Evento</MenuItem>
            </TextField>
          </Grid>

          {reportType === 'sales' ? (
            <>
              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  label="Data Inicial"
                  type="datetime-local"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  InputLabelProps={{
                    shrink: true,
                  }}
                />
              </Grid>
              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  label="Data Final"
                  type="datetime-local"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  InputLabelProps={{
                    shrink: true,
                  }}
                />
              </Grid>
            </>
          ) : (
            <Grid item xs={12} md={6}>
              <TextField
                select
                fullWidth
                label="Evento"
                value={selectedEvent}
                onChange={(e) => setSelectedEvent(e.target.value)}
                onClick={() => !events.length && loadEvents()}
              >
                {events.map((event) => (
                  <MenuItem key={event.id} value={event.id}>
                    {event.name}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
          )}

          <Grid item xs={12} md={3}>
            <Button
              variant="contained"
              onClick={handleGenerateReport}
              disabled={loading}
              sx={{ height: '56px' }}
              fullWidth
            >
              {loading ? 'Gerando...' : 'Gerar Relatório'}
            </Button>
          </Grid>
        </Grid>
      </Paper>

      {reportData.length > 0 &&
        (reportType === 'sales' ? renderSalesReport() : renderEventReport())}
    </div>
  );
}
