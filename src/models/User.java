package models;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User implements Serializable{
	
	private static final long serialVersionUID = -7788619177798333712L;

	//@Id
	//private Integer id;
	private Integer age;
	@Indexed(unique = true)
	private String username;
	private String password;
	private String fullname;
	private String location;
	private String email;
	private String gender;
	
	public User(){
		
	}
	
	public User(Integer age, String fullname, String email, String location, String gender, String username, String password) {
		//this.id = id;
		this.age = age;
		this.fullname = fullname;
		this.email = email;
		this.location = location;
		this.gender = gender;
		this.username = username;
		this.password = password;
	}
	/*
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	*/
	
	public Integer getAge() {  
    	return age;  
    }  
  
    public void setAge(Integer age) {  
    	this.age = age;  
    }  
	
	public String getfullname() {  
        return fullname;  
    }  
      
    public void setfullname(String fullname) {  
        this.fullname = fullname;  
    }
    
    public String getEmail() {  
        return email;  
    }  
      
    public void setEmail(String email) {  
        this.email = email;  
    }  
  
    public String getLocation() {  
    	return location;  
    }  
  
    public void setLocation(String location) {  
    	this.location = location;  
    }  

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getGender() {  
    	return gender;  
    }  
  
    public void setGender(String gender) {  
    	this.gender = gender;  
    } 
         
    @Override
    public String toString(){
        return "User: {Name="+fullname+",Email="+email+", Gender="+gender+", Age="+age+",Location="+location+"}";
    }
}
