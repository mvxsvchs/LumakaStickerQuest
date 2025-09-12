import 'package:flutter/material.dart';
import 'todo.dart';
import 'todo_service.dart';

// -------- Light Palette (ans Bild angelehnt) --------
const peachPrimary = Color(0xFFF3B5A7);
const peachAccent  = Color(0xFFFBD5C9);
const lightBg      = Color(0xFFFFF7F3);
const cardBg       = Color(0xFFFFFFFF);
const textMain     = Color(0xFF4A3B35);
const textMuted    = Color(0xFF8E7D75);
const borderSoft   = Color(0xFFEBD9D2);

// Kategorien
const List<String> kCategories = [
  'Alle',
  'Allgemein',
  'Arbeit',
  'Schule',
  'Haushalt',
  'Fitness',
  'Sonstiges',
];

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Lumaka - Sticker Quest',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme(
          brightness: Brightness.light,
          primary: peachPrimary,
          onPrimary: Colors.white,
          secondary: peachAccent,
          onSecondary: textMain,
          surface: cardBg,
          onSurface: textMain,
          background: lightBg,
          onBackground: textMain,
          error: const Color(0xFFEF5350),
          onError: Colors.white,
          surfaceVariant: cardBg,
          onSurfaceVariant: textMain,
          outline: borderSoft,
          tertiary: peachAccent,
          onTertiary: textMain,
          scrim: Colors.black26,
        ),
        scaffoldBackgroundColor: lightBg,
        appBarTheme: const AppBarTheme(
          backgroundColor: peachPrimary,
          foregroundColor: Colors.white,
          centerTitle: false,
          elevation: 0,
        ),
        cardTheme: CardThemeData(
          color: cardBg,
          surfaceTintColor: Colors.transparent,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
            side: const BorderSide(color: borderSoft),
          ),
          elevation: 0,
          margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        ),
        textTheme: const TextTheme(
          bodyMedium: TextStyle(color: textMain),
          bodyLarge: TextStyle(color: textMain, fontWeight: FontWeight.w500),
          titleMedium: TextStyle(color: textMain, fontWeight: FontWeight.w600),
        ),
        inputDecorationTheme: InputDecorationTheme(
          filled: true,
          fillColor: cardBg,
          hintStyle: const TextStyle(color: textMuted),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: borderSoft),
          ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: borderSoft),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(color: peachPrimary, width: 2),
          ),
          contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
        ),
        checkboxTheme: CheckboxThemeData(
          fillColor: const WidgetStatePropertyAll(peachPrimary),
          checkColor: const WidgetStatePropertyAll(Colors.white),
          side: const BorderSide(color: textMuted, width: 1.2),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(6)),
        ),
        filledButtonTheme: FilledButtonThemeData(
          style: FilledButton.styleFrom(
            backgroundColor: peachPrimary,
            foregroundColor: Colors.white,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
          ),
        ),
        chipTheme: ChipThemeData(
          backgroundColor: cardBg,
          selectedColor: peachPrimary,
          disabledColor: cardBg,
          labelStyle: const TextStyle(color: textMain, fontWeight: FontWeight.w500),
          secondaryLabelStyle: const TextStyle(color: Colors.white),
          side: const BorderSide(color: borderSoft),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
        ),
        iconTheme: const IconThemeData(color: textMain),
        dividerColor: borderSoft,
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
  final svc = LocalTodoService();
  final controller = TextEditingController();
  List<Todo> todos = [];
  bool loading = true;

  String selectedFilter = 'Alle';
  String selectedAddCategory = 'Allgemein';

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
    final t = await svc.create(text, category: selectedAddCategory);
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

  // Sichtbare Liste (Filter)
  List<Todo> get _visibleTodos {
    if (selectedFilter == 'Alle') return todos;
    return todos.where((t) => t.category == selectedFilter).toList();
  }

  // Drag & Drop (sichtbar -> persistent)
  Future<void> _reorderVisible(int oldIndex, int newIndex) async {
    final visible = _visibleTodos;
    final movedId = visible[oldIndex].id;

    final tmp = List.of(visible);
    final item = tmp.removeAt(oldIndex);
    tmp.insert(newIndex > oldIndex ? newIndex - 1 : newIndex, item);

    final newPos = <String, int>{ for (var i = 0; i < tmp.length; i++) tmp[i].id: i };

    setState(() {
      todos.sort((a, b) {
        final ai = newPos[a.id];
        final bi = newPos[b.id];
        if (ai != null && bi != null) return ai.compareTo(bi);
        if (ai != null) return -1;
        if (bi != null) return 1;
        return 0;
      });
    });

    final realOld = todos.indexWhere((t) => t.id == movedId);
    final newIdxAfterSort = todos.indexWhere((t) => t.id == movedId);
    await svc.reorder(realOld, newIdxAfterSort);
  }

  int get _openCount => _visibleTodos.where((t) => !t.done).length;

  // --- Swipe to delete mit Undo ---
  Future<void> _dismissWithUndo(Todo t, int visibleIndex) async {
    final id = t.id;
    final realIndex = todos.indexWhere((x) => x.id == id);

    // UI direkt entfernen
    setState(() {
      todos = todos.where((x) => x.id != id).toList();
    });

    // persistent entfernen (und Objekt merken)
    final removed = await svc.removeAndReturn(id);

    // Snackbar mit Undo
    if (!mounted) return;
    ScaffoldMessenger.of(context).clearSnackBars();
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('Gelöscht: ${t.title}'),
        action: SnackBarAction(
          label: 'Undo',
          onPressed: () async {
            // UI wieder einfügen
            setState(() {
              todos.insert(realIndex, removed ?? t);
            });
            // persistent wiederherstellen
            await svc.restore(removed ?? t, realIndex);
          },
        ),
        duration: const Duration(seconds: 4),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    return Scaffold(
      appBar: AppBar(
        title: Text(_openCount == 0 ? 'Alles erledigt' : '${_openCount} offen'),
        actions: [
          if (_visibleTodos.any((t) => t.done))
            IconButton(
              tooltip: 'Erledigte löschen (sichtbar)',
              onPressed: () async {
                final doneIds = _visibleTodos.where((t) => t.done).map((t) => t.id).toList();
                for (final id in doneIds) {
                  await svc.remove(id);
                }
                setState(() {
                  todos = todos.where((t) => !doneIds.contains(t.id)).toList();
                });
              },
              icon: const Icon(Icons.delete_sweep),
            ),
        ],
      ),
      body: Column(
        children: [
          // Filter-Chips
          SizedBox(
            height: 54,
            child: ListView.separated(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              scrollDirection: Axis.horizontal,
              itemCount: kCategories.length,
              itemBuilder: (context, i) {
                final cat = kCategories[i];
                final selected = selectedFilter == cat;
                return ChoiceChip(
                  label: Text(cat),
                  selected: selected,
                  onSelected: (_) => setState(() => selectedFilter = cat),
                  labelStyle: TextStyle(
                    color: selected ? Colors.white : textMain,
                    fontWeight: FontWeight.w600,
                  ),
                  selectedColor: peachPrimary,
                  backgroundColor: cardBg,
                  side: const BorderSide(color: borderSoft),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                );
              },
              separatorBuilder: (_, __) => const SizedBox(width: 8),
            ),
          ),

          // Eingabe + Kategorie
          Padding(
            padding: const EdgeInsets.fromLTRB(12, 6, 12, 6),
            child: Row(
              children: [
                Container(
                  decoration: BoxDecoration(
                    color: cardBg,
                    border: Border.all(color: borderSoft),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  padding: const EdgeInsets.symmetric(horizontal: 10),
                  child: DropdownButton<String>(
                    value: selectedAddCategory,
                    underline: const SizedBox.shrink(),
                    borderRadius: BorderRadius.circular(12),
                    items: kCategories
                        .where((c) => c != 'Alle')
                        .map((c) => DropdownMenuItem(value: c, child: Text(c)))
                        .toList(),
                    onChanged: (v) => setState(() => selectedAddCategory = v ?? 'Allgemein'),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: TextField(
                    controller: controller,
                    autocorrect: false,
                    enableSuggestions: false,
                    textCapitalization: TextCapitalization.none,
                    keyboardType: TextInputType.text,
                    decoration: const InputDecoration(hintText: 'Neue Aufgabe…'),
                    onSubmitted: (_) => _add(),
                  ),
                ),
                const SizedBox(width: 8),
                FilledButton(onPressed: _add, child: const Text('Add')),
              ],
            ),
          ),

          // Liste: Reorderable + Swipe-to-Delete
          Expanded(
            child: _visibleTodos.isEmpty
                ? const _EmptyState()
                : ReorderableListView.builder(
              padding: const EdgeInsets.only(bottom: 12),
              itemCount: _visibleTodos.length,
              buildDefaultDragHandles: false,
              onReorder: (oldIndex, newIndex) => _reorderVisible(oldIndex, newIndex),
              itemBuilder: (context, i) {
                final t = _visibleTodos[i];
                return Dismissible(
                  key: ValueKey('dismiss-${t.id}'),
                  direction: DismissDirection.endToStart,
                  background: const _SwipeBg(),
                  onDismissed: (_) => _dismissWithUndo(t, i),
                  child: Card(
                    key: ValueKey(t.id),
                    child: ListTile(
                      leading: Checkbox(
                        value: t.done,
                        onChanged: (_) => _toggle(t.id),
                      ),
                      title: Text(
                        t.title,
                        style: TextStyle(
                          color: t.done ? textMuted : textMain,
                          fontWeight: t.done ? FontWeight.w400 : FontWeight.w500,
                          decoration: t.done ? TextDecoration.lineThrough : TextDecoration.none,
                          decorationColor: t.done ? textMuted : null,
                          decorationThickness: 2,
                        ),
                      ),
                      subtitle: Text(
                        t.category,
                        style: const TextStyle(color: textMuted, fontSize: 12),
                      ),
                      trailing: ReorderableDragStartListener(
                        index: i,
                        child: const Icon(Icons.drag_handle),
                      ),
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

class _SwipeBg extends StatelessWidget {
  const _SwipeBg();

  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.centerRight,
      padding: const EdgeInsets.symmetric(horizontal: 16),
      decoration: BoxDecoration(
        color: const Color(0xFFFF6B6B),
        borderRadius: BorderRadius.circular(16),
      ),
      child: const Icon(Icons.delete, color: Colors.white),
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
                color: cardBg,
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: borderSoft),
                boxShadow: const [
                  BoxShadow(
                    blurRadius: 8,
                    offset: Offset(0, 4),
                    color: Color(0x1A000000),
                  ),
                ],
              ),
              child: const Icon(Icons.checklist, size: 56, color: peachPrimary),
            ),
            const SizedBox(height: 14),
            const Text(
              'Noch keine Aufgaben',
              style: TextStyle(
                color: textMain,
                fontSize: 18,
                fontWeight: FontWeight.w600,
              ),
            ),
            const SizedBox(height: 6),
            const Text(
              'Wähle oben eine Kategorie',
              style: TextStyle(color: textMuted),
            ),
          ],
        ),
      ),
    );
  }
}
