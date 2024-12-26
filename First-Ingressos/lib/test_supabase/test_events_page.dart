import 'package:flutter/material.dart';
import '../backend/supabase/supabase.dart';
import '../backend/schema/events_record.dart';
import '../flutter_flow/flutter_flow_theme.dart';
import '../flutter_flow/flutter_flow_widgets.dart';

class TestEventsPage extends StatefulWidget {
  const TestEventsPage({Key? key}) : super(key: key);

  @override
  _TestEventsPageState createState() => _TestEventsPageState();
}

class _TestEventsPageState extends State<TestEventsPage> {
  final _formKey = GlobalKey<FormState>();
  bool _isLoading = false;
  String? _errorMessage;

  // Controladores para o formulário
  final _titleController = TextEditingController();
  final _descriptionController = TextEditingController();
  final _locationController = TextEditingController();
  final _priceController = TextEditingController();
  final _totalTicketsController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _initializeSupabase();
  }

  Future<void> _initializeSupabase() async {
    try {
      if (SupaFlow.currentUser == null) {
        await SupaFlow.client.auth.signInWithPassword(
          email: 'teste@firstingressos.com',
          password: 'teste123',
        );
      }
    } catch (e) {
      print('Erro ao inicializar Supabase: $e');
    }
  }

  @override
  void dispose() {
    _titleController.dispose();
    _descriptionController.dispose();
    _locationController.dispose();
    _priceController.dispose();
    _totalTicketsController.dispose();
    super.dispose();
  }

  // Função para buscar eventos
  Future<List<EventsRecord>> _fetchEvents() async {
    try {
      final response = await SupaFlow.client
          .from('events')
          .select()
          .order('created_at', ascending: false)
          .execute();

      if (response.error != null) {
        throw response.error!;
      }

      final data = response.data as List;
      return data.map((json) => EventsRecord.fromJson(json as Map<String, dynamic>)).toList();
    } catch (e) {
      print('Erro ao buscar eventos: $e');
      setState(() {
        _errorMessage = 'Erro ao buscar eventos: $e';
      });
      return [];
    }
  }

  // Função para criar um novo evento
  Future<void> _createEvent() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final eventData = {
        'title': _titleController.text,
        'description': _descriptionController.text,
        'location': _locationController.text,
        'price': double.parse(_priceController.text),
        'total_tickets': int.parse(_totalTicketsController.text),
        'available_tickets': int.parse(_totalTicketsController.text),
        'date': DateTime.now().add(const Duration(days: 30)).toIso8601String(),
        'created_at': DateTime.now().toIso8601String(),
        'updated_at': DateTime.now().toIso8601String(),
      };

      final response = await SupaFlow.client
          .from('events')
          .insert(eventData)
          .execute();

      if (response.error != null) {
        throw response.error!;
      }

      // Limpar o formulário
      _titleController.clear();
      _descriptionController.clear();
      _locationController.clear();
      _priceController.clear();
      _totalTicketsController.clear();

      setState(() {
        _isLoading = false;
      });

      // Atualizar a lista de eventos
      setState(() {});
    } catch (e) {
      print('Erro ao criar evento: $e');
      setState(() {
        _isLoading = false;
        _errorMessage = 'Erro ao criar evento: $e';
      });
    }
  }

  // Função para criar dados de teste
  Future<void> _createTestData() async {
    try {
      // Criar usuário de teste
      final userResponse = await SupaFlow.signUp(
        email: 'test@example.com',
        password: 'password123',
      );

      if (!userResponse) {
        print('Erro ao criar usuário de teste');
        return;
      }

      // Criar categoria de teste
      final categoryResponse = await SupaFlow.client.from('categories').insert({
        'name': 'Shows',
        'description': 'Eventos musicais e shows',
      }).select().single();

      // Criar local de teste
      final venueResponse = await SupaFlow.client.from('venues').insert({
        'name': 'Arena Test',
        'address': 'Rua Teste, 123',
        'city': 'São Paulo',
        'state': 'SP',
        'country': 'Brasil',
        'zip_code': '01234-567',
        'capacity': 1000,
      }).select().single();

      // Criar organizador de teste
      await SupaFlow.client.from('organizers').insert({
        'user_id': SupaFlow.currentUser?.id,
        'name': 'Test Organizer',
        'email': 'organizer@example.com',
      });

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Dados de teste criados com sucesso!')),
      );
    } catch (e) {
      print('Erro ao criar dados de teste: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erro ao criar dados de teste: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Teste Supabase - Eventos'),
        backgroundColor: FlutterFlowTheme.of(context).primary,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Form(
              key: _formKey,
              child: Column(
                children: [
                  TextFormField(
                    controller: _titleController,
                    decoration: const InputDecoration(labelText: 'Título'),
                    validator: (value) =>
                        value?.isEmpty ?? true ? 'Campo obrigatório' : null,
                  ),
                  TextFormField(
                    controller: _descriptionController,
                    decoration: const InputDecoration(labelText: 'Descrição'),
                    validator: (value) =>
                        value?.isEmpty ?? true ? 'Campo obrigatório' : null,
                  ),
                  TextFormField(
                    controller: _locationController,
                    decoration: const InputDecoration(labelText: 'Localização'),
                    validator: (value) =>
                        value?.isEmpty ?? true ? 'Campo obrigatório' : null,
                  ),
                  TextFormField(
                    controller: _priceController,
                    decoration: const InputDecoration(labelText: 'Preço'),
                    keyboardType: TextInputType.number,
                    validator: (value) {
                      if (value?.isEmpty ?? true) return 'Campo obrigatório';
                      if (double.tryParse(value!) == null)
                        return 'Digite um número válido';
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _totalTicketsController,
                    decoration: const InputDecoration(labelText: 'Total de Ingressos'),
                    keyboardType: TextInputType.number,
                    validator: (value) {
                      if (value?.isEmpty ?? true) return 'Campo obrigatório';
                      if (int.tryParse(value!) == null)
                        return 'Digite um número válido';
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  if (_errorMessage != null)
                    Padding(
                      padding: const EdgeInsets.only(bottom: 16),
                      child: Text(
                        _errorMessage!,
                        style: TextStyle(color: Colors.red),
                      ),
                    ),
                  ElevatedButton(
                    onPressed: _isLoading ? null : _createEvent,
                    child: _isLoading
                        ? const CircularProgressIndicator()
                        : const Text('Criar Evento'),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: FlutterFlowTheme.of(context).primary,
                      padding: const EdgeInsets.symmetric(vertical: 16),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 32),
            const Text(
              'Eventos Recentes',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            Expanded(
              child: FutureBuilder<List<EventsRecord>>(
                future: _fetchEvents(),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const Center(child: CircularProgressIndicator());
                  }

                  if (snapshot.hasError) {
                    return Center(
                      child: Text(
                        'Erro ao carregar eventos: ${snapshot.error}',
                        style: const TextStyle(color: Colors.red),
                      ),
                    );
                  }

                  final events = snapshot.data ?? [];

                  if (events.isEmpty) {
                    return const Center(
                      child: Text('Nenhum evento encontrado'),
                    );
                  }

                  return ListView.builder(
                    itemCount: events.length,
                    itemBuilder: (context, index) {
                      final event = events[index];
                      return Card(
                        margin: const EdgeInsets.only(bottom: 8),
                        child: ListTile(
                          title: Text(event.title ?? 'Sem título'),
                          subtitle: Text(event.description ?? 'Sem descrição'),
                          trailing: Text(
                            'R\$ ${event.price?.toStringAsFixed(2) ?? '0.00'}',
                            style: const TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 16,
                            ),
                          ),
                        ),
                      );
                    },
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
