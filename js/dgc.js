var dwarves = [];
var defaultDwarf = {
	attributes: {
		"strength": 1250,
		"musicality": 1000,
		"agility": 900,
		"empathy": 1000,
		"toughness": 1250,
		"social awareness": 1000,
		"endurance": 1000,
		"disease resistance": 1000,
		"recuperation": 1000,
		"analytical ability": 1250,
		"memory": 1250,
		"creatvity": 1250,
		"intuition": 1000,
		"focus": 1500,
		"willpower": 1000,
		"patience": 1250,
		"spatial sense": 1500,
		"kinaesthetic sense": 1000,
		"linguistic ability": 1000
	},
	traits: {
		"emotionality": 50,
		"excitement seeking": 50,
		"friendliness": 50,
		"gregariousness": 50,
		"achievement striving": 50,
		"imagination": 50,
		"activity level": 50,
		"immoderation": 55,
		"adventurousness": 50,
		"intellectual curiosity": 50,
		"artistic interest": 50,
		"self-consciousness": 50,
		"assertiveness": 50,
		"self-discipline": 50,
		"cheerfulness": 50,
		"straightforwardness": 55,
		"cooperation": 50,
		"sympathy": 50,
		"depression": 50,
		"trust": 50,
		"dutifulness": 50,
		"vulnerability": 45,
		"altruism": 50,
		"liberalism": 50,
		"anger": 50,
		"modesty": 50,
		"anxiety": 50,
		"orderliness": 50,
		"cautiousness": 50,
		"self-efficacy": 50
	},
	skills: {}
}
var urist = {};
//TODO: this is a bit silly!
urist.skills = Object.create(defaultDwarf.skills);
urist.attributes = Object.create(defaultDwarf.attributes);
urist.traits = Object.create(defaultDwarf.traits);

/*
 * New jobs can be easily added!
 * A job is a collection of attributes, skills and traits that work well together
 * Skills and attributes will be assumed to have a positive influence on job performance
 * (though maybe this should be changed to take into account sherrifs)
 * Traits will be seen as min/max. If it is positive it will be seen as the minimum required. 
 * If a trait is negetive only dwarves with less than the value will be selected.
 */
var jobs = {
    'leader/manager': {
		skills: ['consoling', 'pacification'],
		attributes: ['linguistic ability','empathy','social awareness', 'analytical ability','creatvity'], //LEmSaAaC,
		traits: {
			'cooperation': +39,
			'straightforwardness': +39
		}
	},
    'broker': {
		skills: ['appraisal', 'judging intent'],
		attributes: ['intuition','empathy','social awareness', 'analytical ability','memory','linguistic ability'], //IEmSaAaML,
		traits: {}
	},
    'bookkeeper': {
		skills: ['record keeping'],
		attributes: ['analytical ability','memory','focus'], //AaMF,
		traits: {}
	},
    'chief medical dwarf': {
		skills: ['diagnostics'],
		attributes: ['analytical ability','memory','intuition'], //AaMI,
		traits: {}
	},
    'ranged soldier': {
		skills: ['observation', 'crossbow','hammer',  'ambusher', 'archer', 'blowgunner', 'bow'],
		attributes: ['agility','focus','spatial sense','kinaesthetic sense'], //AFSsKs,
		traits: {}
	},
    'melee soldier': {
		skills: ['kick', 'strike', 'pike', 'ax', 'observation', 'sword', 'spear', 'armour', 'shield', 'wrestling', 'dodging', 'mace', 'dagger', 'hammer', 'whip', 'blowgun', 'throw', 'fighting'],
		attributes: ['strength','agility','toughness','willpower','spatial sense','kinaesthetic sense'], //SATWSsKs,
		traits: {}
	},
    'socializer': {
		skills: ['conversationalist', 'flatterer', 'negotiator', 'persuader', 'consoler', 'leader', 'pacifier', 'teacher'],
		attributes: ['linguistic ability','empathy','social awareness'], //LEmSa,
		traits: {}
	},
    'balance': {
		skills: ['balance'],
		attributes: ['strength','agility','kinaesthetic sense'], //SAKs,
		traits: {},
	},
    'coordination': {
		skills: ['coordination'],
		attributes: ['agility','kinaesthetic sense','kinaesthetic sense'], //ASsK ,
		traits: {},
	},
    'speaker': {
		skills: ['speaking'],
		attributes: ['linguistic ability'], //L,
		traits: {},
	},
    'reader': {
		skills: ['reading'],
		attributes: ['memory','focus','linguistic ability'], //MFL,
		traits: {},
	},
    'writer': {
		skills: ['writing'],
		attributes: ['creatvity','intuition','linguistic ability'], //CIL,
		traits: {},
	},
    'wordsmith': {
		skills: ['wordsmith'],
		attributes: ['creatvity','intuition','linguistic ability'], //CIL,
		traits: {},
	},
    'poet': {
		skills: ['poet'],
		attributes: ['creatvity','intuition','linguistic ability'], //CIL,
		traits: {},
	},
    'disciplinarian': {
		skills: ['disciplinarian'],
		attributes: ['disease resistance','willpower'], //FW,
		traits: {},
	},
    'tracker': {
		skills: ['tracking'],
		attributes: ['analytical ability','focus','spatial sense'], //AaFSs,
		traits: {},
	},
    'druid': {
		skills: ['druid'],
		attributes: ['agility','toughness','endurance','focus','willpower','empathy'], //ATEFWEm,
		traits: {},
	},
    'military tactician': {
		skills: ['military tactician'],
		attributes: ['analytical ability','creatvity','intuition'], //AaCI,
		traits: {},
	},
    'student': {
		skills: ['student'],
		attributes: ['analytical ability','memory','focus'], //AaMF,
		traits: {},
	},
    'observer': {
		skills: ['observation'],
		attributes: ['intuition','focus','spatial sense'], //IFSs,
		traits: {},
	},
    'concentrator': {
		skills: ['concentrator'],
		attributes: ['focus','willpower','patience'], //FWP,
		traits: {},
	}, 
    'liar': {
		skills: ['lying'],
		attributes:  ['creatvity','linguistic ability','social awareness'], //CLSa,
		traits: {
			'straightforwardness': -61
		},
	},
    'judge of intent': {
		skills: ['judging intent'],
		attributes: ['intuition','empathy','social awareness'], //IEmSa,
		traits: {},
	},
    'intimidator': {
		skills: ['intimidation'],
		attributes: ['agility','kinaesthetic sense','linguistic ability'], //AKsL,
		traits: {
			'cooperation': -61
		},
	},
    'comedian': {
		skills: ['comedy'],
		attributes: ['agility','creatvity','kinaesthetic sense','linguistic ability'], //ACKsL,
		traits: {
			'self-consciousness': -76
		},
	},
    'wrestler': {
		skills: ['wrestling'],
		attributes: ['strength','agility','endurance','willpower','spatial sense','kinaesthetic sense'], //SAEWSsKs,
		traits: {},
	},
    'dodger': {
		skills: ['dodging'],
		attributes: ['agility','toughness','endurance','willpower','spatial sense','kinaesthetic sense'], //ATEWSsKs,
		traits: {},
	},
    'biter': {
		skills: ['bitting'],
		attributes: ['strength','toughness','endurance','willpower','spatial sense','kinaesthetic sense'], //STEWSsKs
		traits: {},
	},
    'armor user': {
		skills: ['armor'],
		attributes: ['strength','toughness','endurance','willpower','kinaesthetic sense'], //STEWKs,
		traits: {},
	},
    'record keeper': {
		skills: ['record keeping'],
		attributes: ['analytical ability','memory','focus'], //AaMF,
		traits: {},
	},
    'organizer': {
		skills: ['organization'],
		attributes: ['analytical ability','creatvity','social awareness'], //AaCSa,
		traits: {},
	},
    'building designer': {
		skills: ['building design'],
		attributes: ['analytical ability','creatvity','spatial sense'], //AaCSs,
		traits: {},
	},
    'appraiser': {
		skills: ['appraisal'],
		attributes: ['analytical ability','memory','intuition'], //AaMI,
		traits: {},
	},
    'knapper': {
		skills: ['knapping'],
		attributes: ['strength','agility','analytical ability','spatial sense','kinaesthetic sense'], //SAAaSsKs,
		traits: {},
	},
    'swimmer': {
		skills: ['swimming'],
		attributes: ['strength','agility','endurance','willpower','spatial sense','kinaesthetic sense'], //SAEWSsKs,
		traits: {},
	},
    'alchemist': {
		skills: ['alchemist'],
		attributes: ['agility','analytical ability','creatvity','intuition'], //AAaCI,
		traits: {},
	},
    'siege operator': {
		skills: ['siege operation'],
		attributes: ['strength','toughness','endurance','analytical ability','focus','spatial sense'], //STEAaFSs,
		traits: {},
	},
    'siege engineer': {
		skills: ['siege engineering'],
		attributes: ['strength','agility','endurance','analytical ability','creatvity','spatial sense'], //SAEAaCSs,
		traits: {},
	},
    'pump operator': {
		skills: ['pump operation'],
		attributes: ['strength','toughness','endurance','willpower','kinaesthetic sense'], //STEWKs,
		traits: {},
	},
    'mechanic': {
		skills: ['machinery'],
		attributes: ['strength','agility','endurance','analytical ability','creatvity','spatial sense'], //SAEAaCSs,
		traits: {},
	},
    'strand extractor': {
		skills: ['strand extracting'],
		attributes: ['strength','agility','endurance','analytical ability','kinaesthetic sense'], //SAEAaKs,
		traits: {},
	},
    'wood crafter': {
		skills: ['wood crafting'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,
		traits: {},
	},
    'weaver': {
		skills: ['weaving'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,,
		traits: {},
	},
    'stone crafter': {
		skills: ['stone crafting'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,
		traits: {},
	},
    'leatherworker': {
		skills: ['leatherworkering'],
		attributes: ['strength','agility','endurance','creatvity','spatial sense','kinaesthetic sense'], //SAECSsKs,
		traits: {},
	},
    'glassmaker': {
		skills: ['glassmaking'],
		attributes: ['strength','agility','endurance','creatvity','spatial sense','kinaesthetic sense'], //SAECSsKs,
		traits: {},
	},
    'clothier': {
		skills: ['clothiing'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,,
		traits: {},
	},
    'bone carver': {
		skills: ['bone carving'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,,
		traits: {},
	},
    'gem setter': {
		skills: ['gem setting'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,,
		traits: {},
	},
    'gem cutter': {
		skills: ['gem cutting'],
		attributes:  ['agility','analytical ability','spatial sense','kinaesthetic sense'], //AAaSsKs,
		traits: {},
	},
    'weaponsmith': {
		skills: ['weaponsmithing'],
		attributes: ['strength','agility','endurance','creatvity','spatial sense','kinaesthetic sense'], //SAECSsKs,
		traits: {},
	},
    'metalsmith': {
		skills: ['metalsmithing'],
		attributes: ['strength','agility','endurance','creatvity','spatial sense','kinaesthetic sense'], //SAECSsKs,
		traits: {},
	},
    'metal crafter': {
		skills: ['metal crafting'],
		attributes: ['strength','agility','endurance','creatvity','spatial sense','kinaesthetic sense'], //SAECSsKs
		traits: {},
	},
    'furnace operator': {
		skills: ['furnace operation'],
		attributes: ['strength','toughness','endurance','analytical ability','kinaesthetic sense'], //STEAaKs,
		traits: {},
	},
    'armorsmith': {
		skills: ['armorsmithing'],
		attributes: ['strength','agility','endurance','creatvity','spatial sense','kinaesthetic sense'], //SAECSsKs,
		traits: {},
	},
    'fisherdwarf': {
		skills: ['fishing'],
		attributes: ['strength','agility','focus','patience','kinaesthetic sense'], //SAFPKs,
		traits: {},
	},
    'fish dissector': {
		skills: ['fish dissection'],
		attributes: ['agility','kinaesthetic sense'],
		traits: {},
	},
    'fish cleaner': {
		skills: ['fish cleaning'],
		attributes: ['agility','endurance','kinaesthetic sense'], //AEKs,
		traits: {},
	},
    'wood burner': {
		skills: ['wood burning'],
		attributes:  ['strength','toughness','endurance','kinaesthetic sense'], //STEKs,
		traits: {},
	},
    'thresher': {
		skills: ['threshing'],
		attributes: ['strength','agility','endurance','kinaesthetic sense'], //SAEKs,
		traits: {},
	},
    'tanner': {
		skills: ['tanning'],
		attributes: ['agility','kinaesthetic sense'],
		traits: {},
	},
    'soaper': {
		skills: ['soap making'],
		attributes:  ['strength','toughness','endurance','kinaesthetic sense'], //STEKs,
		traits: {},
	},
    'potash maker': {
		skills: ['potash making'],
		attributes:  ['strength','toughness','endurance','kinaesthetic sense'], //STEKs,
		traits: {},
	},
    'miller': {
		skills: ['milling'],
		attributes: ['strength','agility','endurance','kinaesthetic sense'], //SAEKs,
		traits: {},
	},
    'milker': {
		skills: ['milking'],
		attributes: ['strength','agility','endurance','kinaesthetic sense'], //SAEKs,
		traits: {},
	},
    'lye maker': {
		skills: ['lye making'],
		attributes:  ['strength','toughness','endurance','kinaesthetic sense'], //STEKs,
		traits: {},
	},
    'herbalist': {
		skills: ['herbalist'],//??
		attributes: ['agility','memory','kinaesthetic sense'], //AMKs,
		traits: {},
	},
    'grower': {
		skills: ['growing'],
		attributes: ['strength','agility','endurance','kinaesthetic sense'], //SAEKs,
		traits: {},
	},
    'dyer': {
		skills: ['dying'],//??
		attributes: ['strength','agility','endurance','kinaesthetic sense'], //SAEKs,
		traits: {},
	},
    'cook': {
		skills: ['cooking'],
		attributes: ['agility','analytical ability','creatvity','kinaesthetic sense'], //AAaCKs,
		traits: {},
	},
    'cheese maker': {
		skills: ['cheese making'],
		attributes: ['strength','agility','endurance','analytical ability','creatvity','kinaesthetic sense'], //SAEAaCKs,
		traits: {},
	},
    'butcher': {
		skills: ['butchery'],
		attributes: ['strength','agility','endurance','kinaesthetic sense'], //SAEKs,
		traits: {},
	},
    'brewer': {
		skills: ['brewing'],
		attributes: ['strength','agility','kinaesthetic sense'], //SAKs,
		traits: {},
	},
    'wound dresser': {
		skills: ['wound dressing'],
		attributes: ['agility','spatial sense','kinaesthetic sense','empathy'], //ASsKsEm,
		traits: {},
	},
    'suturer': {
		skills: ['suturing'],
		attributes: ['agility','focus','spatial sense','kinaesthetic sense'], //AFSsKs,
		traits: {},
	},
    'surgeon': {
		skills: ['surgery'],
		attributes: ['agility','focus','spatial sense','kinaesthetic sense'], //AFSsKs,
		traits: {},
	},
    'crutch walker': {
		skills: ['crutch walking'],
		attributes: ['agility','endurance','willpower','spatial sense','kinaesthetic sense'], //AEWSsKs,
		traits: {},
	},
    'bone doctor': {
		skills: ['bone setting'],
		attributes: ['strength','agility','focus','spatial sense','kinaesthetic sense'], //SAFSsKs,
		traits: {},
	},
    'trapper': {
		skills: ['trapping'],
		attributes: ['agility','analytical ability','creatvity','spatial sense'], //AAaCSs,
		traits: {},
	},
    'animal trainer': {
		skills: ['animal training'],
		attributes: ['agility','toughness','endurance','intuition','patience','empathy'], //ATEIPEm,
		traits: {},
	},
    'animal dissector': {
		skills: ['small animal dissection'],
		attributes: ['agility','kinaesthetic sense'],
		traits: {},
	},
    'animal caretaker': {
		skills: ['animal caretaking'],
		attributes: ['agility','analytical ability','memory','empathy'], //AAaMEm,
		traits: {},
	},
    'ambusher': {
		skills: ['ambushing'],
		attributes: ['agility','focus','spatial sense','kinaesthetic sense'], //AFSsKs,
		traits: {},
	},
    'mason': {
		skills: ['masonry'],
		attributes: ['strength','agility','endurance','creatvity','spatial sense','kinaesthetic sense'], //SAECSsKs,
		traits: {},
	},
    'engraver': {
		skills: ['engraving'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,,
		traits: {},
	},
    'wood cutter': {
		skills: ['wood cutting'],
		attributes: ['strength','agility','endurance','willpower','spatial sense','kinaesthetic sense'], //SAEWSsKs,
		traits: {},
	},
    'carpenter': {
		skills: ['carpentry'],
		attributes: ['strength','agility','creatvity','spatial sense','kinaesthetic sense'], //SACSsKs,
		traits: {},
	},
    'bowyer': {
		skills: ['bowmaking'],
		attributes: ['agility','creatvity','spatial sense','kinaesthetic sense'], //ACSsKs,,
		traits: {},
	},
    'miner': {
		skills: ['mining'],
		attributes: ['strength','toughness','endurance','willpower','spatial sense','kinaesthetic sense'], //STEWSsKs,
		traits: {},
	}
};

delete jobs['knapper'];
delete jobs['alchemist'];
delete jobs['animal caretaker'];
delete jobs['ambusher'];
delete jobs['balance'];
delete jobs['coordination'];
delete jobs['speaker'];
delete jobs['reader'];
delete jobs['writer'];
delete jobs['wordsmith'];
delete jobs['poet'];
delete jobs['disciplinarian'];
delete jobs['tracker'];
delete jobs['druid'];
delete jobs['military tactician'];
delete jobs['concentrator'];


var attributeFunctionMap = {
	'strength': 'plusattrtopct',
	'agility': 'minusattrtopct',
	'toughness': 'plusattrtopct',
	'endurance': 'avgattrtopct',
	'disease resistance': 'avgattrtopct',
	'recuperation': 'avgattrtopct',
	'analytical ability': 'plusattrtopct',
	'memory': 'plusattrtopct',
	'creatvity': 'plusattrtopct',
	'intuition': 'avgattrtopct',
	'focus': 'dubplusattrtopct',
	'willpower': 'avgattrtopct',
	'patience': 'plusattrtopct',
	'spatial sense': 'dubplusattrtopct',
	'kinaesthetic sense': 'avgattrtopct',
	'linguistic ability': 'avgattrtopct',
	'musicality': 'avgattrtopct',
	'empathy': 'avgattrtopct',
	'social awareness': 'avgattrtopct'
}

var attributeFunctions = {
    minusattrtopct: function(x) {
    var xpct;
    if (x <= 599) {
        xpct = ((0.0370356 * x) - 5.51835)
    } else if (x >= 600 && x <= 799) {
        xpct = ((0.0837487 * x) - 33.5822)
    } else if (x >= 800 && x <= 899) {
        xpct = ((0.168333 * x) - 101.333)
    } else if (x >= 900 && x <= 999) {
        xpct = ((0.168343 * x) - 101.509)
    } else if (x >= 1000 && x <= 1099) {
        xpct = ((0.168343 * x) - 101.676)
    } else if (x >= 1100) {
        xpct = ((0.0417669 * x) + 37.3904)
    };
    return xpct;
},
    avgattrtopct: function(x) {
    var xpct;
    if (x <= 749) {
        xpct = ((0.030357 * x) - 6.0714)
    } else if (x >= 750 && x <= 899) {
        xpct = ((0.111852 * x) - 67.2223)
    } else if (x >= 900 && x <= 999) {
        xpct = ((0.168333 * x) - 118.166)
    } else if (x >= 1000 && x <= 1099) {
        xpct = ((0.168343 * x) - 118.343)
    } else if (x >= 1100 && x <= 1299) {
        xpct = ((0.0837538 * x) - 25.4621)
    } else if (x >= 1300) {
        xpct = ((0.0238412 * x) + 52.3404)
    };
    return xpct;
},
    plusattrtopct: function(x) {
    var xpct;
    if (x <= 949) {
        xpct = ((0.0333988 * x) - 15.0295)
    } else if (x >= 950 && x <= 1149) {
        xpct = ((0.0837487 * x) - 62.8943)
    } else if (x >= 1150 && x <= 1249) {
        xpct = ((0.168333 * x) - 160.249)
    } else if (x >= 1250 && x <= 1349) {
        xpct = ((0.168343 * x) - 160.429)
    } else if (x >= 1350 && x <= 1549) {
        xpct = ((0.0837538 * x) - 46.4006)
    } else if (x >= 1550) {
        xpct = ((0.0238412 * x) + 46.3801)
    };
    return xpct;
},
    dubplusattrtopct: function(x) {
    var xpct;
    if (x <= 1199) {
        xpct = ((0.0333988 * x) - 23.3792)
    } else if (x >= 1200 && x <= 1399) {
        xpct = ((0.0837487 * x) - 83.8315)
    } else if (x >= 1400 && x <= 1499) {
        xpct = ((0.168333 * x) - 202.333)
    } else if (x >= 1500 && x <= 1599) {
        xpct = ((0.168343 * x) - 202.515)
    } else if (x >= 1600 && x <= 1799) {
        xpct = ((0.0837538 * x) - 67.339)
    } else if (x >= 1800) {
        xpct = ((0.0238412 * x) + 40.4198)
    };
    return xpct;
}
};
var changelog = [''];

var xmlDoc;
var microsoft = false;
var currentDwarf;

if(typeof console === "undefined") {
    console = { log: function() { } };
}

function sum(nums) {
	if (nums.length == 0){
		return 0;
	}
	return nums.reduce(function(x,y){return x+y;});
}
function avg(nums) {
	return sum(nums) / nums.length;
}

/*Alias for getElementById*/
function gid(id, doc) {
	return (doc || document).getElementById(id);
};

/*So we can clone objects easily*/
Object.create = function (o) {
    function F() {}
    F.prototype = o;
    return new F();
};

String.prototype.toCapitalize = function() { 
   return this.toLowerCase().replace(/^.|\s\S/g, function(a) { return a.toUpperCase(); });
}

function disableEnterKey(e) {
    var key;
    if (window.event) key = window.event.keyCode; //IE
    else key = e.which; //firefox
    return (key != 13);
}

function disablebodyenter(e) {
    var key;
    if (window.event) key = window.event.keyCode; //IE
    else key = e.which; //firefox
    if (key == 13) {
        directBodySubmit();
        gid("directbodynum").value = ""
    };
    return (key != 13);
}

function disablesoulenter(e) {
    var key;
    if (window.event) key = window.event.keyCode; //IE
    else key = e.which; //firefox
    if (key == 13) {
        directSoulSubmit();
        gid("directsoulnum").value = ""
    };
    return (key != 13);
}

function disablepersenter(e) {
    var key;
    if (window.event) key = window.event.keyCode; //IE
    else key = e.which; //firefox
    if (key == 13) {
        directTraitSubmit();
        gid("directpersnum").value = ""
    };
    return (key != 13);
}

function disableverbenter(e) {
    var key;
    if (window.event) key = window.event.keyCode; //IE
    else key = e.which; //firefox
    if (key == 13) {
        var update = verbalevalsubmit(gid("verbaleval").value);
		if (update){
			var dwarf = dwarves[currentDwarf] || urist;
			verbalUpdate(dwarf, update);
			changelogupdate(update[1] + " set to " + update[2] + " by Counselor.");
			gid("verbaleval").value = "";
		}
		attributeUpdate();
		traitUpdate();
		guide();
    };
    return (key != 13);
}

function getFiles(){
	
	//tries getting files of format dwarves(-n)?.xml
	
	var base = 'Dwarves';
	var ext = 'xml';
	var files = {};
	var file;
	
	var filename = base+'.'+ext;
	if (file = getFile('Dwarves.xml')){
		files[filename] = file;
	}
	
	for (var i=0; i<20; i++) {
		filename = base+'-'+i+'.'+ext;
		if (file = getFile(filename)){
			files[filename] = file;
		}
	}
	return files;
}

function getFile(filename){
	filename = 'xml/'+filename;

	var xhttp = new XMLHttpRequest();
    try {
        xhttp.open('GET', filename, false);
        xhttp.send(null);
        var xmlDoc = xhttp.responseXML;
    } catch (e) {
		if (e.name !== 'NS_ERROR_DOM_BAD_URI') {
			window.microsoft = true;
			var xml = document.createElement("xml");
			xml.src = filename;
			document.body.appendChild(xml);
			var xmlDoc = xml.XMLDocument;
			document.body.removeChild(xml);
		}
    }
	
	return xmlDoc || false;
}

function option(text, value){
	var o = document.createElement("option");
	o.text = text;
	o.value = value || text;
	return o;
}

function xmlUpdate() {
	
	var fileList = gid('filelist');
	var currentFile = fileList.value || false;
	
	fileList.innerHTML = ''; //faster than removing DOM nodes
	
	var files = getFiles();
	console.log('files:', files);
	
	fileList.options.add(option('No File', ''), fileList.options.length);
	var altFile;
	for (f in files){
		fileList.options.add(option(f), fileList.options.length);
		altFile = f;
	}
	
	currentFile = currentFile || altFile;
	
	fileList.value = currentFile;
	
	//selectedIndex
	if (!currentFile) {
		return;
	}
	console.log('loading ' + currentFile)
	xmlDoc = files[currentFile];
	dwarves = [];
	
    if (microsoft) {
        var dwarfCount = xmlDoc.getElementsByTagName("Creatures")[0].childNodes.length;
    } else {
        var dwarfCount = ((xmlDoc.getElementsByTagName("Creatures")[0].childNodes.length - 1) / 2);
    }
    dwarfCount = parseInt(dwarfCount);
    for (var currentDwarf=0; currentDwarf < dwarfCount; currentDwarf++) {
	
		var dwarf = {};
		xmlDwarf = xmlDoc.getElementsByTagName("Creature")[currentDwarf];
		
		//console.log(currentDwarf +'/'+dwarfCount, xmlDwarf)
		
        try {
            dwarf.name = xmlDwarf.getElementsByTagName("Nickname")[0].childNodes[0].nodeValue;
        } catch (e) {
            dwarf.name = xmlDwarf.getElementsByTagName("Name")[0].childNodes[0].nodeValue;
        }
		
		var sex = xmlDwarf.getElementsByTagName("Sex")[0].childNodes[0].nodeValue;
		dwarf.gender = sex;
		
		//update attributes
		var xmlAttributes = xmlDwarf.getElementsByTagName('Attributes')[0].childNodes;
		dwarf.attributes = Object.create(defaultDwarf.attributes);
		
		for (var i = 0; i < xmlAttributes.length-1; i++) if (xmlAttributes[i].nodeType == 1) {
			var attrName = xmlAttributes[i].nodeName.replace(/([^A-Z]+)([A-Z])/g,'$1 $2').toLowerCase();
			var attrValue = parseInt(xmlAttributes[i].textContent);
			dwarf.attributes[attrName] = attrValue;
		}
		
		//update skills
		var numberRegex = /.*\[([^\]]+)\].*/;
		var xmlSkills = xmlDwarf.getElementsByTagName('Skill');
		dwarf.skills = Object.create(defaultDwarf.skills);
		
		for (var i = 0; i < xmlSkills.length-1; i++) if (xmlSkills[i].nodeType == 1) {
			var skillName = xmlSkills[i].getElementsByTagName('Name')[0].textContent.toLowerCase();
			var skillLevel = xmlSkills[i].getElementsByTagName('Level')[0].textContent;
			skillLevel = parseInt(numberRegex.exec(skillLevel)[1]);
			dwarf.skills[skillName] = skillLevel;
		}
		
		//update traits

		var xmlTraits = xmlDwarf.getElementsByTagName('Trait');
		dwarf.traitTexts = [];
		dwarf.traits = Object.create(defaultDwarf.traits);
		
		for (var i = 0; i < xmlTraits.length-1; i++) {
			var traitText = xmlTraits[i].childNodes[0].nodeValue;
			dwarf.traitTexts.push(traitText);
			var update = verbalevalsubmit(traitText);
			verbalUpdate(dwarf, update);
		}
		
		dwarf.attributeSum = 0;
		for (var a in dwarf.attributes) {
			dwarf.attributeSum = dwarf.attributeSum + dwarf.attributes[a];
		}
		
		dwarves[currentDwarf] = dwarf;
    }
	// Sort dwarves alphebetically
	// Doesn't really make a difference however it's nice for drop downs
	var compareDwarves = function(a,b) {
		if (a.name < b.name)
			return -1;
		if (a.name > b.name)
			return 1;
		return 0;
	}
	dwarves.sort(compareDwarves);

	var dwarfList = $("#dwarves ol").empty();
		
	//add select options
    for (var d in dwarves) {
		dwarves[d].index = d;
		
		dwarfList.append(dwarfHTML(dwarves[d]));
	}
	
    for (var j in jobs) {
		var jList = gid("joblist");
		jList.options.add(option(j.toCapitalize(), j), jList.options.length);
	}
	
	currentDwarf = undefined;
}

function dwarfHTML(d) {
	return '<li class="ui-widget-content dwarf" id="dwarf-'+d.index+'" did="'+d.index+'" dname="'+d.name+'">'
		+genderSymbol(d.gender)+' '+d.name+'<div class="dwarfworth">'+dwarfWorth(d.attributeSum)+'</div></li>';
}

/*
 * Works out which dwarf is selected and calls guide
 */
function viewDwarf(did) {
	
	currentDwarf = did;
	dwarf = dwarves[currentDwarf];
	
    printname();
	printgender();
	traitUpdate();
	attributeUpdate();
	guide();
}

function changelogupdate(message) {
	if (message.toLowerCase().search('error') != -1) {
		message = '<span class="error">' + message + '</span>';
	}
	changelog.unshift(message);
    changelog.length = 5;
    gid("changelogoutput").innerHTML = changelog.join("<br>");
}

function printname() {
	var name = dwarves[currentDwarf].name;
    gid("printdwarf").innerHTML = name;
    gid("printdwarf2").innerHTML = name;
}

function printgender() {
	var dwarf = dwarves[currentDwarf] || urist;
	gid('printdwarfgender').innerHTML = genderSymbol(dwarf.gender); 
	gid('printdwarfgender2').innerHTML = genderSymbol(dwarf.gender);
}

function genderSymbol(gender){
	if (gender.toLowerCase() == 'male') {
		return '<span class="male">♂</span>';
	} else {
		return '<span class="female">♀</span>';
	}
}

function attributeUpdate() {
	var attrsBox = $('#attributes').empty();
	
	for (var attr in defaultDwarf.attributes) {
		var attribute = (dwarves[currentDwarf] || urist).attributes[attr];
		var defaultAttribute = defaultDwarf.attributes[attr];
		var distance = Math.abs(attribute - defaultAttribute);
		var intensity = distance / (attribute < defaultAttribute ? -defaultAttribute : 5000 - defaultAttribute);
		var distance = Math.abs(attribute - defaultAttribute);
		
		var safeName = attr.replace(/ /g,'-');
		
		attrsBox.append('<div><input type="text" id="'+safeName+'" name="'+safeName+'" value="'+attribute+'" style="background: '+getInfoColor(intensity, 210, 0)+';">'
						+'<label for="'+safeName+'">'+attr+'</label></div>');
	}
}

function traitUpdate() {
	var traitsBox = $('#traits').empty();
	
	for (var t in defaultDwarf.traits) {
		var trait = (dwarves[currentDwarf] || urist).traits[t];
		var defaultTrait = defaultDwarf.traits[t];
		var distance = Math.abs(trait - defaultTrait);
		var intensity = distance / (trait < defaultTrait ? defaultTrait : 100 - defaultTrait);
			
		var safeName = t.replace(/ /g,'-');
		
		traitsBox.append('<div><input type="text" id="'+safeName+'" name="'+safeName+'" value="'+trait+'" style="background: '+getInfoColor(intensity, 260)+';">'
						+'<label for="'+safeName+'">'+t+'</label></div>');
	
    }
}

function getInfoColor(intensity, color, negativeColor) {
	if ((negativeColor > -1) && (intensity < 0)) {
		color = negativeColor;
		intensity = Math.abs(intensity);
	}
	return 'hsl(' + color + ', ' + Math.round(intensity*100) + '%, 70%)';
}

function directAttributeSubmit(rawAttribute, rawValue) {
	var dwarf = dwarves[currentDwarf] || urist;

    var attributeName = rawAttribute.toLowerCase().replace(/ \(.*\)/, '');
	var safeAttributeName = attributeName.replace(/ /, '-');
	var attributeValue = parseInt(rawValue);
	
    if (isNaN(attributeValue)) {
        changelogupdate("Error! " + rawValue + " must be a number!");
    } else if (attributeValue < 0 || attributeValue > 5000) {
        changelogupdate("Error! " + rawValue + " must be between 0 and 5000!");
    } else {
		dwarf.attributes[attributeName] = attributeValue;
        changelogupdate(rawAttribute + " set to " + attributeValue + " by User.");
		attributeUpdate();
        guide();
    }
	
}

function directSoulSubmit() {	
    var rawAttribute = gid("directsoul").value;
	var rawValue = gid("directsoulnum").value;
	
	directAttributeSubmit(rawAttribute, rawValue);
}
function directBodySubmit() {	
    var rawAttribute = gid("directbody").value;
	var rawValue = gid("directbodynum").value;
	
	directAttributeSubmit(rawAttribute, rawValue);
}
function directTraitSubmit() {
    var rawTrait = gid("directpers").value;
	var rawValue = gid("directpersnum").value;
	
	var dwarf = dwarves[currentDwarf] || urist;
	
	var traitName = rawTrait.toLowerCase().replace(/ \(.*\)/, '');
	var safeTraitName = traitName.replace(/ /, '-');
	var traitValue = parseInt(rawValue);
	
    if (isNaN(traitValue)) {
        changelogupdate("Error! " + rawValue + " must be a number!");
    } else if (traitValue < 0 || traitValue > 100) {
        changelogupdate("Error! " + rawValue + " must be between 0 and 100!");
    } else {
		dwarf.traits[traitName] = traitValue;
        changelogupdate(rawTrait + " set to " + traitValue + " by User.");
		traitUpdate();
        guide();
    }
	
}

function dwarfWorth(sum){
	return Math.round((sum * 100) / 21835) / 100;
}

function bestdwarf() {

	/*global*/
	nameRates = {
		superb: [],
		verygood: [],
		good: [],
		highavg: [],
		lowavg: [],
		poor: [],
		verypoor: [],
		horrible: [],
		deek: []
	};
    var jobIndex = gid("joblist").selectedIndex;
    var jobName = gid("joblist").value.toLowerCase();
	var job = jobs[jobName];
    var dwarfallattrs = [];
    var dwarfbodyattrs = [];
    var dwarfsoulattrs = [];
    var attrsum = [];
    var skillcomp = [];
    var ratelist = [];
    var ratejob = gid("joblist").options[jobIndex].text;
    var fortsum = 0;
    var fortavg = 0;
    var dwarfworth = 0;
	var dwarf;
	
	if (jobIndex == 0) {
        gid("printdwarf2").innerHTML = "\u263C " + "Your Embark";
        gid("printdwarfgender2").innerHTML = "\u263C";
		
		var dwarvesAttributeSum = 0;
        for (var i in dwarves) {
            dwarvesAttributeSum += dwarves[i].attributeSum;
        }
    
        dwarvesAttributeAvg = Math.round(dwarvesAttributeSum / dwarves.length);
        dwarvesAttributeAvg = Math.round(((dwarvesAttributeAvg * 89) / 1976) - (916735 / 988));
        dwarfworth = dwarfWorth(dwarvesAttributeSum);
        gid("guidance").innerHTML = "Your " + dwarves.length + " dwarves have the attribute points of " 
						+ dwarfworth + " average dwarves. If this is your starting seven, about " 
						+ dwarvesAttributeAvg + "% of embarks will have as many or fewer attribute points as this embark.";
        return;
    }
	else {
        for (var dwarfNum = 0, l = dwarves.length; dwarfNum<l; dwarfNum++) {
            dwarf = dwarves[dwarfNum];
            if ($( '#dwarves li[did='+dwarfNum+']' ).hasClass('ui-selected')) {
                skillcomp[dwarfNum] = jobFitness(dwarf, job);
				ratelist[dwarfNum] = namerate(dwarf, skillcomp[dwarfNum], jobName);;
            }
        }
	}
	
    if (nameRates.superb.length >= 1) {
        nameRates.superb.unshift("<div class=\"guidances\"><p>You are lucky to have a superb " + ratejob + " in:</p> <ul>");
        nameRates.superb.push("</ul></div>");
    };
    if (nameRates.verygood.length >= 1) {
        nameRates.verygood.unshift("<div class=\"guidances\"><p>You'll have a very good " + ratejob + " in:</p> <ul>");
        nameRates.verygood.push("</ul></div>");
    };
    if (nameRates.good.length >= 1) {
        nameRates.good.unshift("<div class=\"guidances\"><p>If you want a good " + ratejob + "; there's:</p> <ul>");
        nameRates.good.push("</ul></div>");
    };
    if (nameRates.highavg.length >= 1) {
        nameRates.highavg.unshift("<div class=\"guidances\"><p> For a " + ratejob + " a bit above average; try:</p> <ul>");
        nameRates.highavg.push("</ul></div>");
    };
    if (nameRates.lowavg.length >= 1) {
        nameRates.lowavg.unshift("<div class=\"guidances\"><p>If you'll settle for a below average " + ratejob + "; there's:</p> <ul>");
        nameRates.lowavg.push("</ul></div>");
    };
    if (nameRates.poor.length >= 1) {
        nameRates.poor.unshift("<div class=\"guidances\"><p>You will have a poor " + ratejob + " in:</p> <ul>");
        nameRates.poor.push("</ul></div>");
    };
    if (nameRates.verypoor.length >= 1) {
        nameRates.verypoor.unshift("<div class=\"guidances\"><p>You'll have a very poor " + ratejob + " in:</p> <ul>");
        nameRates.verypoor.push("</ul></div>");
    };
    if (nameRates.horrible.length >= 1) {
        nameRates.horrible.unshift("<div class=\"guidances\"><p>You're unlucky to have a horrible " + ratejob + " in:</p> <ul>");
        nameRates.horrible.push("</ul></div>");
    };
    if (nameRates.deek.length >= 1) {
        nameRates.deek.unshift("<div class=\"guidances\"><p>The following dwarves cannot be a " + ratejob + " because of personality problems:</p> <ul>");
        nameRates.deek.push("</ul></div>");
    };
	
	
    gid("printdwarf2").innerHTML = "\u263C " + "Best " + gid("joblist").options[jobIndex].text;
    gid("printdwarfgender2").innerHTML = "\u263C";
    gid("guidance").innerHTML = nameRates.superb.join('') + " " 
		+ nameRates.verygood.join('') + " " 
		+ nameRates.good.join('') + " " 
		+ nameRates.highavg.join('') + " " 
		+ nameRates.lowavg.join('') + " " 
		+ nameRates.poor.join('') + " " 
		+ nameRates.verypoor.join('') + " " 
		+ nameRates.horrible.join('') + " " 
		+ nameRates.deek.join('');
}

/* appends a string to the appropriate array */
function namerate(dwarf, pct, j) {
	var job = jobs[j];
	var pctName = pctToText(pct);
	var newname = '<li class="' + pctName + '" onclick="goToDwarf(' + dwarf.index + ')">['+pct+'] ' + dwarf.name + relatedSkills(dwarf, job) + '</li>';
	nameRates[pctName].unshift(newname);
}
/* appends a string to the appropriate array */
function jobrate(dwarf, pct, j) {
	var job = jobs[j];
	var pctName = pctToText(pct);
	var newname = '<li class="' + pctName + '" onclick="goToJob(\'' + j + '\')">['+pct+'] ' + j.toCapitalize() + relatedSkills(dwarf, job) + '</li>';
	analysis[pctName].unshift(newname);
}

function relatedSkills(dwarf, job){
	var skills = job.skills;
	var level, related = [];
	for (var s in skills){
		level = dwarf.skills[skills[s]];
		if (level > 0){
			related.push(level + ' ' + skills[s].toCapitalize());
		}
	}
	if (related.length){
		return '<br>(' + related.join(', ') + ')';
	}
	return '';
}

function pctToText(pct){
	var text;
    if (pct == 'deek') {
        text = pct;
    } else if (pct > 90) {
        text = 'superb';
    } else if (pct > 75) {
        text = 'verygood';
    } else if (pct > 60) {
        text = 'good';
    } else if (pct > 49) {
        text = 'highavg';
    } else if (pct > 39) {
        text = 'lowavg';
    } else if (pct > 24) {
        text = 'poor';
    } else if (pct > 9) {
        text = 'verypoor';
    } else {
        text = 'horrible';
    }
	return text;
}

function goToDwarf(did){
	console.log('going to', did);
	$('#dwarves ol')
		.find('li')
		  .removeClass('ui-selected')
		  .end()
		.find('[did="'+did+'"]')
		  .addClass('ui-selected');

	viewDwarf(did);
}

function goToJob(job){
	gid('joblist').value = job;
	
	$('#dwarves ol')
		.find('li')
		  .addClass('ui-selected')
		  .end();
		  
	bestdwarf();
}

/* when given a text trait 
 * returns the attribute or trait that it relates to and the corosponding value
 * TODO: Use map instead of if statements
 */
function verbalevalsubmit(verbal) {
    verbal = verbal.toLowerCase();
	
	// Type = Soul, Body or Trait
	var type, trait, value;

    // STRENGTH
    if (verbal.search("unbelievably strong") != -1) {
        value = 2250;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("mighty") != -1) {
        value = 2125;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("very strong") != -1) {
        value = 1875;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("strong") != -1 && verbal.search("urges") == -1 && verbal.search("duty") == -1) {
        value = 1625;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("average strength") != -1) {
        value = 1250;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("unfathomably weak") != -1) {
        value = 125;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("unquestionably weak") != -1) {
        value = 375;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("very weak") != -1) {
        value = 625;
        trait = "Strength";
		type = "Body";
    } else if (verbal.search("weak") != -1) {
        value = 875;
        trait = "Strength";
		type = "Body";
    }
    // AGILITY
    else if (verbal.search("amazingly agile") != -1) {
        value = 1900;
        trait = "Agility";
		type = "Body";
    } else if (verbal.search("extremely agile") != -1) {
        value = 1775;
        trait = "Agility";
		type = "Body";
    } else if (verbal.search("very agile") != -1) {
        value = 1525;
        trait = "Agility";
		type = "Body";
    } else if (verbal.search("agile") != -1 && verbal.search("fragile") == -1) {
        value = 1275;
        trait = "Agility";
		type = "Body";
    } else if (verbal.search("average agility") != -1) {
        value = 900;
        trait = "Agility";
		type = "Body";
    } else if (verbal.search("abysmally clumsy") != -1) {
        
        changelogupdate("No dwarf is abysmally clumsy!");
        return;
    } else if (verbal.search("totally clumsy") != -1) {
        value = 75;
        trait = "Agility";
		type = "Body";
    } else if (verbal.search("quite clumsy") != -1) {
        value = 275;
        trait = "Agility";
		type = "Body";
    } else if (verbal.search("clumsy") != -1 && verbal.search("very clumsy") == -1) {
        value = 525;
        trait = "Agility";
		type = "Body";
    }
    // TOUGHNESS
    else if (verbal.search("basically unbreakable") != -1) {
        value = 2250;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("incredibly tough") != -1) {
        value = 2125;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("quite durable") != -1) {
        value = 1875;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("average toughness") != -1) {
        value = 1250;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("tough") != -1) {
        value = 1625;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("shockingly fragile") != -1) {
        value = 125;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("remarkably flimsy") != -1) {
        value = 375;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("very flimsy") != -1) {
        value = 625;
        trait = "Toughness";
		type = "Body";
    } else if (verbal.search("flimsy") != -1) {
        value = 875;
        trait = "Toughness";
		type = "Body";
    }
    // ENDURANCE
    else if (verbal.search("absolutely inexhaustible") != -1) {
        value = 2000;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("indefatigable") != -1) {
        value = 1875;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("very slow to tire") != -1) {
        value = 1625;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("slow to tire") != -1) {
        value = 1375;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("average endurance") != -1) {
        value = 1000;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("truly quick to tire") != -1) {
        value = 0;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("extremely quick to tire") != -1) {
        value = 125;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("very quick to tire") != -1) {
        value = 375;
        trait = "Endurance";
		type = "Body";
    } else if (verbal.search("quick to tire") != -1) {
        value = 625;
        trait = "Endurance";
		type = "Body";
    }
    // DISEASE RESISTANCE
    else if (verbal.search("virtually never sick") != -1) {
        value = 2000;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("almost never sick") != -1) {
        value = 1875;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("very rarely sick") != -1) {
        value = 1625;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("rarely sick") != -1) {
        value = 1375;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("average disease resistance") != -1) {
        value = 1000;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("stunningly susceptible to disease") != -1) {
        value = 0;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("really susceptible to disease") != -1) {
        value = 125;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("quite susceptible to disease") != -1) {
        value = 375;
        trait = "Disease-Resistance";
		type = "Body";
    } else if (verbal.search("susceptible to disease") != -1) {
        value = 625;
        trait = "Disease-Resistance";
		type = "Body";
    }
    // RECUPERATION
    else if (verbal.search("possessed of amazing recuperative powers") != -1) {
        value = 2000;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("incredibly quick to heal") != -1) {
        value = 1875;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("quite quick to heal") != -1) {
        value = 1625;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("quick to heal") != -1) {
        value = 1375;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("average recuperation") != -1) {
        value = 1000;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("shockingly slow to heal") != -1) {
        value = 0;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("really slow to heal") != -1) {
        value = 125;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("very slow to heal") != -1) {
        value = 375;
        trait = "Recuperation";
		type = "Body";
    } else if (verbal.search("slow to heal") != -1) {
        value = 625;
        trait = "Recuperation";
		type = "Body";
    }
    // ANALYTICAL ABILITY
    else if (verbal.search("awesome intellectual powers") != -1) {
        value = 2250;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("great analytical abilities") != -1) {
        value = 2125;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("sharp intellect") != -1) {
        value = 1875;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("good intellect") != -1) {
        value = 1625;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("average analytical ability") != -1) {
        value = 1250;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("stunning lack of analytical ability") != -1) {
        value = 125;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("lousy intellect") != -1) {
        value = 375;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("very bad analytical abilities") != -1) {
        value = 625;
        trait = "Analytical-Ability";
		type = "Soul";
    } else if (verbal.search("poor analytical abilities") != -1) {
        value = 875;
        trait = "Analytical-Ability";
		type = "Soul";
    }
    // MEMORY
    else if (verbal.search("astonishing memory") != -1) {
        value = 2250;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("amazing memory") != -1) {
        value = 2125;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("great memory") != -1) {
        value = 1875;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("good memory") != -1) {
        value = 1625;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("average memory") != -1) {
        value = 1250;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("little memory to speak of") != -1) {
        value = 125;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("really bad memory") != -1) {
        value = 375;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("poor memory") != -1) {
        value = 625;
        trait = "Memory";
		type = "Soul";
    } else if (verbal.search("iffy memory") != -1) {
        value = 875;
        trait = "Memory";
		type = "Soul";
    }
    // CREATIVITY
    else if (verbal.search("boundless creative imagination") != -1) {
        value = 2250;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("great creativity") != -1) {
        value = 2125;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("very good creativity") != -1) {
        value = 1875;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("good creativity") != -1) {
        value = 1625;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("average creativity") != -1) {
        value = 1250;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("next to no creative talent") != -1) {
        value = 125;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("lousy creativity") != -1) {
        value = 375;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("poor creativity") != -1) {
        value = 625;
        trait = "Creatvity";
		type = "Soul";
    } else if (verbal.search("meager creativity") != -1) {
        value = 875;
        trait = "Creatvity";
		type = "Soul";
    }
    // INTUITION
    else if (verbal.search("uncanny intuition") != -1) {
        value = 2000;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("great intuition") != -1) {
        value = 1875;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("very good intuition") != -1) {
        value = 1625;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("good intuition") != -1) {
        value = 1375;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("average intuition") != -1) {
        value = 1000;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("horrible intuition") != -1) {
        value = 0;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("lousy intuition") != -1) {
        value = 125;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("very bad intuition") != -1) {
        value = 375;
        trait = "Intuition";
		type = "Soul";
    } else if (verbal.search("bad intuition") != -1) {
        value = 625;
        trait = "Intuition";
		type = "Soul";
    }
    // FOCUS
    else if (verbal.search("unbreakable focus") != -1) {
        value = 2542;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("great ability to focus") != -1) {
        value = 2417;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("very good focus") != -1) {
        value = 2167;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("ability to focus") != -1) {
        value = 1917;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("average focus") != -1) {
        value = 1500;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("absolute inability to focus") != -1) {
        value = 271;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("really poor focus") != -1) {
        value = 668;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("quite poor focus") != -1) {
        value = 918;
        trait = "Focus";
		type = "Soul";
    } else if (verbal.search("poor focus") != -1) {
        value = 1168;
        trait = "Focus";
		type = "Soul";
    }
    // WILLPOWER
    else if (verbal.search("unbreakable will") != -1) {
        value = 2000;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("iron will") != -1) {
        value = 1875;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("lot of willpower") != -1) {
        value = 1625;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("average willpower") != -1) {
        value = 1000;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("absolutely no willpower") != -1) {
        value = 0;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("next to no willpower") != -1) {
        value = 125;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("large deficit of willpower") != -1) {
        value = 375;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("little willpower") != -1) {
        value = 625;
        trait = "Willpower";
		type = "Soul";
    } else if (verbal.search("willpower") != -1 && verbal.search("great") == -1) {
        value = 1375;
        trait = "Willpower";
		type = "Soul";
    }
    // PATIENCE
    else if (verbal.search("absolutely boundless patience") != -1) {
        value = 2250;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("deep well of patience") != -1) {
        value = 2125;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("great deal of patience") != -1) {
        value = 1875;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("sum of patience") != -1) {
        value = 1625;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("average patience") != -1) {
        value = 1250;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("no patience at all") != -1) {
        value = 125;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("very little patience") != -1) {
        value = 375;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("little patience") != -1) {
        value = 625;
        trait = "Patience";
		type = "Soul";
    } else if (verbal.search("shortage of patience") != -1) {
        value = 875;
        trait = "Patience";
		type = "Soul";
    }
    // SPATIAL SENSE
    else if (verbal.search("stunning feel for spatial relationships") != -1) {
        value = 2542;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("amazing spatial sense") != -1) {
        value = 2417;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("great feel for the surrounding space") != -1) {
        value = 2167;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("good spatial sense") != -1) {
        value = 1917;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("average spatial sense") != -1) {
        value = 1500;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("no sense for spatial relationships") != -1) {
        value = 271;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("atrocious spatial sense") != -1) {
        value = 668;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("poor spatial senses") != -1) {
        value = 918;
        trait = "Spatial-Sense";
		type = "Soul";
    } else if (verbal.search("questionable spatial sense") != -1) {
        value = 1168;
        trait = "Spatial-Sense";
		type = "Soul";
    }
    // kinaesthetic SENSE
    else if (verbal.search("astounding feel for the position") != -1) {
        value = 2000;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("great kinaesthetic sense") != -1) {
        value = 1875;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("very good sense of the position") != -1) {
        value = 1625;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("good kinaesthetic sense") != -1) {
        value = 1375;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("average kinaesthetic sense") != -1) {
        value = 1000;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("unbelievably atrocious sense of the position") != -1) {
        value = 0;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("very clumsy kinaesthetic sense") != -1) {
        value = 125;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("poor kinaesthetic sense") != -1) {
        value = 375;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    } else if (verbal.search("meager kinaesthetic sense") != -1) {
        value = 625;
        trait = "kinaesthetic-Sense";
		type = "Soul";
    }
    // LINGUISTIC ABILITY
    else if (verbal.search("astonishing ability with languages") != -1) {
        value = 2000;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("great affinity for language") != -1) {
        value = 1875;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("natural inclination toward language") != -1) {
        value = 1625;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("way with words") != -1) {
        value = 1375;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("average linguistic ability") != -1) {
        value = 1000;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("difficulty with words and language") != -1) {
        value = 0;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("very little linguistic ability") != -1) {
        value = 125;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("little linguistic ability") != -1) {
        value = 375;
        trait = "Linguistic-Ability";
		type = "Soul";
    } else if (verbal.search("little difficulty with words") != -1) {
        value = 625;
        trait = "Linguistic-Ability";
		type = "Soul";
    }
    // MUSICALITY
    else if (verbal.search("astonishing knack for music") != -1) {
        value = 2000;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("great musical sense") != -1) {
        value = 1875;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("natural ability with music") != -1) {
        value = 1625;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("feel for music") != -1 && verbal.search("absolutely no") == -1) {
        value = 1375;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("average musicality") != -1) {
        value = 1000;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("absolutely no feel for music") != -1) {
        value = 0;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("next to no natural musical ability") != -1) {
        value = 125;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("little natural inclination toward music") != -1) {
        value = 375;
        trait = "Musicality";
		type = "Soul";
    } else if (verbal.search("iffy sense for music") != -1) {
        value = 675;
        trait = "Musicality";
		type = "Soul";
    }
    // EMPATHY
    else if (verbal.search("remarkable sense of others' emotions") != -1) {
        value = 2000;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("great sense of empathy") != -1) {
        value = 1875;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("very good sense of empathy") != -1) {
        value = 1625;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("ability to read emotions") != -1) {
        value = 1375;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("average empathy") != -1) {
        value = 1000;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("utter inability to judge others' emotions") != -1) {
        value = 0;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("next to no empathy") != -1) {
        value = 125;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("very bad sense of empathy") != -1) {
        value = 375;
        trait = "Empathy";
		type = "Soul";
    } else if (verbal.search("poor empathy") != -1) {
        value = 625;
        trait = "Empathy";
		type = "Soul";
    }
    // SOCIAL AWARENESS
    else if (verbal.search("profound feel for social relationships") != -1) {
        value = 2000;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("great feel for social relationships") != -1) {
        value = 1875;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("very good feel for social relationships") != -1) {
        value = 1625;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("good feel for social relationships") != -1) {
        value = 1375;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("average social awareness") != -1) {
        value = 1000;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("inability to understand social relationships") != -1) {
        value = 0;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("lack of understanding of social relationships") != -1) {
        value = 125;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("poor ability to manage or understand") != -1) {
        value = 375;
        trait = "Social-Awareness";
		type = "Soul";
    } else if (verbal.search("meager ability with social relationships") != -1) {
        value = 625;
        trait = "Social-Awareness";
		type = "Soul";
    }
    // ACHIEVEMENT STRIVING
    else if (verbal.search("strives for perfection") != -1) {
        value = 95;
        trait = "Achievement_Striving";
		type = "Trait";
    } else if (verbal.search("important to strive for excellence") != -1) {
        value = 83;
        trait = "Achievement_Striving";
		type = "Trait";
    } else if (verbal.search("strives for excellence") != -1) {
        value = 68;
        trait = "Achievement_Striving";
		type = "Trait";
    } else if (verbal.search("average achievement striving") != -1) {
        value = 50;
        trait = "Achievement_Striving";
		type = "Trait";
    } else if (verbal.search("does the bare minimum") != -1) {
        value = 5;
        trait = "Achievement_Striving";
		type = "Trait";
    } else if (verbal.search("rarely does more work") != -1) {
        value = 17;
        trait = "Achievement_Striving";
		type = "Trait";
    } else if (verbal.search("more work than necessary") != -1) {
        value = 32;
        trait = "Achievement_Striving";
		type = "Trait";
    }
    // ACTIVITY LEVEL
    else if (verbal.search("constantly active and energetic") != -1) {
        value = 95;
        trait = "Activity_Level";
		type = "Trait";
    } else if (verbal.search("very energetic and active") != -1) {
        value = 83;
        trait = "Activity_Level";
		type = "Trait";
    } else if (verbal.search("very active") != -1) {
        value = 68;
        trait = "Activity_Level";
		type = "Trait";
    } else if (verbal.search("average activity level") != -1) {
        value = 50;
        trait = "Activity_Level";
		type = "Trait";
    } else if (verbal.search("be bothered with frantic") != -1) {
        value = 5;
        trait = "Activity_Level";
		type = "Trait";
    } else if (verbal.search("life at a leisurely") != -1) {
        value = 17;
        trait = "Activity_Level";
		type = "Trait";
    } else if (verbal.search("relaxed") != -1) {
        value = 32;
        trait = "Activity_Level";
		type = "Trait";
    }
    // ADVENTUROUSNESS
    else if (verbal.search("loves fresh experiences") != -1) {
        value = 95;
        trait = "Adventurousness";
		type = "Trait";
    } else if (verbal.search("eager for new experiences") != -1) {
        value = 83;
        trait = "Adventurousness";
		type = "Trait";
    } else if (verbal.search("likes to try new things") != -1) {
        value = 68;
        trait = "Adventurousness";
		type = "Trait";
    } else if (verbal.search("average adventurousness") != -1) {
        value = 50;
        trait = "Adventurousness";
		type = "Trait";
    } else if (verbal.search("resistant to change") != -1) {
        value = 5;
        trait = "Adventurousness";
		type = "Trait";
    } else if (verbal.search("uncomfortable with change") != -1) {
        value = 17;
        trait = "Adventurousness";
		type = "Trait";
    } else if (verbal.search("prefers familiar routines") != -1) {
        value = 32;
        trait = "Adventurousness";
		type = "Trait";
    }
    // ALTRUISM
    else if (verbal.search("truly fulfilled by assisting") != -1) {
        value = 95;
        trait = "Altruism";
		type = "Trait";
    } else if (verbal.search("helping others very rewarding") != -1) {
        value = 83;
        trait = "Altruism";
		type = "Trait";
    } else if (verbal.search("helping others rewarding") != -1) {
        value = 68;
        trait = "Altruism";
		type = "Trait";
    } else if (verbal.search("average altruism") != -1) {
        value = 50;
        trait = "Altruism";
		type = "Trait";
    } else if (verbal.search("helping others as an imposition") != -1) {
        value = 5;
        trait = "Altruism";
		type = "Trait";
    } else if (verbal.search("dislikes helping others") != -1) {
        value = 17;
        trait = "Altruism";
		type = "Trait";
    } else if (verbal.search("way to help others") != -1) {
        value = 32;
        trait = "Altruism";
		type = "Trait";
    }
    // ANGER
    else if (verbal.search("state of internal rage") != -1) {
        value = 95;
        trait = "Anger";
		type = "Trait";
    } else if (verbal.search("very quick to anger") != -1) {
        value = 83;
        trait = "Anger";
		type = "Trait";
    } else if (verbal.search("quick to anger") != -1) {
        value = 68;
        trait = "Anger";
		type = "Trait";
    } else if (verbal.search("average anger") != -1) {
        value = 50;
        trait = "Anger";
		type = "Trait";
    } else if (verbal.search("never becomes angry") != -1) {
        value = 5;
        trait = "Anger";
		type = "Trait";
    } else if (verbal.search("very slow to anger") != -1) {
        value = 17;
        trait = "Anger";
		type = "Trait";
    } else if (verbal.search("slow to anger") != -1) {
        value = 32;
        trait = "Anger";
		type = "Trait";
    }
    // ANXIETY
    else if (verbal.search("nervous wreck") != -1) {
        value = 95;
        trait = "Anxiety";
		type = "Trait";
    } else if (verbal.search("tense and jittery") != -1) {
        value = 83;
        trait = "Anxiety";
		type = "Trait";
    } else if (verbal.search("often nervous") != -1) {
        value = 68;
        trait = "Anxiety";
		type = "Trait";
    } else if (verbal.search("average anxiety") != -1) {
        value = 50;
        trait = "Anxiety";
		type = "Trait";
    } else if (verbal.search("incredibly calm") != -1) {
        value = 5;
        trait = "Anxiety";
		type = "Trait";
    } else if (verbal.search("very calm") != -1) {
        value = 17;
        trait = "Anxiety";
		type = "Trait";
    } else if (verbal.search("calm") != -1) {
        value = 32;
        trait = "Anxiety";
		type = "Trait";
    }
    // ARTISTIC INTEREST
    else if (verbal.search("easily become absorbed") != -1) {
        value = 95;
        trait = "Artistic_Interest";
		type = "Trait";
    } else if (verbal.search("greatly appreciates art") != -1) {
        value = 83;
        trait = "Artistic_Interest";
		type = "Trait";
    } else if (verbal.search("appreciates art") != -1) {
        value = 68;
        trait = "Artistic_Interest";
		type = "Trait";
    } else if (verbal.search("average artistic interest") != -1) {
        value = 50;
        trait = "Artistic_Interest";
		type = "Trait";
    } else if (verbal.search("completely uninterested in art") != -1) {
        value = 5;
        trait = "Artistic_Interest";
		type = "Trait";
    } else if (verbal.search("not interested in art") != -1) {
        value = 17;
        trait = "Artistic_Interest";
		type = "Trait";
    } else if (verbal.search("not have a great aesthetic") != -1) {
        value = 32;
        trait = "Artistic_Interest";
		type = "Trait";
    }
    // ASSERTIVENESS
    else if (verbal.search("loves to take charge") != -1) {
        value = 95;
        trait = "Assertiveness";
		type = "Trait";
    } else if (verbal.search("very assertive") != -1) {
        value = 83;
        trait = "Assertiveness";
		type = "Trait";
    } else if (verbal.search("average assertiveness") != -1) {
        value = 50;
        trait = "Assertiveness";
		type = "Trait";
    } else if (verbal.search("never speaks out") != -1) {
        value = 5;
        trait = "Assertiveness";
		type = "Trait";
    } else if (verbal.search("others handle the leadership") != -1) {
        value = 17;
        trait = "Assertiveness";
		type = "Trait";
    } else if (verbal.search("unassertive") != -1) {
        value = 32;
        trait = "Assertiveness";
		type = "Trait";
    } else if (verbal.search("assertive") != -1 && verbal.search("average") == -1) {
        value = 68;
        trait = "Assertiveness";
		type = "Trait";
    }
    // CAUTIOUSNESS
    else if (verbal.search("thinks through every alternative") != -1) {
        value = 95;
        trait = "Cautiousness";
		type = "Trait";
    } else if (verbal.search("extremely cautious") != -1) {
        value = 83;
        trait = "Cautiousness";
		type = "Trait";
    } else if (verbal.search("takes time when making decisions") != -1) {
        value = 68;
        trait = "Cautiousness";
		type = "Trait";
    } else if (verbal.search("average cautiousness") != -1) {
        value = 50;
        trait = "Cautiousness";
		type = "Trait";
    } else if (verbal.search("thinking through possibilities") != -1) {
        value = 5;
        trait = "Cautiousness";
		type = "Trait";
    } else if (verbal.search("acts impulsively") != -1) {
        value = 17;
        trait = "Cautiousness";
		type = "Trait";
    } else if (verbal.search("first thing that comes to mind") != -1) {
        value = 32;
        trait = "Cautiousness";
		type = "Trait";
    }
    // CHEERFULNESS
    else if (verbal.search("feels filled with joy") != -1) {
        value = 95;
        trait = "Cheerfulness";
		type = "Trait";
    } else if (verbal.search("very happy and optimistic") != -1) {
        value = 83;
        trait = "Cheerfulness";
		type = "Trait";
    } else if (verbal.search("often cheerful") != -1) {
        value = 68;
        trait = "Cheerfulness";
		type = "Trait";
    } else if (verbal.search("average cheerfulness") != -1) {
        value = 50;
        trait = "Cheerfulness";
		type = "Trait";
    } else if (verbal.search("never optimistic") != -1) {
        value = 5;
        trait = "Cheerfulness";
		type = "Trait";
    } else if (verbal.search("pessimist") != -1) {
        value = 17;
        trait = "Cheerfulness";
		type = "Trait";
    } else if (verbal.search("rarely happy or enthusiastic") != -1) {
        value = 32;
        trait = "Cheerfulness";
		type = "Trait";
    }
    // COOPERATION
    else if (verbal.search("needs to get along with others") != -1) {
        value = 95;
        trait = "Cooperation";
		type = "Trait";
    } else if (verbal.search("dislikes confrontations") != -1) {
        value = 83;
        trait = "Cooperation";
		type = "Trait";
    } else if (verbal.search("willing to compromise") != -1) {
        value = 68;
        trait = "Cooperation";
		type = "Trait";
    } else if (verbal.search("average cooperation") != -1) {
        value = 50;
        trait = "Cooperation";
		type = "Trait";
    } else if (verbal.search("to compromise with somebody else") != -1) {
        value = 5;
        trait = "Cooperation";
		type = "Trait";
    } else if (verbal.search("rather intimidate others") != -1) {
        value = 17;
        trait = "Cooperation";
		type = "Trait";
    } else if (verbal.search("like to compromise") != -1) {
        value = 32;
        trait = "Cooperation";
		type = "Trait";
    }
    // DEPRESSION
    else if (verbal.search("frequently depressed") != -1) {
        value = 95;
        trait = "Depression";
		type = "Trait";
    } else if (verbal.search("often sad and dejected") != -1) {
        value = 83;
        trait = "Depression";
		type = "Trait";
    } else if (verbal.search("often feels discouraged") != -1) {
        value = 68;
        trait = "Depression";
		type = "Trait";
    } else if (verbal.search("average depression") != -1) {
        value = 50;
        trait = "Depression";
		type = "Trait";
    } else if (verbal.search("never feels discouraged") != -1 && verbal.search("almost") == -1) {
        value = 5;
        trait = "Depression";
		type = "Trait";
    } else if (verbal.search("almost never feels discouraged") != -1) {
        value = 17;
        trait = "Depression";
		type = "Trait";
    } else if (verbal.search("rarely feels discouraged") != -1) {
        value = 32;
        trait = "Depression";
		type = "Trait";
    }
    // DUTIFULNESS
    else if (verbal.search("profound sense of duty") != -1) {
        value = 95;
        trait = "Dutifulness";
		type = "Trait";
    } else if (verbal.search("strong sense of duty") != -1) {
        value = 83;
        trait = "Dutifulness";
		type = "Trait";
    } else if (verbal.search("sense of duty") != -1) {
        value = 68;
        trait = "Dutifulness";
		type = "Trait";
    } else if (verbal.search("average dutifulness") != -1) {
        value = 50;
        trait = "Dutifulness";
		type = "Trait";
    } else if (verbal.search("contracts and other confining") != -1) {
        value = 5;
        trait = "Dutifulness";
		type = "Trait";
    } else if (verbal.search("dislikes contracts and regulations") != -1) {
        value = 17;
        trait = "Dutifulness";
		type = "Trait";
    } else if (verbal.search("finds rules confining") != -1) {
        value = 32;
        trait = "Dutifulness";
		type = "Trait";
    }
    // EMOTIONALITY
    else if (verbal.search("profound understanding") != -1 && verbal.search("own emotions") != -1) {
        value = 95;
        trait = "Emotionality";
		type = "Trait";
    } else if (verbal.search("great awareness") != -1 && verbal.search("own emotions") != -1) {
        value = 83;
        trait = "Emotionality";
		type = "Trait";
    } else if (verbal.search("good awareness") != -1 && verbal.search("own emotions") != -1) {
        value = 68;
        trait = "Emotionality";
		type = "Trait";
    } else if (verbal.search("average emotionality") != -1) {
        value = 50;
        trait = "Emotionality";
		type = "Trait";
    } else if (verbal.search("does not display") != -1 && verbal.search("own emotions") != -1) {
        value = 5;
        trait = "Emotionality";
		type = "Trait";
    } else if (verbal.search("mostly unaware") != -1 && verbal.search("own emotions") != -1) {
        value = 17;
        trait = "Emotionality";
		type = "Trait";
    } else if (verbal.search("tends not to openly express emotions") != -1) {
        value = 32;
        trait = "Emotionality";
		type = "Trait";
    }
    // EXCITEMENT SEEKING
    else if (verbal.search("lives for risk") != -1) {
        value = 95;
        trait = "Excitement_Seeking";
		type = "Trait";
    } else if (verbal.search("risk-taker and a thrill-seeker") != -1) {
        value = 83;
        trait = "Excitement_Seeking";
		type = "Trait";
    } else if (verbal.search("loves a good thrill") != -1) {
        value = 68;
        trait = "Excitement_Seeking";
		type = "Trait";
    } else if (verbal.search("average excitement seeking") != -1) {
        value = 50;
        trait = "Excitement_Seeking";
		type = "Trait";
    } else if (verbal.search("entirely adverse to risk") != -1) {
        value = 5;
        trait = "Excitement_Seeking";
		type = "Trait";
    } else if (verbal.search("need thrills or risks") != -1) {
        value = 17;
        trait = "Excitement_Seeking";
		type = "Trait";
    } else if (verbal.search("not a risk-taker") != -1) {
        value = 32;
        trait = "Excitement_Seeking";
		type = "Trait";
    }
    // FRIENDLINESS
    else if (verbal.search("genuinely likes others") != -1) {
        value = 95;
        trait = "Friendliness";
		type = "Trait";
    } else if (verbal.search("makes friends quickly") != -1) {
        value = 83;
        trait = "Friendliness";
		type = "Trait";
    } else if (verbal.search("very friendly") != -1) {
        value = 68;
        trait = "Friendliness";
		type = "Trait";
    } else if (verbal.search("average friendliness") != -1) {
        value = 50;
        trait = "Friendliness";
		type = "Trait";
    } else if (verbal.search("incredibly distant and reserved") != -1) {
        value = 5;
        trait = "Friendliness";
		type = "Trait";
    } else if (verbal.search("very distant and reserved") != -1) {
        value = 17;
        trait = "Friendliness";
		type = "Trait";
    } else if (verbal.search("somewhat reserved") != -1) {
        value = 32;
        trait = "Friendliness";
		type = "Trait";
    }
    // GREGARIOUSNESS
    else if (verbal.search("treasures the company of others") != -1) {
        value = 95;
        trait = "Gregariousness";
		type = "Trait";
    } else if (verbal.search("enjoys being in crowds") != -1) {
        value = 83;
        trait = "Gregariousness";
		type = "Trait";
    } else if (verbal.search("enjoys the company of others") != -1) {
        value = 68;
        trait = "Gregariousness";
		type = "Trait";
    } else if (verbal.search("average gregariousness") != -1) {
        value = 50;
        trait = "Gregariousness";
		type = "Trait";
    } else if (verbal.search("spending time alone much more") != -1) {
        value = 5;
        trait = "Gregariousness";
		type = "Trait";
    } else if (verbal.search("prefers to be alone") != -1) {
        value = 17;
        trait = "Gregariousness";
		type = "Trait";
    } else if (verbal.search("tends to avoid crowds") != -1) {
        value = 32;
        trait = "Gregariousness";
		type = "Trait";
    }
    // IMAGINATION
    else if (verbal.search("bored by reality") != -1) {
        value = 95;
        trait = "Imagination";
		type = "Trait";
    } else if (verbal.search("incredibly creative") != -1) {
        value = 83;
        trait = "Imagination";
		type = "Trait";
    } else if (verbal.search("fertile imagination") != -1) {
        value = 68;
        trait = "Imagination";
		type = "Trait";
    } else if (verbal.search("average imagination") != -1) {
        value = 50;
        trait = "Imagination";
		type = "Trait";
    } else if (verbal.search("interested only in facts") != -1) {
        value = 5;
        trait = "Imagination";
		type = "Trait";
    } else if (verbal.search("grounded in reality") != -1) {
        value = 17;
        trait = "Imagination";
		type = "Trait";
    } else if (verbal.search("flights of fancy") != -1) {
        value = 32;
        trait = "Imagination";
		type = "Trait";
    }
    // IMMODERATION
    else if (verbal.search("ruled by irresistible cravings") != -1) {
        value = 95;
        trait = "Immoderation";
		type = "Trait";
    } else if (verbal.search("feels strong urges") != -1) {
        value = 83;
        trait = "Immoderation";
		type = "Trait";
    } else if (verbal.search("occasionally overindulges") != -1 || verbal.search("occassionally overindulges") != -1) {
        value = 68;
        trait = "Immoderation";
		type = "Trait";
    } else if (verbal.search("average immoderation") != -1) {
        value = 55;
        trait = "Immoderation";
		type = "Trait";
    } else if (verbal.search("never feels tempted") != -1) {
        value = 5;
        trait = "Immoderation";
		type = "Trait";
    } else if (verbal.search("rarely feels strong cravings") != -1) {
        value = 17;
        trait = "Immoderation";
		type = "Trait";
    } else if (verbal.search("experience strong cravings") != -1) {
        value = 32;
        trait = "Immoderation";
		type = "Trait";
    }
    // INTELLECTUAL CURIOSITY
    else if (verbal.search("entranced by riddles") != -1) {
        value = 95;
        trait = "Intellectual_Curiosity";
		type = "Trait";
    } else if (verbal.search("new and fresh ideas") != -1) {
        value = 83;
        trait = "Intellectual_Curiosity";
		type = "Trait";
    } else if (verbal.search("open-minded to new ideas") != -1) {
        value = 68;
        trait = "Intellectual_Curiosity";
		type = "Trait";
    } else if (verbal.search("average intellectual curiosity") != -1) {
        value = 50;
        trait = "Intellectual_Curiosity";
		type = "Trait";
    } else if (verbal.search("uninterested in ideas") != -1) {
        value = 5;
        trait = "Intellectual_Curiosity";
		type = "Trait";
    } else if (verbal.search("regards intellectual exercises") != -1) {
        value = 17;
        trait = "Intellectual_Curiosity";
		type = "Trait";
    } else if (verbal.search("dislikes intellectual discussions") != -1) {
        value = 32;
        trait = "Intellectual_Curiosity";
		type = "Trait";
    }
    // LIBERALISM
    else if (verbal.search("revels in chaos") != -1) {
        value = 95;
        trait = "Liberalism";
		type = "Trait";
    } else if (verbal.search("loves to defy convention") != -1) {
        value = 83;
        trait = "Liberalism";
		type = "Trait";
    } else if (verbal.search("put off by authority") != -1) {
        value = 68;
        trait = "Liberalism";
		type = "Trait";
    } else if (verbal.search("average liberalism") != -1) {
        value = 50;
        trait = "Liberalism";
		type = "Trait";
    } else if (verbal.search("ardent believer in convention") != -1) {
        value = 5;
        trait = "Liberalism";
		type = "Trait";
    } else if (verbal.search("prefers stability and security") != -1) {
        value = 17;
        trait = "Liberalism";
		type = "Trait";
    } else if (verbal.search("admires tradition") != -1) {
        value = 32;
        trait = "Liberalism";
		type = "Trait";
    }
    // MODESTY
    else if (verbal.search("never claim to be better") != -1) {
        value = 95;
        trait = "Modesty";
		type = "Trait";
    } else if (verbal.search("immodesty distasteful") != -1) {
        value = 83;
        trait = "Modesty";
		type = "Trait";
    } else if (verbal.search("average modesty") != -1) {
        value = 50;
        trait = "Modesty";
		type = "Trait";
    } else if (verbal.search("would never shy away") != -1) {
        value = 5;
        trait = "Modesty";
		type = "Trait";
    } else if (verbal.search("very willing to compare") != -1) {
        value = 17;
        trait = "Modesty";
		type = "Trait";
    } else if (verbal.search("immodest") != -1) {
        value = 32;
        trait = "Modesty";
		type = "Trait";
    } else if (verbal.search("modest") != -1) {
        value = 68;
        trait = "Modesty";
		type = "Trait";
    }
    // ORDERLINESS
    else if (verbal.search("loves to make lists") != -1) {
        value = 95;
        trait = "Orderliness";
		type = "Trait";
    } else if (verbal.search("well-organized life") != -1) {
        value = 83;
        trait = "Orderliness";
		type = "Trait";
    } else if (verbal.search("average orderliness") != -1) {
        value = 50;
        trait = "Orderliness";
		type = "Trait";
    } else if (verbal.search("completely disorganized") != -1) {
        value = 5;
        trait = "Orderliness";
		type = "Trait";
    } else if (verbal.search("very disorganized") != -1) {
        value = 17;
        trait = "Orderliness";
		type = "Trait";
    } else if (verbal.search("disorganized") != -1) {
        value = 32;
        trait = "Orderliness";
		type = "Trait";
    } else if (verbal.search("organized") != -1) {
        value = 68;
        trait = "Orderliness";
		type = "Trait";
    }
    // SELF-CONSCIOUSNESS
    else if (verbal.search("socially crippled") != -1) {
        value = 95;
        trait = "self-consciousness";
		type = "Trait";
    } else if (verbal.search("rejection and ridicule") != -1) {
        value = 83;
        trait = "self-consciousness";
		type = "Trait";
    } else if (verbal.search("average self-consciousness") != -1) {
        value = 50;
        trait = "self-consciousness";
		type = "Trait";
    } else if (verbal.search("self-conscious") != -1) {
        value = 68;
        trait = "self-consciousness";
		type = "Trait";
    } else if (verbal.search("absolutely unfazed") != -1) {
        value = 5;
        trait = "self-consciousness";
		type = "Trait";
    } else if (verbal.search("very comfortable in social situations") != -1) {
        value = 17;
        trait = "self-consciousness";
		type = "Trait";
    } else if (verbal.search("comfortable in social situations") != -1) {
        value = 32;
        trait = "self-consciousness";
		type = "Trait";
    }
    // SELF-DISCIPLINE
    else if (verbal.search("will persist in the face") != -1) {
        value = 95;
        trait = "Self-Discipline";
		type = "Trait";
    } else if (verbal.search("great willpower") != -1) {
        value = 83;
        trait = "Self-Discipline";
		type = "Trait";
    } else if (verbal.search("self-disciplined") != -1) {
        value = 68;
        trait = "Self-Discipline";
		type = "Trait";
    } else if (verbal.search("average self-discipline") != -1) {
        value = 50;
        trait = "Self-Discipline";
		type = "Trait";
    } else if (verbal.search("rarely completes tasks") != -1) {
        value = 5;
        trait = "Self-Discipline";
		type = "Trait";
    } else if (verbal.search("little self-discipline") != -1) {
        value = 17;
        trait = "Self-Discipline";
		type = "Trait";
    } else if (verbal.search("procrastination") != -1) {
        value = 32;
        trait = "Self-Discipline";
		type = "Trait";
    }
    // SELF-EFFICACY
    else if (verbal.search("incredibly confident") != -1) {
        value = 95;
        trait = "Self-Efficacy";
		type = "Trait";
    } else if (verbal.search("very confident") != -1) {
        value = 83;
        trait = "Self-Efficacy";
		type = "Trait";
    } else if (verbal.search("confident") != -1 && verbal.search("under") == -1) {
        value = 68;
        trait = "Self-Efficacy";
		type = "Trait";
    } else if (verbal.search("average self-efficacy") != -1) {
        value = 50;
        trait = "Self-Efficacy";
		type = "Trait";
    } else if (verbal.search("feels as if") != -1 && verbal.search("not in control") != -1) {
        value = 5;
        trait = "Self-Efficacy";
		type = "Trait";
    } else if (verbal.search("does not feel effective") != -1) {
        value = 17;
        trait = "Self-Efficacy";
		type = "Trait";
    } else if (verbal.search("lacks confidence") != -1) {
        value = 32;
        trait = "Self-Efficacy";
		type = "Trait";
    }
    // STRAIGHTFORWARDNESS
    else if (verbal.search("incredibly frank and candid") != -1) {
        value = 95;
        trait = "Straightforwardness";
		type = "Trait";
    } else if (verbal.search("very straightforward") != -1) {
        value = 83;
        trait = "Straightforwardness";
		type = "Trait";
    } else if (verbal.search("candid and sincere") != -1) {
        value = 68;
        trait = "Straightforwardness";
		type = "Trait";
    } else if (verbal.search("average straightforwardness") != -1) {
        value = 55;
        trait = "Straightforwardness";
		type = "Trait";
    } else if (verbal.search("some deception is necessary") != -1) {
        value = 5;
        trait = "Straightforwardness";
		type = "Trait";
    } else if (verbal.search("not straightforward") != -1) {
        value = 17;
        trait = "Straightforwardness";
		type = "Trait";
    } else if (verbal.search("guarded in relationships") != -1) {
        value = 32;
        trait = "Straightforwardness";
		type = "Trait";
    }
    // SYMPATHY
    else if (verbal.search("incredibly compassionate") != -1) {
        value = 95;
        trait = "Sympathy";
		type = "Trait";
    } else if (verbal.search("compassionate") != -1) {
        value = 68;
        trait = "Sympathy";
		type = "Trait";
    } else if (verbal.search("average sympathy") != -1) {
        value = 50;
        trait = "Sympathy";
		type = "Trait";
    } else if (verbal.search("tempered by mercy or pity") != -1) {
        value = 5;
        trait = "Sympathy";
		type = "Trait";
    } else if (verbal.search("suffering of others") != -1) {
        value = 17;
        trait = "Sympathy";
		type = "Trait";
    } else if (verbal.search("not easily moved to pity") != -1) {
        value = 32;
        trait = "Sympathy";
		type = "Trait";
    } else if (verbal.search("easily moved to pity") != -1) {
        value = 83;
        trait = "Sympathy";
		type = "Trait";
    }
    // TRUST
    else if (verbal.search("naturally trustful") != -1) {
        value = 95;
        trait = "Trust";
		type = "Trait";
    } else if (verbal.search("very trusting") != -1) {
        value = 83;
        trait = "Trust";
		type = "Trait";
    } else if (verbal.search("trusting") != -1) {
        value = 68;
        trait = "Trust";
		type = "Trait";
    } else if (verbal.search("average trust") != -1) {
        value = 50;
        trait = "Trust";
		type = "Trait";
    } else if (verbal.search("selfish and conniving") != -1) {
        value = 5;
        trait = "Trust";
		type = "Trait";
    } else if (verbal.search("not trust others") != -1) {
        value = 17;
        trait = "Trust";
		type = "Trait";
    } else if (verbal.search("slow to trust") != -1) {
        value = 32;
        trait = "Trust";
		type = "Trait";
    }
    // VULNERABILITY
    else if (verbal.search("completely helpless") != -1) {
        value = 95;
        trait = "Vulnerability";
		type = "Trait";
    } else if (verbal.search("cracks easily") != -1) {
        value = 83;
        trait = "Vulnerability";
		type = "Trait";
    } else if (verbal.search("handle stress well") != -1) {
        value = 68;
        trait = "Vulnerability";
		type = "Trait";
    } else if (verbal.search("average vulnerability") != -1) {
        value = 45;
        trait = "Vulnerability";
		type = "Trait";
    } else if (verbal.search("effects of stress") != -1) {
        value = 5;
        trait = "Vulnerability";
		type = "Trait";
    } else if (verbal.search("confident under pressure") != -1) {
        value = 17;
        trait = "Vulnerability";
		type = "Trait";
    } else if (verbal.search("can handle stress") != -1) {
        value = 32;
        trait = "Vulnerability";
		type = "Trait";
    } else {
        
        changelogupdate("Error! Counselor could not evaluate your verbal input.");
        return;
    }
    return [type, trait, value];
}

function verbalUpdate(dwarf, update) {
	var type = update[0];
	var trait = update[1];
	var value = update[2];
	var safeTrait = trait.toLowerCase().replace(/_/, ' ');
	dwarf[(type == "Trait" ? "traits" : "attributes")][safeTrait] = value;
}

function rateDwarfForJob(dwarf, job) {
    var pcts = [];
	for (var a in job.attributes) {
		var attrFunc = attributeFunctions[attributeFunctionMap[job.attributes[a]]];
		var attrPct = attrFunc(dwarf.attributes[job.attributes[a]])
		pcts.push(attrPct);
	}
	return Math.round(avg(pcts));
}

var jobFitness = function(dwarf, job) {
	for (t in job.traits){
		var jobTrait = job.traits[t];
		var dwarfTrait = dwarf.traits[t];
		if (jobTrait < 0 && dwarfTrait > Math.abs(jobTrait)){
			return 'deek';
		}
		else if (jobTrait > 0 && dwarfTrait < Math.abs(jobTrait)){
			return 'deek';
		}
	}
	return rateDwarfForJob(dwarf, job);
}

function guide() {
	var dwarf = dwarves[currentDwarf] || urist;
	
    analysis = {
		'superb': [],
		'verygood': [],
		'good': [],
		'highavg': [],
		'lowavg': [],
		'poor': [],
		'verypoor': [],
		'horrible': [],
		'deek': [],
		'output': []
	};
	
    if (dwarf.gender == "Male") {
        var subjpro = "He";
        var objpro = "Him";
        var lexpro = "Himself";
        var posspro = "His";
        var detpro = "His";
    } else {
        var subjpro = "She";
        var objpro = "Her";
        var lexpro = "Herself";
        var posspro = "Hers";
        var detpro = "Her";
    }
	
	var pct;
	for (var j in jobs) {
		pct = jobFitness(dwarf, jobs[j]);
		jobrate(dwarf, pct, j);
	}
	
    if (analysis.superb.length >= 1) {
        analysis.superb.unshift('<div class="guidances"><p>' + subjpro + " bears the mien of a superb:</p> <ul>");
        analysis.superb.push("</ul></div>")
    };
    if (analysis.verygood.length >= 1) {
        analysis.verygood.unshift('<div class="guidances"><p>' + subjpro + " has the potential to be a very good:</p> <ul>");
        analysis.verygood.push("</ul></div>");
    };
	
    if (analysis.good.length >= 1) {
        analysis.good.unshift('<div class="guidances"><p>' + subjpro + " has the look of a good:</p> <ul>");
        analysis.good.push("</ul></div>");
    };
	
    if (analysis.highavg.length >= 1) {
        analysis.highavg.unshift('<div class="guidances"><p>' + subjpro + " will be a little better than most as a:</p> <ul>");
        analysis.highavg.push("</ul></div>");
    };
	
    if (analysis.lowavg.length >= 1) {
        analysis.lowavg.unshift('<div class="guidances"><p>' + subjpro + " comes across as a below average:</p> <ul>");
        analysis.lowavg.push("</ul></div>");
    };
	
    if (analysis.poor.length >= 1) {
        analysis.poor.unshift('<div class="guidances"><p>' + subjpro + " will make a poor:</p> <ul>");
        analysis.poor.push("</ul></div>");
    };
	
    if (analysis.verypoor.length >= 1) {
        analysis.verypoor.unshift('<div class="guidances"><p>' + subjpro + " seems like a very poor:</p> <ul>");
        analysis.verypoor.push("</ul></div>");
    };
	
    if (analysis.horrible.length >= 1) {
        analysis.horrible.unshift('<div class="guidances"><p>' + subjpro + " would be horrible as a:</p> <ul>");
        analysis.horrible.push("</ul></div>");
    };
    if (analysis.deek.length >= 1) {
        analysis.deek.unshift('<div class="guidances"><p>Because of personality problems ' + subjpro + " cannot be a:</p> <ul>");
        analysis.deek.push("</ul></div>");
    };
	
	var brightside = gid("brightside").checked;
    if (brightside) {
		if (analysis.superb.length + analysis.verygood.length + analysis.good.length + analysis.highavg.length < 20) {
			analysis.output.push(analysis.superb + " " + analysis.verygood + " " + analysis.good + " " + analysis.highavg + " " + analysis.lowavg);
		} else if (analysis.superb.length + analysis.verygood.length + analysis.good.length < 20) {
			analysis.output.push(analysis.superb + " " + analysis.verygood + " " + analysis.good + " " + analysis.highavg);
		} else {
			analysis.output.push(analysis.superb + " " + analysis.verygood + " " + analysis.good);
		}
    } else {
		analysis.output.push(analysis.superb + " " + analysis.verygood + " " + analysis.good + " " + analysis.poor + " " + analysis.verypoor + " " + analysis.horrible+ " " + analysis.deek);
    }
	
	
    if (dwarf.traits['self-discipline'] < 25) {
        analysis.output.push('<p>This lazy sack of booze prefers to spend time <span class="horrible"' + ">On Break.</span>");
    } else if (dwarf.traits['self-discipline'] > 75) {
        analysis.output.push("<p>Excellent! " + detpro + ' breaks will be <span class="superb"' + ">shorter</span> than most.");
    }
    if (dwarf.traits['anger'] < 25) {
        analysis.output.push('<p>' + subjpro + ' can\'t find the spirit to <span class="horrible"' + ">rage</span> in battle.");
    } else if (dwarf.traits['anger'] > 75) {
        analysis.output.push("<p>When battles go poorly, " + subjpro.toLowerCase() + ' is prone to flying against the enemy in a <span class="superb"' + ">rage.</span>");
    }
    if (dwarf.traits['altruism'] < 40) {
        analysis.output.push('<p>' + subjpro + ' seems unhappy at the thought of <span class="horrible"' + ">helping others</span>.");
    } else if (dwarf.traits['altruism'] > 60) {
        analysis.output.push('<p>' + subjpro + ' seems happy at the thought of <span class="superb"' + ">helping others</span>.");
    }
    if (dwarf.traits['assertiveness'] < 40) {
        analysis.output.push('<p>' + detpro + " unassertive nature won\'t allow " + objpro.toLowerCase() + ' to attempt <span class="horrible"' + ">Persuasion</span>.</p>");
    }
    if (dwarf.traits['cooperation'] < 40) {
        analysis.output.push('<p>' + subjpro + ' cannot be relied on to <span class="horrible"' + ">Pacify</span> others.");
    } else if (dwarf.traits['cooperation'] > 60) {
        analysis.output.push("<p>A communitarian dwarf; " + subjpro.toLowerCase() + ' will never <span class="horrible"' + ">Intimidate</span> others.");
    }
    if (dwarf.traits['friendliness'] < 40) {
        analysis.output.push('<p>When others try to start a <span class="horrible"' + ">Conversation</span>; " + subjpro.toLowerCase() + " doesn't respond.");
    }
    if (dwarf.traits['self-consciousness'] > 75) {
        analysis.output.push("<p>Held back by self-consciousness; " + subjpro.toLowerCase() + ' will not attempt <span class="horrible"' + ">Comedy</span>.");
    }
    if (dwarf.traits['straightforwardness'] < 40) {
        analysis.output.push('<p>' + subjpro + ' cannot be honest enough to <span class="horrible"' + ">Console</span> another.");
    } else if (dwarf.traits['straightforwardness'] > 39 && dwarf.traits['straightforwardness'] < 61) {
        analysis.output.push('<p>' + subjpro + ' is too direct to <span class="horrible"' + ">Lie</span>.");
    } else if (dwarf.traits['straightforwardness'] > 60) {
        analysis.output.push('<p>' + subjpro + ' is too straightforward to <span class="horrible"' + '>Lie</span> or <span class="horrible"' + ">Flatter</span>.");
    }
	
    var guideprint = analysis.output.toString();
    guideprint = guideprint.replace(/,/g, " ");
    guideprint = guideprint.replace(/\u003B/g, "");
    gid("guidance").innerHTML = guideprint;
}


function setColors() {
	document.body.className = (gid('colorcode').checked ? 'color' : 'no-color');
}

$(function (){

	$( "#main" ).tabs();
	$( "#dwarves ol" ).selectable({
		stop: function() {
			
			$( ".ui-selected", this ).each(function() {
				var index = $( "#dwarves ol li" ).index( this );
				
				dwarves[index].selected = true;
			});
			$( ":not(.ui-selected)", this ).each(function() {
				var index = $( "#dwarves ol li" ).index( this );
				if (index > -1){
					dwarves[index].selected = false;
				}
			});
		
			var selected = $( ".ui-selected", this );
			if (selected.length === 1) {
				var index = $( "#dwarves ol li" ).index( selected[0] )
				viewDwarf(index);
			} else {
				bestdwarf();
			}
		}
	});
	$( "#dwarf-form" ).dialog({
		autoOpen: false,
		height: 400,
		width: 450,
		modal: true,
		buttons: {
			"Add Dwarf": function() {
				
				var name = $('dwarfname'), gender = $('gender');
				
				//TODO: Validation
				addDwarf(name.val(), gender.val());
				
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		}
	});
	$( "#gender" ).buttonset();

	$( "#add-dwarf" )
		.button({
            icons: {
                primary: "ui-icon-plus"
            }
        })
		.click(function() {
			$( "#dwarf-form" ).dialog( "open" );
		});
	
	xmlUpdate();
	attributeUpdate();
	traitUpdate();
	setColors();
});