import 'package:flutter/material.dart';
import 'package:gen_ai_powered_ar_app/MainScreen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    var title = 'AR Gen ✦︎';
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: title,
      theme: ThemeData.dark().copyWith( // Apply dark theme
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.white,
          brightness: Brightness.dark, // Ensure it's a dark theme
        ),
        scaffoldBackgroundColor: Colors.black,
      ),
      home: MainScreen(title: title),
    );
  }
}
