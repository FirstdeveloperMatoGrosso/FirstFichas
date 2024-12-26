import 'package:flutter/material.dart';

class CarouselConfig {
  static const Duration autoPlayInterval = Duration(seconds: 5);
  static const Duration animationDuration = Duration(milliseconds: 800);
  static const Curve animationCurve = Curves.fastOutSlowIn;
  
  static double getViewportFraction(BuildContext context) {
    final width = MediaQuery.of(context).size.width;
    if (width < 600) return 1.0; // Mobile
    if (width < 1200) return 0.8; // Tablet
    return 0.6; // Desktop
  }
  
  static double getHeight(BuildContext context) {
    final width = MediaQuery.of(context).size.width;
    if (width < 600) return 200; // Mobile
    if (width < 1200) return 300; // Tablet
    return 400; // Desktop
  }
}
