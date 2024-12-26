import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:rxdart/rxdart.dart';

import '../flutter_flow/flutter_flow_util.dart';
import 'schema/util/schema_util.dart';

import 'schema/users_record.dart';
import 'schema/events_record.dart';
import 'schema/tickets_record.dart';
import 'schema/orders_record.dart';
import 'schema/payments_record.dart';
import 'schema/organizers_record.dart';
import 'schema/venues_record.dart';
import 'schema/categories_record.dart';

export 'dart:async' show StreamSubscription;
export 'package:supabase_flutter/supabase_flutter.dart';

export 'schema/index.dart';
export 'schema/util/schema_util.dart';

export 'supabase/supabase.dart';

/// Functions to query records from Supabase tables.
/// 
/// Example:
/// ```dart
/// final users = await queryUsers(
///   queryBuilder: (q) => q.eq('email', 'user@example.com'),
/// ).get();
/// ```

Future<List<UsersRecord>> queryUsers({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('users').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => UsersRecord.fromJson(json)).toList();
}

Future<List<EventsRecord>> queryEvents({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('events').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => EventsRecord.fromJson(json)).toList();
}

Future<List<TicketsRecord>> queryTickets({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('tickets').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => TicketsRecord.fromJson(json)).toList();
}

Future<List<OrdersRecord>> queryOrders({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('orders').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => OrdersRecord.fromJson(json)).toList();
}

Future<List<PaymentsRecord>> queryPayments({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('payments').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => PaymentsRecord.fromJson(json)).toList();
}

Future<List<OrganizersRecord>> queryOrganizers({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('organizers').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => OrganizersRecord.fromJson(json)).toList();
}

Future<List<VenuesRecord>> queryVenues({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('venues').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => VenuesRecord.fromJson(json)).toList();
}

Future<List<CategoriesRecord>> queryCategories({
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
}) async {
  final query = SupaFlow.client.from('categories').select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  final response = await query;
  return response.map((json) => CategoriesRecord.fromJson(json)).toList();
}

Future<int> queryCollectionCount(
  String tableName, {
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
}) async {
  final query = SupaFlow.client.from(tableName).select('*', const FetchOptions(count: CountOption.exact));
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  final response = await query;
  return response.count ?? 0;
}

Stream<List<T>> queryStream<T>({
  required String tableName,
  required T Function(Map<String, dynamic>) fromJson,
  PostgrestFilterBuilder Function(PostgrestFilterBuilder)? queryBuilder,
  int limit = -1,
  bool includeDeleted = false,
}) {
  final query = SupaFlow.client.from(tableName).select();
  if (queryBuilder != null) {
    queryBuilder(query);
  }
  if (limit > 0) {
    query.limit(limit);
  }
  return query.stream().map((response) => 
    response.map((json) => fromJson(json as Map<String, dynamic>)).toList()
  );
}
