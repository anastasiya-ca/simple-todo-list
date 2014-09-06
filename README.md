Simple Todo List - android app
================

Simple Todo List is a basic android app built as a prework for the Android bootcamp. The project was described on 
[Android Bootcamp Pre-work](https://gist.github.com/nesquena/843228e83fdc4f5ddc4e)

The app allows building a todo list and basic  todo items management functionality including adding new todo, marking todo as completed, editing and deleting an existing todo. The built app is an extension of basic app requested and includes the following functionality:

* [x]	User can **view the todo items list**. Active and completed todos are displayed as opposed to deleted
* [x]	User can **mark a todo item as completed/not completed**. Completed todo is formated differently, remains in the list and can be eddited or deleted.
* [x]	User can **add a new todo** item to the list by specify todo name and clicking “Add” button. If user tries to save a todo with blank name then message (toast) is displayed and nothing is saved
* [x]	User can **remove existing todo** item from the list by doing a long click on the relevant item in the list. Todo is marked as deleted in DB and cleared from the list. User can not restore deleted todo.
* [x]	User can **edit a todo** item name. Todo list item click bring Edit Todo Name dialog. So user can edit and then cancel or save changes to the todo name. Saving blank name is not allowed. Clearing the name to blank will bring error message and Save button disabled.

The app was tested on HTC One (Android 4.1.2) and on AVDs.

Walkthrough of implemeted user stories:


![Video Walkthrough](simple_todo_app_demo.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).


The app is different from the basic spec in the following aspects:
-	Some basic theme changes (color, font, alert dialog style, button and checkbox styles)
-	Todo item can be marked as completed/ not completed
-	AlertDialog fragment is used for todo editing in place of the second activity
-	Some basic validation and messaging is implemented (not blank name)
-	Custom ArrayList Adapter is used to populate todo items ListView 
-	SQLite is used for persisting data

Points to consider for future development:
-	Setting priority (star on/off)
-	Labels to categorize TODOs

