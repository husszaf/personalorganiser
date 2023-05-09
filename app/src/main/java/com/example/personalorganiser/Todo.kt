//To-Do data class for the database
data class Todo(
    val id: Long = 0,
    val title: String,
    var isChecked: Boolean = false
) {
    constructor(title: String) : this(0, title, false)
}
