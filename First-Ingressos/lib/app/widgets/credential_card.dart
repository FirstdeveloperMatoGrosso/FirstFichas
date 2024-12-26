import 'package:flutter/material.dart';

enum CredentialType { card, ticket, wristband }

class CredentialCard extends StatelessWidget {
  final CredentialType type;
  final double? balance;
  final String? lastUsed;
  final String? eventName;
  final String? eventDate;

  const CredentialCard({
    Key? key,
    required this.type,
    this.balance,
    this.lastUsed,
    this.eventName,
    this.eventDate,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(16),
      ),
      child: Container(
        width: type == CredentialType.ticket ? double.infinity : 300,
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          gradient: LinearGradient(
            colors: _getGradientColors(),
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          ),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Icon(
                  _getCredentialIcon(),
                  color: Colors.white,
                  size: 32,
                ),
                if (type != CredentialType.ticket)
                  PopupMenuButton(
                    icon: const Icon(
                      Icons.more_vert,
                      color: Colors.white,
                    ),
                    itemBuilder: (context) => [
                      const PopupMenuItem(
                        value: 'recharge',
                        child: Text('Recarregar'),
                      ),
                      const PopupMenuItem(
                        value: 'block',
                        child: Text('Bloquear'),
                      ),
                      const PopupMenuItem(
                        value: 'history',
                        child: Text('Histórico'),
                      ),
                    ],
                    onSelected: (value) {
                      // TODO: Implementar ações
                    },
                  ),
              ],
            ),
            const Spacer(),
            if (type != CredentialType.ticket) ...[
              Text(
                'Saldo',
                style: TextStyle(
                  color: Colors.white.withOpacity(0.8),
                  fontSize: 16,
                ),
              ),
              const SizedBox(height: 4),
              Text(
                'R\$ ${balance?.toStringAsFixed(2) ?? '0.00'}',
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 8),
              Text(
                'Último uso: ${lastUsed ?? 'Nunca utilizado'}',
                style: TextStyle(
                  color: Colors.white.withOpacity(0.8),
                  fontSize: 12,
                ),
              ),
            ] else ...[
              Text(
                eventName ?? 'Evento',
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 8),
              Text(
                eventDate ?? 'Data não definida',
                style: TextStyle(
                  color: Colors.white.withOpacity(0.8),
                  fontSize: 16,
                ),
              ),
              const SizedBox(height: 16),
              ElevatedButton.icon(
                onPressed: () {
                  // TODO: Mostrar QR Code
                },
                icon: const Icon(Icons.qr_code),
                label: const Text('Mostrar QR Code'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.white,
                  foregroundColor: Colors.blue,
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  List<Color> _getGradientColors() {
    switch (type) {
      case CredentialType.card:
        return [Colors.blue, Colors.blue.shade800];
      case CredentialType.ticket:
        return [Colors.purple, Colors.purple.shade800];
      case CredentialType.wristband:
        return [Colors.orange, Colors.orange.shade800];
    }
  }

  IconData _getCredentialIcon() {
    switch (type) {
      case CredentialType.card:
        return Icons.credit_card;
      case CredentialType.ticket:
        return Icons.confirmation_number;
      case CredentialType.wristband:
        return Icons.watch;
    }
  }
}
