package Cameo.Model;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class Preferences
{
	public SimpleStringProperty font = new SimpleStringProperty();
	public SimpleIntegerProperty fontSize = new SimpleIntegerProperty();
	public SimpleObjectProperty<Color> fontColor = new SimpleObjectProperty<Color>();
}
