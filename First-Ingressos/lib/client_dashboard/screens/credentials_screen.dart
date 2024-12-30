import 'package:flutter/material.dart';
import '../widgets/responsive_layout.dart';

class CredentialsScreen extends StatefulWidget {
  const CredentialsScreen({Key? key}) : super(key: key);

  @override
  State<CredentialsScreen> createState() => _CredentialsScreenState();
}

class _CredentialsScreenState extends State<CredentialsScreen> {
  final List<String> _credentialTypes = [
    'Ingressos',
    'Fichas',
    'Pulseiras',
    'Cartões NFC',
  ];

  String _selectedType = 'Ingressos';
  bool _showActiveOnly = false;

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
          _buildTypeSelector(),
          const SizedBox(height: 16),
          Expanded(
            child: _buildCredentialsList(),
          ),
        ],
      ),
    );
  }

  Widget _buildHeader() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        const Text(
          'Controle de Credenciais',
          style: TextStyle(
            fontSize: 24,
            fontWeight: FontWeight.bold,
          ),
        ),
        Row(
          children: [
            OutlinedButton.icon(
              onPressed: () {
                _showImportDialog();
              },
              icon: const Icon(Icons.upload_file),
              label: const Text('Importar'),
            ),
            const SizedBox(width: 8),
            ElevatedButton.icon(
              onPressed: () {
                _showAddCredentialDialog();
              },
              icon: const Icon(Icons.add),
              label: const Text('Nova Credencial'),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildFilters() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Row(
              children: [
                Expanded(
                  child: TextField(
                    decoration: InputDecoration(
                      hintText: 'Buscar por código, lote ou evento...',
                      prefixIcon: const Icon(Icons.search),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 16),
                SwitchListTile(
                  title: const Text('Mostrar apenas ativos'),
                  value: _showActiveOnly,
                  onChanged: (value) {
                    setState(() {
                      _showActiveOnly = value;
                    });
                  },
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTypeSelector() {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(
        children: _credentialTypes.map((type) {
          return Padding(
            padding: const EdgeInsets.only(right: 8),
            child: ChoiceChip(
              label: Text(type),
              selected: _selectedType == type,
              onSelected: (selected) {
                if (selected) {
                  setState(() {
                    _selectedType = type;
                  });
                }
              },
            ),
          );
        }).toList(),
      ),
    );
  }

  Widget _buildCredentialsList() {
    return Card(
      child: ListView(
        children: [
          PaginatedDataTable(
            columns: const [
              DataColumn(label: Text('Código')),
              DataColumn(label: Text('Tipo')),
              DataColumn(label: Text('Evento/Local')),
              DataColumn(label: Text('Valor')),
              DataColumn(label: Text('Status')),
              DataColumn(label: Text('Última Utilização')),
              DataColumn(label: Text('Ações')),
            ],
            source: _CredentialsDataSource(context),
          ),
        ],
      ),
    );
  }

  void _showAddCredentialDialog() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Nova Credencial'),
        content: SizedBox(
          width: 500,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              DropdownButtonFormField<String>(
                decoration: const InputDecoration(
                  labelText: 'Tipo de Credencial',
                ),
                value: _selectedType,
                items: _credentialTypes.map((type) {
                  return DropdownMenuItem(
                    value: type,
                    child: Text(type),
                  );
                }).toList(),
                onChanged: (value) {
                  setState(() {
                    _selectedType = value!;
                  });
                },
              ),
              const SizedBox(height: 16),
              TextField(
                decoration: const InputDecoration(
                  labelText: 'Quantidade',
                  hintText: 'Digite a quantidade de credenciais',
                ),
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: 16),
              TextField(
                decoration: const InputDecoration(
                  labelText: 'Valor',
                  hintText: 'Digite o valor da credencial',
                  prefixText: 'R\$ ',
                ),
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: 16),
              DropdownButtonFormField<String>(
                decoration: const InputDecoration(
                  labelText: 'Evento/Local',
                ),
                items: const [
                  DropdownMenuItem(
                    value: 'evento1',
                    child: Text('Festa de Ano Novo'),
                  ),
                  DropdownMenuItem(
                    value: 'evento2',
                    child: Text('Carnaval 2024'),
                  ),
                ],
                onChanged: (value) {},
              ),
              const SizedBox(height: 16),
              if (_selectedType == 'Cartões NFC' || _selectedType == 'Pulseiras')
                Column(
                  children: [
                    TextField(
                      decoration: const InputDecoration(
                        labelText: 'Prefixo do Lote',
                        hintText: 'Ex: CARD-2024-',
                      ),
                    ),
                    const SizedBox(height: 16),
                    SwitchListTile(
                      title: const Text('Recarregável'),
                      value: true,
                      onChanged: (value) {},
                    ),
                  ],
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
              // TODO: Implementar criação de credenciais
              Navigator.of(context).pop();
            },
            child: const Text('Criar'),
          ),
        ],
      ),
    );
  }

  void _showImportDialog() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Importar Credenciais'),
        content: SizedBox(
          width: 400,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text(
                'Selecione um arquivo CSV ou Excel com a lista de credenciais',
                style: TextStyle(color: Colors.grey),
              ),
              const SizedBox(height: 16),
              OutlinedButton.icon(
                onPressed: () {
                  // TODO: Implementar seleção de arquivo
                },
                icon: const Icon(Icons.upload_file),
                label: const Text('Selecionar Arquivo'),
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
              // TODO: Implementar importação
              Navigator.of(context).pop();
            },
            child: const Text('Importar'),
          ),
        ],
      ),
    );
  }
}

class _CredentialsDataSource extends DataTableSource {
  final BuildContext context;

  _CredentialsDataSource(this.context);

  @override
  DataRow? getRow(int index) {
    // Exemplo de dados
    return DataRow(
      cells: [
        DataCell(Text('CARD-2024-${index + 1}')),
        const DataCell(Text('Cartão NFC')),
        const DataCell(Text('Festa de Ano Novo')),
        const DataCell(Text('R\$ 100,00')),
        DataCell(
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
            decoration: BoxDecoration(
              color: index % 2 == 0 ? Colors.green[100] : Colors.grey[100],
              borderRadius: BorderRadius.circular(12),
            ),
            child: Text(index % 2 == 0 ? 'Ativo' : 'Inativo'),
          ),
        ),
        DataCell(Text(index % 2 == 0 ? '25/12/2023 15:30' : '--')),
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
                  // TODO: Implementar bloqueio
                },
              ),
              IconButton(
                icon: const Icon(Icons.history, size: 20),
                onPressed: () {
                  _showHistoryDialog(context);
                },
              ),
            ],
          ),
        ),
      ],
    );
  }

  @override
  bool get isRowCountApproximate => false;

  @override
  int get rowCount => 50; // Exemplo com 50 registros

  @override
  int get selectedRowCount => 0;

  void _showHistoryDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Histórico de Utilização'),
        content: SizedBox(
          width: 600,
          height: 400,
          child: ListView.builder(
            itemCount: 5,
            itemBuilder: (context, index) {
              return ListTile(
                leading: const Icon(Icons.access_time),
                title: Text('Utilização #${index + 1}'),
                subtitle: Text('25/12/2023 ${15 - index}:30'),
                trailing: Text('R\$ ${10 * (index + 1)},00'),
              );
            },
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Fechar'),
          ),
          ElevatedButton.icon(
            onPressed: () {
              // TODO: Implementar exportação do histórico
            },
            icon: const Icon(Icons.download),
            label: const Text('Exportar'),
          ),
        ],
      ),
    );
  }
}
