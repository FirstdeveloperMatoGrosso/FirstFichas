import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../services/stone_pix_service.dart';
import 'package:qr_flutter/qr_flutter.dart';

class PixPaymentScreen extends StatefulWidget {
  final double amount;
  final String eventName;
  final int ticketQuantity;

  const PixPaymentScreen({
    Key? key,
    required this.amount,
    required this.eventName,
    required this.ticketQuantity,
  }) : super(key: key);

  @override
  State<PixPaymentScreen> createState() => _PixPaymentScreenState();
}

class _PixPaymentScreenState extends State<PixPaymentScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _cpfController = TextEditingController();
  final _emailController = TextEditingController();
  
  bool _isLoading = false;
  String? _pixCode;
  String? _qrCode;
  Timer? _statusCheckTimer;

  final _stonePixService = StonePixService(
    clientId: const String.fromEnvironment('STONE_CLIENT_ID'),
    clientSecret: const String.fromEnvironment('STONE_CLIENT_SECRET'),
  );

  @override
  void dispose() {
    _nameController.dispose();
    _cpfController.dispose();
    _emailController.dispose();
    _statusCheckTimer?.cancel();
    super.dispose();
  }

  Future<void> _createPixCharge() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() {
      _isLoading = true;
    });

    try {
      final response = await _stonePixService.createPixCharge(
        description: '${widget.ticketQuantity}x Ingresso(s) - ${widget.eventName}',
        amount: widget.amount,
        customerName: _nameController.text,
        customerTaxId: _cpfController.text.replaceAll(RegExp(r'[^\d]'), ''),
        customerEmail: _emailController.text,
      );

      setState(() {
        _pixCode = response['pix_code'];
        _qrCode = response['qr_code'];
        _isLoading = false;
      });

      _startStatusCheck(response['charge_id']);
    } catch (e) {
      setState(() {
        _isLoading = false;
      });

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Erro ao gerar Pix: ${e.toString()}'),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  void _startStatusCheck(String chargeId) {
    _statusCheckTimer = Timer.periodic(const Duration(seconds: 5), (timer) async {
      try {
        final status = await _stonePixService.getPixStatus(chargeId);
        if (status['status'] == 'paid') {
          timer.cancel();
          if (mounted) {
            _showSuccessDialog();
          }
        }
      } catch (e) {
        print('Erro ao verificar status: ${e.toString()}');
      }
    });
  }

  void _showSuccessDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        title: const Text('Pagamento Confirmado!'),
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
              'Seu pagamento foi confirmado com sucesso!',
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 8),
            Text(
              'Você comprou ${widget.ticketQuantity} ingresso(s) para ${widget.eventName}',
              textAlign: TextAlign.center,
              style: const TextStyle(color: Colors.grey),
            ),
          ],
        ),
        actions: [
          ElevatedButton(
            onPressed: () {
              Navigator.of(context).popUntil((route) => route.isFirst);
              // TODO: Navegar para tela de ingressos
            },
            child: const Text('Ver Meus Ingressos'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Pagamento via Pix'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  children: [
                    Text(
                      'Total a Pagar:',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'R\$ ${widget.amount.toStringAsFixed(2)}',
                      style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                            color: Theme.of(context).colorScheme.primary,
                            fontWeight: FontWeight.bold,
                          ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      '${widget.ticketQuantity}x Ingresso(s) - ${widget.eventName}',
                      style: Theme.of(context).textTheme.bodyMedium,
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),
            if (_pixCode == null) ...[
              Form(
                key: _formKey,
                child: Column(
                  children: [
                    TextFormField(
                      controller: _nameController,
                      decoration: const InputDecoration(
                        labelText: 'Nome Completo',
                        prefixIcon: Icon(Icons.person),
                      ),
                      validator: (value) {
                        if (value?.isEmpty ?? true) {
                          return 'Por favor, insira seu nome';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 16),
                    TextFormField(
                      controller: _cpfController,
                      decoration: const InputDecoration(
                        labelText: 'CPF',
                        prefixIcon: Icon(Icons.badge),
                      ),
                      keyboardType: TextInputType.number,
                      inputFormatters: [
                        FilteringTextInputFormatter.digitsOnly,
                        // TODO: Adicionar máscara de CPF
                      ],
                      validator: (value) {
                        if (value?.isEmpty ?? true) {
                          return 'Por favor, insira seu CPF';
                        }
                        // TODO: Validar CPF
                        return null;
                      },
                    ),
                    const SizedBox(height: 16),
                    TextFormField(
                      controller: _emailController,
                      decoration: const InputDecoration(
                        labelText: 'E-mail',
                        prefixIcon: Icon(Icons.email),
                      ),
                      keyboardType: TextInputType.emailAddress,
                      validator: (value) {
                        if (value?.isEmpty ?? true) {
                          return 'Por favor, insira seu e-mail';
                        }
                        if (!value!.contains('@')) {
                          return 'Por favor, insira um e-mail válido';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 24),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: _isLoading ? null : _createPixCharge,
                        child: _isLoading
                            ? const SizedBox(
                                height: 20,
                                width: 20,
                                child: CircularProgressIndicator(),
                              )
                            : const Text('Gerar QR Code Pix'),
                      ),
                    ),
                  ],
                ),
              ),
            ] else ...[
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      QrImageView(
                        data: _qrCode!,
                        version: QrVersions.auto,
                        size: 200.0,
                      ),
                      const SizedBox(height: 16),
                      const Text(
                        'Escaneie o QR Code acima com seu aplicativo bancário',
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 24),
                      const Text(
                        'Ou copie o código Pix abaixo:',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 8),
                      InkWell(
                        onTap: () {
                          Clipboard.setData(ClipboardData(text: _pixCode!));
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                              content: Text('Código Pix copiado!'),
                            ),
                          );
                        },
                        child: Container(
                          padding: const EdgeInsets.all(12),
                          decoration: BoxDecoration(
                            color: Theme.of(context).colorScheme.surface,
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(
                              color: Theme.of(context).colorScheme.outline,
                            ),
                          ),
                          child: Row(
                            children: [
                              Expanded(
                                child: Text(
                                  _pixCode!,
                                  style: const TextStyle(
                                    fontFamily: 'monospace',
                                  ),
                                ),
                              ),
                              const Icon(Icons.copy),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 16),
              const Card(
                child: Padding(
                  padding: EdgeInsets.all(16),
                  child: Column(
                    children: [
                      CircularProgressIndicator(),
                      SizedBox(height: 16),
                      Text(
                        'Aguardando confirmação do pagamento...',
                        textAlign: TextAlign.center,
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      SizedBox(height: 8),
                      Text(
                        'Assim que o pagamento for confirmado, seus ingressos serão gerados automaticamente.',
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
