import 'package:flutter/material.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:rxdart/rxdart.dart';

import '../backend/supabase/supabase.dart';
import '../flutter_flow/flutter_flow_util.dart';

class FirstIngressosUser {
  FirstIngressosUser(this.user);
  final User? user;
  final String? displayName;
  final String? email;
  final String? photoUrl;
  final String? uid;

  bool get loggedIn => user != null;
}

FirstIngressosUser? currentUser;
bool get loggedIn => currentUser?.loggedIn ?? false;

Stream<FirstIngressosUser> firstingressosUserStream() => SupaFlow.client.auth.onAuthStateChange
    .map((event) => FirstIngressosUser(event.session?.user));

Future<User?> signInOrCreateAccount(
  BuildContext context,
  Future<User?> Function() signInFunc,
  String authProvider,
) async {
  try {
    final user = await signInFunc();
    if (user == null) {
      return null;
    }
    return user;
  } catch (e) {
    ScaffoldMessenger.of(context).hideCurrentSnackBar();
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Error: $e')),
    );
    return null;
  }
}

Future signOut() => SupaFlow.client.auth.signOut();

Future resetPassword({
  required String email,
  required BuildContext context,
}) async {
  try {
    await SupaFlow.client.auth.resetPasswordForEmail(email);
  } catch (e) {
    ScaffoldMessenger.of(context).hideCurrentSnackBar();
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Error: $e')),
    );
    return null;
  }
}

String get currentUserEmail => currentUser?.email ?? '';

String get currentUserUid => currentUser?.uid ?? '';

String get currentUserDisplayName => currentUser?.displayName ?? '';

String get currentUserPhoto => currentUser?.photoUrl ?? '';

// Set when using phone verification (after phone number is provided).
String? _phoneAuthVerificationCode;
// Set when using phone sign in in web mode (ignored otherwise).
ConfirmationResult? _webPhoneAuthConfirmationResult;

Future beginPhoneAuth({
  required BuildContext context,
  required String phoneNumber,
  required VoidCallback onCodeSent,
}) async {
  // TODO: Implement phone authentication
}

Future verifySmsCode({
  required BuildContext context,
  required String smsCode,
}) async {
  // TODO: Implement SMS verification
}

Future<User?> signInWithEmail(
  BuildContext context,
  String email,
  String password,
) async {
  final response = await SupaFlow.client.auth.signInWithPassword(
    email: email,
    password: password,
  );
  return response.user;
}

Future<User?> createAccountWithEmail(
  BuildContext context,
  String email,
  String password,
) async {
  final response = await SupaFlow.client.auth.signUp(
    email: email,
    password: password,
  );
  return response.user;
}

Future<User?> signInAnonymously(BuildContext context) async {
  // TODO: Implement anonymous sign in if needed
  return null;
}

Future<User?> signInWithApple(BuildContext context) async {
  // TODO: Implement Apple sign in
  return null;
}

Future<User?> signInWithGoogle(BuildContext context) async {
  // TODO: Implement Google sign in
  return null;
}

Future<User?> signInWithGithub(BuildContext context) async {
  // TODO: Implement Github sign in
  return null;
}

Future<User?> signInWithJwtToken(
  BuildContext context,
  String jwtToken,
) async {
  // TODO: Implement JWT token sign in
  return null;
}
