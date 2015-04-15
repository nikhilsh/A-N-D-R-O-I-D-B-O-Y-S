# Spell Arena
Android app for 50.003 - Software Construction

## Introduction
__Spell Arena__ is an online battle arena Android game designed by ANDROIDBOYS group.

## Tutorial
### Create Lobby
In order to play __Spell Arena__, you need to connect your device to the Internet.

Click "Create Game" to create a room. Other player can view list of rooms at the main screen. Click "Join Game" to join a room.

If the room contains two players, the room owner can start game.

### Characters
Two players will fight against each other with their own characters in an arena.

The player can control the character with the controller located at the bottom left corner of the screen, and attack the enemy or heal self with spells. Each spell can be invoked with a unique combination of three elements -- Quas, Wex, Exort. There are 10 different spells in game.

Touch the screen to cast a spell on enemy (or on self).

### Spells
* QQQ = Divine Shield
* QQW = Force Staff
* QQE = Mine
* WWQ = Stasis Trap
* WWW = Atos
* WWE = Acid
* EEQ = Fan of Knives
* EEW = Dark Pact
* EEE = Laser
* QWE = Sprout

__Divine Shield (QQQ)__
> Invulnerable for 3 seconds.
> Mana cost: 50

__Force Staff (QQW)__
> Pushes character 100 units in the direction he is facing.
> Mana cost: 30

__Mine (QQE)__
> (to be completed...)

__Statis Trap (WWQ)__
> Prohibit enemy's movement for 5 seconds.

__Atos (WWW)__
> Slows the enemy's movement speed.
> Enemy's velocity will be slowed down to 100 for 3 seconds.
> Mana cost: 50.

__Acid (WWE)__
> (to be completed...)

__Fan of Knives (EEQ)__
> Damage.
> Radius: 100.
> Mana cost: 100.

__Dark Pact (EEW)__
> Damage.
> Radius: 300.
> Effect Delay: 3 sec.
> Mana cost: 80.

__Laser (EEE)__
> Damage.
> Area: 300X30.
> Mana cost: 80.

## LibGDX Classes and Interfaces
`com.badlogic.gdx.Gdx`
> Environment class holding references to the Application, Graphics, Audio, Files and Input instances.

`com.badlogic.gdx.Game`
> An `ApplicationListener` that delegates to a Screen. This allows an application to easily have multiple screens.

`com.badlogic.gdx.InputMultiplexer`
> An `InputProcessor` that delegates to an ordered list of other InputProcessors.

`com.badlogic.gdx.InputProcessor`
> An InputProcessor is used to receive input events from the keyboard and the touch screen (mouse on the desktop).

`com.badlogic.gdx.Screen`
> Represents one of many application screens, such as a main menu, a settings menu, the game screen and so on.

`com.badlogic.gdx.audio.Sound`
> A Sound is a short audio clip that can be played numerous times in parallel.

`com.badlogic.gdx.graphics.Color`
> A color class, holding the r, g, b and alpha component as floats in the range [0,1]. All methods perform clamping on the internal values after execution.

`com.badlogic.gdx.graphics.GL20`
> Interface wrapping all the methods of OpenGL ES 2.0.

`com.badlogic.gdx.graphics.OrthographicCamera`
> A camera with orthographic projection.

`com.badlogic.gdx.graphics.Pixmap`
> A Pixmap represents an image in memory.

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

`com.badlogic.gdx.scenes.scene2d.ui.Skin`
> A skin stores resources for UI widgets to use (texture regions, ninepatches, fonts, colors, etc).

`com.badlogic.gdx.scenes.scene2d.ui.Touchpad`
> An on-screen joystick. The movement area of the joystick is circular, centered on the touchpad, and its size determined by the smaller touchpad dimension.

`com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable`
> Drawable for a TextureRegion.

`com.badlogic.gdx.utils.Array`
> A resizable, ordered or unordered array of objects. If unordered, this class avoids a memory copy when removing elements (the last element is moved to the removed element's position).

`com.badlogic.gdx.utils.Pool`
> A pool of objects that can be reused to avoid allocation.

`com.badlogic.gdx.utils.viewport.StretchViewport`
> A ScalingViewport that uses Scaling.stretch so it does not keep the aspect ratio, the world is scaled to take the whole screen.

## Classes in core (with public methods)
`com.androidboys.spellarena.game.SpellArena extends Game`
* `create()`
* `render()`
* `dispose()`
* `switchScreen(ScreenType)`
* `addScreen(Screen)`
* `getClient(): NetworkInterface`
* `backToPreviousScreen()`
* `getNumberScreens(): int`

`com.androidboys.spellarena.gameworld.GameFactory`
* `getGameModel(int): GameModel`
* `getGameModel(): GameModel`

`com.androidboys.spellarena.gameworld.GameRenderer`
* `GameRenderer(SpriteBatch, GameWorld)`
* `initGameObjects()`
* `render(float)`
* `moveCamera()`
* `removePlayer(String)`

`com.androidboys.spellarena.gameworld.GameWorld`
* `initialize(GameModel)`
* `getBob(): Bob`
* `update(float)`
* `getPlayerModel(String): Bob`
* `getNextGameIndex(): int`
* `addPlayerModel(Bob)`
* `updateEnemy(float, float, float, float, int)`
* `getSpell(): Spells`
* `setSpell(Spells)`
* `getEnemy(): Bob`
* `getPlayerModels(): Map<String, Bob>`
* `movePlayer(long, String, int, float, float)`
* `updatePlayer(String, long, Vector2, Vector2)`
* `movePlayer(String, int)`
* `removePlayer(String)`
* `isGameEnd(): boolean`

`com.androidboys.spellarena.helper.AssetLoader`
* `queueLoading()`
* `setMainMenuResources()`
* `setGameResources()`
* `update(): boolean`
* `load()`

`com.androidboys.spellarena.helper.InputHandler (@Deprecated)`

`com.androidboys.spellarena.helper.StyleLoader`
* `prepareStyles()`

`com.androidboys.spellarena.mediators.GameScreenMediator extends Mediator`
* `GameScreenMediator(SpellArena, NetworkInterface)`
* `onPlayerLeftRoom(String)`
* `createScreen(): Screen`
* `getNetworkListenerAdapter(): NetworkListenerAdapter`
* `getRoom(): RoomModel`
* `setRoom(RoomModel)`
* `setGameServer(GameServer)`
* `startGame()`
* `move(int)`
* `update(Bob)`
* `onServerStarted()`
* `onServerStartFail()`
* `sendServerAddress(String)`
* `connectToServerSuccess(GameClient)`
* `connectToServerFailed()`
* `processMessage(String)`
* `initCommandHandler()`
* `leaveRoom()`
* `disconnect(boolean)`

`com.androidboys.spellarena.mediators.MainMenuMediator extends Mediator`
* `MainMenuMediator(SpellArena)`
* `createScreen(): Screen`

`com.androidboys.spellarena.mediators.Mediator`
* `Mediator(SpellArena)`
* `createScreen(): Screen`
* `onScreenShowInternal()`

`com.androidboys.spellarena.model.Bob`
* `Bob(int, int, boolean)`
* `Bob()`
* `setStateChangeListener(StateChangeListener)`
* `getPosition(): Vector2`
* `onClick()`
* `update(float)`
* `getVelocity(): Vector2`
* `getDirection(): Direction`
* `setVelocity(float, float)`
* `setTouchpad(Touchpad)`
* `sendLocation(int)`
* `setPosition(float, float)`
* `decrementLifeCount()`
* `setLifeCount(int)`
* `getLifeCount(): int`
* `getManaCount(): float`
* `decrementManaCount(int)`
* `incrementManaCount()`
* `getState(): int`
* `setState(int)`
* `updateObstacles(Array<Rectangle>)`
* `getTiles(): Array<Rectangle>`
* `getbobRect(): Rectangle`
* `isInvulnerable(): boolean`
* `getGameIndex(): int`
* `setPlayerName(String)`
* `getPlayerName(): String`
* `setPosition(Vector2)`
* `setGameIndex(int)`
* `setAtosSpeed()`
* `move(long, int, float, float)`
* `setUpdateDetails(long, Vector2, Vector2)`
* `move(int)`

`com.androidboys.spellarena.model.Spell`
* `Spell(GameWorld)`
* `update(float)`
* `getRemainingSeconds(): float`
* `setRemainingSeconds(float)`
* `spellSettler(int, int, int)`
* `castSpell()`
* `sendSpell()`
* `getSpell(): Spells`
* `checkCollision(Bob): Boolean`

## Structure
### android
[Android version](https://github.com/nikhilsh/A-N-D-R-O-I-D-B-O-Y-S/tree/master/android)
* src
  * com.androidboys.spellarena.game.android
    * AndroidLauncher.java
* gen: Generated Java Files
* assets
  * Downloads
    * Torstan's100pxTiles
  * fonts
    * Fantasy.ttf
    * header.TTF
    * play.TTF
    * starttext.ttf
  * images
    * androidboys.png
    * bg.jpg
    * exort.png
    * frame1.jpg
    * loadingbackground.png
    * ParchmentLabel.png
    * quas.png
    * wex.png
  * maps
    * Dungeon.tmx
  * sprites
    * dust.png
    * qwe.png
    * wizard.png
  * badlogic.jpg
* bin
  * res
* AndroidManifest.xml
* jarlist.cache
* build
* libs
  * armeabi
    * libgdx-box2d.so
    * libgdx-bullet.so
    * libgdx-freetype.so
    * libgdx.so
  * armeabi-v7a
    * libgdx-box2d.so
    * libgdx-bullet.so
    * libgdx-freetype.so
    * libgdx.so
  * x86
  	* libgdx-box2d.so
    * libgdx-bullet.so
    * libgdx-freetype.so
    * libgdx.so
* res
  * drawable-\*: Some auto-generated icon files
  * values
    * strings.xml
    * styles.xml
* AndroidManifest.xml
* build.gradle
* ic_launcher-web.png
* proguard-project.txt
* project.properties

### desktop
[Desktop version](https://github.com/nikhilsh/A-N-D-R-O-I-D-B-O-Y-S/tree/master/desktop)
* src
 * com.androidboys.spellarena
  * game/desktop
   * DesktopLauncher.java
  * utils
   * TextureSetup.java
* assets
 * _(See "assets" folder in "android" part)_
* build
* Users
  * _(Users...)_
   * Desktop
    * Development
     * Spell Arena
      * android
       * assets
        * items
         * textures.pack.atlas
         * textures.pack.png
* build.gradle

### core
[Core code of the game](https://github.com/nikhilsh/A-N-D-R-O-I-D-B-O-Y-S/tree/master/core)
* src
 * com
  * androidboys.spellarena
   * game
    * SpellArena.java
   * gameworld
    * GameFactory.java
    * GameRenderer.java
    * GameWorld.java
   * helper
    * AssetLoader.java
    * InputHandler.java
    * StyleLoader.java
   * mediators
    * GameScreenMediator.java
    * MainMenuMediator.java
    * Mediator.java
   * model
    * Acid.java
    * Bob.java
    * GameObject.java
    * Spell.java
    * Tornado.java
   * net
    * appwarp
     * AppWarpClient.java
     * NotificationListenerAdapter.java
     * RoomListenerAdapter.java
     * ZoneListenerAdapter.java
    * model
     * RoomModel.java
    * protocol
     * ClockSyncReqCommand.java
     * ClockSyncResCommand.java
     * Command.java
     * CommandFactory.java
     * CreateGameCommand.java
     * GameEndCommand.java
     * MoveCommand.java
     * ReadyCommand.java
     * SpellCommand.java
     * StartGameCommand.java
     * UndefinedCommand.java
     * UpdateCommand.java
    * ChatListener.java
    * ConnectionListener.java
    * NetworkInterface.java
    * NetworkListenerAdapter.java
    * WarpController.java
    * WarpListener.java
   * servers
    * GameClient.java
    * GameServer.java
   * session
    * UserSession.java
   * view
    * widgets
     * ButtonWidget.java
     * ChangingLabel.java
     * GameListWidget.java
     * GameWidget.java
     * LoadingWidget.java
    * GameScreen.java
    * MainMenuScreen.java
    * SplashScreen.java
    * StartMultiplayerScreen.java
  * esotericsoftware.kryonet
   * rmi
    * ObjectSpace.java
    * RemoteObject.java
    * TimeoutException.java
   * util
    * InputStreamSender.java
    * ObjectIntMap.java
    * TcpIdleSender.java
   * Client.java
   * ClientDiscoveryHandler.java
   * Connection.java
   * EndPoint.java
   * FrameworkMessage.java
   * JsonSerialization.java
   * KryoNetException.java
   * KryoSerialization.java
   * Listener.java
   * Serialization.java
   * Server.java
   * ServerDiscoveryHandler.java
   * TcpConnection.java
   * UdpConnection.java
* build
* build.gradle
