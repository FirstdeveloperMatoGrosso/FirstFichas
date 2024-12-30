import React, { useState, useEffect } from 'react';
import {
  Typography,
  Paper,
  Chip,
} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

import { getTransactions } from '../services/supabase';

export default function Transactions() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const { data, error } = await getTransactions();
      if (error) throw error;
      setTransactions(data);
    } catch (error) {
      console.error('Erro ao carregar transações:', error);
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      field: 'created_at',
      headerName: 'Data',
      flex: 1,
      valueFormatter: (params) =>
        format(new Date(params.value), 'dd/MM/yyyy HH:mm', { locale: ptBR }),
    },
    {
      field: 'amount',
      headerName: 'Valor',
      flex: 1,
      valueFormatter: (params) =>
        new Intl.NumberFormat('pt-BR', {
          style: 'currency',
          currency: 'BRL',
        }).format(params.value / 100),
    },
    {
      field: 'payment_type',
      headerName: 'Forma de Pagamento',
      flex: 1,
      valueFormatter: (params) => {
        const types = {
          CREDIT: 'Crédito',
          DEBIT: 'Débito',
          PIX: 'PIX',
        };
        return types[params.value] || params.value;
      },
    },
    {
      field: 'status',
      headerName: 'Status',
      flex: 1,
      renderCell: (params) => {
        const status = {
          APPROVED: { label: 'Aprovado', color: 'success' },
          PENDING: { label: 'Pendente', color: 'warning' },
          REJECTED: { label: 'Rejeitado', color: 'error' },
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
      field: 'terminal',
      headerName: 'Terminal',
      flex: 1,
      valueGetter: (params) =>
        params.row.terminals ? params.row.terminals.serial : 'N/A',
    },
    {
      field: 'installments',
      headerName: 'Parcelas',
      flex: 1,
      valueFormatter: (params) =>
        params.value > 1 ? `${params.value}x` : 'À vista',
    },
  ];

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Transações
      </Typography>

      <Paper sx={{ height: 'calc(100vh - 200px)' }}>
        <DataGrid
          rows={transactions}
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
    </div>
  );
}
