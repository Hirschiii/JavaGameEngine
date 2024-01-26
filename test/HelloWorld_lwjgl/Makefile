# Makefile for JavaFX application

# Define variables
# Determine the operating system
UNAME := $(shell uname)

ifeq ($(UNAME),Darwin)
	JAVAC = javac
	JAVA = java -XstartOnFirstThread
	JAVAFX_PATH = ./lib/javafx/
	LIBARYS = .:./lib/*
else ifeq ($(UNAME),Linux)
	JAVAC = javac
	JAVA = java
	JAVAFX_PATH = ./lib/javafx/
	LIBARYS = .:./lib/*
else ifeq ($(OS),Windows_NT)
	JAVAC = javac
	JAVA = java
	JAVAFX_PATH = ./lib/javafx/
	LIBARYS = .:./lib/*
else
    $(error Unsupported operating system: $(UNAME))
endif
# Determine the operating system

# Define the source files
SOURCES = $(wildcard ./src/java/*.java)

# Define the class files
CLASSES = $(SOURCES:.java=.class)

ATTRIBUTES = -cp $(LIBARYS)


# Rule to create .class files from .java files
all:
	# $(JAVAC) -cp .:./lib/*:./native/macos/* HelloWorld.java
	$(JAVAC) $(ATTRIBUTES) HelloWorld.java

# Rule to run the application
run:
	# $(JAVA) -Djava.library.path=native/macos -XstartOnFirstThread -cp .:./lib/*:./native/macos/* HelloWorld
	$(JAVA) $(ATTRIBUTES) HelloWorld
# Rule to clean the workspace
clean:
	rm -f *.class

# Replace 'Main' with the name of your main class
# Replace 'path/to/javafx-sdk/lib' with the actual path to the JavaFX SDK library

