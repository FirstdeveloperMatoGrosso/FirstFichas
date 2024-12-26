import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/foundation.dart';

Future initFirebase() async {
  if (kIsWeb) {
    await Firebase.initializeApp(
        options: FirebaseOptions(
            apiKey: "AIzaSyCOMUfpHV5i64QlK-Ks752bHcyBbRChqdU",
            authDomain: "firstingressos.firebaseapp.com",
            projectId: "firstingressos",
            storageBucket: "firstingressos.appspot.com",
            messagingSenderId: "831065561379",
            appId: "1:831065561379:web:b4fec4ca63d4c79e89d496",
            measurementId: "G-SCW2YF00J7"));
  } else {
    await Firebase.initializeApp();
  }
}
