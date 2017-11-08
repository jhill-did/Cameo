package Cameo;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import Cameo.Model.Document;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.*;

// | container : border setup placing lineNumbers on the left and textArea in the center. 
//	 | scrollPane : Scrolls the innerContainer
//	 	| lineNumbers : displays line numbers using Labels by counting up textArea linebreaks.
//	 | textArea : displays document text.

public class DocumentTab extends Tab
{
	Document document;
	TextArea textArea = new TextArea();
	VBox lineNumbers = new VBox();
	
	public DocumentTab(Document document)
	{
		this.document = document;
		
		// Set tab title
		setText(document.filename);
		
		textArea.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				int newLineCount = newValue.length() - newValue.replace("\n", "").length();
				while (lineNumbers.getChildren().size() - 1 != newLineCount)
				{
					int currentLineCount = lineNumbers.getChildren().size() - 1;
					if (currentLineCount < newLineCount)
					{
						Label temp = new Label(currentLineCount + 2 + "");
						lineNumbers.getChildren().add(temp);
					}
					else
					{
						lineNumbers.getChildren().remove(currentLineCount - 1);
					}
				}
				
				document.content = newValue;
			}
		});

		textArea.setText(document.content);
		textArea.getStylesheets().add("style/style.css");
		textArea.applyCss();

		double lineNumbersTopPadding = 4;
		lineNumbers.setStyle("-fx-padding: " + lineNumbersTopPadding + " 5 0 5");
		lineNumbers.setAlignment(Pos.CENTER_RIGHT);
		
		ScrollBar sizeTest = new ScrollBar();
		sizeTest.setOrientation(Orientation.HORIZONTAL);
		
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
		
		BorderPane container = new BorderPane();
		container.setLeft(scrollPane);
		container.setCenter(textArea);
		
		setContent(container);
	}
	
	public String getDocumentId()
	{
		return document.id;
	}
	
	public String getDocumentPath()
	{
		return document.fullPath;
	}
	
	public String getDocumentFilename()
	{
		return document.filename;
	}
	
	public String getDocumentText()
	{
		return document.content;
	}
}
