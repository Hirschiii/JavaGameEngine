package main;

import javafx.application.Application;

public class GameWindow extends Application {
	@Override
	public void start(Stage stage) {
		try {
			Scene scene = new Scene(new StackPane(), 640, 480);
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main() {
		launch();
	}

}
