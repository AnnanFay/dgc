(ns dgc.config)

(def physical-attributes  [	:strength
														:agility
														:toughness
														:endurance
														:recuperation
														:disease-resistance])
(def mental-attributes    [	:analytical-ability
														:fucus
														:willpower
														:creativity
														:intuition
														:patience
														:memory
														:linguistic-ability
														:spacial-sense
														:musicality
														:kinaesthetic-sense
														:empathy
														:social-aweareness])
(def traits               [	:anxiety
														:anger
														:depression
														:self-consciousness
														:immoderation
														:vulnerability
														:friendliness
														:gregariousness
														:assertiveness
														:activity-level
														:excitement_seeking
														:cheerfulness
														:imagination
														:artistic-interest
														:emotionality
														:adventurousness
														:intellectual-curiosity
														:liberalism
														:trust
														:straightforwardness
														:altruism
														:cooperation
														:modesty
														:sympathy
														:self-efficacy
														:orderliness
														:dutifulness
														:achievement_striving
														:self-discipline
														:cautiousness])
(def labors               [ :mining
														:stone-hauling
														:wood-hauling
														:burial
														:food-hauling
														:refuse-hauling
														:item-hauling
														:furniture-hauling
														:animal-hauling
														:cleaning
														:wood-cutting
														:carpentry
														:stone-detailing
														:masonry
														:architecture
														:animal-training
														:animal-care
														:health-care
														:butchery
														:trapping
														:animal-dissection
														:leatherworking
														:tanning
														:brewing
														:alchemy
														:soapmaking
														:weaving
														:clothesmaking
														:milling
														:plant-processing
														:cheesemaking
														:milking
														:cooking
														:farming
														:plant-gathering
														:fishing
														:fish-cleaning
														:fish-dissection
														:hunting
														:furnace-operating
														:weaponsmithing
														:armor-smithing
														:blacksmithing
														:metalcrafting
														:gemcutting
														:gemsetting
														:woodcrafting
														:stonecrafting
														:bonecarving
														:glassmaking
														:strand-extracting
														:axe
														:sword
														:mace
														:hammer
														:spear
														nil
														:crossbow
														nil
														nil
														nil
														nil
														nil
														nil
														:siege-engineering
														:siege-operating
														:bowyer
														:mechanics
														nil
														:potash-making
														:lye-making
														:dyeing
														:wood-burning
														:pump-operating])
(def skills               {	:MINING "Mining"
														:WOODCUTTING "Wood Cutting"
														:CARPENTRY "Carpentry"
														:DETAILSTONE "Engraving"
														:MASONRY "Masonry"
														:ANIMALTRAIN "Animal Training"
														:ANIMALCARE "Animal Caretaking"
														:DISSECT_FISH "Fish Dissection"
														:DISSECT_VERMIN "Animal Dissection"
														:PROCESSFISH "Fish Cleaning"
														:BUTCHER "Butchery"
														:TRAPPING "Trapping"
														:TANNER "Tanning"
														:WEAVING "Weaving"
														:BREWING "Brewing"
														:ALCHEMY "Alchemy"
														:CLOTHESMAKING "Clothes Making"
														:MILLING "Milling"
														:PROCESSPLANTS "Threshing"
														:CHEESEMAKING "Cheese Making"
														:MILK "Milking"
														:COOK "Cooking"
														:PLANT "Growing"
														:HERBALISM "Herbalism"
														:FISH "Fishing"
														:SMELT "Furnace Operation"
														:EXTRACT_STRAND "Strand Extraction"
														:FORGE_WEAPON "Weaponsmithing"
														:FORGE_ARMOR "Armorsmithing"
														:FORGE_FURNITURE "Metalsmithing"
														:CUTGEM "Gem Cutting"
														:ENCRUSTGEM "Gem Setting"
														:WOODCRAFT "Wood Crafting"
														:STONECRAFT "Stone Crafting"
														:METALCRAFT "Metal Crafting"
														:GLASSMAKER "Glassmaking"
														:LEATHERWORK "Leatherworkering"
														:BONECARVE "Bone Carving"

														:AXE "Axe"
														:SWORD "Sword"
														:DAGGER "Knife"
														:MACE "Mace"
														:HAMMER "Hammer"
														:SPEAR "Spear"
														:CROSSBOW "Crossbow"
														:SHIELD "Shield"
														:ARMOR "Armor"
														:SIEGECRAFT "Siege Engineering"
														:SIEGEOPERATE "Siege Operation"
														:BOWYER "Bowmaking"
														:PIKE "Pike"
														:WHIP "Lash"
														:BOW "Bow"
														:BLOWGUN "Blowgun"
														:THROW "Throwing"

														:MECHANICS "Machinery"
														:MAGIC_NATURE "Nature"
														:SNEAK "Ambush"
														:DESIGNBUILDING "Building Design"
														:DRESS_WOUNDS "Wound Dressing"
														:DIAGNOSE "Diagnostics"
														:SURGERY "Surgery"
														:SET_BONE "Bone Setting"
														:SUTURE "Suturing"
														:CRUTCH_WALK "Crutch-walking"
														:WOOD_BURNING "Wood Burning"
														:LYE_MAKING "Lye Making"
														:SOAP_MAKING "Soap Making"
														:POTASH_MAKING "Potash Making"
														:DYER "Dyeing"
														:OPERATE_PUMP "Pump Operation"
														:SWIMMING "Swimming"
														:PERSUASION "Persuasion"
														:NEGOTIATION "Negotiation"
														:JUDGING_INTENT "Judging Intent"
														:APPRAISAL "Appraisal"
														:ORGANIZATION "Organization"
														:RECORD_KEEPING "Record Keeping"
														:LYING "Lying"
														:INTIMIDATION "Intimidation"
														:CONVERSATION "Conversation"
														:COMEDY "Comedy"
														:FLATTERY "Flattery"
														:CONSOLE "Consoling"
														:PACIFY "Pacification"
														:TRACKING "Tracking"
														:KNOWLEDGE_ACQUISITION "Studying"
														:CONCENTRATION "Concentration"
														:DISCIPLINE "Discipline"
														:SITUATIONAL_AWARENESS "Observation"
														:WRITING "Writing"
														:PROSE "Prose"
														:POETRY "Poetry"
														:READING "Reading"
														:SPEAKING "Speaking"
														:COORDINATION "Coordination"
														:BALANCE "Balance"
														:LEADERSHIP "Leadership"
														:TEACHING "Teaching"
														:MELEE_COMBAT "Fighting"
														:RANGED_COMBAT "Archery"
														:WRESTLING "Wrestling"
														:BITE "Biting"
														:GRASP_STRIKE "Striking"
														:STANCE_STRIKE "Kicking"
														:DODGING "Dodging"
														:MISC_WEAPON "Misc. Object"
														:KNAPPING "Knapping"
														:MILITARY_TACTICS "Military Tactics"
														:SHEARING "Shearing"
														:SPINNING "Spinning"
														:POTTERY "Pottery"
														:GLAZING "Glazing"
														:PRESSING "Pressing"
														:BEEKEEPING "Beekeeping"
														:WAX_WORKING "Wax Working"})