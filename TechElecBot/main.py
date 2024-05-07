from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.select import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException
import time
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

# Technical Elective Bot

def main():
    # Initialization of Selenium
    chrome_options = Options()
    chrome_options.add_argument('--no-sandbox')
    chrome_options.binary_location = '/usr/bin/google-chrome'
    driver = webdriver.Remote(command_executor="http://localhost:9515",options=chrome_options)
    driver.get("https://courses.osu.edu/psp/csosuct/EMPLOYEE/PUB/c/COMMUNITY_ACCESS.CLASS_SEARCH.GBL?PortalKeyStruct=yes")
    frame = driver.find_element_by_xpath("//iframe[@id='ptifrmtgtframe']")
    driver.switch_to.frame(frame)
    wait = WebDriverWait(driver, 10)

    # Select the semester
    semester_dropdown = wait.until(EC.presence_of_element_located((By.ID, 'CLASS_SRCH_WRK2_STRM$35$')))
    semesterObject = Select(semester_dropdown)
    semesterObject.select_by_visible_text('Autumn 2023')

    # Select the campus
    wait = WebDriverWait(driver, 10)
    time.sleep(1)
    campus_dropdown = wait.until(EC.presence_of_element_located((By.ID, 'SSR_CLSRCH_WRK_CAMPUS$0')))
    campusObject = Select(campus_dropdown)
    # campusObject.select_by_visible_text('Columbus')   
    campusObject.select_by_value('COL')   

    # Select the major
    time.sleep(1)
    major_dropdown = wait.until(EC.presence_of_element_located((By.ID, 'SSR_CLSRCH_WRK_SUBJECT_SRCH$1')))
    majorObject = Select(major_dropdown)
    majorObject.select_by_visible_text('Computer Science & Engineering')

    # Input the class number
    time.sleep(1)
    classNumber = wait.until(EC.presence_of_element_located((By.ID, 'SSR_CLSRCH_WRK_CATALOG_NBR$2')))
    classNumber.clear()
    classNumber.send_keys('3461')

    # Click the search button
    time.sleep(1)
    searchButton = driver.find_element_by_id('CLASS_SRCH_WRK2_SSR_PB_CLASS_SRCH')
    searchButton.click()

    # Reclick the search button until it is no longer there (because the class page would replace it)
    counter = 0
    while (True):
        counter += 1
        try:
            time.sleep(1)
            researchButton = driver.find_element_by_id('CLASS_SRCH_WRK2_SSR_PB_CLASS_SRCH')
            print("Class not found, researching...\nTimes Ran: " + str(counter))
            time.sleep(10)
            researchButton.click()
        except NoSuchElementException:
            print ("Class found!\nSending Email\nRan " + str(counter) + " times.")
            # Gmail account details
            email = ''
            password = ''
            sendto = ''

            # Create a multipart message
            message = MIMEMultipart()
            message['From'] = email
            message['To'] = sendto
            message['Subject'] = 'CSE 5463 SECTION OPENED'

            # Email body
            email_body = '''
            !!!Server Alert!!!

            CSE 5463 section has been opened!

            Enroll!     Enroll!     Enroll!
            '''
            message.attach(MIMEText(email_body, 'plain'))

            # Connect to the SMTP server (Gmail)
            smtp_server = 'smtp.gmail.com'
            smtp_port = 587
            smtp_connection = smtplib.SMTP(smtp_server, smtp_port)
            smtp_connection.starttls()
            smtp_connection.login(email, password)
            smtp_connection.sendmail(email, sendto, message.as_string())
            smtp_connection.quit()
            break

if __name__ == "__main__":
    main()