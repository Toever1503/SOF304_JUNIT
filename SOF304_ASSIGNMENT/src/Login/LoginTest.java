package Login;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xalan.lib.sql.ObjectArray;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;

public class LoginTest {
	private WebDriver driver;

	@BeforeMethod
	public void beforeMethod() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\haunv\\OneDrive\\Máy tính\\java\\chorme driver\\chromedriver.exe");
		driver = new ChromeDriver();
		String url = "https://animenews.life/shiki-admin";
		driver.get(url);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@DataProvider(name = "Provider")
	public Object[][] getDataFromExcel() {
		Object[][] vals = new Object[30][5];
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream("loginData.xls"));
			Sheet sheet = wb.getSheetAt(0);

			int i = 0, j = i;
			for (Row row : sheet) {
				for (Cell cell : row) {
					try {
						String text = cell.toString().trim();
						vals[i][j++] = text.isEmpty() ? "" : text.replace(".0", "");
					} catch (Exception e) {
						// TODO: handle exception
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

	public static void main(String[] args) {
		Object[][] vals = new Object[30][5];
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream("loginData.xls"));
			Sheet sheet = wb.getSheetAt(0);

			int i = 0, j = i;
			for (Row row : sheet) {
				for (Cell cell : row) {
					try {
						String text = cell.toString().trim();
						vals[i][j++] = text.isEmpty() ? "" : text.replace(".0", "");
					} catch (Exception e) {
						// TODO: handle exception
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
		for (Object[] o : vals) {
			for (Object o1 : o) {
				System.out.print(o1 + "-");
			}
			System.out.println();
//			System.out.println(o.length);
		}

	}

	@AfterMethod
	public void afterMethod() {
		driver.close();
	}

	@Test(dataProvider = "Provider")
	@Parameters({ "testname", "username", "password", "expected" })
	public void login(String testname, String username, String password, String expected, String type) {

		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys(username);
		driver.findElement(By.xpath("//*[@id=\"user_pass\"]")).sendKeys(password);
		driver.findElement(By.xpath("//*[@id=\"wp-submit\"]")).click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String actual = null;
		if (type.equalsIgnoreCase("1")) { // for login success
			actual = driver.getTitle().trim();
			assertEquals(actual, expected);
		} else { // for login wrong
			try {
				actual = driver.findElement(By.xpath("//*[@id=\"login_error\"]")).getText().trim();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			assertEquals(actual, expected);
		}
	}

	@Test
	@Parameters({ "testname", "username", "password", "expected", "type" })
	public void loginRemember(@Optional("") String testName, @Optional("") String username,
			@Optional("") String password, @Optional("1") Boolean expected, @Optional("1") int type) {
		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys("aaaaa");
		driver.findElement(By.xpath("//*[@id=\"user_pass\"]")).sendKeys("aaaaa");
		if (type == 1) {
			driver.findElement(By.xpath("//*[@id=\"rememberme\"]")).click();
		}
		driver.findElement(By.xpath("//*[@id=\"wp-submit\"]")).click();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.manage().getCookies().forEach(cookie -> {

			if (cookie.getName().contains("wordpress_logged") == true) {
				Boolean actual = cookie.getExpiry() == null;
				assertEquals(actual, expected);
			}

		});

	}

}