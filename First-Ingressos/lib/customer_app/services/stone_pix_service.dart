import 'dart:convert';
import 'package:http/http.dart' as http;

class StonePixService {
  static const String _baseUrl = 'https://api.stone.com.br/v2';
  final String _clientId;
  final String _clientSecret;
  String? _accessToken;

  StonePixService({
    required String clientId,
    required String clientSecret,
  })  : _clientId = clientId,
        _clientSecret = clientSecret;

  Future<void> _authenticate() async {
    final response = await http.post(
      Uri.parse('$_baseUrl/oauth/token'),
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'client_id': _clientId,
        'client_secret': _clientSecret,
        'grant_type': 'client_credentials',
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      _accessToken = data['access_token'];
    } else {
      throw Exception('Falha na autenticação com a Stone');
    }
  }

  Future<Map<String, dynamic>> createPixCharge({
    required String description,
    required double amount,
    required String customerName,
    required String customerTaxId,
    String? customerEmail,
  }) async {
    if (_accessToken == null) {
      await _authenticate();
    }

    final response = await http.post(
      Uri.parse('$_baseUrl/charge'),
      headers: {
        'Authorization': 'Bearer $_accessToken',
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'amount': (amount * 100).toInt(), // Convertendo para centavos
        'description': description,
        'payment_type': 'pix',
        'customer': {
          'name': customerName,
          'tax_id': customerTaxId,
          if (customerEmail != null) 'email': customerEmail,
        },
      }),
    );

    if (response.statusCode == 201) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Falha ao criar cobrança Pix');
    }
  }

  Future<Map<String, dynamic>> getPixStatus(String chargeId) async {
    if (_accessToken == null) {
      await _authenticate();
    }

    final response = await http.get(
      Uri.parse('$_baseUrl/charge/$chargeId'),
      headers: {
        'Authorization': 'Bearer $_accessToken',
      },
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Falha ao verificar status do Pix');
    }
  }
}
