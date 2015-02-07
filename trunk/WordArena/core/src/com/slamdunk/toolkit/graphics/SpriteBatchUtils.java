package com.slamdunk.toolkit.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpriteBatchUtils {
    public static enum TextAlignment {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }

    /**
     * Dessine le texte indiqué en le centrant dans la zone bounds.
     * @param drawBatch
     * @param font
     * @param text
     * @param align
     * @param bounds
     */
    public static void drawString(SpriteBatch spriteBatch, BitmapFont font, String text, TextAlignment align, Rectangle bounds) {
        // Détermine la taille du texte
        TextBounds textBounds = font.getBounds(text);

        // Détermine les coordonnées
        float x = bounds.x;
        float y = bounds.y;
        switch (align) {
        case TOP_LEFT:
            x = bounds.x;
            y = bounds.y + bounds.height - textBounds.height;
            break;
        case TOP_CENTER:
            x = bounds.x + bounds.width / 2 - textBounds.width / 2;
            y = bounds.y + bounds.height - textBounds.height;
            break;
        case TOP_RIGHT:
            x = bounds.x + bounds.width - textBounds.width;
            y = bounds.y + bounds.height - textBounds.height;
            break;
        case MIDDLE_LEFT:
            x = bounds.x;
            y = bounds.y + bounds.height / 2 - textBounds.height / 2;
            break;
        case MIDDLE_CENTER:
            x = bounds.x + bounds.width / 2 - textBounds.width / 2;
            y = bounds.y + bounds.height / 2 - textBounds.height / 2;
            break;
        case MIDDLE_RIGHT:
            x = bounds.x + bounds.width - textBounds.width;
            y = bounds.y + bounds.height / 2 - textBounds.height / 2;
            break;
        case BOTTOM_LEFT:
            x = bounds.x;
            y = bounds.y;
            break;
        case BOTTOM_CENTER:
            x = bounds.x + bounds.width / 2 - textBounds.width / 2;
            y = bounds.y;
            break;
        case BOTTOM_RIGHT:
            x = bounds.x + bounds.width - textBounds.width;
            y = bounds.y;
            break;
        }

        // Dessine le texte
        font.draw(spriteBatch, text, x, y);
    }
    
    /**
     * Crée un Sprite qui se répète le long d'une ligne 
     * @param p1
     * @param p2
     * @return
     */
    public static Sprite createSpritedLine(final Texture texture, final Vector2 p1, final Vector2 p2) {
		int length = (int) p1.dst(p2);
		int height = texture.getHeight();
		
		final Sprite sprite = new Sprite(texture, 0, 0, length, height);
		sprite.setOrigin(0, height / 2);
		sprite.setPosition(p1.x, p1.y);
		float degrees = (float)Math.toDegrees(Math.atan2(p2.y - p1.y, p2.x - p1.x));
		sprite.setRotation(degrees);
		return sprite;
   }
}