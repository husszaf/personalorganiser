import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TodoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        public const val DATABASE_NAME = "todo.db"
        public const val DATABASE_VERSION = 1

        public const val TABLE_TODO = "todo"
        public const val COLUMN_ID = "_id"
        public const val COLUMN_TITLE = "title"
        public const val COLUMN_CHECKED = "checked"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_TODO ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CHECKED INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Implement this method if you need to upgrade the schema
    }

}
