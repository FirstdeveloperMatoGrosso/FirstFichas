import 'package:supabase_flutter/supabase_flutter.dart';
import 'supabase_config.dart';

class SupaFlow {
  static SupabaseClient get client => Supabase.instance.client;

  static User? get currentUser => client.auth.currentUser;

  static String get supabaseUrl => SupabaseConfig.supabaseUrl;
  static String get supabaseAnonKey => SupabaseConfig.supabaseAnonKey;

  static Future<bool> signUp({
    required String email,
    required String password,
  }) async {
    try {
      final response = await client.auth.signUp(
        email: email,
        password: password,
      );
      return response.user != null;
    } catch (e) {
      print('Erro ao criar usuário: $e');
      return false;
    }
  }

  static Future<bool> signIn({
    required String email,
    required String password,
  }) async {
    try {
      final response = await client.auth.signInWithPassword(
        email: email,
        password: password,
      );
      return response.user != null;
    } catch (e) {
      print('Erro ao fazer login: $e');
      return false;
    }
  }

  static Future<void> signOut() async {
    await client.auth.signOut();
  }
}
