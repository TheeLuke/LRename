<div align="center">

# LRename

**The Ultimate Modern Item Editor & Templating System for Spigot/Paper 1.21+**

![Spigot/Paper](https://img.shields.io/badge/Spigot%2FPaper-1.21-blue?style=for-the-badge&logo=minecraft)
![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Version](https://img.shields.io/badge/Version-1.0.0-success?style=for-the-badge)
</div>

---

## Overview

More than just a renaming tool, LRename is a full item management system. With it, players can copy their favorite name and lore combinations, save them as permanent templates on disk, and paste them onto new gear. Complete with Vault economy costs, strict admin blacklists, and smart tab-completion, it is the only item editor your server will ever need.

## Features

* **Modern Text Formatting:** Fully supports the modern MiniMessage format (`<gradient>`, `<rainbow>`, `<#FF5555>`, etc.) alongside legacy color codes.
* **Clipboard Memory:** Players can instantly copy an item's custom name, lore, and hidden flags to an active memory clipboard, and paste it perfectly onto new items.
* **Persistent Templates:** Save your clipboard to disk! Create a library of item templates to quickly apply your favorite naming conventions anytime.
* **Economy Integration:** Keep your server's economy balanced. Charge players Vault money or Vanilla XP levels for every action (renaming, lore editing, pasting, etc.).
* **PlaceholderAPI Support:** Bake live variables directly into your items. Apply `%player_name%`, `%vault_eco_balance%`, or any other placeholder right onto your sword's lore.
* **Smart Blacklists:** Protect your game balance. Blacklist specific materials (like `COMMAND_BLOCK` or `BEDROCK`) from being edited, and utilize a built-in profanity filter for names and lore.
* **Item Flag Toggling:** Easily hide messy enchantment texts and attribute modifiers (`HIDE_ENCHANTS`, `HIDE_ATTRIBUTES`) to create clean, glowing items.
* **Intelligent Tab-Completion:** Fully dynamic, context-aware tab completion that actively suggests the player's saved template names and calculates the exact lines of lore on the item they are holding.
* **Update Checker & bStats:** Admins are notified in-game when a new version is released, and anonymous server statistics are tracked via bStats.

---

## Installation

1. Download the latest `LRename-1.0.0.jar` from the [Releases](https://www.google.com/search?q=../../releases) tab (or compile from source).
2. Drop the `.jar` file into your server's `plugins/` directory.
3. *(Optional but Recommended)* Install [Vault](https://www.google.com/search?q=https://www.spigotmc.org/resources/vault.34315/) and an economy provider (like EssentialsX) for economy support.
4. *(Optional but Recommended)* Install [PlaceholderAPI](https://www.google.com/search?q=https://www.spigotmc.org/resources/placeholderapi.6245/) for dynamic variable support.
5. Restart your server.
6. Configure the plugin in `plugins/LRename/config.yml` and use `/lr reload` to apply changes!

---

## Commands & Permissions

| Command                                 | Description | Permission |
|-----------------------------------------| --- | --- |
| `/lr`                                   | Base command & help menu | `lrename.use` |
| `/lr reload`                            | Reloads the configuration | `lrename.admin` |
| `/lr rename <name>`                     | Renames the held item | `lrename.rename` |
| `/lr lore add <text>`                   | Adds a line of lore | `lrename.lore` |
| `/lr lore delline <#>`                  | Deletes a specific line of lore | `lrename.lore` |
| `/lr clear [name/lore]`                 | Clears specific or all custom text | `lrename.clear` |
| `/lr hide/unhide [enchants/attributes]` | Toggles item flags | `lrename.hide` |
| `/lr copy`                              | Copies the item's name/lore/flags | `lrename.copypaste` |
| `/lr paste`                             | Pastes copied data to a new item | `lrename.copypaste` |
| `/lr template save <name>`              | Saves current clipboard as a template | `lrename.templates` |
| `/lr template load <name>`              | Loads a saved template onto an item | `lrename.templates` |
| `/lr template delete <name>`            | Deletes a saved template | `lrename.templates` |
| `/lr templates`                         | Lists all your saved templates | `lrename.templates` |
| **N/A**                                 | Bypasses all XP/Vault economy costs | `lrename.bypasscost` |

---

## Configuration

LRename is highly customizable. Translate every single message, tweak your blacklists, and balance your economy costs all from `config.yml`.

```yaml
# Enable/Disable specific features
settings:
  allow-renaming: true
  allow-lore-editing: true
  allow-copy-paste: true
  max-lore-lines: 10

# Protect your server economy and chat
blacklists:
  words:
    - "admin"
    - "owner"
  materials:
    - "BEDROCK"
    - "BARRIER"

# Choose between NONE, VAULT, or XP
economy:
  type: "VAULT"
  costs:
    rename: 100.0
    lore-add: 50.0
    paste: 150.0

# Fully MiniMessage supported translations!
messages:
  prefix: "<dark_gray>[<yellow>LRename</yellow>]</dark_gray> "
  item-renamed: "<#55FF55>Successfully renamed your item!"
  not-enough-money: "<red>You need <yellow>%cost%</yellow> to do this!</red>"

```

---

## Compiling from Source

LRename is built using Maven and Java 21. If you wish to build the plugin yourself:

1. Clone the repository:
```bash
git clone https://github.com/TheeLuke/LRename.git
cd LRename

```


2. Build the project using Maven:
```bash
mvn clean package

```


3. The compiled and shaded `.jar` will be located in the `target/` directory.

---

## Support & Contributing

If you encounter any bugs, issues, or have feature requests, please open an issue on the [GitHub Issues tracker](https://www.google.com/search?q=../../issues).

For direct contact, you can reach out on Discord: `@TheeLuke`

---

<div align="center">
<b>⭐ If you find this plugin useful, consider dropping a star on the repository! ⭐</b>
</div>
