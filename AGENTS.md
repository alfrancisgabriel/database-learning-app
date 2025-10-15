# Agent Task Roadmap: DatabaseLearningApp

This document tracks the development progress of the DatabaseLearningApp, breaking down the project blueprint into actionable steps for the agent.

## Guiding Principles for the Agent

- **Verify Before Acting:** Before starting a task, the agent will first ensure it has the necessary tools and information.
- **Revert on Error:** If a step fails, the agent will undo any changes made during that step. This includes reverting code changes and un-ticking the corresponding checkbox in this document. This ensures the project remains in a consistent state.

## Phase 1: Core UI and Database Management

### 1.1. Database Management Screen

-   [ ] **Initial Run Experience:**
    -   [ ] Detect if any `.sqlite` databases exist in the app's file directory.
    -   [ ] If not, display a "Create Your First Database" dialog upon launch.
    -   [ ] Implement logic to create a new database file based on user input from the dialog.
    -   [ ] On successful creation, navigate to the `MainQueryScreen`.
-   [ ] **Database List:**
    -   [ ] Implement a `LazyColumn` to display all found `.sqlite` files.
    -   [ ] Create a list item composable to show the database name and last modified date.
    -   [ ] Implement navigation from a list item to the `MainQueryScreen` for the selected database.
-   [ ] **Create New Database FAB:**
    -   [ ] Add a Material Design Floating Action Button (FAB) to the screen.
    -   [ ] Wire the FAB to show the "Create New Database" dialog.

## Phase 2: Query Interface

### 2.1. Main Query Screen - UI Structure

-   [ ] **Split-Pane Layout:** Create the basic vertical split-screen layout for the editor and results viewer.
-   [ ] **SQL Editor:**
    -   [ ] Implement a `BasicTextField` for multiline SQL input.
    -   [ ] Add a "Run" `IconButton` next to the editor.
-   [ ] **Results Viewer:**
    -   [ ] Implement the `TabRow` with "Data" and "Schema" tabs.
    -   [ ] Create placeholder content for each tab.
-   [ ] **Feedback Bar:**
    -   [ ] Add a `SnackbarHost` to the screen's scaffold.
    -   [ ] Implement a `Text` composable area for displaying persistent error messages.

### 2.2. State Management

-   [ ] Create a `MainQueryViewModel` to hold UI state for the query screen (editor text, results, schema, etc.).
-   [ ] Connect the `ViewModel` to the `MainQueryScreen` composables.

## Phase 3: Core Functionality

### 3.1. Database Connection Management

-   [ ] **`DatabaseManager`:**
    -   [ ] Create a singleton `DatabaseManager` to handle `SQLiteOpenHelper` instances.
    -   [ ] Implement a method to get or create a `SQLiteOpenHelper` for a given database name.
-   [ ] **Lifecycle Management:**
    -   [ ] Ensure database connections are opened in `onResume` or a similar lifecycle-aware manner.
    -   [ ] Ensure database connections are properly closed in `onPause` to prevent leaks.

### 3.2. Query Execution Logic

-   [ ] **`Run` Button Logic:**
    -   [ ] In the `ViewModel`, create a function to execute the query from the editor text.
    -   [ ] Differentiate between `SELECT` and other statements (DML/DDL).
-   [ ] **`SELECT` Query Handling:**
    -   [ ] Use `db.rawQuery()` to execute the query.
    -   [ ] Process the returned `Cursor` into a data structure suitable for the UI (e.g., a list of lists/maps).
    -   [ ] Ensure the `Cursor` is always closed in a `finally` block.
    -   [ ] Update the `ViewModel` state with the results.
-   [ ] **DML/DDL Query Handling:**
    -   [ ] Use `db.execSQL()` to execute the statement.
    -   [ ] Show success/error feedback using the `SnackbarHost`.
    -   [ ] Trigger a refresh of the schema information after successful execution.

### 3.3. Schema Discovery and Display

-   [ ] **Schema Fetching:**
    -   [ ] Implement a function to query `sqlite_master` to get all table names.
    -   [ ] For each table, use `PRAGMA table_info(tableName)` to get column details.
    -   [ ] Store this schema information in the `ViewModel`.
-   [ ] **Schema Tab UI:**
    -   [ ] Implement the `LazyColumn` to display a list of tables.
    -   [ ] Make each table item expandable to show its columns and data types.

### 3.4. Results Data Table UI

-   [ ] **Dynamic Table:**
    -   [ ] Use the query results from the `ViewModel` to populate the Data tab.
    -   [ ] Implement the `LazyColumn`/`LazyRow` combination for a scrollable data grid.
    -   [ ] Implement sticky headers to show column names.

## Phase 4: Polish and Enhancements (Future Work)

-   [ ] **Syntax Highlighting:**
    -   [ ] Implement logic using `AnnotatedString` and `SpanStyle` to parse and color SQL keywords, strings, and numbers in the editor.
-   [ ] **Sample Database:**
    -   [ ] Add a menu option to create and populate a sample database.
-   [ ] **Query History:**
    -   [ ] Design and implement a mechanism to save and retrieve past queries.
-   [ ] **CSV Export:**
    -   [ ] Add functionality to export `SELECT` results to a CSV file.
-   [ ] **Editor Autocomplete:**
    -   [ ] Investigate and implement basic autocomplete for SQL keywords or schema names.
-   [ ] **Sortable Data Table:**
    -   [ ] Add logic to the results table headers to allow sorting the data by that column.
