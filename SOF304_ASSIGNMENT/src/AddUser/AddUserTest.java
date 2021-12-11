package AddUser;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class AddUserTest {
	private WebDriver driver;

	@BeforeClass
	public void prepareDashboard() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\haunv\\OneDrive\\Máy tính\\java\\chorme driver\\chromedriver.exe");
		driver = new ChromeDriver();
//		String url = "https://animenews.life/shiki-admin/";
		String url = "https://truyenmoinhat.xyz/wp-admin";
		driver.get(url);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys("wordpress");
//		driver.findElement(By.xpath("//*[@id=\"user_pass\"]")).sendKeys("Oris15031601");
		
		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id=\"user_pass\"]")).sendKeys("$wordpress1234");
		driver.findElement(By.xpath("//*[@id=\"wp-submit\"]")).click();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@BeforeMethod
	public void refreshPageAddUser() {
//		driver.navigate().to("https://animenews.life/wp-admin/user-new.php");
		driver.navigate().to("https://truyenmoinhat.xyz/wp-admin/user-new.php");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.navigate().refresh();
		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"pass1\"]")).clear();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@AfterMethod
//	public void redirectUserpage() {
//		driver.navigate().to("https://animenews.life/wp-admin/users.php");
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	@AfterClass
	public void close() {
		driver.close();
	}

	@DataProvider(name = "Provider")
	public Object[][] getDataFromExcel() {
		Object[][] vals = new Object[36][12];
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream("AddUser.xls"));
			Sheet sheet = wb.getSheetAt(0);

			int i = 0, j = i;
			for (Row row : sheet) {
				if (i != 0) {
					for (Cell cell : row) {
						String text = cell.toString().trim();
						vals[i][j++] = text.isEmpty() ? null : text.replace(".0", "");
					}
				}
				j = 0;
				i++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vals;
	}

	@Test
	@Parameters({ "username", "email" })
	public void checkFocusUserAndEmail(@Optional("9999") String username, @Optional("9999") String email) {
		driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[1]/td/input")).sendKeys(username);
		driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[2]/td/input")).sendKeys(email);
		driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/p/input")).click();
		String actual = null;
		if(email.isEmpty()) { // focus email none
			actual = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[2]/td/input")).getCssValue("border-color");
			assertEquals(actual, "rgb(214, 54, 56)");
		}
		else { // focus username none
			actual = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[1]/td/input")).getCssValue("border-color");
			assertEquals(actual, "rgb(214, 54, 56)");
		}
		
	}

	@Test
	@Parameters({ "password" })
	public void checkFocusPassword(@Optional("9999") String password) {
		driver.findElement(By.xpath("//*[@id=\"pass1\"]")).sendKeys(password);
		driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/p/input")).click();
		String actual= driver.findElement(By.xpath("//*[@id=\"pass1\"]")).getCssValue("border-color");
		
		System.out.println(actual);
		assertEquals(actual, "rgb(214, 54, 56)");
	}

	@Test
	@Parameters({ "password", "expected" })
	public void activeCheckBoxConfirmWeakPassword(@Optional("9999") String password, @Optional("9999") String expected) {
		
		driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/p/input")).click();
		String actual = actual = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[9]"))
				.getCssValue("display");
		
		assertEquals(actual, expected);
	}

	@Test
	@Parameters({ "username", "email", "website" })
	public void isWebsiteFiedSave() {
	}

	@Test(dataProvider = "Provider")
	@Parameters({ "testname", "username", "email", "firstname", "lastname", "website", "language", "password",
			"sendUserNotice", "role", "expected", "type" })
	public void addUser(@Optional("9999") String testname, @Optional("9999") String username,
			@Optional("9999") String email, @Optional("9999") String firstname, @Optional("9999") String lastname,
			@Optional("9999") String website, @Optional("9999") String language, @Optional("9999") String password,
			@Optional("9999") String sendUserNoTice, @Optional("9999") String role, @Optional("9999") String expected,
			@Optional("9999") String type) {

//		if (testname.equalsIgnoreCase("9999") || username.equalsIgnoreCase("9999") || email.equalsIgnoreCase("9999")
//				|| firstname.equalsIgnoreCase("9999") || lastname.equalsIgnoreCase("9999")
//				|| website.equalsIgnoreCase("9999") || language.equalsIgnoreCase("9999")
//				|| password.equalsIgnoreCase("9999") || sendUserNoTice.equalsIgnoreCase("9999")
//				|| role.equalsIgnoreCase("9999") || expected.equalsIgnoreCase("9999")
//				|| type.equalsIgnoreCase("9999")) {
//			assertFalse(true);
//		} 
		System.out.println("before fill form");
		try {
			driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys(username);
			
			driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(email);
			
			driver.findElement(By.xpath("//*[@id=\"first_name\"]")).sendKeys(firstname);
			
			driver.findElement(By.xpath("//*[@id=\"last_name\"]")).sendKeys(lastname);
			
			driver.findElement(By.xpath("//*[@id=\"url\"]")).sendKeys(website);
			
			driver.findElement(By.xpath("//*[@id=\"pass1\"]")).clear();
			
			driver.findElement(By.xpath("//*[@id=\"pass1\"]")).sendKeys(password);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("fill error");
		}
		
		if(language.equalsIgnoreCase("0")) {
			driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[6]/td/select/option[3]")).click();
			
		}
		if(role.equalsIgnoreCase("0")) {
			driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[11]/td/select/option[2]")).click();
		}
		if(sendUserNoTice.equalsIgnoreCase("0")) {
			driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/form/table/tbody/tr[10]/td/input")).click();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		driver.findElement(By.xpath("//*[@id=\"createusersub\"]")).click();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String actual = null;
		WebElement actualElement = null; 
		System.out.println("1type->"+type);
		
		if (type.equalsIgnoreCase("1")) {
			System.out.println("true");
			actual = driver.getTitle();
			try {
				actualElement = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[4]/div[2]/p"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			actual = actualElement == null ? "0000" : actualElement.getText().trim();
			assertEquals(actual, expected);
		} else {
			System.out.println("wrong");
			try {
				actualElement = driver.findElement(By.xpath("//*[@id=\"wpbody-content\"]/div[4]/div[2]/p[1]"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			actual = actualElement == null ? "0000" : actualElement.getText().trim();
			assertEquals(actual, expected);
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\haunv\\OneDrive\\Máy tính\\java\\chorme driver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String url = "https://truyenmoinhat.xyz/wp-admin";
		driver.get(url);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id=\"user_pass\"]")).sendKeys("$wordpress1234");
		driver.findElement(By.xpath("//*[@id=\"wp-submit\"]")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driver.navigate().to("https://truyenmoinhat.xyz/wp-admin/user-new.php");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys("afasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfaafasfa");
		driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("asf");
//		driver.findElement(By.xpath("//*[@id=\"createusersub\"]")).submit();
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}
}
