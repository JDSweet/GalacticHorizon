package org.origin.spacegame.gui;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class StringSelectBox extends SelectBox<String>
{
    public StringSelectBox(Skin skin) {
        super(skin);
    }

    public StringSelectBox(Skin skin, String styleName) {
        super(skin, styleName);
    }

    public StringSelectBox(SelectBoxStyle style) {
        super(style);
    }
}
