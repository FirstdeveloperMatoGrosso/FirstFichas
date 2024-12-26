class Event {
  final String id;
  final String name;
  final String description;
  final DateTime date;
  final String location;
  final String imageUrl;
  final double price;
  final String category;
  final int availableTickets;
  final List<String> images;
  final Map<String, dynamic> ticketTypes;
  final String organizer;
  final Map<String, dynamic> venue;
  final bool isFeatured;

  Event({
    required this.id,
    required this.name,
    required this.description,
    required this.date,
    required this.location,
    required this.imageUrl,
    required this.price,
    required this.category,
    required this.availableTickets,
    required this.images,
    required this.ticketTypes,
    required this.organizer,
    required this.venue,
    this.isFeatured = false,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    return Event(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      date: DateTime.parse(json['date']),
      location: json['location'],
      imageUrl: json['imageUrl'],
      price: json['price'].toDouble(),
      category: json['category'],
      availableTickets: json['availableTickets'],
      images: List<String>.from(json['images']),
      ticketTypes: json['ticketTypes'],
      organizer: json['organizer'],
      venue: json['venue'],
      isFeatured: json['isFeatured'] ?? false,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'description': description,
      'date': date.toIso8601String(),
      'location': location,
      'imageUrl': imageUrl,
      'price': price,
      'category': category,
      'availableTickets': availableTickets,
      'images': images,
      'ticketTypes': ticketTypes,
      'organizer': organizer,
      'venue': venue,
      'isFeatured': isFeatured,
    };
  }
}
