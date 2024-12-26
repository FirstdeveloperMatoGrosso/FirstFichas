import 'package:flutter/material.dart';
import '../widgets/responsive_layout.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({Key? key}) : super(key: key);

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  final List<String> _settingsSections = [
    'Empresa',
    'Pagamentos',
    'Notificações',
    'Integrações',
    'Segurança',
    'Personalização',
  ];

  int _selectedSection = 0;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (!ResponsiveLayout.isMobile(context)) _buildSettingsSidebar(),
          if (!ResponsiveLayout.isMobile(context)) const SizedBox(width: 24),
          Expanded(
            child: _buildSettingsContent(),
          ),
        ],
      ),
    );
  }

  Widget _buildSettingsSidebar() {
    return Card(
      child: SizedBox(
        width: 250,
        child: ListView.builder(
          shrinkWrap: true,
          itemCount: _settingsSections.length,
          itemBuilder: (context, index) {
            return ListTile(
              leading: Icon(_getSectionIcon(_settingsSections[index])),
              title: Text(_settingsSections[index]),
              selected: _selectedSection == index,
              onTap: () {
                setState(() {
                  _selectedSection = index;
                });
              },
            );
          },
        ),
      ),
    );
  }

  Widget _buildSettingsContent() {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              _settingsSections[_selectedSection],
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 24),
            _buildSettingsForm(),
          ],
        ),
      ),
    );
  }

  Widget _buildSettingsForm() {
    switch (_settingsSections[_selectedSection]) {
      case 'Empresa':
        return _buildCompanySettings();
      case 'Pagamentos':
        return _buildPaymentSettings();
      case 'Notificações':
        return _buildNotificationSettings();
      case 'Integrações':
        return _buildIntegrationSettings();
      case 'Segurança':
        return _buildSecuritySettings();
      case 'Personalização':
        return _buildCustomizationSettings();
      default:
        return const SizedBox();
    }
  }

  Widget _buildCompanySettings() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        TextField(
          decoration: const InputDecoration(
            labelText: 'Nome da Empresa',
            hintText: 'Digite o nome da sua empresa',
          ),
        ),
        const SizedBox(height: 16),
        TextField(
          decoration: const InputDecoration(
            labelText: 'CNPJ',
            hintText: 'Digite o CNPJ da empresa',
          ),
        ),
        const SizedBox(height: 16),
        TextField(
          decoration: const InputDecoration(
            labelText: 'Endereço',
            hintText: 'Digite o endereço da empresa',
          ),
        ),
        const SizedBox(height: 16),
        TextField(
          decoration: const InputDecoration(
            labelText: 'Email de Contato',
            hintText: 'Digite o email de contato',
          ),
        ),
        const SizedBox(height: 16),
        TextField(
          decoration: const InputDecoration(
            labelText: 'Telefone',
            hintText: 'Digite o telefone de contato',
          ),
        ),
        const SizedBox(height: 24),
        Row(
          children: [
            ElevatedButton(
              onPressed: () {
                // TODO: Implementar salvamento
              },
              child: const Text('Salvar Alterações'),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildPaymentSettings() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Métodos de Pagamento Aceitos',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        CheckboxListTile(
          title: const Text('Cartão de Crédito'),
          value: true,
          onChanged: (value) {},
        ),
        CheckboxListTile(
          title: const Text('Cartão de Débito'),
          value: true,
          onChanged: (value) {},
        ),
        CheckboxListTile(
          title: const Text('PIX'),
          value: true,
          onChanged: (value) {},
        ),
        CheckboxListTile(
          title: const Text('Boleto'),
          value: false,
          onChanged: (value) {},
        ),
        const SizedBox(height: 24),
        const Text(
          'Configurações de Pagamento',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        TextField(
          decoration: const InputDecoration(
            labelText: 'Chave PIX',
            hintText: 'Digite sua chave PIX',
          ),
        ),
        const SizedBox(height: 16),
        TextField(
          decoration: const InputDecoration(
            labelText: 'Token Stone',
            hintText: 'Digite seu token Stone',
          ),
        ),
        const SizedBox(height: 24),
        Row(
          children: [
            ElevatedButton(
              onPressed: () {
                // TODO: Implementar salvamento
              },
              child: const Text('Salvar Alterações'),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildNotificationSettings() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Notificações por Email',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        SwitchListTile(
          title: const Text('Novas Vendas'),
          subtitle: const Text('Receber notificações de novas vendas'),
          value: true,
          onChanged: (value) {},
        ),
        SwitchListTile(
          title: const Text('Relatórios Diários'),
          subtitle: const Text('Receber relatórios diários de vendas'),
          value: false,
          onChanged: (value) {},
        ),
        SwitchListTile(
          title: const Text('Alertas de Estoque'),
          subtitle: const Text('Receber alertas de estoque baixo'),
          value: true,
          onChanged: (value) {},
        ),
      ],
    );
  }

  Widget _buildIntegrationSettings() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        ListTile(
          leading: const Icon(Icons.payment),
          title: const Text('Stone'),
          subtitle: const Text('Integração com gateway de pagamento'),
          trailing: Switch(
            value: true,
            onChanged: (value) {},
          ),
        ),
        ListTile(
          leading: const Icon(Icons.receipt),
          title: const Text('Emissor de NFe'),
          subtitle: const Text('Integração com sistema de notas fiscais'),
          trailing: Switch(
            value: false,
            onChanged: (value) {},
          ),
        ),
        ListTile(
          leading: const Icon(Icons.analytics),
          title: const Text('Google Analytics'),
          subtitle: const Text('Integração com análise de dados'),
          trailing: Switch(
            value: true,
            onChanged: (value) {},
          ),
        ),
      ],
    );
  }

  Widget _buildSecuritySettings() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Autenticação de Dois Fatores',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        SwitchListTile(
          title: const Text('Ativar 2FA'),
          subtitle: const Text('Adiciona uma camada extra de segurança'),
          value: false,
          onChanged: (value) {},
        ),
        const SizedBox(height: 24),
        const Text(
          'Senhas e Acessos',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        ElevatedButton(
          onPressed: () {
            // TODO: Implementar alteração de senha
          },
          child: const Text('Alterar Senha'),
        ),
      ],
    );
  }

  Widget _buildCustomizationSettings() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Aparência',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        ListTile(
          title: const Text('Tema Escuro'),
          trailing: Switch(
            value: false,
            onChanged: (value) {},
          ),
        ),
        const SizedBox(height: 24),
        const Text(
          'Personalização da Marca',
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 16),
        ElevatedButton.icon(
          onPressed: () {
            // TODO: Implementar upload de logo
          },
          icon: const Icon(Icons.upload),
          label: const Text('Upload Logo'),
        ),
      ],
    );
  }

  IconData _getSectionIcon(String section) {
    switch (section) {
      case 'Empresa':
        return Icons.business;
      case 'Pagamentos':
        return Icons.payment;
      case 'Notificações':
        return Icons.notifications;
      case 'Integrações':
        return Icons.integration_instructions;
      case 'Segurança':
        return Icons.security;
      case 'Personalização':
        return Icons.palette;
      default:
        return Icons.settings;
    }
  }
}
