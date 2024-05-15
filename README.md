# Custom TNT Plugin for Minecraft 1.20.4

This plugin adds custom TNT types with unique properties, crafting recipes, and explosion mechanics. Perfect for adding new explosive dynamics to your Minecraft gameplay.

## Features

- **Custom TNT Types:** 
  - Speed TNT
  - Powered TNT

- **Unique Properties:**
  - Custom explosion radii
  - Specific crafting recipes
  - Explosion parameters such as fuse time, power, and fire effects
  - Block immunity to explosions
  - Water behavior during explosions

## TNT Types

### Speed TNT

- **Crafting Recipe:**
  ```
  ["gunpowder", "gunpowder", "gunpowder",
   "gunpowder", "tnt", "gunpowder",
   "gunpowder", "gunpowder", "gunpowder"]
  ```
- **Metadata:**
  - **Name:** `&cМощный`
  - **Lore:**
    - `&6lalalal`
    - `&aLOLOL`
- **Parameters:**
  - **Fuse Time:** 20 ticks
  - **Explosion Power:** 2.0
  - **Fire on Explosion:** Yes
  - **Unbreakable Blocks:** `["bedrock", "water", "lava"]`
  - **Chain Explosion:** Yes
  - **Ignores Water:** Yes

### Powered TNT

- **Crafting Recipe:**
  ```
  ["tnt", "gunpowder", "tnt",
   "gunpowder", "tnt", "gunpowder",
   "tnt", "gunpowder", "tnt"]
  ```
- **Metadata:**
  - **Name:** `&cМощный`
  - **Lore:**
    - `&6l111111`
    - `&a22222222`
- **Parameters:**
  - **Fuse Time:** 60 ticks
  - **Explosion Power:** 10.0
  - **Fire on Explosion:** Yes
  - **Unbreakable Blocks:** `["bedrock", "water"]` or `%all%`
  - **Chain Explosion:** Yes
  - **Ignores Water:** No

## Installation

1. Download the plugin jar file.
2. Place the jar file in your server's `plugins` folder.
3. Restart the server to load the plugin.

## Configuration

You can customize the properties of each TNT type in the plugin configuration file. Adjust the crafting recipes, explosion parameters, and metadata as needed.

## Usage

Craft the custom TNT using the specified recipes and use them just like regular TNT. Enjoy the unique explosions and effects!

## Contribution

Feel free to contribute to the development of this plugin by submitting pull requests or reporting issues on GitHub.

## License

This project is licensed under the MIT License.

---

For more details, please visit the GitHub repository.

Happy exploding! 💣

