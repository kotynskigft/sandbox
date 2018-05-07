# What is it?
Simple database-based project with spaghetti code

# How to run?
mvnw compile exec:java

# What functionality is in the app?
It is an appointments organizer.
1) load from database
2) add new appointment
3) remove one of the existing appointments
4) update existing appointment
5) save to database, close
6) open, load fro mdatabase
7) changes made in steps 2-4 should be loaded from database

# Task
Convert legacy application to 3 somehow separated layers
1) View
2) Logic
3) Database

View should be aware of Logic
Logic should be aware of Database
