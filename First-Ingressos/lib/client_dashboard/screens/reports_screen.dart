import 'package:flutter/material.dart';
import '../widgets/responsive_layout.dart';

class ReportsScreen extends StatefulWidget {
  const ReportsScreen({Key? key}) : super(key: key);

  @override
  State<ReportsScreen> createState() => _ReportsScreenState();
}

class _ReportsScreenState extends State<ReportsScreen> {
  final List<String> _reportTypes = [
    'Vendas por Período',
    'Vendas por Produto',
    'Vendas por Evento',
    'Análise de Clientes',
    'Relatório Financeiro',
    'Performance de Vendedores',
  ];

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _buildHeader(),
          const SizedBox(height: 24),
          _buildFilters(),
          const SizedBox(height: 16),
          Expanded(
            child: _buildReportsGrid(),
          ),
        ],
      ),
    );
  }

  Widget _buildHeader() {
    return const Text(
      'Relatórios',
      style: TextStyle(
        fontSize: 24,
        fontWeight: FontWeight.bold,
      ),
    );
  }

  Widget _buildFilters() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        'Período',
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Expanded(
                            child: TextField(
                              decoration: InputDecoration(
                                hintText: 'Data Inicial',
                                prefixIcon: const Icon(Icons.calendar_today),
                                border: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(8),
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: TextField(
                              decoration: InputDecoration(
                                hintText: 'Data Final',
                                prefixIcon: const Icon(Icons.calendar_today),
                                border: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(8),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                const SizedBox(width: 16),
                ElevatedButton.icon(
                  onPressed: () {
                    // TODO: Implementar geração de relatório
                  },
                  icon: const Icon(Icons.file_download),
                  label: const Text('Exportar'),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildReportsGrid() {
    return GridView.builder(
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: _getGridCrossAxisCount(context),
        childAspectRatio: 1.5,
        crossAxisSpacing: 16,
        mainAxisSpacing: 16,
      ),
      itemCount: _reportTypes.length,
      itemBuilder: (context, index) {
        return _buildReportCard(_reportTypes[index]);
      },
    );
  }

  Widget _buildReportCard(String reportType) {
    return Card(
      child: InkWell(
        onTap: () {
          _showReportDetails(reportType);
        },
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                _getReportIcon(reportType),
                size: 48,
                color: Theme.of(context).primaryColor,
              ),
              const SizedBox(height: 16),
              Text(
                reportType,
                style: const TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 8),
              Text(
                _getReportDescription(reportType),
                style: TextStyle(
                  fontSize: 12,
                  color: Colors.grey[600],
                ),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _showReportDetails(String reportType) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(reportType),
        content: SizedBox(
          width: 800,
          height: 500,
          child: Column(
            children: [
              // Aqui você pode adicionar gráficos, tabelas e outros elementos específicos para cada tipo de relatório
              const Placeholder(
                fallbackHeight: 400,
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Fechar'),
          ),
          ElevatedButton.icon(
            onPressed: () {
              // TODO: Implementar exportação
            },
            icon: const Icon(Icons.file_download),
            label: const Text('Exportar'),
          ),
        ],
      ),
    );
  }

  IconData _getReportIcon(String reportType) {
    switch (reportType) {
      case 'Vendas por Período':
        return Icons.timeline;
      case 'Vendas por Produto':
        return Icons.inventory;
      case 'Vendas por Evento':
        return Icons.event;
      case 'Análise de Clientes':
        return Icons.people;
      case 'Relatório Financeiro':
        return Icons.attach_money;
      case 'Performance de Vendedores':
        return Icons.person;
      default:
        return Icons.bar_chart;
    }
  }

  String _getReportDescription(String reportType) {
    switch (reportType) {
      case 'Vendas por Período':
        return 'Análise detalhada das vendas em um período específico';
      case 'Vendas por Produto':
        return 'Desempenho individual de cada produto';
      case 'Vendas por Evento':
        return 'Análise de vendas por evento realizado';
      case 'Análise de Clientes':
        return 'Informações sobre o comportamento dos clientes';
      case 'Relatório Financeiro':
        return 'Visão geral das finanças e faturamento';
      case 'Performance de Vendedores':
        return 'Análise do desempenho da equipe de vendas';
      default:
        return '';
    }
  }

  int _getGridCrossAxisCount(BuildContext context) {
    if (ResponsiveLayout.isMobile(context)) {
      return 1;
    } else if (ResponsiveLayout.isTablet(context)) {
      return 2;
    } else {
      return 3;
    }
  }
}
