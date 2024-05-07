import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/*
 * SCHEDULE PLANNER BOT
 */
public final class SchedulePlannerBot {
    private SchedulePlannerBot() {
    }

    public static void main(String[] args) throws InterruptedException {
        /*
         * FIND CLASS
         */
        System.setProperty("webdriver.chrome.driver",
                "H:\\Eclipse\\chromedriver.exe");
        boolean print = false;
        int attempt = 0;
        ChromeOptions option = new ChromeOptions();
        option.setExperimentalOption("debuggerAddress", "localhost:9222");
        WebDriver driver = new ChromeDriver(option);
        // driver.get("https://ohiostate.collegescheduler.com/terms/Autumn%202022%20Semester/courses/8594654");
        while (true) {
            while (true) {
                try {
                    driver.navigate().refresh();
                    TimeUnit.SECONDS.sleep(10);
                    if (driver
                            .findElement(By.xpath(
                                    "//*[text()='Regular Academic Term']"))
                            .isDisplayed()
                            || driver
                                    .findElement(
                                            By.xpath("//*[text()='Columbus']"))
                                    .isDisplayed()
                            || driver.findElement(By.xpath("//*[text()='LEC']"))
                                    .isDisplayed()
                            || driver
                                    .findElement(
                                            By.xpath("//*[text()='Online']"))
                                    .isDisplayed()) {
                        print = true;
                        break;
                    }
                } catch (Exception ie) {
                    //Safe to wait up until 45 minutes
                    TimeUnit.MINUTES.sleep(30);
                    driver.navigate().refresh();
                }
                attempt++;
                System.out.println(print + "! - attempts: " + attempt);
            }
            System.out.println(print + "! - attempts: " + attempt);

            /*
             * EMAIL ME
             */

            // Recipient's email ID needs to be mentioned.
            String to = "";

            // Sender's email ID needs to be mentioned
            String from = "";

            // Assuming you are sending email from through gmails smtp
            String host = "smtp.gmail.com";

            // Get system properties
            Properties properties = System.getProperties();

            // Setup mail server
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            // Get the Session object.// and pass username and password
            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {

                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication(
                                    "",
                                    "");
                        }

                    });

            // Used to debug SMTP issues
            session.setDebug(true);

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("COMP NETWORKING SECTION HAS BEEN CREATED");

                // Now set the actual message
                message.setText("SYSTEM RAN: " + attempt
                        + " TIMES\nGO, GO, GO!");

                System.out.println("sending...");
                // Send message
                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }

            //Wait an hour to recheck - 30 min interval for page life
            driver.navigate().refresh();
            TimeUnit.MINUTES.sleep(30);
            driver.navigate().refresh();
            TimeUnit.MINUTES.sleep(30);
            attempt = 0;
        }

    }

}