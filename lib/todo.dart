class Todo {
  String id;
  String title;
  bool done;
  String category;

  Todo({
    required this.id,
    required this.title,
    this.done = false,
    this.category = 'Allgemein',
  });

  factory Todo.fromJson(Map<String, dynamic> json) {
    return Todo(
      id: json['id'] as String,
      title: json['title'] as String,
      done: json['done'] as bool,
      category: (json['category'] as String?) ?? 'Allgemein',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'done': done,
      'category': category,
    };
  }
}
