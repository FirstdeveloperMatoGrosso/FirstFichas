import 'package:flutter/material.dart';

class TenantsScreen extends StatelessWidget {
  const TenantsScreen({Key? key}) : super(key: key);

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
                'Clientes (Tenants)',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              ElevatedButton.icon(
                onPressed: () {
                  // TODO: Implementar adição de novo tenant
                },
                icon: const Icon(Icons.add),
                label: const Text('Novo Cliente'),
              ),
            ],
          ),
          const SizedBox(height: 20),
          Expanded(
            child: Card(
              child: ListView(
                children: [
                  DataTable(
                    columns: const [
                      DataColumn(label: Text('Nome da Empresa')),
                      DataColumn(label: Text('Plano')),
                      DataColumn(label: Text('Status')),
                      DataColumn(label: Text('Eventos Ativos')),
                      DataColumn(label: Text('Ações')),
                    ],
                    rows: [
                      _buildTenantRow(
                        name: 'Show Productions',
                        plan: 'Premium',
                        status: 'Ativo',
                        activeEvents: '12',
                      ),
                      _buildTenantRow(
                        name: 'Festival Manager',
                        plan: 'Basic',
                        status: 'Ativo',
                        activeEvents: '3',
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

  DataRow _buildTenantRow({
    required String name,
    required String plan,
    required String status,
    required String activeEvents,
  }) {
    return DataRow(
      cells: [
        DataCell(Text(name)),
        DataCell(Text(plan)),
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
        DataCell(Text(activeEvents)),
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
                icon: const Icon(Icons.delete, size: 20),
                onPressed: () {
                  // TODO: Implementar exclusão
                },
              ),
            ],
          ),
        ),
      ],
    );
  }
}
