import 'package:flutter/material.dart';
import '../widgets/credential_card.dart';
import '../widgets/balance_card.dart';

class WalletScreen extends StatefulWidget {
  const WalletScreen({Key? key}) : super(key: key);

  @override
  State<WalletScreen> createState() => _WalletScreenState();
}

class _WalletScreenState extends State<WalletScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  final List<String> _tabs = ['Ativos', 'Histórico'];

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: _tabs.length, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Minha Carteira'),
        actions: [
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () {
              _showAddCredentialDialog();
            },
          ),
        ],
      ),
      body: Column(
        children: [
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: BalanceCard(),
          ),
          TabBar(
            controller: _tabController,
            tabs: _tabs.map((tab) => Tab(text: tab)).toList(),
          ),
          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: [
                _buildActiveCredentials(),
                _buildCredentialsHistory(),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildActiveCredentials() {
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        const Text(
          'Cartões e Pulseiras',
          style: TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        SizedBox(
          height: 200,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            itemCount: 3,
            itemBuilder: (context, index) => const Padding(
              padding: EdgeInsets.only(right: 16),
              child: CredentialCard(
                type: CredentialType.card,
                balance: 150.0,
                lastUsed: '25/12/2023',
              ),
            ),
          ),
        ),
        const SizedBox(height: 24),
        const Text(
          'Ingressos',
          style: TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        ListView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          itemCount: 3,
          itemBuilder: (context, index) => const Padding(
            padding: EdgeInsets.only(bottom: 16),
            child: CredentialCard(
              type: CredentialType.ticket,
              eventName: 'Festa de Ano Novo',
              eventDate: '31/12/2023',
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildCredentialsHistory() {
    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: 10,
      itemBuilder: (context, index) {
        return Card(
          child: ListTile(
            leading: const CircleAvatar(
              child: Icon(Icons.receipt),
            ),
            title: Text('Transação #${index + 1}'),
            subtitle: const Text('25/12/2023 15:30'),
            trailing: const Text('R\$ 50,00'),
            onTap: () {
              _showTransactionDetails();
            },
          ),
        );
      },
    );
  }

  void _showAddCredentialDialog() {
    showModalBottomSheet(
      context: context,
      builder: (context) => Container(
        padding: const EdgeInsets.all(16),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Text(
              'Adicionar Nova Credencial',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 24),
            ListTile(
              leading: const Icon(Icons.qr_code_scanner),
              title: const Text('Escanear QR Code'),
              onTap: () {
                // TODO: Implementar scanner
                Navigator.pop(context);
              },
            ),
            ListTile(
              leading: const Icon(Icons.nfc),
              title: const Text('Aproximar Cartão NFC'),
              onTap: () {
                // TODO: Implementar leitura NFC
                Navigator.pop(context);
              },
            ),
            ListTile(
              leading: const Icon(Icons.numbers),
              title: const Text('Digite o Código'),
              onTap: () {
                Navigator.pop(context);
                _showCodeInputDialog();
              },
            ),
          ],
        ),
      ),
    );
  }

  void _showCodeInputDialog() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Digite o Código'),
        content: TextField(
          decoration: const InputDecoration(
            hintText: 'Digite o código da credencial',
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () {
              // TODO: Implementar validação do código
              Navigator.pop(context);
            },
            child: const Text('Adicionar'),
          ),
        ],
      ),
    );
  }

  void _showTransactionDetails() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Detalhes da Transação'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: const [
            DetailRow(label: 'Data', value: '25/12/2023'),
            DetailRow(label: 'Hora', value: '15:30'),
            DetailRow(label: 'Tipo', value: 'Consumo'),
            DetailRow(label: 'Local', value: 'Bar Principal'),
            DetailRow(label: 'Valor', value: 'R\$ 50,00'),
            DetailRow(label: 'Saldo Anterior', value: 'R\$ 200,00'),
            DetailRow(label: 'Saldo Atual', value: 'R\$ 150,00'),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Fechar'),
          ),
          ElevatedButton.icon(
            onPressed: () {
              // TODO: Implementar compartilhamento
            },
            icon: const Icon(Icons.share),
            label: const Text('Compartilhar'),
          ),
        ],
      ),
    );
  }
}

class DetailRow extends StatelessWidget {
  final String label;
  final String value;

  const DetailRow({
    Key? key,
    required this.label,
    required this.value,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            label,
            style: const TextStyle(
              fontWeight: FontWeight.bold,
            ),
          ),
          Text(value),
        ],
      ),
    );
  }
}
