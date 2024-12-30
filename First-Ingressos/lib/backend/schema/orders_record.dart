import 'dart:convert';

class OrdersRecord {
  final String id;
  final String userId;
  final String eventId;
  final int quantity;
  final double totalAmount;
  final String status; // pending, completed, cancelled
  final DateTime createdAt;
  final DateTime? completedAt;
  final DateTime? cancelledAt;
  final String? paymentId;
  final Map<String, dynamic>? extraData;

  OrdersRecord({
    required this.id,
    required this.userId,
    required this.eventId,
    required this.quantity,
    required this.totalAmount,
    required this.status,
    required this.createdAt,
    this.completedAt,
    this.cancelledAt,
    this.paymentId,
    this.extraData,
  });

  factory OrdersRecord.fromJson(Map<String, dynamic> json) => OrdersRecord(
        id: json['id'] as String,
        userId: json['user_id'] as String,
        eventId: json['event_id'] as String,
        quantity: json['quantity'] as int,
        totalAmount: (json['total_amount'] as num).toDouble(),
        status: json['status'] as String,
        createdAt: DateTime.parse(json['created_at'] as String),
        completedAt: json['completed_at'] == null
            ? null
            : DateTime.parse(json['completed_at'] as String),
        cancelledAt: json['cancelled_at'] == null
            ? null
            : DateTime.parse(json['cancelled_at'] as String),
        paymentId: json['payment_id'] as String?,
        extraData: json['extra_data'] == null
            ? null
            : jsonDecode(json['extra_data'] as String)
                as Map<String, dynamic>,
      );

  Map<String, dynamic> toJson() => {
        'id': id,
        'user_id': userId,
        'event_id': eventId,
        'quantity': quantity,
        'total_amount': totalAmount,
        'status': status,
        'created_at': createdAt.toIso8601String(),
        'completed_at': completedAt?.toIso8601String(),
        'cancelled_at': cancelledAt?.toIso8601String(),
        'payment_id': paymentId,
        'extra_data': extraData == null ? null : jsonEncode(extraData),
      };
}

Map<String, dynamic> createOrdersRecordData({
  required String userId,
  required String eventId,
  required int quantity,
  required double totalAmount,
  String status = 'pending',
  DateTime? createdAt,
  DateTime? completedAt,
  DateTime? cancelledAt,
  String? paymentId,
  Map<String, dynamic>? extraData,
}) {
  final data = <String, dynamic>{
    'user_id': userId,
    'event_id': eventId,
    'quantity': quantity,
    'total_amount': totalAmount,
    'status': status,
    'created_at': (createdAt ?? DateTime.now()).toIso8601String(),
    'completed_at': completedAt?.toIso8601String(),
    'cancelled_at': cancelledAt?.toIso8601String(),
    'payment_id': paymentId,
    'extra_data': extraData == null ? null : jsonEncode(extraData),
  }.withoutNulls;

  return data;
}

class OrdersRecordDocumentEquality implements Equality<OrdersRecord> {
  const OrdersRecordDocumentEquality();

  @override
  bool equals(OrdersRecord? e1, OrdersRecord? e2) {
    return e1?.userId == e2?.userId &&
        e1?.eventId == e2?.eventId &&
        e1?.quantity == e2?.quantity &&
        e1?.totalAmount == e2?.totalAmount &&
        e1?.status == e2?.status &&
        e1?.createdAt == e2?.createdAt &&
        e1?.completedAt == e2?.completedAt &&
        e1?.cancelledAt == e2?.cancelledAt &&
        e1?.paymentId == e2?.paymentId &&
        e1?.extraData == e2?.extraData;
  }

  @override
  int hash(OrdersRecord? e) => const ListEquality().hash([
        e?.userId,
        e?.eventId,
        e?.quantity,
        e?.totalAmount,
        e?.status,
        e?.createdAt,
        e?.completedAt,
        e?.cancelledAt,
        e?.paymentId,
        e?.extraData,
      ]);

  @override
  bool isValidKey(Object? o) => o is OrdersRecord;
}
