import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'widgets/client_sidebar.dart';
import 'widgets/responsive_layout.dart';
import 'screens/dashboard_screen.dart';
import 'screens/products_screen.dart';
import 'screens/events_screen.dart';
import 'screens/reports_screen.dart';
import 'screens/settings_screen.dart';
import 'screens/sales_screen.dart';
import 'screens/credentials_screen.dart';

class ClientDashboard extends ConsumerStatefulWidget {
  const ClientDashboard({Key? key}) : super(key: key);

  @override
  ConsumerState<ClientDashboard> createState() => _ClientDashboardState();
}

class _ClientDashboardState extends ConsumerState<ClientDashboard> {
  int _selectedIndex = 0;
  bool _isSidebarExpanded = true;

  final List<({Widget screen, String title, IconData icon})> _screens = [
    (
      screen: const DashboardScreen(),
      title: 'Dashboard',
      icon: Icons.dashboard,
    ),
    (
      screen: const ProductsScreen(),
      title: 'Produtos',
      icon: Icons.inventory,
    ),
    (
      screen: const EventsScreen(),
      title: 'Eventos',
      icon: Icons.event,
    ),
    (
      screen: const CredentialsScreen(),
      title: 'Credenciais',
      icon: Icons.badge,
    ),
    (
      screen: const SalesScreen(),
      title: 'Vendas',
      icon: Icons.point_of_sale,
    ),
    (
      screen: const ReportsScreen(),
      title: 'Relatórios',
      icon: Icons.bar_chart,
    ),
    (
      screen: const SettingsScreen(),
      title: 'Configurações',
      icon: Icons.settings,
    ),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ResponsiveLayout(
        mobile: _buildMobileLayout(),
        tablet: _buildTabletLayout(),
        desktop: _buildDesktopLayout(),
      ),
    );
  }

  Widget _buildMobileLayout() {
    return Scaffold(
      appBar: AppBar(
        title: Text(_screens[_selectedIndex].title),
      ),
      drawer: ClientSidebar(
        selectedIndex: _selectedIndex,
        onItemSelected: _onItemSelected,
        screens: _screens,
      ),
      body: _screens[_selectedIndex].screen,
      bottomNavigationBar: NavigationBar(
        selectedIndex: _selectedIndex,
        onDestinationSelected: _onItemSelected,
        destinations: _screens.map((screen) {
          return NavigationDestination(
            icon: Icon(screen.icon),
            label: screen.title,
          );
        }).toList(),
      ),
    );
  }

  Widget _buildTabletLayout() {
    return Row(
      children: [
        ClientSidebar(
          selectedIndex: _selectedIndex,
          onItemSelected: _onItemSelected,
          screens: _screens,
          isExpanded: _isSidebarExpanded,
          onToggleExpanded: _toggleSidebar,
        ),
        Expanded(
          child: Column(
            children: [
              AppBar(
                title: Text(_screens[_selectedIndex].title),
                automaticallyImplyLeading: false,
              ),
              Expanded(child: _screens[_selectedIndex].screen),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildDesktopLayout() {
    return Row(
      children: [
        ClientSidebar(
          selectedIndex: _selectedIndex,
          onItemSelected: _onItemSelected,
          screens: _screens,
          isExpanded: _isSidebarExpanded,
          onToggleExpanded: _toggleSidebar,
        ),
        Expanded(
          child: Column(
            children: [
              AppBar(
                title: Text(_screens[_selectedIndex].title),
                automaticallyImplyLeading: false,
                actions: [
                  IconButton(
                    icon: const Icon(Icons.notifications),
                    onPressed: () {
                      // TODO: Implementar notificações
                    },
                  ),
                  const SizedBox(width: 8),
                  const CircleAvatar(
                    child: Icon(Icons.person),
                  ),
                  const SizedBox(width: 16),
                ],
              ),
              Expanded(child: _screens[_selectedIndex].screen),
            ],
          ),
        ),
      ],
    );
  }

  void _onItemSelected(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  void _toggleSidebar() {
    setState(() {
      _isSidebarExpanded = !_isSidebarExpanded;
    });
  }
}
