package utilities.Gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import cucumber.api.DataTable;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import utilities.GetProperties;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class GmailAPI {

    private Gmail service;
    private static final List<String> PROCESSED_LABEL = Arrays.asList("Label_1");

    public GmailAPI() throws Exception {
        this.service = getGmailService();
    }

    private static final String APPLICATION_NAME = "match-ui-he QA BDD Framework";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.dir") + "/src/bddTest/resources/Gmail");
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at /src/bddTest/resources/Gmail/StoredCredential
     *
     */
    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS,GmailScopes.GMAIL_READONLY,GmailScopes.GMAIL_MODIFY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException -
     */
    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(System.getProperty("user.dir") + "/src/bddTest/resources/Gmail/gmail-api-credentials.json");
        // This doesn't work because it wants to set permissions and can't handle Windows 10, apparently...
        // InputStream in = GmailAPI.class.getResourceAsStream("C:\\SeleniumScripts\\RadiusQAAutomation\\src\\test\\resources\\Gmail\\gmail-api-credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        // TODO - Can we automate this so that no manual intervention is needed when credentials expire?
        // This will check saved credentials, and open default browser on machine if they are expired.
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Gmail user credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException -
     */
    private static Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }


    /**
     * Return emails from the Gmail API for the given criteria, within the last day and without the "Processed" label.
     * @param inputData DataTable containing message information to search for.  Valid search fields are:
     *                  From, CC, Subject, Body, Messages (number of messages that should be found)
     * @return a List of Email objects
     * @throws Exception when DataTable contains invalid data, or when number of messages returned is incorrect.
     */
    public List<Email> getMessages(DataTable inputData) throws Exception {
        List<Email> messageList = new LinkedList<>();
        List<Map<String, String>> inputs = inputData.asMaps(String.class, String.class);
        String gmailQueryString = "newer_than:1d -label:Processed ";
        int expectedResults;
        for (Map<String,String> input : inputs){
            expectedResults = 1;
            for (String key : input.keySet()){
                switch (key.toLowerCase()){
                    case "from":
                        gmailQueryString = gmailQueryString.concat("from:" + input.get(key) + " ");
                        break;
                    case "to":
                        gmailQueryString = gmailQueryString.concat("to:" + input.get(key) + " ");
                        break;
                    case "cc":
                        gmailQueryString = gmailQueryString.concat("cc:" + input.get(key) + " ");
                        break;
                    case "subject":
                        gmailQueryString = gmailQueryString.concat("subject:" + input.get(key) + " ");
                        break;
                    case "body":
                        gmailQueryString = gmailQueryString.concat("\"" + input.get(key) + "\" ");
                        break;
                    case "messages":
                        expectedResults = Integer.parseInt(input.get(key));
                        break;
                    default:
                        throw new Exception("Error: Unrecognized input in DataTable for getMessage()");
                }
            }
            DateTime now = null;
            ListMessagesResponse messageResponse;
            long timeoutExpiredSeconds = (System.currentTimeMillis() / 1000) + Long.parseLong(GetProperties.get("gmailAPIWait"));
            Boolean foundMessages = false;
            List<Message> messages = null;
            while (!foundMessages) {
                now = new DateTime(System.currentTimeMillis());
                messageResponse = service.users().messages().list("me").setQ(gmailQueryString).execute();
                messages = messageResponse.getMessages();
                foundMessages = (messages != null);
                Long seconds = (System.currentTimeMillis() / 1000);
                if (seconds >= timeoutExpiredSeconds) {
                    throw new Exception("Error:  Timed out waiting for Gmail messages after " + Long.parseLong(GetProperties.get("gmailAPIWait")) + " seconds.");
                }
            }
            now = now.plus(Period.seconds(30));
            DateTime now1 = now.minus(Period.minutes(2));
            Interval last1 = new Interval(now1,now);
            Integer totalMessages = messages.size();
            for (Message message : messages) {
                Email email = new Email();
                String messageId = (String) message.get("id");
                Boolean recent = false;

                // Set the "Processed" label on the message.
                ModifyMessageRequest mods = new ModifyMessageRequest().setAddLabelIds(PROCESSED_LABEL);
                service.users().messages().modify("me", messageId, mods).execute();

                // Get the message contents.
                Message messageContent = service.users().messages().get("me", messageId).execute();

                // Parse Headers for what we need.
                MessagePartHeader header;
                Iterator it = messageContent.getPayload().getHeaders().iterator();
                while (it.hasNext()){
                    header = (MessagePartHeader) it.next();
                    switch (header.getName()){
                        case "From":
                            email.setFrom(header.getValue());
                            break;
                        case "Subject":
                            email.setSubject(header.getValue());
                            break;
                        case "Cc":
                            email.setCC(header.getValue());
                            break;
                        case "Date":
                            // Gmail JSON date format is:
                            // Mon, 13 Jun 2016 19:07:07 +0000
                            DateTime emailTime = DateTime.parse(header.getValue(), DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z"));
                            // Did our message arrive in the last minute?
                            if(last1.contains(emailTime.toInstant())){
                                recent = true;
                            }
                            break;
                    }
                    it.remove();
                }
                String messageBody;
                if (messageContent.getPayload().getBody().getSize() != 0) {
                    messageBody = messageContent.getPayload().getBody().getData();
                } else {  // multi-part message, get the body content from parts instead
                    messageBody = messageContent.getPayload().getParts().get(0).getBody().getData();
                }
                // Gmail's Base64 encoding uses specific parameters, this cleans up so we can decode to the real text.
                messageBody = messageBody.replace("-", "+");
                messageBody = messageBody.replace("_", "/");
                email.setBody(new String(DatatypeConverter.parseBase64Binary(messageBody)));
                // If our message is from the last minute, it's probably the one we're interested in.
                if (recent) {
                    messageList.add(email);
                // It's not from the last minute, it's probably from a failed test earlier in the day, ignore it.
                } else {
                    totalMessages--;
                }
            }
            if (totalMessages != expectedResults) {
                throw new Exception("Error: Expected to find " + expectedResults + " messages, but found " + totalMessages);
            }
        }
        return messageList;
    }
}
