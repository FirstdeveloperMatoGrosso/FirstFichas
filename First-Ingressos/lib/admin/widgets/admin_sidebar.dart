import 'package:flutter/material.dart';

class AdminSidebar extends StatelessWidget {
  final int selectedIndex;
  final Function(int) onItemSelected;

  const AdminSidebar({
    Key? key,
    required this.selectedIndex,
    required this.onItemSelected,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 250,
      color: Colors.white,
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(20),
            child: const Text(
              'First Ingressos Admin',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
          _buildMenuItem(
            index: 0,
            title: 'Visão Geral',
            icon: Icons.dashboard,
          ),
          _buildMenuItem(
            index: 1,
            title: 'Clientes (Tenants)',
            icon: Icons.business,
          ),
          _buildMenuItem(
            index: 2,
            title: 'Eventos',
            icon: Icons.event,
          ),
          _buildMenuItem(
            index: 3,
            title: 'Usuários',
            icon: Icons.people,
          ),
          _buildMenuItem(
            index: 4,
            title: 'Terminais',
            icon: Icons.point_of_sale,
          ),
        ],
      ),
    );
  }

  Widget _buildMenuItem({
    required int index,
    required String title,
    required IconData icon,
  }) {
    final isSelected = selectedIndex == index;
    return ListTile(
      leading: Icon(
        icon,
        color: isSelected ? Colors.blue : Colors.grey,
      ),
      title: Text(
        title,
        style: TextStyle(
          color: isSelected ? Colors.blue : Colors.black,
          fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
        ),
      ),
      selected: isSelected,
      onTap: () => onItemSelected(index),
    );
  }
}
