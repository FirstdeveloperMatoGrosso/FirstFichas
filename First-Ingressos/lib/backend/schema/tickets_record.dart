import 'dart:convert';

class TicketsRecord {
  final String id;
  final String eventId;
  final String userId;
  final String orderId;
  final String ticketNumber;
  final bool used;
  final DateTime purchaseDate;
  final DateTime? usedDate;
  final Map<String, dynamic>? extraData;

  TicketsRecord({
    required this.id,
    required this.eventId,
    required this.userId,
    required this.orderId,
    required this.ticketNumber,
    required this.used,
    required this.purchaseDate,
    this.usedDate,
    this.extraData,
  });

  factory TicketsRecord.fromJson(Map<String, dynamic> json) => TicketsRecord(
        id: json['id'] as String,
        eventId: json['event_id'] as String,
        userId: json['user_id'] as String,
        orderId: json['order_id'] as String,
        ticketNumber: json['ticket_number'] as String,
        used: json['used'] as bool,
        purchaseDate: DateTime.parse(json['purchase_date'] as String),
        usedDate: json['used_date'] == null
            ? null
            : DateTime.parse(json['used_date'] as String),
        extraData: json['extra_data'] == null
            ? null
            : jsonDecode(json['extra_data'] as String)
                as Map<String, dynamic>,
      );

  Map<String, dynamic> toJson() => {
        'id': id,
        'event_id': eventId,
        'user_id': userId,
        'order_id': orderId,
        'ticket_number': ticketNumber,
        'used': used,
        'purchase_date': purchaseDate.toIso8601String(),
        'used_date': usedDate?.toIso8601String(),
        'extra_data': extraData == null ? null : jsonEncode(extraData),
      };
}

Map<String, dynamic> createTicketsRecordData({
  required String eventId,
  required String userId,
  required String orderId,
  required String ticketNumber,
  bool used = false,
  DateTime? purchaseDate,
  DateTime? usedDate,
  Map<String, dynamic>? extraData,
}) {
  final data = <String, dynamic>{
    'event_id': eventId,
    'user_id': userId,
    'order_id': orderId,
    'ticket_number': ticketNumber,
    'used': used,
    'purchase_date': (purchaseDate ?? DateTime.now()).toIso8601String(),
    'used_date': usedDate?.toIso8601String(),
    'extra_data': extraData == null ? null : jsonEncode(extraData),
  }.withoutNulls;

  return data;
}

class TicketsRecordDocumentEquality implements Equality<TicketsRecord> {
  const TicketsRecordDocumentEquality();

  @override
  bool equals(TicketsRecord? e1, TicketsRecord? e2) {
    return e1?.eventId == e2?.eventId &&
        e1?.userId == e2?.userId &&
        e1?.orderId == e2?.orderId &&
        e1?.ticketNumber == e2?.ticketNumber &&
        e1?.used == e2?.used &&
        e1?.purchaseDate == e2?.purchaseDate &&
        e1?.usedDate == e2?.usedDate &&
        e1?.extraData == e2?.extraData;
  }

  @override
  int hash(TicketsRecord? e) => const ListEquality().hash([
        e?.eventId,
        e?.userId,
        e?.orderId,
        e?.ticketNumber,
        e?.used,
        e?.purchaseDate,
        e?.usedDate,
        e?.extraData,
      ]);

  @override
  bool isValidKey(Object? o) => o is TicketsRecord;
}
