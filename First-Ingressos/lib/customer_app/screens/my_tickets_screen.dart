import 'package:flutter/material.dart';
import '../widgets/ticket_card.dart';

class MyTicketsScreen extends StatefulWidget {
  const MyTicketsScreen({Key? key}) : super(key: key);

  @override
  State<MyTicketsScreen> createState() => _MyTicketsScreenState();
}

class _MyTicketsScreenState extends State<MyTicketsScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  final List<String> _tabs = ['Ativos', 'Utilizados'];

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
        title: const Text('Meus Ingressos'),
        bottom: TabBar(
          controller: _tabController,
          tabs: _tabs.map((tab) => Tab(text: tab)).toList(),
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          _buildActiveTickets(),
          _buildUsedTickets(),
        ],
      ),
    );
  }

  Widget _buildActiveTickets() {
    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: 3,
      itemBuilder: (context, index) => Padding(
        padding: const EdgeInsets.only(bottom: 16),
        child: TicketCard(
          eventName: 'Festa de Ano Novo',
          date: '31/12/2023',
          time: '22:00',
          location: 'Centro de Eventos',
          ticketType: 'VIP',
          ticketNumber: 'TICKET-2023-${1000 + index}',
          onViewQR: () {
            _showQRCode(context);
          },
          onViewDetails: () {
            _showTicketDetails(context);
          },
          onShare: () {
            // TODO: Implementar compartilhamento
          },
          onDownload: () {
            // TODO: Implementar download do PDF
          },
        ),
      ),
    );
  }

  Widget _buildUsedTickets() {
    return ListView.builder(
      padding: const EdgeInsets.all(16),
      itemCount: 5,
      itemBuilder: (context, index) => Padding(
        padding: const EdgeInsets.only(bottom: 16),
        child: TicketCard(
          eventName: 'Evento ${index + 1}',
          date: '25/12/2023',
          time: '20:00',
          location: 'Local ${index + 1}',
          ticketType: 'Pista',
          ticketNumber: 'TICKET-2023-${900 + index}',
          isUsed: true,
          usedDate: '25/12/2023 20:30',
          onViewDetails: () {
            _showTicketDetails(context);
          },
          onDownload: () {
            // TODO: Implementar download do PDF
          },
        ),
      ),
    );
  }

  void _showQRCode(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Ingresso Digital'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 200,
              height: 200,
              decoration: BoxDecoration(
                color: Colors.white,
                border: Border.all(color: Colors.grey),
                borderRadius: BorderRadius.circular(8),
              ),
              child: const Center(
                child: Icon(
                  Icons.qr_code,
                  size: 150,
                ),
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              'TICKET-2023-1001',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              'Apresente este QR Code na entrada do evento',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Fechar'),
          ),
          ElevatedButton.icon(
            onPressed: () {
              // TODO: Implementar download
            },
            icon: const Icon(Icons.download),
            label: const Text('Baixar PDF'),
          ),
        ],
      ),
    );
  }

  void _showTicketDetails(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Detalhes do Ingresso'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: const [
            DetailRow(label: 'Evento', value: 'Festa de Ano Novo'),
            DetailRow(label: 'Data', value: '31/12/2023'),
            DetailRow(label: 'Horário', value: '22:00'),
            DetailRow(label: 'Local', value: 'Centro de Eventos'),
            DetailRow(label: 'Tipo', value: 'VIP'),
            DetailRow(label: 'Número', value: 'TICKET-2023-1001'),
            DetailRow(label: 'Status', value: 'Válido'),
            DetailRow(label: 'Comprador', value: 'João Silva'),
            DetailRow(label: 'CPF', value: '***.***.***-**'),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Fechar'),
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
