import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'todo.dart';

abstract class TodoService {
  Future<List<Todo>> list();
  Future<Todo> create(String title, {String category = 'Allgemein'});
  Future<Todo> toggle(String id);
  Future<void> remove(String id);
}

class LocalTodoService implements TodoService {
  // neue Version, weil wir ein Feld ergänzt haben
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
        _cache = decoded.map<Todo>((e) {
          final m = Map<String, dynamic>.from(e as Map);
          return Todo.fromJson(m);
        }).toList();
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
}
