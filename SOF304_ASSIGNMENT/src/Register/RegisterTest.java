package Register;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class RegisterTest {
	private WebDriver driver;
	private String url = "https://aa.testapi123.xyz/wp/wp-login.php?action=register";

	@BeforeClass
	public void beforeClass() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\haunv\\OneDrive\\Máy tính\\java\\chorme driver\\chromedriver.exe");
		driver = new ChromeDriver();
	}

	@BeforeMethod
	public void refreshRegisterPage() {
		driver.navigate().to(url);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public void close() {
		driver.close();
	}

	@DataProvider(name = "Provider")
	public Object[][] getDataFromExcel() {
		Object[][] vals = new Object[24][5];
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream("Register.xls"));
			Sheet sheet = wb.getSheetAt(0);

			int i = 0, j = i;
			for (Row row : sheet) {
				for (Cell cell : row) {
					String text = cell.toString().trim();
					vals[i][j++] = text.isEmpty() ? "" : text.replace(".0", "");
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

	@Test(dataProvider = "Provider")
	@Parameters({ "testname", "username", "password", "expected", "type" })
	public void register(@Optional("null") String testname, @Optional("null") String username,
			@Optional("null") String email, @Optional("null") String expected, @Optional("null") String type) {

		driver.findElement(By.xpath("//*[@id=\"user_login\"]")).sendKeys(username);
		driver.findElement(By.xpath("//*[@id=\"user_email\"]")).sendKeys(email);
		driver.findElement(By.xpath("//*[@id=\"wp-submit\"]")).submit();

		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement actualElement = null;
		String actual = null;

		if (type.equalsIgnoreCase("1")) { // for success
			try {
				actualElement = driver.findElement(By.xpath("//*[@id=\"login\"]/p[1]"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			actual = actualElement == null ? "9999" : actualElement.getText().trim();
		} else { // for error
			try {
				actualElement = driver.findElement(By.xpath("//*[@id=\"login_error\"]"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			actual = actualElement == null ? "9999" : actualElement.getText().trim();
		}

		assertEquals(actual, expected);
	}

	public static void main(String[] args) {
		Object[][] vals = new Object[24][5];
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream("Register.xls"));
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
		for (Object[] row : vals) {
			for (Object cell : row) {
				System.out.println(cell + "-");
			}
			System.out.println();
		}
	}
}
