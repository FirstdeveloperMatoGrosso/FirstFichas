import 'package:flutter/material.dart';

class UsersScreen extends StatelessWidget {
  const UsersScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text(
                'Usuários',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Row(
                children: [
                  OutlinedButton.icon(
                    onPressed: () {
                      // TODO: Implementar busca avançada
                    },
                    icon: const Icon(Icons.search),
                    label: const Text('Busca Avançada'),
                  ),
                  const SizedBox(width: 10),
                  ElevatedButton.icon(
                    onPressed: () {
                      // TODO: Implementar adição de novo usuário
                    },
                    icon: const Icon(Icons.add),
                    label: const Text('Novo Usuário'),
                  ),
                ],
              ),
            ],
          ),
          const SizedBox(height: 20),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      decoration: InputDecoration(
                        hintText: 'Buscar usuários...',
                        prefixIcon: const Icon(Icons.search),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 20),
          Expanded(
            child: Card(
              child: ListView(
                children: [
                  DataTable(
                    columns: const [
                      DataColumn(label: Text('Nome')),
                      DataColumn(label: Text('Email')),
                      DataColumn(label: Text('Tipo')),
                      DataColumn(label: Text('Cliente')),
                      DataColumn(label: Text('Status')),
                      DataColumn(label: Text('Ações')),
                    ],
                    rows: [
                      _buildUserRow(
                        name: 'João Silva',
                        email: 'joao@showproductions.com',
                        type: 'Admin',
                        client: 'Show Productions',
                        status: 'Ativo',
                      ),
                      _buildUserRow(
                        name: 'Maria Santos',
                        email: 'maria@festivalmanager.com',
                        type: 'Operador',
                        client: 'Festival Manager',
                        status: 'Ativo',
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  DataRow _buildUserRow({
    required String name,
    required String email,
    required String type,
    required String client,
    required String status,
  }) {
    return DataRow(
      cells: [
        DataCell(Text(name)),
        DataCell(Text(email)),
        DataCell(Text(type)),
        DataCell(Text(client)),
        DataCell(
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
            decoration: BoxDecoration(
              color: status == 'Ativo' ? Colors.green[100] : Colors.grey[100],
              borderRadius: BorderRadius.circular(12),
            ),
            child: Text(status),
          ),
        ),
        DataCell(
          Row(
            children: [
              IconButton(
                icon: const Icon(Icons.edit, size: 20),
                onPressed: () {
                  // TODO: Implementar edição
                },
              ),
              IconButton(
                icon: const Icon(Icons.block, size: 20),
                onPressed: () {
                  // TODO: Implementar bloqueio/desbloqueio
                },
              ),
            ],
          ),
        ),
      ],
    );
  }
}
