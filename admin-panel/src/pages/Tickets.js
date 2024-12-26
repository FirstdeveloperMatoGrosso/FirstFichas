import React, { useState, useEffect } from 'react';
import {
  Typography,
  Paper,
  Chip,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import QRCode from 'qrcode.react';

import { getTickets, validateTicket } from '../services/supabase';

export default function Tickets() {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);

  useEffect(() => {
    fetchTickets();
  }, []);

  const fetchTickets = async () => {
    try {
      const { data, error } = await getTickets();
      if (error) throw error;
      setTickets(data);
    } catch (error) {
      console.error('Erro ao carregar ingressos:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleValidate = async (id) => {
    try {
      await validateTicket(id);
      fetchTickets();
    } catch (error) {
      console.error('Erro ao validar ingresso:', error);
    }
  };

  const handleOpenDialog = (ticket) => {
    setSelectedTicket(ticket);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setSelectedTicket(null);
    setOpenDialog(false);
  };

  const columns = [
    {
      field: 'event',
      headerName: 'Evento',
      flex: 1,
      valueGetter: (params) => params.row.events?.name || 'N/A',
    },
    {
      field: 'created_at',
      headerName: 'Data da Compra',
      flex: 1,
      valueFormatter: (params) =>
        format(new Date(params.value), 'dd/MM/yyyy HH:mm', { locale: ptBR }),
    },
    {
      field: 'status',
      headerName: 'Status',
      flex: 1,
      renderCell: (params) => {
        const status = {
          ACTIVE: { label: 'Ativo', color: 'success' },
          VALIDATED: { label: 'Validado', color: 'primary' },
          CANCELLED: { label: 'Cancelado', color: 'error' },
        };
        const current = status[params.value] || {
          label: params.value,
          color: 'default',
        };
        return (
          <Chip
            label={current.label}
            color={current.color}
            variant="outlined"
          />
        );
      },
    },
    {
      field: 'validated_at',
      headerName: 'Data de Validação',
      flex: 1,
      valueFormatter: (params) =>
        params.value
          ? format(new Date(params.value), 'dd/MM/yyyy HH:mm', { locale: ptBR })
          : 'Não validado',
    },
    {
      field: 'actions',
      headerName: 'Ações',
      flex: 1,
      renderCell: (params) => (
        <>
          <Button
            variant="outlined"
            size="small"
            onClick={() => handleOpenDialog(params.row)}
            sx={{ mr: 1 }}
          >
            Ver QR Code
          </Button>
          {params.row.status === 'ACTIVE' && (
            <Button
              variant="contained"
              size="small"
              onClick={() => handleValidate(params.row.id)}
              color="primary"
            >
              Validar
            </Button>
          )}
        </>
      ),
    },
  ];

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Ingressos
      </Typography>

      <Paper sx={{ height: 'calc(100vh - 200px)' }}>
        <DataGrid
          rows={tickets}
          columns={columns}
          loading={loading}
          disableSelectionOnClick
          pagination
          initialState={{
            sorting: {
              sortModel: [{ field: 'created_at', sort: 'desc' }],
            },
          }}
        />
      </Paper>

      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>QR Code do Ingresso</DialogTitle>
        <DialogContent>
          {selectedTicket && (
            <div style={{ textAlign: 'center', padding: '20px' }}>
              <QRCode value={selectedTicket.qr_code} size={256} />
              <Typography variant="body1" sx={{ mt: 2 }}>
                Evento: {selectedTicket.events?.name}
              </Typography>
              <Typography variant="body2" color="textSecondary">
                ID: {selectedTicket.id}
              </Typography>
            </div>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Fechar</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
