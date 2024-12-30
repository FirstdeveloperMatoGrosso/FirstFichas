import React, { useState, useEffect } from 'react';
import {
  Typography,
  Paper,
  Chip,
  IconButton,
  Tooltip,
} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import {
  CheckCircle as OnlineIcon,
  Cancel as OfflineIcon,
  Refresh as RefreshIcon,
} from '@mui/icons-material';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

import { getTerminals, updateTerminal } from '../services/supabase';

export default function Terminals() {
  const [terminals, setTerminals] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTerminals();
  }, []);

  const fetchTerminals = async () => {
    try {
      const { data, error } = await getTerminals();
      if (error) throw error;
      setTerminals(data);
    } catch (error) {
      console.error('Erro ao carregar terminais:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleRefresh = async (id) => {
    try {
      await updateTerminal(id, { last_seen: new Date() });
      fetchTerminals();
    } catch (error) {
      console.error('Erro ao atualizar terminal:', error);
    }
  };

  const columns = [
    { field: 'serial', headerName: 'Serial', flex: 1 },
    { field: 'model', headerName: 'Modelo', flex: 1 },
    {
      field: 'status',
      headerName: 'Status',
      flex: 1,
      renderCell: (params) => (
        <Chip
          icon={params.row.is_active ? <OnlineIcon /> : <OfflineIcon />}
          label={params.row.is_active ? 'Online' : 'Offline'}
          color={params.row.is_active ? 'success' : 'error'}
          variant="outlined"
        />
      ),
    },
    {
      field: 'last_seen',
      headerName: 'Última Atividade',
      flex: 1,
      valueFormatter: (params) =>
        params.value
          ? format(new Date(params.value), 'dd/MM/yyyy HH:mm', { locale: ptBR })
          : 'Nunca',
    },
    {
      field: 'actions',
      headerName: 'Ações',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title="Atualizar Status">
          <IconButton
            size="small"
            onClick={() => handleRefresh(params.row.id)}
            color="primary"
          >
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      ),
    },
  ];

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Terminais
      </Typography>

      <Paper sx={{ height: 'calc(100vh - 200px)' }}>
        <DataGrid
          rows={terminals}
          columns={columns}
          loading={loading}
          disableSelectionOnClick
          pagination
        />
      </Paper>
    </div>
  );
}
