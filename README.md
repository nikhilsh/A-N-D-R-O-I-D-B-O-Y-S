# Spell Arena
Android app for 50.003 - Software Construction

## Plugins
* Gradle 1.0.0
* LibGDX 1.5.4

## Classes and Interfaces
`com.badlogic.gdx.Gdx`
> Environment class holding references to the Application, Graphics, Audio, Files and Input instances.

`com.badlogic.gdx.InputMultiplexer`
> An `InputProcessor` that delegates to an ordered list of other InputProcessors.

`com.badlogic.gdx.InputProcessor`
> An InputProcessor is used to receive input events from the keyboard and the touch screen (mouse on the desktop).

`com.badlogic.gdx.audio.Sound`
> A Sound is a short audio clip that can be played numerous times in parallel.

`com.badlogic.gdx.graphics.Color`
> A color class, holding the r, g, b and alpha component as floats in the range [0,1]. All methods perform clamping on the internal values after execution.

`com.badlogic.gdx.graphics.GL20`
> Interface wrapping all the methods of OpenGL ES 2.0.

`com.badlogic.gdx.graphics.OrthographicCamera`
> A camera with orthographic projection.

`com.badlogic.gdx.graphics.Texture`
> A Texture wraps a standard OpenGL ES texture.

`com.badlogic.gdx.graphics.g2d.Animation`
> An Animation stores a list of TextureRegions representing an animated sequence.

`com.badlogic.gdx.graphics.g2d.BitmapFont`
> Renders bitmap fonts.

`com.badlogic.gdx.graphics.g2d.SpriteBatch`
> Draws batched quads using indices.

`com.badlogic.gdx.graphics.g2d.TextureRegion`
> Defines a rectangular area of a texture.

`com.badlogic.gdx.graphics.glutils.ShapeRenderer`
> Renders points, lines, shape outlines and filled shapes.

`com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType`
> Shape types to be used with ShapeRenderer.

`com.badlogic.gdx.maps.MapLayer`
> Ordered list of MapLayer instances owned by a Map.

`com.badlogic.gdx.maps.MapObject`
> Generic Map entity with basic attributes like name, opacity, color.

`com.badlogic.gdx.math.Rectangle`
> Encapsulates a 2D rectangle defined by its corner point in the bottom left and its extents in x (width) and y (height).

`com.badlogic.gdx.math.Vector3`
> Encapsulates a 3D vector. Allows chaining operations by returning a reference to itself in all modification methods.

`com.badlogic.gdx.physics.box2d.World`
> The world class manages all physics entities, dynamic simulation, and asynchronous queries. The world also contains efficient memory management facilities.

`com.badlogic.gdx.scenes.scene2d.Stage`
> A 2D scene graph containing hierarchies of `actor`s. Stage handles the viewport and distributes input events.

`com.badlogic.gdx.scenes.scene2d.ui.Touchpad`
> An on-screen joystick. The movement area of the joystick is circular, centered on the touchpad, and its size determined by the smaller touchpad dimension.

`com.badlogic.gdx.utils.Array`
> A resizable, ordered or unordered array of objects. If unordered, this class avoids a memory copy when removing elements (the last element is moved to the removed element's position).

`com.badlogic.gdx.utils.Pool`
> A pool of objects that can be reused to avoid allocation.

## Directories
* [desktop](https://github.com/nikhilsh/A-N-D-R-O-I-D-B-O-Y-S/tree/master/desktop): Desktop version
  * src/com/androidboys/spellarena
    * game/desktop
      * DesktopLauncher.java
    * utils
      * TextureSetup.java
  * Users
    * _(Users...)_
* [android](https://github.com/nikhilsh/A-N-D-R-O-I-D-B-O-Y-S/tree/master/android): Android version
  * assets
    * Downloads
      * Torstan's100pxTiles
    * fonts
      * Fantasy.ttf
      * header.TTF
      * play.TTF
      * starttext.ttf
    * images
      * background.jpg
      * background.png
      * bg.jpg
      * splash.jpeg
    * maps
      * Dungeon.tmx
      * _(More maps to be added...)_
    * sprites
      * wizard.png
  * res
    * drawable-\*: Some auto-generated icon files
    * values
      * strings.xml
      * styles.xml
  * src/com/androidboys/spellarena/game/android
    * AndroidLauncher.java
  * AndroidManifest.xml
  * ic_launcher-web.png
  * proguard-project.txt
  * project.properties
* [core](https://github.com/nikhilsh/A-N-D-R-O-I-D-B-O-Y-S/tree/master/core): Core codes of the game
  * src
    * appwarp
      * ChatListener.java
      * ConnectionListener.java
      * NotificationListener.java
      * RoomListener.java
      * WarpController.java
      * WarpListener.java
      * ZoneListener.java
  * com/androidboys/spellarena
    * game
      * SpellArena.java
    * gameworld
      * GameRenderer.java
      * GameWorld.java
    * helper
      * AssetLoader.java
      * InputHandler.java
    * model
      * Bob.java
      * _(More characters to be added (maybe)...)_
    * screens
      * GameScreen.java
      * LoadingScreen.java
      * MainMenuScreen.java
      * StartMultiplayerScreen.java
* [lib](https://github.com/nikhilsh/A-N-D-R-O-I-D-B-O-Y-S/tree/master/libs): Library files
  * App42MultiPlayerGamingSDK.jar
  * java-json.jar
