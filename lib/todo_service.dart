import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'todo.dart';

abstract class TodoService {
  Future<List<Todo>> list();
  Future<Todo> create(String title, {String category = 'Allgemein'});
  Future<Todo> toggle(String id);
  Future<void> remove(String id);
  Future<void> reorder(int oldIndex, int newIndex);

  // Neu für Swipe+Undo
  Future<Todo?> removeAndReturn(String id);
  Future<void> restore(Todo todo, int index);
}

class LocalTodoService implements TodoService {
  // v3 wegen Kategorie + Reihenfolge
  static const _key = 'todos_v3';
  List<Todo> _cache = [];

  Future<SharedPreferences> get _prefs async => SharedPreferences.getInstance();

  @override
  Future<List<Todo>> list() async {
    final p = await _prefs;
    final raw = p.getString(_key);
    if (raw == null) {
      _cache = [];
      return _cache;
    }
    try {
      final decoded = jsonDecode(raw);
      if (decoded is List) {
        _cache = decoded
            .map<Todo>((e) => Todo.fromJson(Map<String, dynamic>.from(e as Map)))
            .toList();
      } else {
        _cache = [];
      }
    } catch (_) {
      _cache = [];
    }
    return _cache;
  }

  Future<void> _save() async {
    final p = await _prefs;
    final data = jsonEncode(_cache.map((e) => e.toJson()).toList());
    await p.setString(_key, data);
  }

  String _newId() => DateTime.now().microsecondsSinceEpoch.toString();

  @override
  Future<Todo> create(String title, {String category = 'Allgemein'}) async {
    final t = Todo(id: _newId(), title: title.trim(), done: false, category: category);
    _cache = [..._cache, t];
    await _save();
    return t;
  }

  @override
  Future<Todo> toggle(String id) async {
    _cache = [
      for (final t in _cache)
        if (t.id == id) Todo(id: t.id, title: t.title, done: !t.done, category: t.category) else t
    ];
    await _save();
    return _cache.firstWhere((t) => t.id == id);
  }

  @override
  Future<void> remove(String id) async {
    _cache = _cache.where((t) => t.id != id).toList();
    await _save();
  }

  // ---- Neu: für Undo ----
  @override
  Future<Todo?> removeAndReturn(String id) async {
    final idx = _cache.indexWhere((t) => t.id == id);
    if (idx == -1) return null;
    final removed = _cache.removeAt(idx);
    await _save();
    return removed;
  }

  @override
  Future<void> restore(Todo todo, int index) async {
    if (index < 0) index = 0;
    if (index > _cache.length) index = _cache.length;
    _cache.insert(index, todo);
    await _save();
  }

  @override
  Future<void> reorder(int oldIndex, int newIndex) async {
    if (oldIndex < 0 || oldIndex >= _cache.length) return;
    if (newIndex > _cache.length) newIndex = _cache.length;
    if (newIndex > oldIndex) newIndex -= 1;
    final item = _cache.removeAt(oldIndex);
    _cache.insert(newIndex, item);
    await _save();
  }
}
