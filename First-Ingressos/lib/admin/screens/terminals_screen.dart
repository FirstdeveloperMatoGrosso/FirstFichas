import 'package:flutter/material.dart';

class TerminalsScreen extends StatelessWidget {
  const TerminalsScreen({Key? key}) : super(key: key);

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
                'Terminais',
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Row(
                children: [
                  OutlinedButton.icon(
                    onPressed: () {
                      // TODO: Implementar sincronização com Stone
                    },
                    icon: const Icon(Icons.sync),
                    label: const Text('Sincronizar com Stone'),
                  ),
                  const SizedBox(width: 10),
                  ElevatedButton.icon(
                    onPressed: () {
                      // TODO: Implementar adição de novo terminal
                    },
                    icon: const Icon(Icons.add),
                    label: const Text('Novo Terminal'),
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
                      DataColumn(label: Text('Serial')),
                      DataColumn(label: Text('Modelo')),
                      DataColumn(label: Text('Cliente')),
                      DataColumn(label: Text('Status')),
                      DataColumn(label: Text('Última Sincronização')),
                      DataColumn(label: Text('Ações')),
                    ],
                    rows: [
                      _buildTerminalRow(
                        serial: 'S123456789',
                        model: 'D150',
                        client: 'Show Productions',
                        status: 'Ativo',
                        lastSync: '25/12/2023 14:30',
                      ),
                      _buildTerminalRow(
                        serial: 'G987654321',
                        model: 'MP35P',
                        client: 'Festival Manager',
                        status: 'Inativo',
                        lastSync: '24/12/2023 18:45',
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

  DataRow _buildTerminalRow({
    required String serial,
    required String model,
    required String client,
    required String status,
    required String lastSync,
  }) {
    return DataRow(
      cells: [
        DataCell(Text(serial)),
        DataCell(Text(TerminalDialog.supportedModels[model] ?? model)),
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
        DataCell(Text(lastSync)),
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
                icon: const Icon(Icons.sync, size: 20),
                onPressed: () {
                  // TODO: Implementar sincronização individual
                },
              ),
              IconButton(
                icon: const Icon(Icons.power_settings_new, size: 20),
                tooltip: 'Ativar/Desativar Terminal',
                onPressed: () {
                  // TODO: Implementar ativação/desativação
                },
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class TerminalDialog extends StatelessWidget {
  final bool isEditing;
  final Map<String, String>? terminalData;

  const TerminalDialog({
    Key? key,
    this.isEditing = false,
    this.terminalData,
  }) : super(key: key);

  // Lista de modelos suportados pela Stone
  static const Map<String, String> supportedModels = {
    'D150': 'Stone Mini (D150)',
    'D175': 'Stone Mini Chip (D175)',
    'S920': 'PAX D200',
    'D195': 'Stone Plus (D195)',
    'S918': 'PAX S918',
    'D195L': 'Stone Plus 2 (D195L)',
    'A930': 'PAX A930',
    'MP35P': 'Gertec MP35P',
    'GPOS700': 'Gertec GPOS700',
    'GPOS400': 'Gertec GPOS400',
    'APOS': 'Ingenico APOS',
    'T2MINI': 'Sunmi T2 Mini',
    'T2LITE': 'Sunmi T2 Lite',
  };

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(isEditing ? 'Editar Terminal' : 'Novo Terminal'),
      content: SizedBox(
        width: 500,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              decoration: const InputDecoration(
                labelText: 'Serial do Terminal',
                hintText: 'Digite o número de série do terminal',
              ),
              controller: TextEditingController(
                text: terminalData?['serial'] ?? '',
              ),
            ),
            const SizedBox(height: 16),
            DropdownButtonFormField<String>(
              decoration: const InputDecoration(
                labelText: 'Modelo',
                helperText: 'Selecione o modelo do terminal',
              ),
              value: terminalData?['model'] ?? 'D150',
              items: supportedModels.entries.map((entry) {
                return DropdownMenuItem(
                  value: entry.key,
                  child: Text(entry.value),
                );
              }).toList(),
              onChanged: (value) {
                // TODO: Implementar mudança de modelo
              },
            ),
            const SizedBox(height: 16),
            DropdownButtonFormField<String>(
              decoration: const InputDecoration(
                labelText: 'Cliente',
              ),
              value: terminalData?['client'] ?? 'Show Productions',
              items: const [
                DropdownMenuItem(
                  value: 'Show Productions',
                  child: Text('Show Productions'),
                ),
                DropdownMenuItem(
                  value: 'Festival Manager',
                  child: Text('Festival Manager'),
                ),
              ],
              onChanged: (value) {
                // TODO: Implementar mudança de cliente
              },
            ),
            const SizedBox(height: 16),
            // Campo adicional para informações do terminal
            TextField(
              decoration: const InputDecoration(
                labelText: 'Chave de Ativação',
                hintText: 'Digite a chave de ativação do terminal',
                helperText: 'Chave fornecida pela Stone',
              ),
              controller: TextEditingController(
                text: terminalData?['activationKey'] ?? '',
              ),
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancelar'),
        ),
        ElevatedButton(
          onPressed: () {
            // TODO: Implementar salvamento
            Navigator.of(context).pop();
          },
          child: Text(isEditing ? 'Salvar' : 'Adicionar'),
        ),
      ],
    );
  }
}
