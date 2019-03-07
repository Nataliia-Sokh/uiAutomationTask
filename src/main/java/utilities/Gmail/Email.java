package utilities.Gmail;

public class Email {

    private String body;
    private String from;
    private String subject;
    private String cc;

    public String toString(){
        return "\n to:      purpleheautomation@gmail.com" +
               "\n from:    " + from +
               "\n cc:      " + cc +
               "\n subject: " + subject +
               "\n body:    " + body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCC() {
        return cc;
    }

    public void setCC(String cc) {
        this.cc = cc;
    }
}
