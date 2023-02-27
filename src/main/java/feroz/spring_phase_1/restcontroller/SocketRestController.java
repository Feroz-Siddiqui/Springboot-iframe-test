package feroz.spring_phase_1.restcontroller;

import java.util.Base64;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import feroz.spring_phase_1.dbmodel.Message;
import feroz.spring_phase_1.dbmodel.OutputMessage;

@RestController
public class SocketRestController {

	
	Logger logger = LoggerFactory.getLogger(getClass());

	@MessageMapping("/socket/chat")
	@SendTo("/topic/messages")
	public OutputMessage send(Message message) throws Exception {
	    String time = new SimpleDateFormat("HH:mm").format(new Date());
	    return new OutputMessage(message.getFrom(), message.getText(), time);
	}
	@MessageMapping("/queue/some-queue")
	public void getMessage(@Payload byte[] bytes) {
		try {
			
			File k =new File("/Users/itadminsalesken/Documents/my.pcm");
			if(!k.exists()) {
				k.createNewFile();
			}
			FileUtils.writeByteArrayToFile(k, bytes, true);

		

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("recieved msg {}", bytes.length);
	}
}
