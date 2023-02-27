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
	
	
<div class="demo-box">
  <div class="Live">
    <h2>Live</h2>
    <div>
      <button id="monitor">
        Turn monitor <span>on</span>
      </button>
      <button id="viz-toggle">
        <span>Pause</span> visualizer
      </button>
    </div>

    <canvas id="live-canvas"></canvas>
  </div>

  <div id="recording">
    <h2>Recording</h2>

    <div>
      <p>Length: <span id="data-len">0</span>sec</p>
      <button id="record">
        <span>Start</span> Recording
      </button>
    </div>

    <canvas id="recording-canvas"></canvas>

    <div>
      <audio id="player" class="w-full" controls></audio>

      <div class="my-2">
        <a href="" id="download">
          <button>download file</button>
        </a>
      </div>
    </div>
  </div>
</div>
	
	
	<script type="text/javascript">
	
	var context;
	let isRecording = false;

	document.addEventListener('click', (element) => {
		context = new AudioContext();

		  init();
		}, {once: true});
	

	async function init() {
	  if (context.state === 'suspended') {
	    await context.resume();
	  }

	  // Get user's microphone and connect it to the AudioContext.
	  const micStream = await navigator.mediaDevices.getUserMedia({
	    audio: {
	      echoCancellation: false,
	      autoGainControl: false,
	      noiseSuppression: false,
	      latency: 0,
	    },
	  });

	  const micSourceNode = context.createMediaStreamSource(micStream);

	  const recordingProperties = {
	    numberOfChannels: micSourceNode.channelCount,
	    sampleRate: context.sampleRate,
	    maxFrameCount: context.sampleRate*10,
	  };
	  
		await context.audioWorklet.addModule('/app_resources/recordingprocessor.js');


	 const recordingNode = new AudioWorkletNode(context,'recording-processor',
		      {
	        processorOptions: recordingProperties,
	      }
	  );
	  const monitorNode = context.createGain();

	  // We can pass this port across the app
	  // and let components handle their relevant messages
	  //const visualizerCallback = setupVisualizers(recordingNode);
	 const recordingCallback = handleRecording(recordingNode.port, recordingProperties);

	  setupMonitor(monitorNode);

	  recordingNode.port.onmessage = (event) => {
	    if (event.data.message === 'UPDATE_VISUALIZERS') {
	      //visualizerCallback(event);
	    } else {
	      recordingCallback(event);
	    }
	  };

	  micSourceNode
	      .connect(recordingNode)
	      .connect(monitorNode)
	      .connect(context.destination);
	}

	function setupMonitor(monitorNode) {
		  // Leave audio volume at zero by default.
		  monitorNode.gain.value = 0;
		  let isMonitoring = false;
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
		      console.log(player.src)
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