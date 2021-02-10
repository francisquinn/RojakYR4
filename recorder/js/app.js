//webkitURL is deprecated but nevertheless
URL = window.URL || window.webkitURL;

var gumStream; 						//stream from getUserMedia()
var rec; 							//Recorder.js object
var input; 							//MediaStreamAudioSourceNode we'll be recording

// shim for AudioContext when it's not avb. 
var AudioContext = window.AudioContext || window.webkitAudioContext;
var audioContext //audio context to help us record

var recordButton = document.getElementById("recordButton");
var stopButton = document.getElementById("stopButton");
var pauseButton = document.getElementById("pauseButton");

//add events to those 2 buttons
recordButton.addEventListener("click", startRecording);
stopButton.addEventListener("click", stopRecording);
pauseButton.addEventListener("click", pauseRecording);

var type = 1;

var event = 
[
    {
        keywords:["kickout","kick out"],
        eventID:1,
        defaultOutcome:1,
        defaultTeam:0,
        statType:"kickout"
    },
    {
        keywords:["sideline","side line"],
        eventID:2,
        defaultOutcome:1,
        defaultTeam:1,
        statType:"shot"
    },
    {
        keywords:["sideline shot","side line shot"],
        eventID:3,
        defaultOutcome:1,
        defaultTeam:1,
        statType:"shot"
    },
    {
        keywords:["free shot"],
        eventID:5,
        defaultTeam:1,
        statType:"shot"
    },
    {
        keywords:["free"],
        eventID:4,
        defaultOutcome:1,
        defaultTeam:1
    },
    {
        keywords:["45 shot","forty-five shot","forty five shot","fortify shot"],
        eventID:7,
        defaultOutcome:1,
        defaultTeam:0,
        defaultPosition:12,
        statType:"shot"
    },
    {
        keywords:["45","forty-five","forty five","fortify"],
        eventID:6,
        defaultOutcome:1,
        defaultTeam:0,
        defaultPosition:12,
        statType:"shot"
    },
    {
        keywords:["kickpass","kick pass","kick past","kickpast", "capace"],
        eventID:8,
        defaultOutcome:1,
        defaultTeam:0,
        statType:"pass"
    },
    {
        keywords:["handpass shot","hand pass shot","handpast shot","hand past shot","and pass shot"],
        eventID:11,
        defaultOutcome:1,
        defaultTeam:1,
        statType:"shot"
    },
    {
        keywords:["shot"],
        eventID:9,
        defaultOutcome:1,
        defaultTeam:1,
        statType:"shot"
    },
    {
        keywords:["handpass","hand pass","handpast","hand past","and pass"],
        eventID:10,
        defaultOutcome:1,
        defaultTeam:0,
        statType:"pass"
    },
    {
        keywords:["advanced mark kick past","advance mark kick pass","advanced mark kick past","advanced mark kick past", "advanced mark capace", "advance mark capace"],
        eventID:13,
        defaultOutcome:1,
        defaultTeam:0
    },
    {
        keywords:["mark kick pass","mark kick past", "mark capace"],
        eventID:12,
        defaultOutcome:1,
        defaultTeam:0
    },
    {
        keywords:["advanced mark shot","advance mark shot"],
        eventID:14,
        defaultOutcome:1,
        defaultTeam:1,
        statType:"shot"
    },
    {
        keywords:["carry ball"],
        eventID:15,
        defaultOutcome:1,
        defaultTeam:0,
        statType:"carry"
    },
    {
        keywords:["start game","game start","game stop","gamestop"],
        eventID:21,
        defaultOutcome:1,
        defaultTeam:-1
    },
    {
        keywords:["throw in"],
        eventID:22,
        defaultOutcome:1,
        defaultTeam:-1
    },
    {
        keywords:["pause game"],
        eventID:23,
        defaultOutcome:1,
        defaultTeam:-1
    },
    {
        keywords:["stop game"],
        eventID:24,
        defaultOutcome:1,
        defaultTeam:-1
    }
    
];

var position = 
[
    {
        keywords:["short left"],
        positionID:1
    },
    {
        keywords:["short centre","short center"],
        positionID:2
    },
    {
        keywords:["short right"],
        positionID:3
    },
    {
        keywords:["mid left","mid-left"],
        positionID:4
    },
    {
        keywords:["mid centre","mid center","mid-center"],
        positionID:5
    },
    {
        keywords:["mid right","mid-right"],
        positionID:6
    },
    {
        keywords:["long left"],
        positionID:7
    },
    {
        keywords:["long centre","long center"],
        positionID:8
    },
    {
        keywords:["long right"],
        positionID:9
    },
    {
        keywords:["attack sixty-five","attack sixty five","attack 65"],
        positionID:10
    },
    {
        keywords:["attack forty-five","attack forty five","attack 45", "tap 45", "tap forty five", "tap forty-five"],
        positionID:11
    },
    {
        keywords:["attack twenty","attack 20", "tap twenty", "tap 20"],
        positionID:12
    }
];

var outcome =
[
    {
        keywords:["pass successful","past successful"],
        outcomeID:1,
        statType:"pass"
    },
    {
        keywords:["ball wide"],
        outcomeID:2,
        statType:"shot fail"
    },
    {
        keywords:["ball out side line","ball out sideline"],
        outcomeID:3,
        statType:"shot fail"
    },
    {
        keywords:["ball out forty five","ball out forty-five","ball out 45"],
        outcomeID:4,
        statType:"shot fail"
    },
    {
        keywords:["free"],
        outcomeID:5
    },
    {
        keywords:["free off ball","free of ball"],
        outcomeID:9
    },
    {
        keywords:["point"],
        outcomeID:6,
        statType:"point"
    },
    {
        keywords:["goal","go","goat"],
        outcomeID:7,
        statType:"goal"
    },
    {
        keywords:["turnover"],
        outcomeID:8,
        statType:"turnover"
    },
    {
        keywords:["mark"],
        outcomeID:10
    },
    {
        keywords:["advance mark","advanced mark"],
        outcomeID:11
    },
    {
        keywords:["won throw in"],
        outcomeID:12,
        statType:"take initiative"
    },
    {
        keywords:["throw in awarded","throw in award"],
        outcomeID:13
    }
]


var playernum =
        [
            {
                keywords:["player"]
            },
            {
                keywords:["1"]
            },
            {
                keywords:["2"]
            },
            {
                keywords:["3"]
            },
            {
                keywords:["4"]
            },
            {
                keywords:["5"]
            },
            {
                keywords:["6"]
            },
            {
                keywords:["7"]
            },
            {
                keywords:["8"]
            },
            {
                keywords:["9"]
            },
            {
                keywords:["10"]
            },
            {
                keywords:["11"]
            },
            {
                keywords:["12"]
            },
            {
                keywords:["13"]
            },
            {
                keywords:["14"]
            },
            {
                keywords:["15"]
            },
            {
                keywords:["16"]
            },
            {
                keywords:["17"]
            },
            {
                keywords:["18"]
            },
            {
                keywords:["19"]
            },
            {
                keywords:["20"]
            },
            {
                keywords:["21"]
            },
            {
                keywords:["22"]
            },
            {
                keywords:["23"]
            },
            {
                keywords:["24"]
            }
        ]

var textArr = event;
var currentIndex = 0;

function setType(typeNum)
{
    type = typeNum;
    currentIndex = 0;
    
    switch(type)
    {
        case 1:
            textArr = event;
            break;
        
        case 2:
            textArr = outcome;
            break;
        
        case 3:
            textArr = position;
            break;
            
        case 4:
            textArr = playernum;
            break;
    }
}

function startRecording() {
    
    document.getElementById("instruction").innerHTML = textArr[currentIndex].keywords[0];
    currentIndex++;
    
    console.log("recordButton clicked");

	/*
		Simple constraints object, for more advanced audio features see
		https://addpipe.com/blog/audio-constraints-getusermedia/
	*/
    
    var constraints = { audio: true, video:false }

 	/*
    	Disable the record button until we get a success or fail from getUserMedia() 
	*/

	recordButton.disabled = true;
	stopButton.disabled = false;
	pauseButton.disabled = false

	/*
    	We're using the standard promise based getUserMedia() 
    	https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia
	*/

	navigator.mediaDevices.getUserMedia(constraints).then(function(stream) {
		console.log("getUserMedia() success, stream created, initializing Recorder.js ...");

		/*
			create an audio context after getUserMedia is called
			sampleRate might change after getUserMedia is called, like it does on macOS when recording through AirPods
			the sampleRate defaults to the one set in your OS for your playback device

		*/
		audioContext = new AudioContext();

		//update the format 
		document.getElementById("formats").innerHTML="Format: 1 channel pcm @ "+audioContext.sampleRate/1000+"kHz"

		/*  assign to gumStream for later use  */
		gumStream = stream;
		
		/* use the stream */
		input = audioContext.createMediaStreamSource(stream);

		/* 
			Create the Recorder object and configure to record mono sound (1 channel)
			Recording 2 channels  will double the file size
		*/
		rec = new Recorder(input,{numChannels:1})

		//start the recording process
		rec.record()

		console.log("Recording started");

	}).catch(function(err) {
	  	//enable the record button if getUserMedia() fails
    	recordButton.disabled = false;
    	stopButton.disabled = true;
    	pauseButton.disabled = true
	});
}

function pauseRecording(){
	console.log("pauseButton clicked rec.recording=",rec.recording );
	if (rec.recording){
		//pause
		rec.stop();
		pauseButton.innerHTML="Resume";
	}else{
		//resume
		rec.record()
		pauseButton.innerHTML="Pause";

	}
}

function stopRecording() {
	console.log("stopButton clicked");

	//disable the stop button, enable the record too allow for new recordings
	stopButton.disabled = true;
	recordButton.disabled = false;
	pauseButton.disabled = true;

	//reset button just in case the recording is stopped while paused
	pauseButton.innerHTML="Pause";
	
	//tell the recorder to stop the recording
	rec.stop();

	//stop microphone access
	gumStream.getAudioTracks()[0].stop();

	//create the wav blob and pass it on to createDownloadLink
	rec.exportWAV(createDownloadLink);
}

function createDownloadLink(blob) {
	
	var url = URL.createObjectURL(blob);
	var au = document.createElement('audio');
	var li = document.createElement('li');
	var link = document.createElement('a');
        
        var uniqueNumber = document.getElementById("unInput").value;
        
	//name of .wav file to use during upload and download (without extendion)
	var filename = "dictionary_"+textArr[currentIndex-1].keywords[0]+"_"+uniqueNumber;

	//add controls to the <audio> element
	au.controls = true;
	au.src = url;

	//save to disk link
	link.href = url;
	link.download = filename+".wav"; //download forces the browser to donwload the file using the  filename
	link.innerHTML = "Save to disk";

	//add the new audio element to li
	li.appendChild(au);
	
	//add the filename to the li
	li.appendChild(document.createTextNode(filename+".wav "))

	//add the save to disk link to li
	li.appendChild(link);
	
	//upload link
	var upload = document.createElement('a');
        upload.setAttribute("visibility","hidden");
	upload.href="#";
	upload.innerHTML = "Upload";
	upload.addEventListener("click", function(event){
		  var xhr=new XMLHttpRequest();
		  xhr.onload=function(e) {
		      if(this.readyState === 4) {
		          console.log("Server returned: ",e.target.responseText);
		      }
		  };
		  var fd=new FormData();
		  fd.append("audio_data",blob, filename);
		  xhr.open("POST","upload.php",true);
		  xhr.send(fd);
	})
	li.appendChild(document.createTextNode (" "))//add a space in between

	//add the li element to the ol
	recordingsList.appendChild(li);
}

function upload()
{
    var filename = new Date().toISOString();
    //filename to send to server without extension 
    //upload link 
    var upload = document.createElement('a');
    upload.href = "#";
    upload.innerHTML = "Upload";
    upload.addEventListener("click", function(event) {
        var xhr = new XMLHttpRequest();
        xhr.onload = function(e) {
            if (this.readyState === 4) {
                console.log("Server returned: ", e.target.responseText);
            }
        };
        var fd = new FormData();
        fd.append("audio_data", blob, filename);
        xhr.open("POST", "upload.php", true);
        xhr.send(fd);
    })
    li.appendChild(document.createTextNode(" ")) //add a space in between 
    li.appendChild(upload) //add the upload link to li
}