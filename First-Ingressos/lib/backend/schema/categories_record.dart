import 'dart:convert';

class CategoriesRecord {
  final String id;
  final String name;
  final String? description;
  final String? iconUrl;
  final String? color;
  final DateTime createdAt;
  final DateTime? updatedAt;
  final Map<String, dynamic>? extraData;

  CategoriesRecord({
    required this.id,
    required this.name,
    this.description,
    this.iconUrl,
    this.color,
    required this.createdAt,
    this.updatedAt,
    this.extraData,
  });

  factory CategoriesRecord.fromJson(Map<String, dynamic> json) =>
      CategoriesRecord(
        id: json['id'] as String,
        name: json['name'] as String,
        description: json['description'] as String?,
        iconUrl: json['icon_url'] as String?,
        color: json['color'] as String?,
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
        'name': name,
        'description': description,
        'icon_url': iconUrl,
        'color': color,
        'created_at': createdAt.toIso8601String(),
        'updated_at': updatedAt?.toIso8601String(),
        'extra_data': extraData == null ? null : jsonEncode(extraData),
      };
}

Map<String, dynamic> createCategoriesRecordData({
  required String name,
  String? description,
  String? iconUrl,
  String? color,
  DateTime? createdAt,
  DateTime? updatedAt,
  Map<String, dynamic>? extraData,
}) {
  final data = <String, dynamic>{
    'name': name,
    'description': description,
    'icon_url': iconUrl,
    'color': color,
    'created_at': (createdAt ?? DateTime.now()).toIso8601String(),
    'updated_at': updatedAt?.toIso8601String(),
    'extra_data': extraData == null ? null : jsonEncode(extraData),
  }.withoutNulls;

  return data;
}

class CategoriesRecordDocumentEquality implements Equality<CategoriesRecord> {
  const CategoriesRecordDocumentEquality();

  @override
  bool equals(CategoriesRecord? e1, CategoriesRecord? e2) {
    return e1?.name == e2?.name &&
        e1?.description == e2?.description &&
        e1?.iconUrl == e2?.iconUrl &&
        e1?.color == e2?.color &&
        e1?.createdAt == e2?.createdAt &&
        e1?.updatedAt == e2?.updatedAt &&
        e1?.extraData == e2?.extraData;
  }

  @override
  int hash(CategoriesRecord? e) => const ListEquality().hash([
        e?.name,
        e?.description,
        e?.iconUrl,
        e?.color,
        e?.createdAt,
        e?.updatedAt,
        e?.extraData,
      ]);

  @override
  bool isValidKey(Object? o) => o is CategoriesRecord;
}
