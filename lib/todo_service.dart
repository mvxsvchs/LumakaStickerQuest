import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'todo.dart';

abstract class TodoService {
  Future<List<Todo>> list();
  Future<Todo> create(String title);
  Future<Todo> toggle(String id);
  Future<void> remove(String id);
}

class LocalTodoService implements TodoService {
  static const _key = 'todos_v2';
  List<Todo> _cache = [];

  Future<SharedPreferences> get _prefs async => SharedPreferences.getInstance();

  @override
  Future<List<Todo>> list() async {
    final p = await _prefs;
    final raw = p.getString(_key);
    print('[prefs:get] $_key = $raw'); // DEBUG-Ausgabe
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
    } catch (e) {
      print('[prefs:parse:error] $e');
      _cache = [];
    }
    return _cache;
  }

  Future<void> _save() async {
    final p = await _prefs;
    final data = jsonEncode(_cache.map((e) => e.toJson()).toList());
    final ok = await p.setString(_key, data);
    print('[prefs:set] ok=$ok $_key = $data'); // DEBUG-Ausgabe
  }

  String _newId() => DateTime.now().microsecondsSinceEpoch.toString();

  @override
  Future<Todo> create(String title) async {
    final t = Todo(id: _newId(), title: title.trim(), done: false);
    _cache = [..._cache, t];
    await _save();
    return t;
  }

  @override
  Future<Todo> toggle(String id) async {
    _cache = [
      for (final t in _cache)
        if (t.id == id) Todo(id: t.id, title: t.title, done: !t.done) else t
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
