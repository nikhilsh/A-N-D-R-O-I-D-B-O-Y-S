package com.androidboys.spellarena.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ButtonExample extends ImageButton
{
    private int commandIndex;

    public ButtonExample(Texture texture_up)//, Texture texture_down, Texture background)
    {
        super(new SpriteDrawable(new Sprite(texture_up)));

        //this.setBackground(new SpriteDrawable(new Sprite(background)));
    }

    public ButtonExample(Texture texture_up, int commandIndex)//, Texture texture_down, Texture background)
    {
        super(new SpriteDrawable(new Sprite(texture_up)));
        this.commandIndex = commandIndex;
        //this.setBackground(new SpriteDrawable(new Sprite(background)));
    }

    public int getCommandIndex() {
        return commandIndex;
    }
}
