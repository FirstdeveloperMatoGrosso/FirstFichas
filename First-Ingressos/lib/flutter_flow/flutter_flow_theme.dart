// ignore_for_file: overridden_fields, annotate_overrides

import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

const kThemeModeKey = '__theme_mode__';
SharedPreferences? _prefs;

abstract class FlutterFlowTheme {
  static ThemeMode get themeMode {
    final darkMode = _prefs?.getBool(kThemeModeKey);
    return darkMode == null
        ? ThemeMode.system
        : darkMode
            ? ThemeMode.dark
            : ThemeMode.light;
  }

  static Future<void> initialize() async {
    _prefs = await SharedPreferences.getInstance();
  }

  static void saveThemeMode(ThemeMode mode) {
    if (mode == ThemeMode.system) {
      _prefs?.remove(kThemeModeKey);
    } else {
      _prefs?.setBool(kThemeModeKey, mode == ThemeMode.dark);
    }
  }

  static FlutterFlowTheme of(BuildContext context) {
    return Theme.of(context).brightness == Brightness.dark
        ? DarkModeTheme()
        : LightModeTheme();
  }

  Color get primary => Colors.blue;
  Color get secondary => Colors.blueAccent;
  Color get tertiary => Colors.blueGrey;
  Color get alternate => Colors.grey;
  Color get primaryText => Colors.black;
  Color get secondaryText => Colors.grey[700]!;
  Color get primaryBackground => Colors.white;
  Color get secondaryBackground => Colors.grey[200]!;
  Color get accent1 => Colors.blue[200]!;
  Color get accent2 => Colors.blue[300]!;
  Color get accent3 => Colors.blue[400]!;
  Color get accent4 => Colors.blue[700]!;
  Color get success => Colors.green;
  Color get warning => Colors.orange;
  Color get error => Colors.red;
  Color get info => Colors.lightBlue;

  TextStyle get displayLarge => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 57.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get displayMedium => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 45.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get displaySmall => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 36.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get headlineLarge => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 32.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get headlineMedium => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 28.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get headlineSmall => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 24.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get titleLarge => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 22.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get titleMedium => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 16.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get titleSmall => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 14.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get labelLarge => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 14.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get labelMedium => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 12.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get labelSmall => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 11.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get bodyLarge => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 16.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get bodyMedium => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 14.0,
        fontWeight: FontWeight.normal,
      );
  TextStyle get bodySmall => const TextStyle(
        fontFamily: 'Readex Pro',
        fontSize: 12.0,
        fontWeight: FontWeight.normal,
      );
}

class LightModeTheme extends FlutterFlowTheme {
  @override
  Color get primary => const Color(0xFF4B39EF);
  @override
  Color get secondary => const Color(0xFF39D2C0);
  @override
  Color get tertiary => const Color(0xFFEE8B60);
  @override
  Color get alternate => const Color(0xFFE0E3E7);
  @override
  Color get primaryText => const Color(0xFF14181B);
  @override
  Color get secondaryText => const Color(0xFF57636C);
  @override
  Color get primaryBackground => const Color(0xFFF1F4F8);
  @override
  Color get secondaryBackground => const Color(0xFFFFFFFF);
  @override
  Color get accent1 => const Color(0x4C4B39EF);
  @override
  Color get accent2 => const Color(0x4D39D2C0);
  @override
  Color get accent3 => const Color(0x4DEE8B60);
  @override
  Color get accent4 => const Color(0xCCFFFFFF);
  @override
  Color get success => const Color(0xFF249689);
  @override
  Color get warning => const Color(0xFFF9CF58);
  @override
  Color get error => const Color(0xFFFF5963);
  @override
  Color get info => const Color(0xFFFFFFFF);
}

class DarkModeTheme extends FlutterFlowTheme {
  @override
  Color get primary => const Color(0xFF4B39EF);
  @override
  Color get secondary => const Color(0xFF39D2C0);
  @override
  Color get tertiary => const Color(0xFFEE8B60);
  @override
  Color get alternate => const Color(0xFF262D34);
  @override
  Color get primaryText => const Color(0xFFFFFFFF);
  @override
  Color get secondaryText => const Color(0xFF95A1AC);
  @override
  Color get primaryBackground => const Color(0xFF1D2428);
  @override
  Color get secondaryBackground => const Color(0xFF14181B);
  @override
  Color get accent1 => const Color(0x4C4B39EF);
  @override
  Color get accent2 => const Color(0x4D39D2C0);
  @override
  Color get accent3 => const Color(0x4DEE8B60);
  @override
  Color get accent4 => const Color(0xB214181B);
  @override
  Color get success => const Color(0xFF249689);
  @override
  Color get warning => const Color(0xFFF9CF58);
  @override
  Color get error => const Color(0xFFFF5963);
  @override
  Color get info => const Color(0xFFFFFFFF);
}
