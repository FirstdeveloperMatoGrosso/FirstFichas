import 'package:flutter/material.dart';
import '../widgets/ticket_type_card.dart';
import '../widgets/map_preview.dart';
import '../widgets/pix_payment_screen.dart'; // Import the PixPaymentScreen widget

class EventDetailsScreen extends StatelessWidget {
  final String eventId;
  final String title; // Add the title property

  const EventDetailsScreen({
    Key? key,
    required this.eventId,
    required this.title, // Add the title parameter
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 200,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              background: Image.asset(
                'assets/images/event.jpg',
                fit: BoxFit.cover,
              ),
              title: Text(title), // Use the title property
            ),
          ),
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      const Icon(Icons.calendar_today),
                      const SizedBox(width: 8),
                      const Text('31 de Dezembro, 2023'),
                      const Spacer(),
                      ElevatedButton.icon(
                        onPressed: () {
                          // TODO: Adicionar ao calendário
                        },
                        icon: const Icon(Icons.add),
                        label: const Text('Adicionar'),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  Row(
                    children: [
                      const Icon(Icons.location_on),
                      const SizedBox(width: 8),
                      const Expanded(
                        child: Text('Centro de Eventos - Rua Principal, 123'),
                      ),
                      TextButton.icon(
                        onPressed: () {
                          // TODO: Abrir mapa
                        },
                        icon: const Icon(Icons.map),
                        label: const Text('Ver Mapa'),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  const MapPreview(
                    latitude: -23.550520,
                    longitude: -46.633308,
                  ),
                  const SizedBox(height: 24),
                  const Text(
                    'Sobre o Evento',
                    style: TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 8),
                  const Text(
                    'Venha celebrar a virada do ano conosco! Uma noite inesquecível com muita música, comida e diversão.',
                    style: TextStyle(fontSize: 16),
                  ),
                  const SizedBox(height: 24),
                  const Text(
                    'Ingressos Disponíveis',
                    style: TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 16),
                  ListView.builder(
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    itemCount: 3,
                    itemBuilder: (context, index) {
                      final types = ['VIP', 'Pista', 'Camarote'];
                      final prices = [200.0, 100.0, 150.0];
                      return Padding(
                        padding: const EdgeInsets.only(bottom: 16),
                        child: TicketTypeCard(
                          type: types[index],
                          price: prices[index],
                          available: true,
                          onSelect: (quantity) {
                            if (quantity > 0) {
                              _showPurchaseConfirmation(context, types[index], quantity, prices[index]);
                            }
                          },
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  void _showPurchaseConfirmation(BuildContext context, String type, int quantity, double price) {
    showModalBottomSheet(
      context: context,
      builder: (context) => Container(
        padding: const EdgeInsets.all(16),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Text(
              'Confirmar Compra',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 16),
            ListTile(
              title: Text('$quantity x Ingresso $type'),
              subtitle: Text('R\$ ${(price * quantity).toStringAsFixed(2)}'),
            ),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  child: OutlinedButton(
                    onPressed: () => Navigator.pop(context),
                    child: const Text('Cancelar'),
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.pop(context);
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => PixPaymentScreen(
                            amount: price * quantity,
                            eventName: title,
                            ticketQuantity: quantity,
                          ),
                        ),
                      );
                    },
                    child: const Text('Pagar com Pix'),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  void _showSuccessDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Compra Realizada!'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Icon(
              Icons.check_circle,
              color: Colors.green,
              size: 64,
            ),
            const SizedBox(height: 16),
            const Text(
              'Seus ingressos foram comprados com sucesso!',
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 8),
            const Text(
              'Você pode acessá-los na seção "Meus Ingressos"',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey),
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('OK'),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.pop(context);
              // TODO: Navegar para Meus Ingressos
            },
            child: const Text('Ver Meus Ingressos'),
          ),
        ],
      ),
    );
  }
}
