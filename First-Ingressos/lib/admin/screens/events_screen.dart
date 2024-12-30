import 'package:flutter/material.dart';

class EventsScreen extends StatelessWidget {
  const EventsScreen({Key? key}) : super(key: key);

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
                'Eventos',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Row(
                children: [
                  OutlinedButton.icon(
                    onPressed: () {
                      // TODO: Implementar filtros
                    },
                    icon: const Icon(Icons.filter_list),
                    label: const Text('Filtros'),
                  ),
                  const SizedBox(width: 10),
                  ElevatedButton.icon(
                    onPressed: () {
                      // TODO: Implementar adição de novo evento
                    },
                    icon: const Icon(Icons.add),
                    label: const Text('Novo Evento'),
                  ),
                ],
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
                      DataColumn(label: Text('Nome do Evento')),
                      DataColumn(label: Text('Cliente')),
                      DataColumn(label: Text('Data')),
                      DataColumn(label: Text('Ingressos Vendidos')),
                      DataColumn(label: Text('Status')),
                      DataColumn(label: Text('Ações')),
                    ],
                    rows: [
                      _buildEventRow(
                        name: 'Festival de Verão 2024',
                        client: 'Show Productions',
                        date: '15/01/2024',
                        ticketsSold: '1250/2000',
                        status: 'Em andamento',
                      ),
                      _buildEventRow(
                        name: 'Conferência Tech',
                        client: 'Festival Manager',
                        date: '20/02/2024',
                        ticketsSold: '450/1000',
                        status: 'Aguardando',
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

  DataRow _buildEventRow({
    required String name,
    required String client,
    required String date,
    required String ticketsSold,
    required String status,
  }) {
    return DataRow(
      cells: [
        DataCell(Text(name)),
        DataCell(Text(client)),
        DataCell(Text(date)),
        DataCell(Text(ticketsSold)),
        DataCell(
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
            decoration: BoxDecoration(
              color: status == 'Em andamento' ? Colors.green[100] : Colors.orange[100],
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
                icon: const Icon(Icons.visibility, size: 20),
                onPressed: () {
                  // TODO: Implementar visualização detalhada
                },
              ),
            ],
          ),
        ),
      ],
    );
  }
}
