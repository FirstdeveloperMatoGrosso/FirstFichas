import React, { useState, useEffect } from 'react';
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Typography,
  Paper,
} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

import { getEvents, createEvent, updateEvent } from '../services/supabase';

export default function Events() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    date: '',
    location: '',
    price: '',
    total_tickets: '',
  });

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const { data, error } = await getEvents();
      if (error) throw error;
      setEvents(data);
    } catch (error) {
      console.error('Erro ao carregar eventos:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (event = null) => {
    if (event) {
      setSelectedEvent(event);
      setFormData({
        name: event.name,
        description: event.description,
        date: format(new Date(event.date), "yyyy-MM-dd'T'HH:mm"),
        location: event.location,
        price: event.price,
        total_tickets: event.total_tickets,
      });
    } else {
      setSelectedEvent(null);
      setFormData({
        name: '',
        description: '',
        date: '',
        location: '',
        price: '',
        total_tickets: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedEvent(null);
    setFormData({
      name: '',
      description: '',
      date: '',
      location: '',
      price: '',
      total_tickets: '',
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const eventData = {
        ...formData,
        price: Number(formData.price) * 100, // Converte para centavos
        total_tickets: Number(formData.total_tickets),
        available_tickets: selectedEvent
          ? undefined
          : Number(formData.total_tickets),
      };

      if (selectedEvent) {
        await updateEvent(selectedEvent.id, eventData);
      } else {
        await createEvent(eventData);
      }

      handleCloseDialog();
      fetchEvents();
    } catch (error) {
      console.error('Erro ao salvar evento:', error);
    }
  };

  const columns = [
    { field: 'name', headerName: 'Nome', flex: 1 },
    { field: 'location', headerName: 'Local', flex: 1 },
    {
      field: 'date',
      headerName: 'Data',
      flex: 1,
      valueFormatter: (params) =>
        format(new Date(params.value), 'dd/MM/yyyy HH:mm', { locale: ptBR }),
    },
    {
      field: 'price',
      headerName: 'Preço',
      flex: 1,
      valueFormatter: (params) =>
        new Intl.NumberFormat('pt-BR', {
          style: 'currency',
          currency: 'BRL',
        }).format(params.value / 100),
    },
    {
      field: 'available_tickets',
      headerName: 'Ingressos Disponíveis',
      flex: 1,
      valueFormatter: (params) =>
        `${params.value} / ${params.row.total_tickets}`,
    },
    {
      field: 'actions',
      headerName: 'Ações',
      flex: 1,
      renderCell: (params) => (
        <Button
          variant="outlined"
          size="small"
          onClick={() => handleOpenDialog(params.row)}
        >
          Editar
        </Button>
      ),
    },
  ];

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Eventos
      </Typography>

      <Button
        variant="contained"
        color="primary"
        onClick={() => handleOpenDialog()}
        sx={{ mb: 2 }}
      >
        Novo Evento
      </Button>

      <Paper sx={{ height: 'calc(100vh - 200px)' }}>
        <DataGrid
          rows={events}
          columns={columns}
          loading={loading}
          disableSelectionOnClick
          pagination
        />
      </Paper>

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {selectedEvent ? 'Editar Evento' : 'Novo Evento'}
        </DialogTitle>
        <DialogContent>
          <form onSubmit={handleSubmit}>
            <TextField
              margin="normal"
              required
              fullWidth
              label="Nome"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
            />
            <TextField
              margin="normal"
              required
              fullWidth
              label="Descrição"
              multiline
              rows={4}
              value={formData.description}
              onChange={(e) =>
                setFormData({ ...formData, description: e.target.value })
              }
            />
            <TextField
              margin="normal"
              required
              fullWidth
              label="Data e Hora"
              type="datetime-local"
              value={formData.date}
              onChange={(e) =>
                setFormData({ ...formData, date: e.target.value })
              }
              InputLabelProps={{
                shrink: true,
              }}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              label="Local"
              value={formData.location}
              onChange={(e) =>
                setFormData({ ...formData, location: e.target.value })
              }
            />
            <TextField
              margin="normal"
              required
              fullWidth
              label="Preço"
              type="number"
              value={formData.price}
              onChange={(e) =>
                setFormData({ ...formData, price: e.target.value })
              }
            />
            <TextField
              margin="normal"
              required
              fullWidth
              label="Total de Ingressos"
              type="number"
              value={formData.total_tickets}
              onChange={(e) =>
                setFormData({ ...formData, total_tickets: e.target.value })
              }
              disabled={selectedEvent}
            />
          </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancelar</Button>
          <Button onClick={handleSubmit} variant="contained" color="primary">
            Salvar
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
