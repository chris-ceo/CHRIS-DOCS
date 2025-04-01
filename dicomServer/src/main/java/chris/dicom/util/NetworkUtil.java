package chris.dicom.util;

import java.net.ConnectException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class NetworkUtil {
	
	public String  postData(Object http_Entity,String url) throws ConnectException {
//		Gson gson = new Gson();
		String opt="";
		try {
			RestTemplate restTemplate = new RestTemplate();
			 HttpHeaders headers = new HttpHeaders();
			 headers.setContentType(MediaType.APPLICATION_JSON);
			 HttpEntity<?> entity = new HttpEntity(http_Entity,headers); 
			 ResponseEntity<String> output =  restTemplate.exchange( url,
						HttpMethod.POST, entity, String.class, new Object[0]);
			 	
				System.out.println(output.getStatusCode());
				System.out.println(output.getBody());
				
				
					opt= output.getBody().toString();
				
		}catch(HttpClientErrorException e) {
			opt= "Error from SWP Portal "+ e.getMessage();
		}
		return opt;
			
	}

}
