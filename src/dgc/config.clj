(ns dgc.config
  (:use [dgc.util]))

;TODO: Psychological Trauma  ???
;TODO: Cave adaption

(def physical-attributes  [ :strength
                            :agility
                            :toughness
                            :endurance
                            :recuperation
                            :disease-resistance])
(def mental-attributes    [ :analytical-ability
                            :fucus
                            :willpower
                            :creativity
                            :intuition
                            :patience
                            :memory
                            :linguistic-ability
                            :spatial-sense
                            :musicality
                            :kinaesthetic-sense
                            :empathy
                            :social-aweareness])
(def traits               [ :anxiety
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
(def skills               { :MINING "Mining"
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

; Attributes are between 0-5000
; Attributes have gened values of [min, avg, max]
; Traits are between 0-100 with avg displayed
; All skills are 0 by default
(def default-puffball {
    :attributes {
        :spatial-sense [700, 1516.66, 2500]
        :focus [700, 1516.66, 2500]
        :analytical-ability [450, 1266.66, 2250]
        :strength [450, 1266.66, 2250]
        :patience [450, 1266.66, 2250]
        :toughness [450, 1266.66, 2250]
        :creatvity [450, 1266.66, 2250]
        :memory [450, 1266.66, 2250]
        :musicality [200, 1000, 2000]
        :empathy [200, 1000, 2000]
        :social-awareness [200, 1000, 2000]
        :endurance [200, 1000, 2000]
        :disease-resistance [200, 1000, 2000]
        :recuperation [200, 1000, 2000]
        :intuition [200, 1000, 2000]
        :willpower [200, 1000, 2000]
        :kinaesthetic-sense [200, 1000, 2000]
        :linguistic-ability [200, 1000, 2000]
        :agility [150, 862.5, 1500]
    }
    :traits {
        :emotionality 50,
        :excitement-seeking 50,
        :friendliness 50,
        :gregariousness 50,
        :achievement-striving 50,
        :imagination 50,
        :activity-level 50,
        :immoderation 55,
        :adventurousness 50,
        :intellectual-curiosity 50,
        :artistic-interest 50,
        :self-consciousness 50,
        :assertiveness 50,
        :self-discipline 50,
        :cheerfulness 50,
        :straightforwardness 55,
        :cooperation 50,
        :sympathy 50,
        :depression 50,
        :trust 50,
        :dutifulness 50,
        :vulnerability 45,
        :altruism 50,
        :liberalism 50,
        :anger 50,
        :modesty 50,
        :anxiety 50,
        :orderliness 50,
        :cautiousness 50,
        :self-efficacy 50
    }
})


;/*
; * New professions can be easily added!
; * A job is a collection of attributes, skills and traits that work well together
; * Skills and attributes will be assumed to have a positive influence on job performance
; * (though maybe this should be changed to, for example, take into account sherrifs)
; * Traits will be seen as min/max. If it is positive it will be seen as the minimum required. 
; * If a trait is negetive only dwarves with less than the value will be selected, it's treated as a positive max.
; */
(def professions (atom (in "professions.clj")))


