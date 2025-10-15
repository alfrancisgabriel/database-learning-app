# Agent Task Roadmap: DatabaseLearningApp

This document tracks the development progress of the DatabaseLearningApp, breaking down the project blueprint into actionable steps for the agent.

## Guiding Principles for the Agent

- **Verify Before Acting:** Before starting a task, the agent will first ensure it has the necessary tools and information.
- **Revert on Error:** If a step fails, the agent will undo any changes made during that step. This includes reverting code changes and un-ticking the corresponding checkbox in this document. This ensures the project remains in a consistent state.

## Phase 1: Core UI and Database Management

### 1.1. Database Management Screen

-   [x] **Initial Run Experience:**
    -   [x] Detect if any `.sqlite` databases exist in the app's file directory.
    -   [x] If not, display a "Create Your First Database" dialog upon launch.
    -   [x] Implement logic to create a new database file based on user input from the dialog.
    -   [x] On successful creation, navigate to the `MainQueryScreen`.
-   [x] **Database List:**
    -   [x] Implement a `LazyColumn` to display all found `.sqlite` files.
    -   [x] Create a list item composable to show the database name and last modified date.
    -   [x] Implement navigation from a list item to the `MainQueryScreen` for the selected database.
-   [x] **Create New Database FAB:**
    -   [x] Add a Material Design Floating Action Button (FAB) to the screen.
    -   [x] Wire the FAB to show the "Create New Database" dialog.

## Phase 2: Query Interface

### 2.1. Main Query Screen - UI Structure

-   [x] **Split-Pane Layout:** Create the basic vertical split-screen layout for the editor and results viewer.
-   [x] **SQL Editor:**
    -   [x] Implement a `BasicTextField` for multiline SQL input.
    -   [x] Add a "Run" `IconButton` next to the editor.
-   [x] **Results Viewer:**
    -   [x] Implement the `TabRow` with "Data" and "Schema" tabs.
    -   [x] Create placeholder content for each tab.
-   [x] **Feedback Bar:**
    -   [x] Add a `SnackbarHost` to the screen's scaffold.
    -   [x] Implement a `Text` composable area for displaying persistent error messages.

### 2.2. State Management

-   [x] Create a `MainQueryViewModel` to hold UI state for the query screen (editor text, results, schema, etc.).
-   [x] Connect the `ViewModel` to the `MainQueryScreen` composables.

## Phase 3: Core Functionality

### 3.1. Database Connection Management

-   [x] **`DatabaseManager`:**
    -   [x] Create a singleton `DatabaseManager` to handle `SQLiteOpenHelper` instances.
    -   [x] Implement a method to get or create a `SQLiteOpenHelper` for a given database name.
-   [x] **Lifecycle Management:**
    -   [x] Ensure database connections are opened in `onResume` or a similar lifecycle-aware manner.
    -   [x] Ensure database connections are properly closed in `onPause` to prevent leaks.

### 3.2. Query Execution Logic

-   [x] **`Run` Button Logic:**
    -   [x] In the `ViewModel`, create a function to execute the query from the editor text.
    -   [x] Differentiate between `SELECT` and other statements (DML/DDL).
-   [x] **`SELECT` Query Handling:**
    -   [x] Use `db.rawQuery()` to execute the query.
    -   [x] Process the returned `Cursor` into a data structure suitable for the UI (e.g., a list of lists/maps).
    -   [x] Ensure the `Cursor` is always closed in a `finally` block.
    -   [x] Update the `ViewModel` state with the results.
-   [x] **DML/DDL Query Handling:**
    -   [x] Use `db.execSQL()` to execute the statement.
    -   [x] Show success/error feedback using the `SnackbarHost`.
    -   [x] Trigger a refresh of the schema information after successful execution.

### 3.3. Schema Discovery and Display

-   [x] **Schema Fetching:**
    -   [x] Implement a function to query `sqlite_master` to get all table names.
    -   [x] For each table, use `PRAGMA table_info(tableName)` to get column details.
    -   [x] Store this schema information in the `ViewModel`.
-   [x] **Schema Tab UI:**
    -   [x] Implement the `LazyColumn` to display a list of tables.
    -   [x] Make each table item expandable to show its columns and data types.

### 3.4. Results Data Table UI

-   [x] **Dynamic Table:**
    -   [x] Use the query results from the `ViewModel` to populate the Data tab.
    -   [x] Implement the `LazyColumn`/`LazyRow` combination for a scrollable data grid.
    -   [x] Implement sticky headers to show column names.

## Phase 4: Polish and Enhancements (Future Work)

-   [x] **Syntax Highlighting:**
    -   [x] Implement logic using `AnnotatedString` and `SpanStyle` to parse and color SQL keywords, strings, and numbers in the editor.
-   [x] **Sample Database:**
    -   [x] Add a menu option to create and populate a sample database.
-   [ ] **Query History:**
    -   [ ] Design and implement a mechanism to save and retrieve past queries.
-   [ ] **CSV Export:**
    -   [ ] Add functionality to export `SELECT` results to a CSV file.
-   [ ] **Editor Autocomplete:**
    -   [ ] Investigate and implement basic autocomplete for SQL keywords or schema names.
-   [ ] **Sortable Data Table:**
    -   [ ] Add logic to the results table headers to allow sorting the data by that column.
