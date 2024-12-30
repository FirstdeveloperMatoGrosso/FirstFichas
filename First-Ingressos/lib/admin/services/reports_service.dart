import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class SalesReport {
  final DateTime date;
  final String eventName;
  final String ticketType;
  final int quantity;
  final double unitPrice;
  final double totalAmount;
  final String paymentMethod;
  final String status;

  SalesReport({
    required this.date,
    required this.eventName,
    required this.ticketType,
    required this.quantity,
    required this.unitPrice,
    required this.totalAmount,
    required this.paymentMethod,
    required this.status,
  });

  double get revenue => totalAmount;
}

class ReportsSummary {
  final double totalRevenue;
  final int totalTickets;
  final Map<String, double> revenueByEvent;
  final Map<String, int> ticketsByEvent;
  final Map<String, double> revenueByPaymentMethod;
  final List<SalesReport> recentSales;

  ReportsSummary({
    required this.totalRevenue,
    required this.totalTickets,
    required this.revenueByEvent,
    required this.ticketsByEvent,
    required this.revenueByPaymentMethod,
    required this.recentSales,
  });

  static Future<ReportsSummary> generate({
    required DateTime startDate,
    required DateTime endDate,
  }) async {
    // TODO: Implementar integração com backend
    // Dados mockados para exemplo
    return ReportsSummary(
      totalRevenue: 15000.0,
      totalTickets: 150,
      revenueByEvent: {
        'Festa de Ano Novo': 8000.0,
        'Show de Verão': 5000.0,
        'Festival de Música': 2000.0,
      },
      ticketsByEvent: {
        'Festa de Ano Novo': 80,
        'Show de Verão': 50,
        'Festival de Música': 20,
      },
      revenueByPaymentMethod: {
        'PIX': 10000.0,
        'Cartão de Crédito': 4000.0,
        'Boleto': 1000.0,
      },
      recentSales: List.generate(
        10,
        (index) => SalesReport(
          date: DateTime.now().subtract(Duration(days: index)),
          eventName: 'Evento ${index + 1}',
          ticketType: index % 2 == 0 ? 'VIP' : 'Pista',
          quantity: (index % 3 + 1),
          unitPrice: 100.0,
          totalAmount: 100.0 * (index % 3 + 1),
          paymentMethod: index % 2 == 0 ? 'PIX' : 'Cartão de Crédito',
          status: 'Confirmado',
        ),
      ),
    );
  }
}

class ReportsService {
  static Future<List<DataPoint>> getRevenueChart(
    DateTime startDate,
    DateTime endDate,
  ) async {
    // TODO: Implementar integração com backend
    // Dados mockados para exemplo
    return List.generate(
      7,
      (index) => DataPoint(
        DateTime.now().subtract(Duration(days: 6 - index)),
        (index + 1) * 1000.0,
      ),
    );
  }

  static Future<List<DataPoint>> getTicketsChart(
    DateTime startDate,
    DateTime endDate,
  ) async {
    // TODO: Implementar integração com backend
    // Dados mockados para exemplo
    return List.generate(
      7,
      (index) => DataPoint(
        DateTime.now().subtract(Duration(days: 6 - index)),
        (index + 1) * 10.0,
      ),
    );
  }
}

class DataPoint {
  final DateTime date;
  final double value;

  DataPoint(this.date, this.value);
}
