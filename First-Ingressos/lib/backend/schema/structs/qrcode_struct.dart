// ignore_for_file: unnecessary_getters_setters

import 'package:flutter/material.dart';
import 'package:json_path/json_path.dart';

class QrcodeStruct {
  QrcodeStruct({
    this.qrcode,
    this.ticketId,
    this.eventId,
    this.userId,
    this.isValid,
    this.validatedAt,
  });

  final String? qrcode;
  final String? ticketId;
  final String? eventId;
  final String? userId;
  final bool? isValid;
  final DateTime? validatedAt;

  static QrcodeStruct fromMap(Map<String, dynamic>? data) => QrcodeStruct(
        qrcode: data?['qrcode'] as String?,
        ticketId: data?['ticket_id'] as String?,
        eventId: data?['event_id'] as String?,
        userId: data?['user_id'] as String?,
        isValid: data?['is_valid'] as bool?,
        validatedAt: data?['validated_at'] == null
            ? null
            : DateTime.parse(data?['validated_at'] as String),
      );

  static QrcodeStruct? maybeFromMap(dynamic data) =>
      data is Map<String, dynamic> ? QrcodeStruct.fromMap(data) : null;

  Map<String, dynamic> toMap() => {
        'qrcode': qrcode,
        'ticket_id': ticketId,
        'event_id': eventId,
        'user_id': userId,
        'is_valid': isValid,
        'validated_at': validatedAt?.toIso8601String(),
      };

  @override
  String toString() =>
      'QrcodeStruct(qrcode: $qrcode, ticketId: $ticketId, eventId: $eventId, userId: $userId, isValid: $isValid, validatedAt: $validatedAt)';

  @override
  bool operator ==(Object other) {
    return other is QrcodeStruct &&
        qrcode == other.qrcode &&
        ticketId == other.ticketId &&
        eventId == other.eventId &&
        userId == other.userId &&
        isValid == other.isValid &&
        validatedAt == other.validatedAt;
  }

  @override
  int get hashCode => Object.hash(
        qrcode,
        ticketId,
        eventId,
        userId,
        isValid,
        validatedAt,
      );
}
