package de.alshikh.haw.tron.client.models.data.datatypes;

import javafx.scene.paint.Color;

import java.io.Serializable;
public class SerializableColor implements Serializable
{
    private final double red;
    private final double green;
    private final double blue;
    private final double alpha;
    public SerializableColor(Color color)
    {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = color.getOpacity();
    }
    public SerializableColor(double red, double green, double blue, double alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    public Color getFXColor()
    {
        return new Color(red, green, blue, alpha);
    }
}
