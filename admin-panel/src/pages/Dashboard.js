import React, { useState, useEffect } from 'react';
import {
  Grid,
  Paper,
  Typography,
  Card,
  CardContent,
  CircularProgress,
} from '@mui/material';
import {
  PointOfSale as SalesIcon,
  ConfirmationNumber as TicketIcon,
  Event as EventIcon,
  PhoneAndroid as TerminalIcon,
} from '@mui/icons-material';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

import { getDashboardStats, getTransactions } from '../services/supabase';

const StatCard = ({ title, value, icon: Icon, loading }) => (
  <Card>
    <CardContent>
      <Grid container spacing={2} alignItems="center">
        <Grid item>
          <Icon color="primary" sx={{ fontSize: 40 }} />
        </Grid>
        <Grid item xs>
          <Typography color="textSecondary" gutterBottom>
            {title}
          </Typography>
          {loading ? (
            <CircularProgress size={20} />
          ) : (
            <Typography variant="h5" component="div">
              {value}
            </Typography>
          )}
        </Grid>
      </Grid>
    </CardContent>
  </Card>
);

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [salesData, setSalesData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Busca estatísticas gerais
        const stats = await getDashboardStats();
        setStats(stats);

        // Busca dados de vendas para o gráfico
        const { data: transactions } = await getTransactions();
        const dailySales = transactions.reduce((acc, transaction) => {
          const date = format(new Date(transaction.created_at), 'dd/MM', { locale: ptBR });
          if (!acc[date]) {
            acc[date] = 0;
          }
          acc[date] += transaction.amount;
          return acc;
        }, {});

        const chartData = Object.entries(dailySales).map(([date, amount]) => ({
          date,
          amount: amount / 100, // Converte centavos para reais
        }));

        setSalesData(chartData);
        setLoading(false);
      } catch (error) {
        console.error('Erro ao carregar dashboard:', error);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value / 100);
  };

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Vendas do Mês"
            value={stats ? formatCurrency(stats.monthlySales) : '-'}
            icon={SalesIcon}
            loading={loading}
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Ingressos Vendidos"
            value={stats?.ticketsSold || 0}
            icon={TicketIcon}
            loading={loading}
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Eventos Ativos"
            value={stats?.activeEvents || 0}
            icon={EventIcon}
            loading={loading}
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Terminais Ativos"
            value={stats?.activeTerminals || 0}
            icon={TerminalIcon}
            loading={loading}
          />
        </Grid>

        <Grid item xs={12}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Vendas Diárias
            </Typography>
            <div style={{ height: 300 }}>
              {loading ? (
                <CircularProgress />
              ) : (
                <ResponsiveContainer>
                  <LineChart data={salesData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="date" />
                    <YAxis />
                    <Tooltip
                      formatter={(value) =>
                        new Intl.NumberFormat('pt-BR', {
                          style: 'currency',
                          currency: 'BRL',
                        }).format(value)
                      }
                    />
                    <Line
                      type="monotone"
                      dataKey="amount"
                      stroke="#1976d2"
                      strokeWidth={2}
                    />
                  </LineChart>
                </ResponsiveContainer>
              )}
            </div>
          </Paper>
        </Grid>
      </Grid>
    </div>
  );
}
