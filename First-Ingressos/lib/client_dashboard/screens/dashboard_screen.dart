import 'package:flutter/material.dart';
import '../widgets/responsive_layout.dart';
import '../widgets/stat_card.dart';
import '../widgets/recent_sales_chart.dart';
import '../widgets/upcoming_events_list.dart';

class DashboardScreen extends StatelessWidget {
  const DashboardScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildWelcomeSection(context),
            const SizedBox(height: 24),
            _buildStatCards(context),
            const SizedBox(height: 24),
            _buildChartSection(context),
            const SizedBox(height: 24),
            _buildUpcomingEvents(context),
          ],
        ),
      ),
    );
  }

  Widget _buildWelcomeSection(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(24),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [Theme.of(context).primaryColor, Theme.of(context).primaryColor.withOpacity(0.8)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(16),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Bem-vindo de volta!',
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              color: Colors.white,
            ),
          ),
          const SizedBox(height: 8),
          Text(
            'Aqui está um resumo do seu negócio',
            style: TextStyle(
              fontSize: 16,
              color: Colors.white.withOpacity(0.9),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStatCards(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        return Wrap(
          spacing: 16,
          runSpacing: 16,
          children: [
            StatCard(
              title: 'Vendas Hoje',
              value: 'R\$ 12.450',
              increase: '+15%',
              icon: Icons.attach_money,
              color: Colors.green,
              width: _getCardWidth(context, constraints),
            ),
            StatCard(
              title: 'Ingressos Vendidos',
              value: '1,234',
              increase: '+8%',
              icon: Icons.confirmation_number,
              color: Colors.blue,
              width: _getCardWidth(context, constraints),
            ),
            StatCard(
              title: 'Eventos Ativos',
              value: '12',
              increase: '+2',
              icon: Icons.event,
              color: Colors.orange,
              width: _getCardWidth(context, constraints),
            ),
            StatCard(
              title: 'Clientes Novos',
              value: '48',
              increase: '+25%',
              icon: Icons.people,
              color: Colors.purple,
              width: _getCardWidth(context, constraints),
            ),
          ],
        );
      },
    );
  }

  Widget _buildChartSection(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Vendas dos Últimos 7 Dias',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 16),
            SizedBox(
              height: 300,
              child: RecentSalesChart(),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildUpcomingEvents(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Próximos Eventos',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 16),
            SizedBox(
              height: 300,
              child: UpcomingEventsList(),
            ),
          ],
        ),
      ),
    );
  }

  double _getCardWidth(BuildContext context, BoxConstraints constraints) {
    if (ResponsiveLayout.isMobile(context)) {
      return constraints.maxWidth;
    } else if (ResponsiveLayout.isTablet(context)) {
      return (constraints.maxWidth - 16) / 2;
    } else {
      return (constraints.maxWidth - 48) / 4;
    }
  }
}
