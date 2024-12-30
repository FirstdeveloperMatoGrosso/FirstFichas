import 'package:flutter/material.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Meu Perfil'),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              // TODO: Navegar para configurações
            },
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            const CircleAvatar(
              radius: 50,
              backgroundImage: AssetImage('assets/images/profile.jpg'),
            ),
            const SizedBox(height: 16),
            const Text(
              'João Silva',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const Text(
              'joao.silva@email.com',
              style: TextStyle(
                color: Colors.grey,
              ),
            ),
            const SizedBox(height: 32),
            _buildSection(
              context,
              title: 'Meus Ingressos',
              icon: Icons.confirmation_number,
              onTap: () {
                // TODO: Navegar para ingressos
              },
            ),
            _buildSection(
              context,
              title: 'Histórico de Compras',
              icon: Icons.history,
              onTap: () {
                // TODO: Navegar para histórico
              },
            ),
            _buildSection(
              context,
              title: 'Eventos Favoritos',
              icon: Icons.favorite,
              onTap: () {
                // TODO: Navegar para favoritos
              },
            ),
            _buildSection(
              context,
              title: 'Formas de Pagamento',
              icon: Icons.payment,
              onTap: () {
                // TODO: Navegar para pagamentos
              },
            ),
            _buildSection(
              context,
              title: 'Endereços',
              icon: Icons.location_on,
              onTap: () {
                // TODO: Navegar para endereços
              },
            ),
            _buildSection(
              context,
              title: 'Notificações',
              icon: Icons.notifications,
              onTap: () {
                // TODO: Navegar para notificações
              },
            ),
            _buildSection(
              context,
              title: 'Ajuda',
              icon: Icons.help,
              onTap: () {
                // TODO: Navegar para ajuda
              },
            ),
            const SizedBox(height: 32),
            SizedBox(
              width: double.infinity,
              child: OutlinedButton.icon(
                onPressed: () {
                  // TODO: Implementar logout
                },
                icon: const Icon(Icons.exit_to_app),
                label: const Text('Sair'),
                style: OutlinedButton.styleFrom(
                  foregroundColor: Colors.red,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSection(
    BuildContext context, {
    required String title,
    required IconData icon,
    required VoidCallback onTap,
  }) {
    return ListTile(
      leading: Icon(
        icon,
        color: Theme.of(context).colorScheme.primary,
      ),
      title: Text(title),
      trailing: const Icon(Icons.chevron_right),
      onTap: onTap,
    );
  }
}
