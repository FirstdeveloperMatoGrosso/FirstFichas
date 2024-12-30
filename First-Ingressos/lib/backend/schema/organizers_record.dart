import 'dart:convert';

class OrganizersRecord {
  final String id;
  final String userId;
  final String name;
  final String? description;
  final String email;
  final String? phoneNumber;
  final String? website;
  final String? logoUrl;
  final bool verified;
  final DateTime createdAt;
  final DateTime? updatedAt;
  final Map<String, dynamic>? extraData;

  OrganizersRecord({
    required this.id,
    required this.userId,
    required this.name,
    this.description,
    required this.email,
    this.phoneNumber,
    this.website,
    this.logoUrl,
    required this.verified,
    required this.createdAt,
    this.updatedAt,
    this.extraData,
  });

  factory OrganizersRecord.fromJson(Map<String, dynamic> json) =>
      OrganizersRecord(
        id: json['id'] as String,
        userId: json['user_id'] as String,
        name: json['name'] as String,
        description: json['description'] as String?,
        email: json['email'] as String,
        phoneNumber: json['phone_number'] as String?,
        website: json['website'] as String?,
        logoUrl: json['logo_url'] as String?,
        verified: json['verified'] as bool,
        createdAt: DateTime.parse(json['created_at'] as String),
        updatedAt: json['updated_at'] == null
            ? null
            : DateTime.parse(json['updated_at'] as String),
        extraData: json['extra_data'] == null
            ? null
            : jsonDecode(json['extra_data'] as String)
                as Map<String, dynamic>,
      );

  Map<String, dynamic> toJson() => {
        'id': id,
        'user_id': userId,
        'name': name,
        'description': description,
        'email': email,
        'phone_number': phoneNumber,
        'website': website,
        'logo_url': logoUrl,
        'verified': verified,
        'created_at': createdAt.toIso8601String(),
        'updated_at': updatedAt?.toIso8601String(),
        'extra_data': extraData == null ? null : jsonEncode(extraData),
      };
}

Map<String, dynamic> createOrganizersRecordData({
  required String userId,
  required String name,
  String? description,
  required String email,
  String? phoneNumber,
  String? website,
  String? logoUrl,
  bool verified = false,
  DateTime? createdAt,
  DateTime? updatedAt,
  Map<String, dynamic>? extraData,
}) {
  final data = <String, dynamic>{
    'user_id': userId,
    'name': name,
    'description': description,
    'email': email,
    'phone_number': phoneNumber,
    'website': website,
    'logo_url': logoUrl,
    'verified': verified,
    'created_at': (createdAt ?? DateTime.now()).toIso8601String(),
    'updated_at': updatedAt?.toIso8601String(),
    'extra_data': extraData == null ? null : jsonEncode(extraData),
  }.withoutNulls;

  return data;
}

class OrganizersRecordDocumentEquality implements Equality<OrganizersRecord> {
  const OrganizersRecordDocumentEquality();

  @override
  bool equals(OrganizersRecord? e1, OrganizersRecord? e2) {
    return e1?.userId == e2?.userId &&
        e1?.name == e2?.name &&
        e1?.description == e2?.description &&
        e1?.email == e2?.email &&
        e1?.phoneNumber == e2?.phoneNumber &&
        e1?.website == e2?.website &&
        e1?.logoUrl == e2?.logoUrl &&
        e1?.verified == e2?.verified &&
        e1?.createdAt == e2?.createdAt &&
        e1?.updatedAt == e2?.updatedAt &&
        e1?.extraData == e2?.extraData;
  }

  @override
  int hash(OrganizersRecord? e) => const ListEquality().hash([
        e?.userId,
        e?.name,
        e?.description,
        e?.email,
        e?.phoneNumber,
        e?.website,
        e?.logoUrl,
        e?.verified,
        e?.createdAt,
        e?.updatedAt,
        e?.extraData,
      ]);

  @override
  bool isValidKey(Object? o) => o is OrganizersRecord;
}
