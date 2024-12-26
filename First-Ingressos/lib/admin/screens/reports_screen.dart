import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:intl/intl.dart';
import '../services/reports_service.dart';
import '../widgets/summary_card.dart';
import '../widgets/chart_card.dart';
import '../widgets/data_table_card.dart';

class ReportsScreen extends StatefulWidget {
  const ReportsScreen({Key? key}) : super(key: key);

  @override
  State<ReportsScreen> createState() => _ReportsScreenState();
}

class _ReportsScreenState extends State<ReportsScreen> {
  DateTimeRange _dateRange = DateTimeRange(
    start: DateTime.now().subtract(const Duration(days: 30)),
    end: DateTime.now(),
  );
  ReportsSummary? _summary;
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadReports();
  }

  Future<void> _loadReports() async {
    setState(() {
      _isLoading = true;
    });

    try {
      final summary = await ReportsSummary.generate(
        startDate: _dateRange.start,
        endDate: _dateRange.end,
      );

      setState(() {
        _summary = summary;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
      });

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Erro ao carregar relatórios: ${e.toString()}'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  Future<void> _selectDateRange() async {
    final picked = await showDateRangePicker(
      context: context,
      firstDate: DateTime(2020),
      lastDate: DateTime.now(),
      initialDateRange: _dateRange,
    );

    if (picked != null && picked != _dateRange) {
      setState(() {
        _dateRange = picked;
      });
      _loadReports();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Relatórios de Vendas'),
        actions: [
          IconButton(
            icon: const Icon(Icons.calendar_today),
            onPressed: _selectDateRange,
          ),
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _loadReports,
          ),
          IconButton(
            icon: const Icon(Icons.download),
            onPressed: () {
              // TODO: Implementar exportação de relatórios
            },
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _summary == null
              ? const Center(child: Text('Nenhum dado disponível'))
              : SingleChildScrollView(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Período: ${DateFormat('dd/MM/yyyy').format(_dateRange.start)} - ${DateFormat('dd/MM/yyyy').format(_dateRange.end)}',
                        style: Theme.of(context).textTheme.titleMedium,
                      ),
                      const SizedBox(height: 16),
                      GridView.count(
                        crossAxisCount: MediaQuery.of(context).size.width > 1200
                            ? 4
                            : MediaQuery.of(context).size.width > 800
                                ? 3
                                : 2,
                        crossAxisSpacing: 16,
                        mainAxisSpacing: 16,
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        children: [
                          SummaryCard(
                            title: 'Receita Total',
                            value:
                                'R\$ ${NumberFormat('#,##0.00', 'pt_BR').format(_summary!.totalRevenue)}',
                            icon: Icons.attach_money,
                            color: Colors.green,
                          ),
                          SummaryCard(
                            title: 'Ingressos Vendidos',
                            value: NumberFormat('#,##0', 'pt_BR')
                                .format(_summary!.totalTickets),
                            icon: Icons.confirmation_number,
                            color: Colors.blue,
                          ),
                          SummaryCard(
                            title: 'Média por Venda',
                            value:
                                'R\$ ${NumberFormat('#,##0.00', 'pt_BR').format(_summary!.totalRevenue / _summary!.totalTickets)}',
                            icon: Icons.analytics,
                            color: Colors.purple,
                          ),
                          SummaryCard(
                            title: 'Taxa de Conversão',
                            value: '65%', // TODO: Implementar cálculo real
                            icon: Icons.trending_up,
                            color: Colors.orange,
                          ),
                        ],
                      ),
                      const SizedBox(height: 24),
                      Row(
                        children: [
                          Expanded(
                            child: ChartCard(
                              title: 'Receita Diária',
                              chart: FutureBuilder<List<DataPoint>>(
                                future: ReportsService.getRevenueChart(
                                  _dateRange.start,
                                  _dateRange.end,
                                ),
                                builder: (context, snapshot) {
                                  if (!snapshot.hasData) {
                                    return const Center(
                                      child: CircularProgressIndicator(),
                                    );
                                  }

                                  return LineChart(
                                    LineChartData(
                                      // TODO: Implementar configuração do gráfico
                                    ),
                                  );
                                },
                              ),
                            ),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: ChartCard(
                              title: 'Ingressos Vendidos',
                              chart: FutureBuilder<List<DataPoint>>(
                                future: ReportsService.getTicketsChart(
                                  _dateRange.start,
                                  _dateRange.end,
                                ),
                                builder: (context, snapshot) {
                                  if (!snapshot.hasData) {
                                    return const Center(
                                      child: CircularProgressIndicator(),
                                    );
                                  }

                                  return BarChart(
                                    BarChartData(
                                      // TODO: Implementar configuração do gráfico
                                    ),
                                  );
                                },
                              ),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 24),
                      DataTableCard(
                        title: 'Vendas por Evento',
                        columns: const ['Evento', 'Ingressos', 'Receita'],
                        rows: _summary!.revenueByEvent.entries
                            .map((e) => [
                                  e.key,
                                  NumberFormat('#,##0', 'pt_BR')
                                      .format(_summary!.ticketsByEvent[e.key]),
                                  'R\$ ${NumberFormat('#,##0.00', 'pt_BR').format(e.value)}',
                                ])
                            .toList(),
                      ),
                      const SizedBox(height: 16),
                      DataTableCard(
                        title: 'Vendas por Forma de Pagamento',
                        columns: const ['Método', 'Valor', 'Porcentagem'],
                        rows: _summary!.revenueByPaymentMethod.entries
                            .map((e) => [
                                  e.key,
                                  'R\$ ${NumberFormat('#,##0.00', 'pt_BR').format(e.value)}',
                                  '${NumberFormat('#,##0.0', 'pt_BR').format(e.value / _summary!.totalRevenue * 100)}%',
                                ])
                            .toList(),
                      ),
                      const SizedBox(height: 16),
                      DataTableCard(
                        title: 'Últimas Vendas',
                        columns: const [
                          'Data',
                          'Evento',
                          'Tipo',
                          'Qtd',
                          'Valor',
                          'Pagamento',
                          'Status'
                        ],
                        rows: _summary!.recentSales
                            .map((sale) => [
                                  DateFormat('dd/MM/yyyy HH:mm')
                                      .format(sale.date),
                                  sale.eventName,
                                  sale.ticketType,
                                  sale.quantity.toString(),
                                  'R\$ ${NumberFormat('#,##0.00', 'pt_BR').format(sale.totalAmount)}',
                                  sale.paymentMethod,
                                  sale.status,
                                ])
                            .toList(),
                      ),
                    ],
                  ),
                ),
    );
  }
}
