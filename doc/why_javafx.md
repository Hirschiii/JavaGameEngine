---
title: 'Wieso brauchen wir JavaFX'
author: "Niklas von Hirschfeld, Jan Knüpfer"
date: 2024-01-18
tags: []
enableToc: true
---

# Was ist JavaFX?

JavaFX bietet viele möglichkeiten mit Java ein grafische Oberfläche zu
Porgrammieren. Es bietet neben bindings für OpenGL eine recht hohe API um auf
diese zuzugreifen. Es ist ein Moderner nachfolger von dem veralteten Swing framework.

JavaFX nutzt einen *scenen Graf* um Objekte und Scenen zu sortieren und zugänglich.

![Grafische darstellung einer Stage](./assets/javafx_stage_graph.jpg)

Die Basis stellt eine *Stage* dar. Die ist vergleichbar mit einer Bühne eines
Theaterstückes, auf der Verschiedene Scenen und Schauspiele auftreten können.
Eine Scene beinhaltet die Schauspieler und die Requisieten. In Java wären diese klassisches
Objekte, welche durch einfache Formen oder Grafiken dargestellt werden. Diese
Objekte beinhalten die Grafische darstellung, position, variablen und
funktionen, welche für dieses Objekt wichtig sind.

## Was bietet JavaFX

Neben einer hohen und einfachen API zu OpenGL bietet JavaFX von Haus aus Module
für *Text*, *Knöpfe* und Bilder. 

```java
public class HelloApp extends Application {

    private Parent createContent() {
        return new StackPane(new Text("Hello World"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent(), 300, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

Diese Zeilen allein (plus import der Libary) reichen aus, um ein Fenster mit dem TExt "Hello World" zu erstellen.

## FXML

## Alternativen
