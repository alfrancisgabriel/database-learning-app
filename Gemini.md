# Polished Android Studio Project Blueprint: DatabaseLearningApp

## 1. Project Overview and Goal

- **App Name:** DatabaseLearningApp
- **Target Platform:** Android (Kotlin, Jetpack Compose)
- **Database Engine:** SQLite (using Android's native `android.database.sqlite` APIs for raw SQL execution).
- **Goal:** To provide a clean, intuitive, and highly functional mobile environment for users to learn and practice raw SQL queries against a local SQLite database. The user experience should be smooth, educational, and forgiving.
- **UI/UX:** Modern Material Design 3, with a dark mode option. The interface should feel like a professional developer tool, but be simple enough for beginners.

## 2. Core Screens and UI Components (Jetpack Compose)

### A. Database Management Screen

This is the entry point of the app, handling database creation and selection.

**Initial Run Flow:**
1.  If no databases exist in the app's private storage (`/data/data/com.stubborndeveloper.databaselearningapp/files`), a "Create Your First Database" dialog appears.
2.  The user provides a database name (e.g., `learning_db`). The `.sqlite` extension will be appended automatically.
3.  The app creates the database file in `Context.getFilesDir()`.
4.  The app navigates to the `MainQueryScreen`, passing the new database name.

**Subsequent Runs Flow:**
1.  The screen displays a list of all existing `.sqlite` files using a `LazyColumn`.
2.  Each item shows the database name and perhaps the last modified date.
3.  Tapping a database navigates to the `MainQueryScreen` for that database.
4.  A Floating Action Button (FAB) with a '+' icon allows users to trigger the "Create New Database" dialog.

### B. Main Query Screen

A split-screen UI for writing queries and viewing results.

**Components:**

1.  **SQL Editor (Top Pane):**
    -   A `BasicTextField` composable, customized for code editing.
    -   **Syntax Highlighting:** Implement using `AnnotatedString` and `SpanStyle` to color keywords (`SELECT`, `FROM`, `WHERE`, etc.), strings, and numbers differently.
    -   A prominent "Run" `IconButton` to execute the query.

2.  **Results Viewer (Bottom Pane):**
    -   A `TabRow` to switch between "Data" and "Schema" views.
    -   **Data Tab:**
        -   Displays `SELECT` query results in a table.
        -   Implementation: A `LazyColumn` for rows, with each row being a `LazyRow` of cells. This ensures good performance for large result sets.
        -   Column headers should be displayed and sticky at the top.
    -   **Schema Tab:**
        -   Displays the database structure.
        -   Implementation: A `LazyColumn` showing a list of tables. Each item can be expanded to show its columns with their data types (`TEXT`, `INTEGER`, etc.).

3.  **Feedback Bar:**
    -   A `SnackbarHost` at the bottom of the screen to show transient feedback messages (e.g., "Query executed successfully," "1 row affected.").
    -   For persistent errors (e.g., "Error: no such table"), display the message in a distinct, colored `Text` composable below the editor.

## 3. Functional Requirements & Implementation Details

-   **Database Helper:**
    -   Create a custom `SQLiteOpenHelper` class for each unique database file the user creates. This is unconventional but necessary to manage multiple, dynamically-named databases.
    -   A singleton manager class (`DatabaseManager`) could be responsible for creating, opening, and closing these `SQLiteOpenHelper` instances.
    -   Connections must be managed carefully and closed in `onPause` or when the user navigates away from the query screen to prevent resource leaks.

-   **Query Execution:**
    -   On "Run", parse the text from the editor. Use a simple `trim()` and check if it starts with `SELECT` (case-insensitive) to differentiate query types.
    -   **`SELECT` queries:** Use `db.rawQuery(sql, null)`. The returned `Cursor` is used to populate the Data Tab. Ensure the cursor is closed in a `finally` block.
    -   **Other queries (DML/DDL):** Use `db.execSQL(sql)`.
    -   After a successful DML/DDL query (e.g., `CREATE TABLE`, `ALTER TABLE`), trigger a refresh of the Schema Tab.

-   **Schema Discovery:**
    -   To populate the Schema Tab, query the `sqlite_master` table: `SELECT name FROM sqlite_master WHERE type='table';`
    -   For each table, get its schema using `PRAGMA table_info(tableName);`. This returns column names, data types, and other constraints.

-   **State Management:**
    -   Use a Jetpack `ViewModel` for the `MainQueryScreen` to hold the editor text, query results, schema information, and selected tab. This ensures state survives configuration changes like screen rotation.

-   **File Management:**
    -   Use `Context.getFilesDir()` to get the private directory for storing database files.
    -   Implement functions to list all files ending with `.sqlite` for the Database Management Screen.

## 4. Enhancements & Future Work

-   **Sample Database:** Include a feature to create a pre-populated sample database (e.g., with "Customers" and "Orders" tables) so users can start querying immediately. This could be triggered from a menu option.
-   **Query History:** Save executed queries locally (perhaps in another SQLite database or a simple file) and provide a way for users to view and re-run them.
-   **CSV Export:** Allow users to export the results of a `SELECT` query to a CSV file.
-   **Editor Autocomplete:** A more advanced editor could suggest SQL keywords or table/column names from the current schema.
-   **Sortable Data Table:** Enhance the results table to allow sorting by tapping on column headers.
