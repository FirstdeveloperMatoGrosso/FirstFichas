import 'package:flutter/material.dart';

class EventsRecord {
  EventsRecord({
    this.id,
    this.title,
    this.description,
    this.date,
    this.location,
    this.price,
    this.totalTickets,
    this.availableTickets,
    this.organizerId,
    this.venueId,
    this.categoryId,
    this.createdAt,
    this.updatedAt,
  });

  final String? id;
  final String? title;
  final String? description;
  final DateTime? date;
  final String? location;
  final double? price;
  final int? totalTickets;
  final int? availableTickets;
  final String? organizerId;
  final String? venueId;
  final String? categoryId;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  static EventsRecord fromJson(Map<String, dynamic> json) => EventsRecord(
        id: json['id'] as String?,
        title: json['title'] as String?,
        description: json['description'] as String?,
        date: json['date'] == null ? null : DateTime.parse(json['date'] as String),
        location: json['location'] as String?,
        price: (json['price'] as num?)?.toDouble(),
        totalTickets: json['total_tickets'] as int?,
        availableTickets: json['available_tickets'] as int?,
        organizerId: json['organizer_id'] as String?,
        venueId: json['venue_id'] as String?,
        categoryId: json['category_id'] as String?,
        createdAt: json['created_at'] == null
            ? null
            : DateTime.parse(json['created_at'] as String),
        updatedAt: json['updated_at'] == null
            ? null
            : DateTime.parse(json['updated_at'] as String),
      );

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'description': description,
        'date': date?.toIso8601String(),
        'location': location,
        'price': price,
        'total_tickets': totalTickets,
        'available_tickets': availableTickets,
        'organizer_id': organizerId,
        'venue_id': venueId,
        'category_id': categoryId,
        'created_at': createdAt?.toIso8601String(),
        'updated_at': updatedAt?.toIso8601String(),
      };

  @override
  String toString() =>
      'EventsRecord(id: $id, title: $title, description: $description, date: $date, location: $location, price: $price, totalTickets: $totalTickets, availableTickets: $availableTickets, organizerId: $organizerId, venueId: $venueId, categoryId: $categoryId, createdAt: $createdAt, updatedAt: $updatedAt)';

  @override
  bool operator ==(Object other) {
    return other is EventsRecord &&
        id == other.id &&
        title == other.title &&
        description == other.description &&
        date == other.date &&
        location == other.location &&
        price == other.price &&
        totalTickets == other.totalTickets &&
        availableTickets == other.availableTickets &&
        organizerId == other.organizerId &&
        venueId == other.venueId &&
        categoryId == other.categoryId &&
        createdAt == other.createdAt &&
        updatedAt == other.updatedAt;
  }

  @override
  int get hashCode => Object.hash(
        id,
        title,
        description,
        date,
        location,
        price,
        totalTickets,
        availableTickets,
        organizerId,
        venueId,
        categoryId,
        createdAt,
        updatedAt,
      );
}

Map<String, dynamic> createEventsRecordData({
  String? id,
  String? title,
  String? description,
  DateTime? date,
  String? location,
  double? price,
  int? totalTickets,
  int? availableTickets,
  String? organizerId,
  String? venueId,
  String? categoryId,
  DateTime? createdAt,
  DateTime? updatedAt,
}) {
  final firestoreData = {
    if (id != null) 'id': id,
    if (title != null) 'title': title,
    if (description != null) 'description': description,
    if (date != null) 'date': date.toIso8601String(),
    if (location != null) 'location': location,
    if (price != null) 'price': price,
    if (totalTickets != null) 'total_tickets': totalTickets,
    if (availableTickets != null) 'available_tickets': availableTickets,
    if (organizerId != null) 'organizer_id': organizerId,
    if (venueId != null) 'venue_id': venueId,
    if (categoryId != null) 'category_id': categoryId,
    if (createdAt != null) 'created_at': createdAt.toIso8601String(),
    if (updatedAt != null) 'updated_at': updatedAt.toIso8601String(),
  };

  return firestoreData;
}
