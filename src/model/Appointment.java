package model;

public class Appointment {

    private String id;                  // appointment id
    private String patientId;           // patient id
    private String clinicianId;         // clinician id
    private String facilityId;          // facility id
    private String appointmentDate;     // appointment date
    private String appointmentTime;     // appointment time
    private String durationMinutes;     // duration 
    private String appointmentType;     // typappointment  
    private String status;              // status            
    private String reasonForVisit;      // reason for visit
    private String notes;               // notes
    private String createdDate;        
    private String lastModified;        // last modified

    public Appointment() { }

    public Appointment(String id,
                       String patientId,
                       String clinicianId,
                       String facilityId,
                       String appointmentDate,
                       String appointmentTime,
                       String durationMinutes,
                       String appointmentType,
                       String status,
                       String reasonForVisit,
                       String notes,
                       String createdDate,
                       String lastModified) {

        this.id = id;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.facilityId = facilityId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.appointmentType = appointmentType;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.notes = notes;
        this.createdDate = createdDate;
        this.lastModified = lastModified;
    }

    public String getId()                { return id; }
    public String getPatientId()         { return patientId; }
    public String getClinicianId()       { return clinicianId; }
    public String getFacilityId()        { return facilityId; }
    public String getAppointmentDate()   { return appointmentDate; }
    public String getAppointmentTime()   { return appointmentTime; }
    public String getDurationMinutes()   { return durationMinutes; }
    public String getAppointmentType()   { return appointmentType; }
    public String getStatus()            { return status; }
    public String getReasonForVisit()    { return reasonForVisit; }
    public String getNotes()             { return notes; }
    public String getCreatedDate()       { return createdDate; }
    public String getLastModified()      { return lastModified; }

    public void setId(String id)                          { this.id = id; }
    public void setPatientId(String patientId)            { this.patientId = patientId; }
    public void setClinicianId(String clinicianId)        { this.clinicianId = clinicianId; }
    public void setFacilityId(String facilityId)          { this.facilityId = facilityId; }
    public void setAppointmentDate(String appointmentDate){ this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(String appointmentTime){ this.appointmentTime = appointmentTime; }
    public void setDurationMinutes(String durationMinutes){ this.durationMinutes = durationMinutes; }
    public void setAppointmentType(String appointmentType){ this.appointmentType = appointmentType; }
    public void setStatus(String status)                  { this.status = status; }
    public void setReasonForVisit(String reasonForVisit)  { this.reasonForVisit = reasonForVisit; }
    public void setNotes(String notes)                    { this.notes = notes; }
    public void setCreatedDate(String createdDate)        { this.createdDate = createdDate; }
    public void setLastModified(String lastModified)      { this.lastModified = lastModified; }
}
