package Cameo;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import Cameo.Model.ApplicationModel;
import javafx.beans.property.Property;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.collections.*;

public class DocumentArea extends StackPane
{
	public DocumentArea(ApplicationModel model)
	{		
		final TabPane tabPane = new TabPane();
		
		// It's pretty stupid, but this connects model.selectedDocument to the tabPane's selectedDocument value.
		tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				model.selectedDocument.set(newValue.intValue());
			}
		});
		model.selectedDocument.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				tabPane.getSelectionModel().select(newValue.intValue());
			}
		});
		
		tabPane.getTabs().addListener(new ListChangeListener<Tab>()
		{
			@Override
			public void onChanged(Change<? extends Tab> change)
			{
				while (change.next())
				{
					if (change.wasRemoved())
					{
						System.out.println(change.getRemoved().get(0).getText());
					}
				}
			}
		});
		
		Tab documentTab1 = new Tab();
		TextArea textArea1 = new TextArea();
		textArea1.setText("Hello World");
		
		
		documentTab1.setText("New Document");
		documentTab1.setContent(textArea1);
		
		Tab documentTab2 = new Tab();
		TextArea textArea2 = new TextArea();
		documentTab2.setText("New Document (2)");
		documentTab2.setContent(textArea2);
		
		tabPane.getTabs().add(documentTab1);
		tabPane.getTabs().add(documentTab2);
		
		this.getChildren().add(tabPane);
	}
}
