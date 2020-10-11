# import time
# # importing webdriver from selenium
# from selenium import webdriver
#
# # Here Chrome  will be used
# driver = webdriver.Chrome()
#
# # URL of website
# url = "C:/Users/bogdan.pugin/Desktop/vtb/index.html"
#
# # Opening the website
# driver.get(url)
#
# # geeting the button by class name
# button = driver.find_element_by_class_name("button")


from selenium import webdriver
import time
from os import listdir
from selenium.common.exceptions import WebDriverException

def main():
    url = "C:/Users/bogdan.pugin/Desktop/vtb/shop.html"
    driver = webdriver.Chrome('C:/Users/bogdan.pugin/Downloads/chromedriver_win32/chromedriver.exe')

    try:
                file_name = 'file://' + url
                driver.get(file_name)
                button = driver.find_element_by_class_name("button1")

                # clicking on the button

                time.sleep(5)
                for _ in range(1, 10):
                    button.click()
    finally:
        driver.quit()

if __name__ == "__main__":
    main()
#
# # clicking on the button
# button.click()