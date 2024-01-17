# Makefile for JavaFX application

# Define variables
JAVAC = javac
JAVA = java
JAVAFX_PATH = ./lib/javafx/

# Define the source files
SOURCES = $(wildcard ./src/java/*.java)

# Define the class files
CLASSES = $(SOURCES:.java=.class)


# Rule to create .class files from .java files
all:
	$(JAVAC) --module-path $(JAVAFX_PATH) --add-modules javafx.controls,javafx.fxml -d bin ./src/java/*.java

# Rule to run the application
run:
	$(JAVA) --module-path $(JAVAFX_PATH) --add-modules javafx.controls,javafx.fxml -cp bin main.Main

# Rule to clean the workspace
clean:
	rm -f *.class

# Replace 'Main' with the name of your main class
# Replace 'path/to/javafx-sdk/lib' with the actual path to the JavaFX SDK library

