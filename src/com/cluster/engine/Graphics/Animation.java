/*
    MIT License

    Copyright (c) 2017 James Bulman

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package com.cluster.engine.Graphics;

import com.cluster.engine.Components.Component;
import com.cluster.engine.Components.Transform;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * A Class which will animate a sprite sheet
 */
public class Animation extends Component {

    /** Whether or not the animation is playing */
    private boolean playing;
    /** Whether or not the animation is active. Inactive animations will not be rendered */
    private boolean active;

    /** The texture source of the animation */
    private Texture texture;

    /** Whether to flip the animation in the x direction */
    private boolean flipX;
    /** Whether to flip the animation in the y direction */
    private boolean flipY;

    /** The size, in pixels, of the animation */
    private Vector2f size;

    /** The offset from the GameObjects transform */
    private Vector2f offset;

    /** The width of a frame in the animation */
    public final int width;
    /** The height of a frame in the animation */
    public final int height;

    /** The number of rows the sprite sheet has */
    private int rows;
    /** The number of columns the sprite sheet has */
    private int columns;

    /** The total number of frames the animation has*/
    public final int totalFrames;
    /** The frame which the animation is currently at */
    private int currentFrame;
    /** How long each frame should last, in seconds */
    private float timePerFrame;

    /** How long has passed since the last frame, in seconds */
    private float accumulator;

    // TODO(James): Sort out scale via Transform
    //private Vector2f scale;

    /**
     * Creates an animation from the given texture key, with a time per frame of 0.1 seconds
     * @param texture The texture to represent the animation
     * @param rows The number of rows in the animation sheet
     * @param columns The number of columns in the animation sheet
     */
    public Animation(String name, Texture texture, int rows, int columns) { this(name, texture, rows, columns, 0.1f); }

    /**
     * Creates an animation from the given texture key, with a time per frame as given
     * @param texture The texture to represent the animation
     * @param rows The number of rows in the animation sheet
     * @param columns The number of columns in the animation sheet
     * @param timePerFrame The time each frame lasts for, in seconds
     */
    public Animation(String name, Texture texture, int rows, int columns, float timePerFrame) {
        super(name);

        // Get the texture from the content manager
        this.texture = texture;

        // Set the number of rows and columns
        this.rows = rows;
        this.columns = columns;

        // Calculate the total frames and set to beginning of the animation
        totalFrames = rows * columns;
        currentFrame = 0;
        this.timePerFrame = timePerFrame;

        // Work out the width and height of the animation
        width = texture.getSize().x / columns;
        height = texture.getSize().y / rows;

        size = new Vector2f(width, height);

        // Sets the offset to (0, 0)
        offset = new Vector2f(0, 0);

        // Sets playing
        playing = true;

        flipX = flipY = false;

        // Resets the time accumulator
        accumulator = 0;

        active = true;
    }

    /**
     * Updates the animation and moves it onto the next frame when necessary
     * @param dt The amount of time passed since last frame
     */
    public void update(float dt) {
        // Return if not playing
        if(!playing || !active) return;

        // Advance the amount of time since last frame
        accumulator += dt;
        if(accumulator >= timePerFrame) {
            // Advance frame
            currentFrame++;

            // If end of animation, loop back to beginning
            if(currentFrame == totalFrames)
                currentFrame = 0;

            // Reset time since last frame
            accumulator = 0;
        }
    }

    /**
     * Draws the animation, at its offset and on its current frame, to the screen
     * @param renderer The {@link RenderWindow} to draw the entity to
     */
    public void render(RenderWindow renderer) {
        if(!active) return;

        Transform tx = getGameObject().getTransform();
        Vector2f position = Vector2f.add(tx.getPosition(), offset);

        // Create a sprite and set its offset
        Sprite sprite = new Sprite(texture);
        sprite.setOrigin(width / 2, height / 2);
        sprite.setPosition(position);
        sprite.setRotation(tx.getRotation().getAngle());

        Vector2f scale = new Vector2f(size.x / (float) width, size.y / (float) height);
        sprite.setScale(scale);

        if(flipX) sprite.setScale(-scale.x, scale.y);
        if(flipY) sprite.setScale(sprite.getScale().x, scale.y);

        // Work out UV coordinates within the sprite sheet
        int row = (int)(((float) currentFrame) / ((float) columns));
        int col = currentFrame % columns;

        // Draw to screen
        sprite.setTextureRect(new IntRect(col * width, row * height, width, height));
        renderer.draw(sprite);
    }

    /**
     * Gets the current offset of the animation
     * @return The offset from the Transform of the GameObject
     */
    public Vector2f getOffset() { return offset; }

    /**
     * Gets how long each frame lasts for, in seconds
     * @return The time each frame lasts for
     */
    public float getTimePerFrame() { return timePerFrame; }

    /**
     * Gets whether or not the animation is currently playing
     * @return True if the animation is playing, otherwise False
     */
    public boolean isPlaying() { return playing; }

    /**
     * Whether or not the animation is active
     * @return True if the animation is active, otherwise false
     */
    public boolean isActive() { return active; }

    /**
     * Whether or not the animation is flipped in the x direction
     * @return True if it is flipped, otherwise False
     */
    public boolean isFlippedX() { return flipX; }

    /**
     * Whether or not the animation is flipped in the y direction
     * @return True if it is flipped, otherwise False
     */
    public boolean isFlippedY() { return flipY; }

    /**
     * Returns the {@link Type#Animation} type
     * @return The animation type
     */
    public Type getType() { return Type.Animation; }

    /**
     * Sets the offset to the given amount
     * @param offset The amount to offset by
     */
    public void setOffset(Vector2f offset) { this.offset = offset; }

    /**
     * Sets whether or not the animation should be flipped in the x direction
     * @param flipX True to flip in the x direction, otherwise False
     */
    public void setFlippedX(boolean flipX) { this.flipX = flipX; }

    /**
     * Sets whether or not the animation should be flipped in the y direction
     * @param flipY True to flip in the y direction, otherwise false
     */
    public void setFlippedY(boolean flipY) { this.flipY = flipY; }

    /**
     * Sets whether or not the animation should play
     * @param playing True to play the animation, False to stop the animation
     */
    public void setPlaying(boolean playing) { this.playing = playing; }

    /**
     * Sets whether or not the animation should be active
     * @param active True to set the animation as active, false to deactivate the animation
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Sets how long each frame lasts for to the time given
     * @param timePerFrame The time each frame should last for, in seconds
     */
    public void setTimePerFrame(float timePerFrame) { this.timePerFrame = timePerFrame; }

    /**
     * Set the sixe of the animation, in pixels
     * @param size A vector containing the width and height of the animation, in pixels
     */
    public void setSize(Vector2f size) { this.size = size; }

}
