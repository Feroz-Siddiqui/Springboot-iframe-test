package feroz.spring_phase_1.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import feroz.spring_phase_1.dbmodel.ProfileDBFile;
import feroz.spring_phase_1.dbrepo.ProfileDBFileRepo;
import org.apache.commons.io.IOUtils;


@SpringBootTest

public class DBFileInsertion {

	
	@Autowired
	ProfileDBFileRepo profileDBFileRepo;
	
	
	@Test
	void contextLoads() {
		System.out.println("hshshhshshshsh....");
		
		retriveFile();
		//storeFile();
		
	}


	private void storeFile() {
		File file = new File("/Users/itadminsalesken/Downloads/test.jpeg");

		try {
			profileDBFileRepo.save(new ProfileDBFile("test2.jpeg", "jpeg", IOUtils.toByteArray(new FileInputStream(file)),file.length()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void retriveFile() {
		Optional<ProfileDBFile> kbc =profileDBFileRepo.findById(3l);
		if(kbc.isPresent()) {
			File file = new File("/Users/itadminsalesken/Downloads/test1.jpeg");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				IOUtils.write(kbc.get().getFile_content(), new FileOutputStream(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}
}
