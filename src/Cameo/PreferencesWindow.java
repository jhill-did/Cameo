package Cameo;

import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import Cameo.Model.ApplicationModel;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;

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
		
		VBox root = new VBox();
		root.setStyle("-fx-padding: 4px");

		Scene mainScene = new Scene(root);
		
		String[] systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		ChoiceBox<String> fontComboBox = new ChoiceBox<String>();
		fontComboBox.getItems().addAll(systemFonts);
		
		model.preferences.font.bind(fontComboBox.valueProperty());
		
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
		fontFaceColumn.getChildren().addAll(fontFaceLabel, fontComboBox);
		
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
		
		VBox fontColorColumn = new VBox();
		fontColorColumn.setStyle("-fx-padding: 0 0 0 4px");
		fontColorColumn.getChildren().addAll(fontColorLabel, fontColorPicker);
		
		HBox firstRow = new HBox();
		firstRow.getChildren().addAll(fontFaceColumn, fontColorColumn);
		
		root.getChildren().add(firstRow);
			
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
		
		parent.documentArea.setStyle(style);
	}
	
	private String getColorHex(Color color)
	{
		return String.format("#%02x%02x%02x", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));  
	}
}
