(ns dgc.config)

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
; * (though maybe this should be changed to take into account sherrifs)
; * Traits will be seen as min/max. If it is positive it will be seen as the minimum required. 
; * If a trait is negetive only dwarves with less than the value will be selected.
; */
(def professions {
    :admin {
        :leader {
            :skills [:consoling  :pacification]
            :attributes [:linguistic-ability :empathy :social-awareness  :analytical-ability :creatvity] ;LEmSaAaC
            :traits {
                :cooperation +39,
                :straightforwardness +39
            }
        }
        :broker {
            :skills [:appraisal  :judging-intent]
            :attributes [:intuition :empathy :social-awareness  :analytical-ability :memory :linguistic-ability] ;IEmSaAaML
        }
        :bookkeeper {
            :skills [:record-keeping]
            :attributes [:analytical-ability :memory :focus] ;AaMF
            :traits {}
        }
        :chief-medical-dwarf {
            :skills [:diagnostics]
            :attributes [:analytical-ability :memory :intuition] ;AaMI
            :traits {}
        }}
    :gathering {
        :miner {
            :skills [:mining]
            :attributes [:strength :toughness :endurance :willpower :spatial-sense :kinaesthetic-sense] ;STEWSsKs
        }
        :wood-cutter {
            :skills [:wood-cutting]
            :attributes [:strength :agility :endurance :willpower :spatial-sense :kinaesthetic-sense] ;SAEWSsKs
            
        }}
    :military {
        :ranged-soldier {
            :skills [:observation  :crossbow :hammer   :ambusher  :archer  :blowgunner  :bow]
            :attributes [:agility :focus :spatial-sense :kinaesthetic-sense] ;AFSsKs
        }
        :melee-soldier {
            :skills [:kick  :strike  :pike  :ax  :observation  :sword  :spear  :armour  :shield  :wrestling  :dodging  :mace  :dagger  :hammer  :whip  :blowgun  :throw  :fighting]
            :attributes [:strength :agility :toughness :willpower :spatial-sense :kinaesthetic-sense] ;SATWSsKs
        }
        :military-tactician {
            :skills [:military-tactician]
            :attributes [:analytical-ability :creatvity :intuition] ;AaCI
            
        }
        :wrestler {
            :skills [:wrestling]
            :attributes [:strength :agility :endurance :willpower :spatial-sense :kinaesthetic-sense] ;SAEWSsKs
            
        }
        :dodger {
            :skills [:dodging]
            :attributes [:agility :toughness :endurance :willpower :spatial-sense :kinaesthetic-sense] ;ATEWSsKs
            
        }
        :biter {
            :skills [:bitting]
            :attributes [:strength :toughness :endurance :willpower :spatial-sense :kinaesthetic-sense] ;STEWSsKs
            
        }
        :armor-user {
            :skills [:armor]
            :attributes [:strength :toughness :endurance :willpower :kinaesthetic-sense] ;STEWKs
            
        }
        :siege-operator {
            :skills [:siege-operation]
            :attributes [:strength :toughness :endurance :analytical-ability :focus :spatial-sense] ;STEAaFSs
            
        }
        :siege-engineer {
            :skills [:siege-engineering]
            :attributes [:strength :agility :endurance :analytical-ability :creatvity :spatial-sense] ;SAEAaCSs
            
        }}
    :crafts {
        :building-designer {
            :skills [:building-design]
            :attributes [:analytical-ability :creatvity :spatial-sense] ;AaCSs
            
        }
        :pump-operator {
            :skills [:pump-operation]
            :attributes [:strength :toughness :endurance :willpower :kinaesthetic-sense] ;STEWKs
            
        }
        :mechanic {
            :skills [:machinery]
            :attributes [:strength :agility :endurance :analytical-ability :creatvity :spatial-sense] ;SAEAaCSs
            
        }
        :strand-extractor {
            :skills [:strand-extracting]
            :attributes [:strength :agility :endurance :analytical-ability :kinaesthetic-sense] ;SAEAaKs
            
        }
        :wood-crafter {
            :skills [:wood-crafting]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs
            
        }
        :weaver {
            :skills [:weaving]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs,
            
        }
        :stone-crafter {
            :skills [:stone-crafting]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs
            
        }
        :leatherworker {
            :skills [:leatherworkering]
            :attributes [:strength :agility :endurance :creatvity :spatial-sense :kinaesthetic-sense] ;SAECSsKs
            
        }
        :glassmaker {
            :skills [:glassmaking]
            :attributes [:strength :agility :endurance :creatvity :spatial-sense :kinaesthetic-sense] ;SAECSsKs
            
        }
        :clothier {
            :skills [:clothiing]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs,
            
        }
        :bone-carver {
            :skills [:bone-carving]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs,
            
        }
        :gem-setter {
            :skills [:gem-setting]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs,
            
        }
        :gem-cutter {
            :skills [:gem-cutting]
            :attributes  [:agility :analytical-ability :spatial-sense :kinaesthetic-sense] ;AAaSsKs
            
        }
        :weaponsmith {
            :skills [:weaponsmithing]
            :attributes [:strength :agility :endurance :creatvity :spatial-sense :kinaesthetic-sense] ;SAECSsKs
            
        }
        :metalsmith {
            :skills [:metalsmithing]
            :attributes [:strength :agility :endurance :creatvity :spatial-sense :kinaesthetic-sense] ;SAECSsKs
            
        }
        :metal-crafter {
            :skills [:metal-crafting]
            :attributes [:strength :agility :endurance :creatvity :spatial-sense :kinaesthetic-sense] ;SAECSsKs
            
        }
        :armorsmith {
            :skills [:armorsmithing]
            :attributes [:strength :agility :endurance :creatvity :spatial-sense :kinaesthetic-sense] ;SAECSsKs
            
        }
        :furnace-operator {
            :skills [:furnace-operation]
            :attributes [:strength :toughness :endurance :analytical-ability :kinaesthetic-sense] ;STEAaKs
            
        }
        :fisherdwarf {
            :skills [:fishing]
            :attributes [:strength :agility :focus :patience :kinaesthetic-sense] ;SAFPKs
            
        }
        :fish-dissector {
            :skills [:fish-dissection]
            :attributes [:agility :kinaesthetic-sense]
            
        }
        :fish-cleaner {
            :skills [:fish-cleaning]
            :attributes [:agility :endurance :kinaesthetic-sense] ;AEKs
            
        }
        :wood-burner {
            :skills [:wood-burning]
            :attributes  [:strength :toughness :endurance :kinaesthetic-sense] ;STEKs
            
        }
        :thresher {
            :skills [:threshing]
            :attributes [:strength :agility :endurance :kinaesthetic-sense] ;SAEKs
            
        }
        :tanner {
            :skills [:tanning]
            :attributes [:agility :kinaesthetic-sense]
            
        }
        :soaper {
            :skills [:soap-making]
            :attributes  [:strength :toughness :endurance :kinaesthetic-sense] ;STEKs
            
        }
        :potash-maker {
            :skills [:potash-making]
            :attributes  [:strength :toughness :endurance :kinaesthetic-sense] ;STEKs
            
        }
        :miller {
            :skills [:milling]
            :attributes [:strength :agility :endurance :kinaesthetic-sense] ;SAEKs
            
        }
        :milker {
            :skills [:milking]
            :attributes [:strength :agility :endurance :kinaesthetic-sense] ;SAEKs
            
        }
        :lye-maker {
            :skills [:lye-making]
            :attributes  [:strength :toughness :endurance :kinaesthetic-sense] ;STEKs
            
        }
        :herbalist {
            :skills [:herbalist];??
            :attributes [:agility :memory :kinaesthetic-sense] ;AMKs
            
        }
        :grower {
            :skills [:growing]
            :attributes [:strength :agility :endurance :kinaesthetic-sense] ;SAEKs
            
        }
        :dyer {
            :skills [:dying];??
            :attributes [:strength :agility :endurance :kinaesthetic-sense] ;SAEKs
            
        }
        :cook {
            :skills [:cooking]
            :attributes [:agility :analytical-ability :creatvity :kinaesthetic-sense] ;AAaCKs
            
        }
        :cheese-maker {
            :skills [:cheese-making]
            :attributes [:strength :agility :endurance :analytical-ability :creatvity :kinaesthetic-sense] ;SAEAaCKs
            
        }
        :butcher {
            :skills [:butchery]
            :attributes [:strength :agility :endurance :kinaesthetic-sense] ;SAEKs
            
        }
        :brewer {
            :skills [:brewing]
            :attributes [:strength :agility :kinaesthetic-sense] ;SAKs
            
        }
        :wound-dresser {
            :skills [:wound-dressing]
            :attributes [:agility :spatial-sense :kinaesthetic-sense :empathy] ;ASsKsEm
            
        }
        :suturer {
            :skills [:suturing]
            :attributes [:agility :focus :spatial-sense :kinaesthetic-sense] ;AFSsKs
            
        }
        :surgeon {
            :skills [:surgery]
            :attributes [:agility :focus :spatial-sense :kinaesthetic-sense] ;AFSsKs
            
        }
        :crutch-walker {
            :skills [:crutch-walking]
            :attributes [:agility :endurance :willpower :spatial-sense :kinaesthetic-sense] ;AEWSsKs
            
        }
        :bone-doctor {
            :skills [:bone-setting]
            :attributes [:strength :agility :focus :spatial-sense :kinaesthetic-sense] ;SAFSsKs
            
        }
        :trapper {
            :skills [:trapping]
            :attributes [:agility :analytical-ability :creatvity :spatial-sense] ;AAaCSs
            
        }
        :animal-trainer {
            :skills [:animal-training]
            :attributes [:agility :toughness :endurance :intuition :patience :empathy] ;ATEIPEm
            
        }
        :animal-dissector {
            :skills [:small-animal-dissection]
            :attributes [:agility :kinaesthetic-sense]
            
        }
        :animal-caretaker {
            :skills [:animal-caretaking]
            :attributes [:agility :analytical-ability :memory :empathy] ;AAaMEm
            
        }
        :ambusher {
            :skills [:ambushing]
            :attributes [:agility :focus :spatial-sense :kinaesthetic-sense] ;AFSsKs
            
        }
        :mason {
            :skills [:masonry]
            :attributes [:strength :agility :endurance :creatvity :spatial-sense :kinaesthetic-sense] ;SAECSsKs
            
        }
        :engraver {
            :skills [:engraving]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs,
            
        }
        :carpenter {
            :skills [:carpentry]
            :attributes [:strength :agility :creatvity :spatial-sense :kinaesthetic-sense] ;SACSsKs
            
        }
        :bowyer {
            :skills [:bowmaking]
            :attributes [:agility :creatvity :spatial-sense :kinaesthetic-sense] ;ACSsKs,
            
        }}
    :misc {
        :socializer {
            :skills [:conversationalist  :flatterer  :negotiator  :persuader  :consoler  :leader  :pacifier  :teacher]
            :attributes [:linguistic-ability :empathy :social-awareness] ;LEmSa
            :traits {}
        }
        :balance {
            :skills [:balance]
            :attributes [:strength :agility :kinaesthetic-sense] ;SAKs
            
        }
        :coordination {
            :skills [:coordination]
            :attributes [:agility :kinaesthetic-sense :kinaesthetic-sense] ;ASsK 
            
        }
        :speaker {
            :skills [:speaking]
            :attributes [:linguistic-ability] ;L
            
        }
        :reader {
            :skills [:reading]
            :attributes [:memory :focus :linguistic-ability] ;MFL
            
        }
        :writer {
            :skills [:writing]
            :attributes [:creatvity :intuition :linguistic-ability] ;CIL
            
        }
        :wordsmith {
            :skills [:wordsmith]
            :attributes [:creatvity :intuition :linguistic-ability] ;CIL
            
        }
        :poet {
            :skills [:poet]
            :attributes [:creatvity :intuition :linguistic-ability] ;CIL
            
        }
        :disciplinarian {
            :skills [:disciplinarian]
            :attributes [:disease-resistance :willpower] ;FW
            
        }
        :tracker {
            :skills [:tracking]
            :attributes [:analytical-ability :focus :spatial-sense] ;AaFSs
            
        }
        :druid {
            :skills [:druid]
            :attributes [:agility :toughness :endurance :focus :willpower :empathy] ;ATEFWEm
            
        }
        :student {
            :skills [:student]
            :attributes [:analytical-ability :memory :focus] ;AaMF
            
        }
        :observer {
            :skills [:observation]
            :attributes [:intuition :focus :spatial-sense] ;IFSs
            
        }
        :concentrator {
            :skills [:concentrator]
            :attributes [:focus :willpower :patience] ;FWP
            
        } 
        :liar {
            :skills [:lying]
            :attributes  [:creatvity :linguistic-ability :social-awareness] ;CLSa
            :traits {
                :straightforwardness -61
            }
        }
        :judge-of-intent {
            :skills [:judging-intent]
            :attributes [:intuition :empathy :social-awareness] ;IEmSa
            
        }
        :intimidator {
            :skills [:intimidation]
            :attributes [:agility :kinaesthetic-sense :linguistic-ability] ;AKsL
            :traits {
                :cooperation -61
            }
        }
        :record-keeper {
            :skills [:record-keeping]
            :attributes [:analytical-ability :memory :focus] ;AaMF
            
        }
        :organizer {
            :skills [:organization]
            :attributes [:analytical-ability :creatvity :social-awareness] ;AaCSa
            
        }
        :appraiser {
            :skills [:appraisal]
            :attributes [:analytical-ability :memory :intuition] ;AaMI
            
        }
        :comedian {
            :skills [:comedy]
            :attributes [:agility :creatvity :kinaesthetic-sense :linguistic-ability] ;ACKsL
            :traits {
                :self-consciousness -76
            }
        }
        :knapper {
            :skills [:knapping]
            :attributes [:strength :agility :analytical-ability :spatial-sense :kinaesthetic-sense] ;SAAaSsKs
            
        }
        :swimmer {
            :skills [:swimming]
            :attributes [:strength :agility :endurance :willpower :spatial-sense :kinaesthetic-sense] ;SAEWSsKs
            
        }
        :alchemist {
            :skills [:alchemist]
            :attributes [:agility :analytical-ability :creatvity :intuition] ;AAaCI
            
        }}})


