package ru.ved.ict;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ved.ict.file_utils.ImageFinder;
import ru.ved.ict.file_utils.ImageFinderImpl;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * JavaFX Application
 */
public class App extends Application {

	private class ImageReadTask extends Task<Path[]> {

		private final Path startFolder;
		final ImageFinder finder = new ImageFinderImpl();

		public ImageReadTask(final Path startFolder) {
			super();
			this.startFolder = startFolder;
		}

		@Override
		protected Path[] call() throws Exception {
			updateMessage("Start finding images into " + startFolder.toString());
			return finder.getImagePaths(startFolder, checkSubfolder.isSelected());
		}

	}

	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private static final Label status = new Label();

	public static void main(final String[] args) {
		launch();
	}

	private final CheckBox checkSubfolder = new CheckBox("Look into subfolders");

	private final EventHandler<ActionEvent> processFolder = event -> {
		event.consume();
		final var chooser = new DirectoryChooser();
		chooser.setTitle("Select folder with images");
		final var folder = chooser.showDialog(null);
		if (folder == null) {
			return;
		}
		final var task = new ImageReadTask(folder.toPath());
		status.textProperty().bind(task.messageProperty());
		final ImageFinder finder = new ImageFinderImpl();
		final var imagePaths = finder.getImagePaths(folder.toPath(), checkSubfolder.isSelected());
		LOG.debug("{} images found", imagePaths.length);
	};

	@Override
	public void start(final Stage stage) {
		final var scene = getScene();
		stage.setScene(scene);
		stage.setTitle("Image Collection ToolSuite");
		stage.show();
	}

	private Scene getScene() {
		final var javafxVersion = SystemInfo.javafxVersion();
		final var javaVersion = SystemInfo.javaVersion();
		final var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
		checkSubfolder.setSelected(true);
		final var selectButton = new Button("Select folder with images");
		selectButton.setOnAction(processFolder);
		final var vBox = new VBox(10, label, checkSubfolder, selectButton, status);
		return new Scene(vBox, 1024, 768);
	}

}