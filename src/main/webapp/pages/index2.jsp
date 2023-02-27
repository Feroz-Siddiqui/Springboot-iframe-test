<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>hello</h1>
	<a href="/logout">logout</a>

	<button onclick="disconnect()">disocnnet</button>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"
		integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw=="
		crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
		integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
		crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	s
	<script type="text/javascript">
var customerSpeechToTextWebsocket;
var source;
var mydata =[];
try {
	navigator.getUserMedia = navigator.getUserMedia
			|| navigator.webkitGetUserMedia
			|| navigator.mozGetUserMedia;
	microphone = navigator.getUserMedia({
		audio : true,
		video : false
	}, onMicrophoneGranted, onMicrophoneDenied);
} catch (e) {
	alert(e)
}
var stompClient = null;


function onMicrophoneDenied() {
	console.log('denied')
}

//language=en&sample_rate=8000&encoding=linear16

async function onMicrophoneGranted(stream) {
	customerSpeechToTextWebsocket = new WebSocket('wss://api.deepgram.com/v1/listen?model=phonecall&language=en&sample_rate=48000&encoding=linear16&punctuate=true', [
	    'token',
	    'db4de9285775c930fc13a4f97a0345d4b2676a46',
	]);
	//customerSpeechToTextWebsocket.binaryType = 'arraybuffer';
      context = new AudioContext({sampleRate: 48000});
	  const micSourceNode = context.createMediaStreamSource(stream);

	  const recordingProperties = {
	    numberOfChannels: micSourceNode.channelCount,
	    sampleRate: context.sampleRate,
	    maxFrameCount: context.sampleRate*10,
	  };
	  const recordingNode = await setupRecordingWorkletNode(recordingProperties);
	  const recordingCallback = handleRecording(
		      recordingNode.port, recordingProperties);
	  recordingNode.port.onmessage = (event) => {
		    if (event.data.message === 'UPDATE_VISUALIZERS') {
		      //visualizerCallback(event);
		    } else {
		      recordingCallback(event);
		    }
		  };
		  
		  micSourceNode
	      .connect(recordingNode)
	       .connect(context.destination);
	 /* var socket = new SockJS('/socket/chat');
     stompClient = Stomp.over(socket);  
     stompClient.connect({}, function(frame) {
         console.log('Connected: ' + frame);
         stompClient.subscribe('/topic/messages', function(messageOutput) {
             showMessageOutput(JSON.parse(messageOutput.body));
         });
     });
	context = new AudioContext({sampleRate: 48000});
	source = context.createMediaStreamSource(stream);
	await context.audioWorklet.addModule('/app_resources/recorderWorkletProcessor.js');

	// NEW A: Loading the worklet processor
	//await context.audioWorklet.addModule("/app_resources/buffer-detector.js")
	// Create the recorder worklet
	recorder = new AudioWorkletNode(context, "recorderWorkletProcessor")
	
	customerSpeechToTextWebsocket.onmessage = (message) => {
        const received = JSON.parse(message.data)
        
        if(received.channel && received.channel.alternatives && received.channel.alternatives.length > 0){
		const transcript = received.channel.alternatives[0].transcript
		console.log(transcript)
        }
		
		console.log(message) */

		
	}

	customerSpeechToTextWebsocket.onclose = () => {
		console.log({ event: 'onclose' })
	}

	customerSpeechToTextWebsocket.onerror = (error) => {
		console.log('Error occured transcription scoket')
		console.log({ event: 'onerror', error })
	}

/* 
	source.connect(recorder);
	recorder.connect(context.destination);
	//source.connect(recorder).connect(context.destination);
	console.log(context)
	recorder.port.onmessage = function(e) {
	    //const intArray = Int16Array.from(e.data);
	    

	   console.log(e);
	
		//customerSpeechToTextWebsocket.send((String.fromCharCode(floatArrayToBytes(e.data).buffer)));
		//stompClient.send('/queue/some-queue', {},btoa((String.fromCharCode(floatArrayToBytes(e.data).buffer))));
	} */
	

    
	
}




	  
	  
function floatArrayToBytes(floats) {
    var output = floats.buffer; // Get the ArrayBuffer from the float array
    return new Uint8Array(output); // Convert the ArrayBuffer to Uint8s.
}


function convertFloat32ToInt16(buffer) {
    let l = buffer.length;
    const buf = new Int16Array(l / 3);

    while (l--) {
      if (l % 3 === 0) {
        buf[l / 3] = buffer[l] * 0xFFFF;
      }
    }
    return buf.buffer;
  }


function _arrayBufferToBase64( buffer ) {
    var binary = '';
    var bytes = new Uint8Array( buffer );
    var len = bytes.byteLength;
    for (var i = 0; i < len; i++) {
        binary += String.fromCharCode( bytes[ i ] );
    }
    return window.btoa( binary );
}


function stopstream() {
	if(source)
	source.disconnect(recorder)
	
	
}


function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    if(customerSpeechToTextWebsocket){
    	customerSpeechToTextWebsocket.close();
    }
    stopstream();
    console.log("Disconnected");
}
function _base64ToArrayBuffer(base64) {
    var binary_string = window.atob(base64);
    var len = binary_string.length;
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes.buffer;
}
function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
}


async function setupRecordingWorkletNode(recordingProperties) {
	  await context.audioWorklet.addModule('recording-processor.js');

	  const WorkletRecordingNode = new AudioWorkletNode(
	      context,
	      'recording-processor',
	      {
	        processorOptions: recordingProperties,
	      },
	  );

	  return WorkletRecordingNode;
	}
	
	
	

function handleRecording(processorPort, recordingProperties) {
  const recordButton = document.querySelector('#record');
  const recordText = recordButton.querySelector('span');
  const player = document.querySelector('#player');
  const downloadButton = document.querySelector('#download');

  let recordingLength = 0;

  // If the max length is reached, we can no longer record.
  const recordingEventCallback = async (event) => {
    if (event.data.message === 'MAX_RECORDING_LENGTH_REACHED') {
      isRecording = false;
      recordText.innerHTML = 'Start';
      recordButton.setAttribute.disabled = true;
    }
    if (event.data.message === 'UPDATE_RECORDING_LENGTH') {
      recordingLength = event.data.recordingLength;

      document.querySelector('#data-len').innerHTML =
          Math.round(recordingLength / context.sampleRate * 100)/100;
    }
    if (event.data.message === 'SHARE_RECORDING_BUFFER') {
      const recordingBuffer = context.createBuffer(
          recordingProperties.numberOfChannels,
          recordingLength,
          context.sampleRate);

      for (let i = 0; i < recordingProperties.numberOfChannels; i++) {
        recordingBuffer.copyToChannel(event.data.buffer[i], i, 0);
      }

      const wavUrl = createLinkFromAudioBuffer(
          recordingBuffer,
          true);

      player.src = wavUrl;
      downloadButton.src = wavUrl;
      downloadButton.download = 'recording.wav';
    }
  };

  recordButton.addEventListener('click', (e) => {
    isRecording = !isRecording;

    // Inform processor that recording was paused.
    processorPort.postMessage({
      message: 'UPDATE_RECORDING_STATE',
      setRecording: isRecording,
    });

    recordText.innerHTML = isRecording ? 'Stop' : 'Start';
  });

  return recordingEventCallback;
}

	
	
/**
 * @license Copyright (c) 2016 Hongchan Choi. MIT License.
 * @fileOverview Canopy PCM wave file exporter.
 * @return {function} create link function
 */

function _writeStringToArray(aString, targetArray, offset) {
  for (let i = 0; i < aString.length; ++i) {
    targetArray[offset + i] = aString.charCodeAt(i);
  }
}

function _writeInt16ToArray(aNumber, targetArray, offset) {
  aNumber = Math.floor(aNumber);
  targetArray[offset + 0] = aNumber & 255; // byte 1
  targetArray[offset + 1] = (aNumber >> 8) & 255; // byte 2
}

function _writeInt32ToArray(aNumber, targetArray, offset) {
  aNumber = Math.floor(aNumber);
  targetArray[offset + 0] = aNumber & 255; // byte 1
  targetArray[offset + 1] = (aNumber >> 8) & 255; // byte 2
  targetArray[offset + 2] = (aNumber >> 16) & 255; // byte 3
  targetArray[offset + 3] = (aNumber >> 24) & 255; // byte 4
}

// Return the bits of the float as a 32-bit integer value.  This
// produces the raw bits; no intepretation of the value is done.
function _floatBits(f) {
  const buf = new ArrayBuffer(4);
  (new Float32Array(buf))[0] = f;
  const bits = (new Uint32Array(buf))[0];
  // Return as a signed integer.
  return bits | 0;
}

function _writeAudioBufferToArray(
    audioBuffer, targetArray, offset, bitDepth) {
  let index = 0; let channel = 0;
  const length = audioBuffer.length;
  const channels = audioBuffer.numberOfChannels;
  let channelData; let sample;

  // Clamping samples onto the 16-bit resolution.
  for (index = 0; index < length; ++index) {
    for (channel = 0; channel < channels; ++channel) {
      channelData = audioBuffer.getChannelData(channel);

      // Branches upon the requested bit depth
      if (bitDepth === 16) {
        sample = channelData[index] * 32768.0;
        if (sample < -32768) {
          sample = -32768;
        } else if (sample > 32767) {
          sample = 32767;
        }
        _writeInt16ToArray(sample, targetArray, offset);
        offset += 2;
      } else if (bitDepth === 32) {
        // This assumes we're going to out 32-float, not 32-bit linear.
        sample = _floatBits(channelData[index]);
        _writeInt32ToArray(sample, targetArray, offset);
        offset += 4;
      } else {
        console.log('Invalid bit depth for PCM encoding.');
        return;
      }
    }
  }
}

/**
   * [createWaveFileBlobFromAudioBuffer description]
   * @param  {AudioBuffer} audioBuffer
   * @param  {Boolean} as32BitFloat
   * @return {Blob} Resulting binary blob.
   */
function _createWaveFileBlobFromAudioBuffer(audioBuffer, as32BitFloat) {
  // Encoding setup.
  const frameLength = audioBuffer.length;
  const numberOfChannels = audioBuffer.numberOfChannels;
  const sampleRate = audioBuffer.sampleRate;
  const bitsPerSample = as32BitFloat ? 32 : 16;
  const bytesPerSample = bitsPerSample / 8;
  const byteRate = sampleRate * numberOfChannels * bitsPerSample / 8;
  const blockAlign = numberOfChannels * bitsPerSample / 8;
  const wavDataByteLength = frameLength * numberOfChannels * bytesPerSample;
  const headerByteLength = 44;
  const totalLength = headerByteLength + wavDataByteLength;
  const waveFileData = new Uint8Array(totalLength);
  const subChunk1Size = 16;
  const subChunk2Size = wavDataByteLength;
  const chunkSize = 4 + (8 + subChunk1Size) + (8 + subChunk2Size);

  _writeStringToArray('RIFF', waveFileData, 0);
  _writeInt32ToArray(chunkSize, waveFileData, 4);
  _writeStringToArray('WAVE', waveFileData, 8);
  _writeStringToArray('fmt ', waveFileData, 12);

  // SubChunk1Size (4)
  _writeInt32ToArray(subChunk1Size, waveFileData, 16);
  // AudioFormat (2): 3 means 32-bit float, 1 means integer PCM.
  _writeInt16ToArray(as32BitFloat ? 3 : 1, waveFileData, 20);
  // NumChannels (2)
  _writeInt16ToArray(numberOfChannels, waveFileData, 22);
  // SampleRate (4)
  _writeInt32ToArray(sampleRate, waveFileData, 24);
  // ByteRate (4)
  _writeInt32ToArray(byteRate, waveFileData, 28);
  // BlockAlign (2)
  _writeInt16ToArray(blockAlign, waveFileData, 32);
  // BitsPerSample (4)
  _writeInt32ToArray(bitsPerSample, waveFileData, 34);
  _writeStringToArray('data', waveFileData, 36);
  // SubChunk2Size (4)
  _writeInt32ToArray(subChunk2Size, waveFileData, 40);

  // Write actual audio data starting at offset 44.
  _writeAudioBufferToArray(audioBuffer, waveFileData, 44, bitsPerSample);

  return new Blob([waveFileData], {
    type: 'audio/wave',
  });
}

/**
   * [createLinkFromAudioBuffer description]
   * @param  {[type]} audioBuffer   [description]
   * @param  {Boolean} as32BitFloat
* @return {String} file url
   */
function createLinkFromAudioBuffer(audioBuffer, as32BitFloat) {
  const blob = _createWaveFileBlobFromAudioBuffer(audioBuffer, as32BitFloat);
  return window.URL.createObjectURL(blob);
}

//export default createLinkFromAudioBuffer;

        </script>

	<div>
		<div>
			<input type="text" id="from" placeholder="Choose a nickname" />
		</div>
		<br />
		<div>
			<button id="connect" onclick="connect();">Connect</button>
			<button id="disconnect" disabled="disabled" onclick="disconnect();">
				Disconnect</button>
		</div>
		<br />
		<div id="conversationDiv">
			<input type="text" id="text" placeholder="Write a message..." />
			<button id="sendMessage" onclick="sendMessage();">Send</button>
			<p id="response"></p>
		</div>
	</div>
</body>
</html>