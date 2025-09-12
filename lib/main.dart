import 'package:flutter/material.dart';
import 'todo.dart';
import 'todo_service.dart';

// --- Cozy Colors ---
const cozyPrimary = Color(0xFF8D6E63); // warmes Braun
const cozyAccent  = Color(0xFFA1887F); // helleres Braun
const cozyBg      = Color(0xFFFFF8F0); // sanftes Beige
const cozyText    = Color(0xFF4E342E); // dunkles Braun

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Lumaka - Sticker Quest',   // App-Name
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: cozyPrimary,
          background: cozyBg,
          brightness: Brightness.light,
        ),
        scaffoldBackgroundColor: cozyBg,
        appBarTheme: const AppBarTheme(
          backgroundColor: cozyPrimary,
          foregroundColor: Colors.white,
          centerTitle: false,
        ),
        cardTheme: CardThemeData( // <-- FIX
          color: Colors.white,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
          elevation: 2,
          margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        ),
        textTheme: const TextTheme(
          bodyMedium: TextStyle(color: cozyText),
          bodyLarge: TextStyle(color: cozyText),
          titleMedium: TextStyle(color: cozyText),
        ),
        checkboxTheme: const CheckboxThemeData(
          fillColor: WidgetStatePropertyAll(cozyPrimary),
        ),
        inputDecorationTheme: InputDecorationTheme(
          filled: true,
          fillColor: Colors.white,
          hintStyle: const TextStyle(color: Color(0xFF7A6A64)),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: Color(0xFFE0D6CF)),
          ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: Color(0xFFE0D6CF)),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: cozyPrimary, width: 2),
          ),
          contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
        ),
        filledButtonTheme: FilledButtonThemeData(
          style: FilledButton.styleFrom(
            backgroundColor: cozyPrimary,
            foregroundColor: Colors.white,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
          ),
        ),
      ),
      home: const HomeScreen(),
    );
  }
}

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final svc = LocalTodoService(); // später easy austauschbar gegen REST-Service
  final controller = TextEditingController();
  List<Todo> todos = [];
  bool loading = true;

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    final data = await svc.list();
    setState(() {
      todos = data;
      loading = false;
    });
  }

  Future<void> _add() async {
    final text = controller.text.trim();
    if (text.isEmpty) return;
    final t = await svc.create(text);
    setState(() {
      todos = [...todos, t];
      controller.clear();
    });
  }

  Future<void> _toggle(String id) async {
    final t = await svc.toggle(id);
    setState(() {
      todos = [for (final x in todos) if (x.id == t.id) t else x];
    });
  }

  Future<void> _remove(String id) async {
    await svc.remove(id);
    setState(() {
      todos = todos.where((t) => t.id != id).toList();
    });
  }

  int get _openCount => todos.where((t) => !t.done).length;

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    return Scaffold(
      appBar: AppBar(
        title: Text(
          _openCount == 0 ? 'Alles erledigt' : '${_openCount} offen',
        ),
        actions: [
          if (todos.any((t) => t.done))
            IconButton(
              tooltip: 'Erledigte löschen',
              onPressed: () async {
                final doneIds = todos.where((t) => t.done).map((t) => t.id).toList();
                for (final id in doneIds) {
                  await svc.remove(id);
                }
                setState(() {
                  todos = todos.where((t) => !t.done).toList();
                });
              },
              icon: const Icon(Icons.delete_sweep),
            ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 12, 12, 6),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: controller,
                    decoration: const InputDecoration(
                      hintText: 'Neue Aufgabe…',
                    ),
                    onSubmitted: (_) => _add(),
                  ),
                ),
                const SizedBox(width: 8),
                FilledButton(
                  onPressed: _add,
                  child: const Text('Add'),
                ),
              ],
            ),
          ),
          Expanded(
            child: todos.isEmpty
                ? const _EmptyState()
                : ListView.builder(
              itemCount: todos.length,
              itemBuilder: (context, i) {
                final t = todos[i];
                return Card(
                  child: ListTile(
                    leading: Checkbox(
                      value: t.done,
                      onChanged: (_) => _toggle(t.id),
                    ),
                    title: Text(
                      t.title,
                      style: TextStyle(
                        decoration: t.done ? TextDecoration.lineThrough : null,
                        color: t.done ? Colors.grey.shade600 : cozyText,
                        fontWeight: t.done ? FontWeight.w400 : FontWeight.w500,
                      ),
                    ),
                    trailing: IconButton(
                      icon: const Icon(Icons.delete),
                      onPressed: () => _remove(t.id),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

class _EmptyState extends StatelessWidget {
  const _EmptyState();

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20),
                boxShadow: const [
                  BoxShadow(
                    blurRadius: 8,
                    offset: Offset(0, 4),
                    color: Color(0x1A000000),
                  ),
                ],
              ),
              child: const Icon(Icons.checklist, size: 56, color: cozyPrimary),
            ),
            const SizedBox(height: 14),
            const Text(
              'Noch keine Aufgaben',
              style: TextStyle(
                color: cozyText,
                fontSize: 18,
                fontWeight: FontWeight.w600,
              ),
            ),
            const SizedBox(height: 6),
            const Text(
              'Schreib dir was Nettes auf',
              style: TextStyle(
                color: Color(0xFF7A6A64),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
