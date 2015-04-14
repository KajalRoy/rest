package init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.User;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import controllers.UsrRestURIConstant;

public class Initialize {
	
public static final String SERVER_URI = "http://localhost:8080/rest/";
    
	static MongoTemplate mongoTemplate;
	static RestTemplate restTemplate;
	
    @SuppressWarnings("resource")
	public static void main(String args[]){
    	
    	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("file:/Users/kajroy/Documents/workspace-sts-3.6.3.RELEASE/rest/WebContent/WEB-INF/rest-servlet.xml");
    	
    	mongoTemplate = context.getBean(MongoTemplate.class);
    	
    	//dropCollection();
    	
    	if (!mongoTemplate.collectionExists(User.class))
			mongoTemplate.createCollection(User.class);
    	
    	restTemplate = new RestTemplate();
    	restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
    	restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

         
        testGetDummyEmployee();
        System.out.println("*****");
        testGetAllEmployee();
        System.out.println("*****");
        testDeleteEmployee();
        testCreateEmployee();
        //dropCollection();
    }
 
    private static void testGetAllEmployee() {
    	System.out.println("testGetAllEmployee");
      
    	HttpHeaders requestHeaders = new HttpHeaders();
    	requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
    	HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
    	
    	ResponseEntity<User[]> responseEntity = restTemplate.exchange(SERVER_URI+"emps/0", HttpMethod.GET, requestEntity, User[].class);
    	User[] emps = responseEntity.getBody();
    	System.out.println(emps.length);
        for(User map : emps){
            System.out.println(map);
        }
    }
 
    private static void testCreateEmployee() {

    	System.out.println("testCreateEmployee");
    	
        User emp = new User();
        //emp.setId(1);
        emp.setfullname("Nidhi Kumar");
        emp.setEmail("nidhi@mail.com"); emp.setAge(21);
        emp.setGender("Female"); emp.setLocation("Delhi");
        emp.setusername("nidhi"); emp.setPassword("nidhi");
        
        try{
        User response = restTemplate.postForObject(SERVER_URI+UsrRestURIConstant.CREATE_EMP, emp, User.class);
        printEmpData(response);
        }catch(HttpClientErrorException e){
        	e.printStackTrace();

        }
  
    }
 
    private static void testGetEmployee() {
    	
    	System.out.println("testGetEmployee");
    	
        User emp = restTemplate.getForObject(SERVER_URI+"emp/nidhi", User.class);
        printEmpData(emp);
        System.out.println("*****");
        emp = restTemplate.getForObject(SERVER_URI+"emp/arjun", User.class);
        printEmpData(emp);
    }
    
    private static void testDeleteEmployee() {
    	
    	System.out.println("testDeleteEmployee");
    	
        restTemplate.delete(SERVER_URI+"emp/delete/nidhi");
    }
 
    private static void testGetDummyEmployee() {
    	
    	System.out.println("testGetDummyEmployee");
    	
        User emp = restTemplate.getForObject(SERVER_URI+UsrRestURIConstant.DUMMY_EMP, User.class);
        if(emp!=null) printEmpData(emp);
    }
     
    public static void printEmpData(User emp){
        System.out.println(emp);
    }
    
    public static void dropCollection() {
		if (mongoTemplate.collectionExists(User.class)) {
			mongoTemplate.dropCollection(User.class);
		}
	}


}
