package controllers;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.BadSecurityHandlerException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import play.Play;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

/**
 * @author valore
 *
 */
public class Application extends Controller {

	public static Result index() {
		return ok();
	}
	
	public static Result protectPdf() {
		DynamicForm form = DynamicForm.form().bindFromRequest();
		MultipartFormData body = request().body().asMultipartFormData();		
		
		String userName = form.get("userName");
		String password = form.get("password");
		FilePart profileImage = body.getFile("pdfDocument");
		
		File pdfDocument = profileImage.getFile();
		String fileName = profileImage.getFilename();
		
		String mainDirectory = Play.application().path() + File.separator + "public" + File.separator + "documents";
		
		File mainDirectoryPath = new File(mainDirectory);
		
		if(mainDirectoryPath.isDirectory()) {
			for (File file : mainDirectoryPath.listFiles()) {
			    try {
					FileDeleteStrategy.FORCE.delete(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		}
		
	    String filePath = mainDirectory + File.separator + fileName;
	    File profileImageDirectory = new File(filePath);    
	    
	    
		try {
			FileUtils.copyFile(pdfDocument, profileImageDirectory);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
    	PDDocument doc = null;
    	
		try {
			doc = PDDocument.load(filePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
    	int keyLength = 128;

    	AccessPermission ap = new AccessPermission();

    	// disable printing, everything else is allowed
    	ap.setCanPrint(false);
    	
    	StandardProtectionPolicy spp = new StandardProtectionPolicy(userName, password, ap);
    	
    	spp.setEncryptionKeyLength(keyLength);
    	spp.setPermissions(ap);
    	
    	try {
			doc.protect(spp);
			doc.save(filePath);
			doc.close();
		} catch (BadSecurityHandlerException | IOException | COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ok(new File(filePath));

	}
}
