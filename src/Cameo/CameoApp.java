package Cameo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import Cameo.Model.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.collections.ListChangeListener;

public class CameoApp extends Application
{
	public static void main(String[] args)
	{
		CameoApp.launch(args);
	}

	ApplicationModel model = new ApplicationModel();
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{		
		model.documents.addListener(new ListChangeListener<Document>() {

			@Override
			public void onChanged(Change<? extends Document> change)
			{
				while (change.next())
				{
					if (change.wasRemoved())
					{
						Document removed = change.getRemoved().get(0);
						System.out.println(removed.fileName);
					}
				}
			}
			
		});

		BorderPane root = new BorderPane();
		
		primaryStage.setTitle("Cameo");
		primaryStage.setMaximized(false);
		
		Scene scene = new Scene(root, 600, 600);
		primaryStage.setScene(scene);
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, hotkeyHandler);

		CameoMenuBar menuBar = new CameoMenuBar();
		
		root.setTop(menuBar);
		
		DocumentArea documentArea = new DocumentArea(model);
		
		root.setCenter(documentArea);
		
		Label statusLabel = new Label();
		statusLabel.textProperty().bind(model.statusMessage);
		
		root.setBottom(statusLabel);
				
		primaryStage.show();
	}
	
	final KeyCombination newFileHotkey = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
	
	EventHandler<KeyEvent> hotkeyHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (newFileHotkey.match(event)) {
                System.out.println("Ctrl+N pressed");
                openDocument();
            }
        }
    };
    
    void openDocument()
    {
    	openDocument(null);
    }

	void openDocument(File file)
	{
		Document temp = new Document();
		if (file == null)
		{
			// If no file was specified let's make a new document.
			temp = new Document();
			temp.fileName = "Untitled";
			temp.fullPath = null;
			temp.content = "";
		}
		else
		{
			// Load file into document.
			temp = loadDocument(file);
		}
		model.documents.add(temp);	
	}
	
	Document loadDocument(File file)
	{
		Document output = new Document();
		output.fileName = file.getName();
		output.fullPath = file.getPath();
		try
		{
			output.content = String.join("\n", Files.readAllLines(file.toPath()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return output;
	}
}
