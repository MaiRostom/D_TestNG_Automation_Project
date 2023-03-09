import net.bytebuddy.build.Plugin;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginTestCases {
    WebDriver driver;
    @BeforeMethod
    public void openBrowser(){
        String key="webdriver.chrome.driver";
        String value=System.getProperty("user.dir")+"//Browsers//chromedriver.exe";
        System.setProperty(key,value);
       driver=new ChromeDriver();
       driver.manage().window().maximize();
       driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
       driver.navigate().to("https://the-internet.herokuapp.com/");
      // driver.findElement(By.cssSelector("a[href=\"/login\"]")).click();
        System.out.println("before Data");

    }

    @Test (priority = 0)
    public void validData(){
      driver.findElement(By.cssSelector("input[type=\"text\"][name=\"username\"]")).sendKeys("tomsmith");
      driver.findElement(By.cssSelector("div[class=\"large-6 small-12 columns\"] input[type=\"password\"]")).sendKeys("SuperSecretPassword!");
      driver.findElement(By.cssSelector("button[class=\"radius\"][type=\"submit\"]")).click();
      //compare actual URL and expected URL
        String actualURL= driver.getCurrentUrl();
        String expectedURL="https://the-internet.herokuapp.com/secure";
        Assert.assertEquals(actualURL,expectedURL);
        //Compare The word Secure Area appears
        String actualText=driver.findElement(By.cssSelector("div[class=\"example\"] h2")).getText();
        String expectedText="Secure Area";
        Assert.assertTrue(actualText.contains(expectedText));
        //verify page contains"you logged into a secure area"
        String expectedSuccess="You logged into a secure area!";
        String actualSuccess=driver.findElement(By.id("flash")).getText();
        Assert.assertTrue(actualSuccess.contains(expectedSuccess),"Third Assert");
        //success msg color is green
        String actualColor=driver.findElement(By.id("flash")).getCssValue("background-color");
        actualColor= Color.fromString(actualColor).asHex();
        String expectedColor="#5da423";
        Assert.assertEquals(actualColor,expectedColor);
        //logout button is dispayed
       boolean isDisplayed= driver.findElement(By.cssSelector("a[href=\"/logout\"]")).isDisplayed();
        Assert.assertTrue(isDisplayed);
    }
    @Test(priority = 1)
    public void invalidDAta(){
        driver.findElement(By.id("username")).sendKeys("mai");
        driver.findElement(By.name("password")).sendKeys("rostom");
        driver.findElement(By.cssSelector("button[class=\"radius\"][type=\"submit\"]")).click();
        //failure message
        SoftAssert soft=new SoftAssert();
        String actualFailedMessage=driver.findElement(By.id("flash")).getText();
        String expectedFailedMessage="Your username is invalid!";
        soft.assertTrue(actualFailedMessage.contains(expectedFailedMessage),"Invalid name");
        soft.assertAll();
        //Color is red
       String actualBackgroudFailedColor=driver.findElement(By.id("flash")).getCssValue("background-color");
       actualBackgroudFailedColor=Color.fromString(actualBackgroudFailedColor).asHex();
       String expectedBackgroundFailedColor="#c60f13";
       soft.assertEquals(actualBackgroudFailedColor,expectedBackgroundFailedColor);
       soft.assertAll();

    }
    @Test(priority = 2)
    public void staticDropdownList() throws InterruptedException {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        WebElement dropdownList= driver.findElement(By.id("dropdown"));
        Select select=new Select(dropdownList);
        Thread.sleep(2000);
        select.selectByValue("1");
    }
    @Test(priority = 2)
    public void dynamicDropdownList() throws InterruptedException {
        driver.get("https://www.google.com/");
        driver.findElement(By.cssSelector("input[class=\"gLFyf\"]")).sendKeys("selenuim");
        Thread.sleep(5000);
        List<WebElement> list = driver.findElements(By.cssSelector("ul[jsname=\"bw4e9b\"]"));
        list.get(3).click();
    }
        @Test(priority = 2)
        public void switchTabs() throws InterruptedException {
        //1-get url
        driver.get("https://www.nopcommerce.com/en/demo");
       //2- go to facebook tab
        driver.findElement(By.cssSelector("a[href=\"https://www.facebook.com/nopCommerce\"]")).click();
        //3-wait until facebook tab is loaded
        //Thread.sleep(2000);
            WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        //4-make arraylist to switch between tabs o,1
        ArrayList <String> tabs=new ArrayList<>(driver.getWindowHandles());
        //5-switch driver from tab1 to tab2
            driver.switchTo().window(tabs.get(1));
            //make assertion
            Thread.sleep(1000);
            Assert.assertEquals(driver.getCurrentUrl(),"https://www.facebook.com/nopCommerce");
        //6-close facebook tab
            driver.close();
        //7-switch to tab 0
            driver.switchTo().window(tabs.get(0));
            //8-go to twitter tab
            driver.findElement(By.cssSelector("a[href=\"https://twitter.com/nopCommerce\"]")).click();
            // wait until tab is loaded
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            //load tabs
            tabs=new ArrayList<>(driver.getWindowHandles());
            //switch between tabs
            driver.switchTo().window(tabs.get(1));
            Thread.sleep(1000);
            Assert.assertEquals(driver.getCurrentUrl(),"https://twitter.com/nopCommerce");
        }


    @Test(priority=3)
    public void KeyPresses(){
        driver.findElement(By.cssSelector("a[href=\"/key_presses\"]")).click();
        driver.findElement(By.id("target")).sendKeys(Keys.chord(("6")+Keys.LEFT_SHIFT +".")+"5");

    }
    @Test(priority = 0)
    public void checkSlider(){
        driver.findElement(By.cssSelector("a[href=\"/horizontal_slider\"]")).click();

        WebElement slider=driver.findElement(By.cssSelector("input[type=\"range\"]"));
        for(int i=0;i<8;i++) {
            slider.sendKeys(Keys.chord(Keys.RIGHT));
        }
        String actualSliderNo = driver.findElement(By.id("range")).getText();
        Assert.assertEquals(actualSliderNo,"4");
    }
    @Test
    public void jsAlertMsg(){
        driver.findElement(By.cssSelector("a[href=\"/javascript_alerts\"]")).click();
        driver.findElement(By.cssSelector("button[onclick=\"jsAlert()\"]")).click();
        driver.switchTo().alert().accept();
        String actualSuccessMsg=driver.findElement(By.id("result")).getText();
        String actualSuccessColor=driver.findElement(By.id("result")).getCssValue("color");
        actualSuccessColor=Color.fromString(actualSuccessColor).asHex();
        Assert.assertEquals(actualSuccessMsg,"You successfully clicked an alert");
        Assert.assertEquals(actualSuccessColor,"#008000");

    }
    @Test
    public void confirmAlertMsg() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href=\"/javascript_alerts\"]")).click();
        driver.findElement(By.cssSelector("button[onclick=\"jsConfirm()\"]")).click();
        Thread.sleep(3000);
        driver.switchTo().alert().dismiss();
        String actualCancelledMsg=driver.findElement(By.id("result")).getText();
        String actualCanceledColor=driver.findElement(By.id("result")).getCssValue("color");
        actualCanceledColor=Color.fromString(actualCanceledColor).asHex();
        Assert.assertEquals(actualCancelledMsg,"You clicked: Cancel");
        Assert.assertEquals(actualCanceledColor,"#008000");
    }

    @Test
    public void jsPromptAlert() throws InterruptedException {
        driver.findElement(By.cssSelector("a[href=\"/javascript_alerts\"]")).click();
        driver.findElement(By.cssSelector("button[onclick=\"jsPrompt()\"]")).click();
        driver.switchTo().alert().sendKeys("hello all");
        Thread.sleep(3000);
        driver.switchTo().alert().accept();
        String actualWrittenMsg=driver.findElement(By.id("result")).getText();
        String actualWrittenColor=driver.findElement(By.id("result")).getCssValue("color");
        actualWrittenColor=Color.fromString(actualWrittenColor).asHex();
        Assert.assertEquals(actualWrittenMsg,"You entered: hello all");
        Assert.assertEquals(actualWrittenColor,"#008000");
    }

    @Test(priority=0)
    public void shifting_Content(){
        driver.findElement(By.linkText("Shifting Content")).click();
        driver.findElement(By.cssSelector("a[href=\"/shifting_content/menu\"]")).click();
        List<WebElement>list=driver.findElements(By.tagName("a"));
        System.out.println(list.size());
    }

    @AfterMethod
    public void quitBrowser() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("after Data");
        driver.quit();


    }
}
