import 'dart:convert';

class VenuesRecord {
  final String id;
  final String name;
  final String? description;
  final String address;
  final String city;
  final String state;
  final String country;
  final String zipCode;
  final double? latitude;
  final double? longitude;
  final int capacity;
  final String? imageUrl;
  final DateTime createdAt;
  final DateTime? updatedAt;
  final Map<String, dynamic>? extraData;

  VenuesRecord({
    required this.id,
    required this.name,
    this.description,
    required this.address,
    required this.city,
    required this.state,
    required this.country,
    required this.zipCode,
    this.latitude,
    this.longitude,
    required this.capacity,
    this.imageUrl,
    required this.createdAt,
    this.updatedAt,
    this.extraData,
  });

  factory VenuesRecord.fromJson(Map<String, dynamic> json) => VenuesRecord(
        id: json['id'] as String,
        name: json['name'] as String,
        description: json['description'] as String?,
        address: json['address'] as String,
        city: json['city'] as String,
        state: json['state'] as String,
        country: json['country'] as String,
        zipCode: json['zip_code'] as String,
        latitude: json['latitude'] == null
            ? null
            : (json['latitude'] as num).toDouble(),
        longitude: json['longitude'] == null
            ? null
            : (json['longitude'] as num).toDouble(),
        capacity: json['capacity'] as int,
        imageUrl: json['image_url'] as String?,
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
        'address': address,
        'city': city,
        'state': state,
        'country': country,
        'zip_code': zipCode,
        'latitude': latitude,
        'longitude': longitude,
        'capacity': capacity,
        'image_url': imageUrl,
        'created_at': createdAt.toIso8601String(),
        'updated_at': updatedAt?.toIso8601String(),
        'extra_data': extraData == null ? null : jsonEncode(extraData),
      };
}

Map<String, dynamic> createVenuesRecordData({
  required String name,
  String? description,
  required String address,
  required String city,
  required String state,
  required String country,
  required String zipCode,
  double? latitude,
  double? longitude,
  required int capacity,
  String? imageUrl,
  DateTime? createdAt,
  DateTime? updatedAt,
  Map<String, dynamic>? extraData,
}) {
  final data = <String, dynamic>{
    'name': name,
    'description': description,
    'address': address,
    'city': city,
    'state': state,
    'country': country,
    'zip_code': zipCode,
    'latitude': latitude,
    'longitude': longitude,
    'capacity': capacity,
    'image_url': imageUrl,
    'created_at': (createdAt ?? DateTime.now()).toIso8601String(),
    'updated_at': updatedAt?.toIso8601String(),
    'extra_data': extraData == null ? null : jsonEncode(extraData),
  }.withoutNulls;

  return data;
}

class VenuesRecordDocumentEquality implements Equality<VenuesRecord> {
  const VenuesRecordDocumentEquality();

  @override
  bool equals(VenuesRecord? e1, VenuesRecord? e2) {
    return e1?.name == e2?.name &&
        e1?.description == e2?.description &&
        e1?.address == e2?.address &&
        e1?.city == e2?.city &&
        e1?.state == e2?.state &&
        e1?.country == e2?.country &&
        e1?.zipCode == e2?.zipCode &&
        e1?.latitude == e2?.latitude &&
        e1?.longitude == e2?.longitude &&
        e1?.capacity == e2?.capacity &&
        e1?.imageUrl == e2?.imageUrl &&
        e1?.createdAt == e2?.createdAt &&
        e1?.updatedAt == e2?.updatedAt &&
        e1?.extraData == e2?.extraData;
  }

  @override
  int hash(VenuesRecord? e) => const ListEquality().hash([
        e?.name,
        e?.description,
        e?.address,
        e?.city,
        e?.state,
        e?.country,
        e?.zipCode,
        e?.latitude,
        e?.longitude,
        e?.capacity,
        e?.imageUrl,
        e?.createdAt,
        e?.updatedAt,
        e?.extraData,
      ]);

  @override
  bool isValidKey(Object? o) => o is VenuesRecord;
}
