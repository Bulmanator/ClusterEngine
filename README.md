# Cluster Engine

This is a small engine built upon JSFML and was created as part of my university Computer Science Group Project.

### Working Features
- Animations
- Particle System
- Input Processor
- Controller Support
    * Xbox 360, Xbox One, Xbox One Elite
    * PS3 (where drivers permit), PS4
- Physics System
    * Rigid body collision detection and resolution
    * Support for irregular concave polygons (SAT detection)
- Utilities
    * Name based content manager 
      * Textures, Fonts, Sounds and Music
    * Game State management
    * Various maths functions (float conversions, Vector maths etc.)
    * Various utility interfaces (Updateable, Renderable, )

### TODO
- macOS/ OSX Support
- Graphics
    * Shader support (RenderTexture instead of RenderWindow)
- Physics System
    * Circle-Circle Collisions
    * Circle-Polygon Collisions
    * Clipping and contact points
    * An actual broad-phase
- Messaging System
    * Broadcast
    * Observer-Listener
    * Message Queue for delayed dispatch
- File-based controller mappings
    * More robust
    * Allows for generic controllers
- Minor Improvements
    * Content Manager file management
    * V-Sync calculation
    * Redo Debugging information
    * Possible lookup table for sine and cosine
    * Engine handling resolution and dectoration changes
- Code samples and Wiki
 
