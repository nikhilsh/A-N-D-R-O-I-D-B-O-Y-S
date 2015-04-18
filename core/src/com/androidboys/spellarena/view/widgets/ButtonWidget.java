package com.androidboys.spellarena.view.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ButtonWidget extends ImageButton
{
    private int commandIndex;

    public ButtonWidget(Texture texture_up)
    {
        super(new SpriteDrawable(new Sprite(texture_up)));
    }

    public ButtonWidget(Texture texture_up, int commandIndex)
    {
        super(new SpriteDrawable(new Sprite(texture_up)));
        this.commandIndex = commandIndex;
    }

    public int getCommandIndex() {
        return commandIndex;
    }
}
