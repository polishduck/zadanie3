package app.model;

import java.util.Calendar;
import java.util.Date;

import sun.util.resources.CalendarData;

public class patientData {

	private String fullname;
	private String name;
	private String surname;
	private Calendar birth;
	private String modality;

    public patientData()
    {
    }

    public patientData(String cname, Calendar cbirth, String cmodality)
    {
    	if (cname == null)
    		fullname = "<unknown>";
    	else
    		fullname = cname;
    			
    	if (cbirth == null)
    		birth = null;
    	else
    		birth = cbirth;
    	if (cmodality == null)
    		modality = "<unknow>";
    	else
    		modality = cmodality;
    }

	
	//GETTERS & SETTERS
	
	public String getFullName() {
		return fullname;
	}

	public void setFullName(String name) {
		this.fullname = name;
	}

	public Calendar getBirthDate() {
		return birth;
	}

	public void setBirthDate(Calendar date) {
		this.birth = date;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String mod) {
		this.modality = mod;
	}



}
