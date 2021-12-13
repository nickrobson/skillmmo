## SkillMMO

SkillMMO is a Fabric mod that enables an RPG-/MMO-like skill system where
certain skills must be gained before the player can do certain things.

It's heavily inspired by the following mods/plugins:
- [Project MMO](https://www.curseforge.com/minecraft/mc-mods/project-mmo), a Forge mod
- [LevelZ](https://www.curseforge.com/minecraft/mc-mods/levelz), a Fabric mod
- [mcMMO](https://mcmmo.org/), a Bukkit/Spigot/Paper plugin

---

### Concepts

SkillMMO has a few core concepts that underpin the mod:
- Skills, which are broad categories of knowledge or proficiency
- Levels, which players gain by getting experience and use to advance in a skill of their choosing
- Unlocks, which allow players to start using a certain thing (block, item, entity, etc.) through reaching a certain level in a skill

All of the above are configured using datapacks.
In fact, all the default skills and unlocks are configured through the datapack system!

---

### Default skills and levels

Skills based on proficiencies:
- Agriculture - hoes, axes, plants, crops, plant-based food, etc.
- Animal Husbandry - animals, meat-based food, etc.
- Building - decoration blocks, scaffolding, etc.
- Combat - swords, bows, armour, etc.
- Engineering - redstone things, dispensers, tnt, etc.
- Mining - pickaxes, shovels, stones, ores, etc.
- Sorcery - potions, enchanting, etc.
- Storage - bundles, barrels, chests, ender chests, shulkers, etc.
- Survival - beds, etc.
- Trading - villagers, wandering trader, etc.

---

### Customisation through datapacks

#### Skills

Skill datapack entries are located in `data/<group>/skills/<id>.json` (e.g. `data/skillmmo/skills/agriculture`).

Skills have the form:
```json5
{
  "replace": true, // "replace" controls whether this skill definition should overwrite everything
                   //     You'll want to set this to true if you're creating a new skill
                   //     If you'd like to just change the name or something, you can set this to false

  "enabled": true, // "enabled" controls whether this skill is available
                   //     You'll want to set this to false if you're wanting to disable an existing skill

  "nameKey": "skillmmo.skill.agriculture.name", // The i18n key for your skill's name
  "descriptionKey": "skillmmo.skill.agriculture.description", // The i18n key for your skill's description

  "maxLevel": 17, // The maximum level (see the "Unlocks" section below for more info)

  "icon": { // The configuration for the icon displayed in the UI
    "type": "item",            // Currently, "type" must be set to "item" - other types may be added in the future
    "value": "minecraft:wheat" // "value" should be set to the item ID (since "type" is always "item")
  }
}
```

You can find plenty of examples in [this mod's datapack](src/main/resources/data/skillmmo/skills).

#### Unlocks

Block, item, and entity unlocks are configured using tags, with the Vanilla resource location formats:
- Blocks: `data/<group>/tags/blocks/skills/<id>/<level>.json`
- Items: `data/<group>/tags/items/skills/<id>/<level>.json`
- Entities: `data/<group>/tags/entity_types/skills/<id>/<level>.json`

You can read about the tag format over on [the Minecraft wiki](https://minecraft.fandom.com/wiki/Tag#JSON_format).

All levels up to and including the `maxLevel` defined in the skill's datapack entry are loaded.
Players will/won't be able to use the items in the tags until they reach at least the given level.

You can find plenty of examples in [this mod's datapack](src/main/resources/data/skillmmo/tags).

---

### Ideas & future plans

Here are some of the ideas I have for this mod.

#### Independent skill XP
Currently, skills all use the same XP pool for leveling/skill points.
I want to allow skills to manage experience on their own - so you might need to use
blocks or kill entities from that skill to gain XP in it and unlock the next level.

#### Attributes-based skills
Currently, all skills are proficiency-based and restrict usage of things in the game.
I want to add the ability for skills to be based on player attributes, such as (for example)
- Health - extra health
- Agility - movement-based? e.g. faster walking, faster sprinting, less fall damage

#### Ability unlocks
I'd like to add another thing that can be attached as an unlock to skills: abilities!
These would be awarded to players for reaching a certain level in a certain skill, and would be
configured as Origins powers, so users with the Origins mod installed could have some richer unlocks!
