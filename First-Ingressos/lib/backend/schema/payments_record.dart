import 'dart:convert';

class PaymentsRecord {
  final String id;
  final String orderId;
  final String userId;
  final double amount;
  final String status; // pending, completed, failed, refunded
  final String paymentMethod;
  final DateTime createdAt;
  final DateTime? completedAt;
  final DateTime? refundedAt;
  final String? transactionId;
  final Map<String, dynamic>? paymentDetails;
  final Map<String, dynamic>? extraData;

  PaymentsRecord({
    required this.id,
    required this.orderId,
    required this.userId,
    required this.amount,
    required this.status,
    required this.paymentMethod,
    required this.createdAt,
    this.completedAt,
    this.refundedAt,
    this.transactionId,
    this.paymentDetails,
    this.extraData,
  });

  factory PaymentsRecord.fromJson(Map<String, dynamic> json) => PaymentsRecord(
        id: json['id'] as String,
        orderId: json['order_id'] as String,
        userId: json['user_id'] as String,
        amount: (json['amount'] as num).toDouble(),
        status: json['status'] as String,
        paymentMethod: json['payment_method'] as String,
        createdAt: DateTime.parse(json['created_at'] as String),
        completedAt: json['completed_at'] == null
            ? null
            : DateTime.parse(json['completed_at'] as String),
        refundedAt: json['refunded_at'] == null
            ? null
            : DateTime.parse(json['refunded_at'] as String),
        transactionId: json['transaction_id'] as String?,
        paymentDetails: json['payment_details'] == null
            ? null
            : jsonDecode(json['payment_details'] as String)
                as Map<String, dynamic>,
        extraData: json['extra_data'] == null
            ? null
            : jsonDecode(json['extra_data'] as String)
                as Map<String, dynamic>,
      );

  Map<String, dynamic> toJson() => {
        'id': id,
        'order_id': orderId,
        'user_id': userId,
        'amount': amount,
        'status': status,
        'payment_method': paymentMethod,
        'created_at': createdAt.toIso8601String(),
        'completed_at': completedAt?.toIso8601String(),
        'refunded_at': refundedAt?.toIso8601String(),
        'transaction_id': transactionId,
        'payment_details':
            paymentDetails == null ? null : jsonEncode(paymentDetails),
        'extra_data': extraData == null ? null : jsonEncode(extraData),
      };
}

Map<String, dynamic> createPaymentsRecordData({
  required String orderId,
  required String userId,
  required double amount,
  String status = 'pending',
  required String paymentMethod,
  DateTime? createdAt,
  DateTime? completedAt,
  DateTime? refundedAt,
  String? transactionId,
  Map<String, dynamic>? paymentDetails,
  Map<String, dynamic>? extraData,
}) {
  final data = <String, dynamic>{
    'order_id': orderId,
    'user_id': userId,
    'amount': amount,
    'status': status,
    'payment_method': paymentMethod,
    'created_at': (createdAt ?? DateTime.now()).toIso8601String(),
    'completed_at': completedAt?.toIso8601String(),
    'refunded_at': refundedAt?.toIso8601String(),
    'transaction_id': transactionId,
    'payment_details': paymentDetails == null ? null : jsonEncode(paymentDetails),
    'extra_data': extraData == null ? null : jsonEncode(extraData),
  }.withoutNulls;

  return data;
}

class PaymentsRecordDocumentEquality implements Equality<PaymentsRecord> {
  const PaymentsRecordDocumentEquality();

  @override
  bool equals(PaymentsRecord? e1, PaymentsRecord? e2) {
    return e1?.orderId == e2?.orderId &&
        e1?.userId == e2?.userId &&
        e1?.amount == e2?.amount &&
        e1?.status == e2?.status &&
        e1?.paymentMethod == e2?.paymentMethod &&
        e1?.createdAt == e2?.createdAt &&
        e1?.completedAt == e2?.completedAt &&
        e1?.refundedAt == e2?.refundedAt &&
        e1?.transactionId == e2?.transactionId &&
        e1?.paymentDetails == e2?.paymentDetails &&
        e1?.extraData == e2?.extraData;
  }

  @override
  int hash(PaymentsRecord? e) => const ListEquality().hash([
        e?.orderId,
        e?.userId,
        e?.amount,
        e?.status,
        e?.paymentMethod,
        e?.createdAt,
        e?.completedAt,
        e?.refundedAt,
        e?.transactionId,
        e?.paymentDetails,
        e?.extraData,
      ]);

  @override
  bool isValidKey(Object? o) => o is PaymentsRecord;
}
