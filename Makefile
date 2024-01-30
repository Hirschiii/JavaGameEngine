# Makefile for JavaFX application

# Define variables
# Determine the operating system
UNAME := $(shell uname)

ifeq ($(UNAME),Darwin)
	JAVAC = javac
	JAVA = java -XstartOnFirstThread
	LIBARYS = .:./lib/*
else ifeq ($(UNAME),Linux)
	JAVAC = javac
	JAVA = java
	LIBARYS = .:./lib/*
else ifeq ($(OS),Windows_NT)
	JAVAC = javac
	JAVA = java
	LIBARYS = .:./lib/*
else
    $(error Unsupported operating system: $(UNAME))
endif
# Determine the operating system

BINDIR := bin
JAVA_FILES := $(shell find ./src/main/java/ -name "*.java")


# Rule to create .class files from .java files
all:
	$(JAVAC) -cp $(BINDIR):$(LIBARYS) -d bin $(JAVA_FILES)

# Rule to run the application
run:
	$(JAVA) -cp $(BINDIR):$(LIBARYS) game.App

docs:
	javadoc -d doc/javadoc -sourcepath src/main/java -classpath "lib/*" src/main/java/gradle_lwjgl/**/*.java
# Rule to clean the workspace
# clean:
# 	rm -f *.class

# .PHONY: all
# all: $(BINDIR) $(CLASSES)
# 	$(JAVA) -cp $(BINDIR):$(LIBARYS) Main
#
# # Regel, um das Ausgabeverzeichnis zu erstellen
# $(BINDIR):
# 	mkdir -p $(BINDIR)
#
# $(BINDIR)/%.class: %.java | $(BINDIR)
# 	$(JAVAC) -d $(BINDIR) -cp $(LIBARYS) $<



# Rule to create .class files from .java files
# all:
# 	# $(JAVAC) -cp .:./lib/*:./native/macos/* HelloWorld.java
# 	$(JAVAC) $(ATTRIBUTES) HelloWorld.java

# Rule to run the application
# run:
# 	# $(JAVA) -Djava.library.path=native/macos -XstartOnFirstThread -cp .:./lib/*:./native/macos/* HelloWorld
# 	$(JAVA) $(ATTRIBUTES) HelloWorld
# Rule to clean the workspace

clean:
	# rm -f *.class
	rm -rf $(BINDIR)

# Replace 'Main' with the name of your main class
# Replace 'path/to/javafx-sdk/lib' with the actual path to the JavaFX SDK library

