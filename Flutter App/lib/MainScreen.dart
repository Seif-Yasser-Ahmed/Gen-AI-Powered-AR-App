import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:gen_ai_powered_ar_app/ARScreen.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key, required this.title});
  final String title;

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen>
{
  final TextEditingController _controller = TextEditingController();

  void _onMenuSelected(String value) {
    switch (value)
    {
      case 'settings':
      // Action for Settings
        showDialog(
          context: context,
          builder: (context) => AlertDialog(
            title: Text("Settings"),
            content: Text("Settings options will go here."),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(),
                child: Text("Close"),
              ),
            ],
          ),
        );
        break;
      case 'about':
      // Action for About
        showDialog(
          context: context,
          builder: (context) => AlertDialog(
            title: Text("About"),
            content: Text("This app generates creative ideas!"),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(),
                child: Text("Close"),
              ),
            ],
          ),
        );
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF121212),
      appBar: AppBar(
        centerTitle: true,
        leading: IconButton(
          onPressed: ()
          {
            // Scaffold.of(context).openDrawer(); // Open the drawer
          },
          icon: const Icon(Icons.segment_rounded),
        ),
        title: Container(
          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 6),
          decoration: const BoxDecoration(
            color: Color(0xFF555555),
            borderRadius: BorderRadius.all(Radius.circular(6)),
          ),
          child: Text(
            widget.title,
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w400),
          ),
        ),
        actions: [
          PopupMenuButton<String>(
            icon: const Icon(Icons.more_vert_rounded),
            onSelected: _onMenuSelected,
            itemBuilder: (BuildContext context) {
              return [
                const PopupMenuItem<String>(
                  value: 'settings',
                  child: Text('Settings'),
                ),
                const PopupMenuItem<String>(
                  value: 'about',
                  child: Text('About'),
                ),
              ];
            },
          ),
        ],
        backgroundColor: const Color(0xFF121212),
      ),
      // drawer: Drawer(),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              const Text(
                'Hey, good evening!',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 26,
                  fontWeight: FontWeight.bold,
                  color: Colors.white,
                ),
              ),
              const SizedBox(height: 10),
              const Text(
                'What do you want to generate?',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 18,
                  color: Colors.white,
                ),
              ),
              const SizedBox(height: 20),
              Container(
                margin: const EdgeInsets.only(top: 40, bottom: 20),
                child: TextField(
                  controller: _controller,
                  style: const TextStyle(color: Colors.white),
                  decoration: InputDecoration(
                    hintMaxLines: 1,
                    helperText: "eg. magic bird",
                    labelText: 'Enter your imagination',
                    labelStyle: const TextStyle(color: Colors.white70),
                    enabledBorder: OutlineInputBorder(
                      borderSide: const BorderSide(color: Colors.blue, width: 2.0),
                      borderRadius: BorderRadius.circular(50.0),
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderSide: const BorderSide(color: Colors.blueAccent, width: 2.0),
                      borderRadius: BorderRadius.circular(50.0),
                    ),
                    suffixIcon: IconButton(
                      icon: const Icon(Icons.clear, color: Colors.white70),
                      onPressed: () {
                        _controller.clear(); // Clear the text field
                      },
                    ),
                    contentPadding: const EdgeInsets.symmetric(vertical: 20.0, horizontal: 20.0),
                  ),
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  ElevatedButton.icon(
                    onPressed: ()
                    {
                      if(_controller.text.isNotEmpty)
                      {
                        print('Button pressed with input: ${_controller.text}');
                        // Navigate to the AR screen
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => const ARScreen()),
                        );
                      }
                    },
                    icon: const Icon(Icons.arrow_forward, color: Colors.black),
                    label: const Text("Generate", style: TextStyle(color: Colors.black)),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.white,
                      padding: const EdgeInsets.all(15),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(50.0),
                      ),
                    ).copyWith(
                      backgroundColor: MaterialStateProperty.resolveWith((states) {
                        if (states.contains(MaterialState.pressed)) {
                          return Colors.grey; // Darker color when pressed
                        }
                        return Colors.white; // Default button color
                      }),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
