package feroz.spring_phase_1.restcontroller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import feroz.spring_phase_1.model.AppResponse;

@RestController
@RequestMapping("/file")
public class FileRestController {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping(value = "/uploadFile")
	public AppResponse submit(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
	    modelMap.addAttribute("file", file);
		File newFile = new File(file.getOriginalFilename());
		
		if(!newFile.exists())
			try {
				newFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		logger.info("file upload getting called ............"+newFile.getAbsolutePath());
		try {
			FileUtils.writeByteArrayToFile(newFile, file.getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		    return new AppResponse(false, 2099);
		}
		
	
	    
	    return new AppResponse(true, 3198);
	}
	

}
