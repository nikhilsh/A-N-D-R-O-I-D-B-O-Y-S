package com.androidboys.spellarena.view.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * The Class ButtonWidget.
 */
public class ButtonWidget extends ImageButton
{
    
    /** The command index. */
    private int commandIndex;

    /**
     * Instantiates a new button widget.
     *
     * @param texture_up the texture_up
     */
    public ButtonWidget(Texture texture_up)
    {
        super(new SpriteDrawable(new Sprite(texture_up)));
    }

    /**
     * Instantiates a new button widget.
     *
     * @param texture_up the texture_up
     * @param commandIndex the command index
     */
    public ButtonWidget(Texture texture_up, int commandIndex)
    {
        super(new SpriteDrawable(new Sprite(texture_up)));
        this.commandIndex = commandIndex;
    }

    /**
     * Gets the command index.
     *
     * @return the command index
     */
    public int getCommandIndex() {
        return commandIndex;
    }
}
