package Cameo;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import Cameo.Model.ApplicationModel;
import Cameo.Model.Document;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.scene.input.*;

// | container : border setup placing lineNumbers on the left and textArea in the center. 
//	 | scrollPane : Scrolls the innerContainer
//	 	| lineNumbers : displays line numbers using Labels by counting up textArea linebreaks.
//	 | textArea : displays document text.

public class DocumentTab extends Tab
{
	ApplicationModel model;
	Document document;
	TextArea textArea = new TextArea();
	VBox lineNumbers = new VBox();
	
	public DocumentTab(DocumentArea parent, Document document)
	{
		this.model = parent.parent.model;
		
		this.getStyleClass().add("document-text");

		this.document = document;
		
		// Set tab title
		updateTitle();
		document.requiresSave.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				updateTitle();
			}
		});
		
		document.filename.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				updateTitle();
			}
		});

		textArea.setText(document.content);
		updateLineNumbers();
		
		// Handle adding and removing line number rows when the file contents are changed.
		textArea.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{	
				document.content = newValue;
				updateLineNumbers();
				document.requiresSave.set(true);
			}
		});

		double lineNumbersTopPadding = 4;
		lineNumbers.getStyleClass().add("line-numbers-box");
		lineNumbers.setAlignment(Pos.TOP_RIGHT);
		lineNumbers.setOnScroll(new EventHandler<ScrollEvent>()
		{
			@Override
			public void handle(ScrollEvent e)
			{
				e.consume();
			}
		});
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(lineNumbers);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		
		// Mirror lineNumber's scroll pane with the text area scroll.
		textArea.scrollTopProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Text textContent = (Text)textArea.lookup(".text");
				scrollPane.setVmin(0);
				scrollPane.setVmax(1 + ((lineNumbersTopPadding * 2) / lineNumbers.getHeight()));
				
				scrollPane.setVvalue((double)newValue / (textContent.getBoundsInLocal().getHeight() - textArea.getHeight()));
			}
		});
		
		setOnCloseRequest(new EventHandler<Event>()
		{
			@Override
			public void handle(Event e)
			{
				if (document.requiresSave.get())
				{
					if (parent.parent.showRequestSaveDialog(document, null) == false)
					{
						e.consume();
					}
				}
			}
		});
		
		BorderPane container = new BorderPane();
		container.getStyleClass().add("document-tab");		
		container.setLeft(scrollPane);
		container.setCenter(textArea);
		
		model.preferences.fontSize.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				String style = "-fx-font-size: " + model.preferences.fontSize.get() + "px;";
				lineNumbers.setStyle(style);
				textArea.setStyle(style);
			}	
		});
		
		
		setContent(container);
	}
	
	private void updateLineNumbers()
	{
		int newLineCount = document.content.length() - document.content.replace("\n", "").length() + 1;
		while (lineNumbers.getChildren().size() != newLineCount)
		{
			int currentLineCount = lineNumbers.getChildren().size();
			if (currentLineCount < newLineCount)
			{
				Label temp = new Label(currentLineCount + 1 + "");
				lineNumbers.getChildren().add(temp);
			}
			else
			{
				lineNumbers.getChildren().remove(currentLineCount - 1);
			}
		}
	}
	
	private void updateTitle()
	{
		if (document.requiresSave.get())
		{
			setText("*" + document.filename.get());
		}
		else
		{
			setText(document.filename.get());
		}
	}
	
	public String getDocumentId()
	{
		return document.id;
	}
	
	public String getDocumentPath()
	{
		return document.fullPath.get();
	}
	
	public String getDocumentFilename()
	{
		return document.filename.get();
	}
	
	public String getDocumentText()
	{
		return document.content;
	}
	
	public void undo()
	{
		textArea.undo();
	}
	
	public void redo()
	{
		textArea.redo();
	}
}
