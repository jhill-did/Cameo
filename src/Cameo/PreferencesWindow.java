package Cameo;

import java.awt.GraphicsEnvironment;
import Cameo.Model.ApplicationModel;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;

public class PreferencesWindow extends Stage
{
	CameoApp parent;
	ApplicationModel model;
	
	public PreferencesWindow(CameoApp parent, ApplicationModel model)
	{
		this.parent = parent;
		this.model = model;

		initOwner(parent.mainStage);
		initModality(Modality.WINDOW_MODAL);
		
		setTitle("Preferences");
		
		setResizable(false);
		
		VBox root = new VBox(6);
		root.setStyle("-fx-padding: 4px");

		Scene mainScene = new Scene(root);
		
		String[] systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		ChoiceBox<String> fontChoiceBox = new ChoiceBox<String>();
		fontChoiceBox.getItems().addAll(systemFonts);
		fontChoiceBox.setValue("Segoe UI");
		
		model.preferences.font.bind(fontChoiceBox.valueProperty());
		model.preferences.font.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				updateStyle();
			}
		});
		
		Label fontFaceLabel = new Label("Font");
		
		VBox fontFaceColumn = new VBox();
		fontFaceColumn.getChildren().addAll(fontFaceLabel, fontChoiceBox);
		
		Label fontColorLabel = new Label("Color");
		
		ColorPicker fontColorPicker = new ColorPicker();
		fontColorPicker.valueProperty().addListener(new ChangeListener<Color>()
		{
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue)
			{
				model.preferences.fontColor.set(newValue);
				updateStyle();
			}
		});
		
		Label fontSizeLabel = new Label("Size");
		ObservableList<Integer> options = FXCollections.observableArrayList(8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72);
		ChoiceBox<Integer> fontSizeChoiceBox = new ChoiceBox<Integer>(options);
		fontSizeChoiceBox.setValue(16);
		
		model.preferences.fontSize.bind(fontSizeChoiceBox.valueProperty());
		model.preferences.fontSize.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> obvservable, Number oldValue, Number newValue1)
			{
				updateStyle();
			}
		});
		
		VBox fontSizeColumn = new VBox();
		fontSizeColumn.getChildren().addAll(fontSizeLabel, fontSizeChoiceBox);
		
		HBox firstRow = new HBox(4);
		firstRow.getChildren().addAll(fontFaceColumn, fontSizeColumn);

		root.getChildren().add(firstRow);
		
		
		ToggleButton boldButton = new ToggleButton("B");
		model.preferences.bold.bind(boldButton.selectedProperty());
		model.preferences.bold.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				updateStyle();
			}
		});

		ToggleButton italicButton = new ToggleButton("I");
		model.preferences.italic.bind(italicButton.selectedProperty());
		model.preferences.italic.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				updateStyle();
			}
		});
		
		VBox fontColorColumn = new VBox();
		fontColorColumn.getChildren().addAll(fontColorLabel, fontColorPicker);
		
		Label fontStyleLabel = new Label("Style");
		
		HBox fontStyleButtons = new HBox(4);
		fontStyleButtons.setAlignment(Pos.BOTTOM_LEFT);
		fontStyleButtons.getChildren().addAll(boldButton, italicButton);
		
		VBox fontStyleColumn = new VBox();
		fontStyleColumn.getChildren().addAll(fontStyleLabel, fontStyleButtons);
		
		HBox secondRow = new HBox(4);
		secondRow.setStyle("-fx-padding: 6px 0 0 0");
		secondRow.setAlignment(Pos.BOTTOM_LEFT);
		secondRow.getChildren().addAll(fontStyleColumn, new Separator(Orientation.VERTICAL), fontColorColumn);

		root.getChildren().add(secondRow);
		
		Button closeButton = new Button("Close");
		closeButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				close();
			}
		});

		HBox thirdRow = new HBox(4);
		thirdRow.getChildren().add(closeButton);
		
		thirdRow.setAlignment(Pos.BOTTOM_CENTER);
		
		root.getChildren().add(thirdRow);		
			
		setScene(mainScene);
	}
	
	private void updateStyle()
	{
		String style = "";
		if (model.preferences.font.get() != null)
		{
			style += "-fx-font-family: '" + model.preferences.font.get() + "';";
		}

		if (model.preferences.fontColor.get() != null)
		{
			style += "textColor: " + getColorHex(model.preferences.fontColor.get()) + ";";
		}
		
		style += "-fx-font-size: " + model.preferences.fontSize.get() + "px;";
		
		if (model.preferences.bold.get())
		{
			style += "-fx-font-weight: bold;";
		}
		
		if (model.preferences.italic.get())
		{
			style += "-fx-font-style: italic;";
		}
		
		parent.documentArea.setStyle(style);
	}
	
	private String getColorHex(Color color)
	{
		return String.format("#%02x%02x%02x", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));  
	}
}
