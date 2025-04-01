package chris.dicom.service;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import chris.dicom.bean.RequestBean;
import chris.dicom.bean.ResponseBean;
import chris.dicom.util.SocketUtil;

@RestController
@RequestMapping("/Chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatService {

	
	
	 @RequestMapping(value="/Query", method=RequestMethod.POST)
	 @ResponseBody
	 public String DBEntry(@RequestBody  RequestBean req) {
		
		Gson gson = new Gson();
		ResponseBean response=new ResponseBean();
		SocketUtil socUtil=new SocketUtil();
		String reply=socUtil.communicate_Brain(req.getQuestion());
		if(reply.startsWith("ERROR")) {
			response.setIsError(1);
			response.setMessage(reply);
			
		}else
			response.setObj(reply);
		
			
		 
		 return  gson.toJson(response);
		 
	 }
}
