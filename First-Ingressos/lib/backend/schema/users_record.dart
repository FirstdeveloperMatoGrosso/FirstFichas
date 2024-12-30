import 'dart:convert';

import 'package:collection/collection.dart';

import '/backend/schema/util/firestore_util.dart';
import '/backend/schema/util/schema_util.dart';

import 'index.dart';
import '/flutter_flow/flutter_flow_util.dart';

class UsersRecord {
  final String id;
  final String? email;
  final String? displayName;
  final String? photoUrl;
  final String? phoneNumber;
  final DateTime? createdTime;
  final Map<String, dynamic>? extraData;

  UsersRecord({
    required this.id,
    this.email,
    this.displayName,
    this.photoUrl,
    this.phoneNumber,
    this.createdTime,
    this.extraData,
  });

  factory UsersRecord.fromJson(Map<String, dynamic> json) => UsersRecord(
        id: json['id'] as String,
        email: json['email'] as String?,
        displayName: json['display_name'] as String?,
        photoUrl: json['photo_url'] as String?,
        phoneNumber: json['phone_number'] as String?,
        createdTime: json['created_time'] == null
            ? null
            : DateTime.parse(json['created_time'] as String),
        extraData: json['extra_data'] == null
            ? null
            : jsonDecode(json['extra_data'] as String)
                as Map<String, dynamic>,
      );

  Map<String, dynamic> toJson() => {
        'id': id,
        'email': email,
        'display_name': displayName,
        'photo_url': photoUrl,
        'phone_number': phoneNumber,
        'created_time': createdTime?.toIso8601String(),
        'extra_data': extraData == null ? null : jsonEncode(extraData),
      };
}

Map<String, dynamic> createUsersRecordData({
  String? email,
  String? displayName,
  String? photoUrl,
  String? phoneNumber,
  DateTime? createdTime,
  Map<String, dynamic>? extraData,
}) {
  final data = <String, dynamic>{
    'email': email,
    'display_name': displayName,
    'photo_url': photoUrl,
    'phone_number': phoneNumber,
    'created_time': createdTime,
    'extra_data': extraData,
  }.withoutNulls;

  return data;
}

class UsersRecordDocumentEquality implements Equality<UsersRecord> {
  const UsersRecordDocumentEquality();

  @override
  bool equals(UsersRecord? e1, UsersRecord? e2) {
    return e1?.email == e2?.email &&
        e1?.displayName == e2?.displayName &&
        e1?.photoUrl == e2?.photoUrl &&
        e1?.phoneNumber == e2?.phoneNumber &&
        e1?.createdTime == e2?.createdTime &&
        e1?.extraData == e2?.extraData;
  }

  @override
  int hash(UsersRecord? e) => const ListEquality().hash([
        e?.email,
        e?.displayName,
        e?.photoUrl,
        e?.phoneNumber,
        e?.createdTime,
        e?.extraData,
      ]);

  @override
  bool isValidKey(Object? o) => o is UsersRecord;
}
