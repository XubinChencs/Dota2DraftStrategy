import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class IOHelper {
	
	private static class LazyHolder {
		private static final IOHelper INSTANCE = new IOHelper();
	}
	
	public static IOHelper getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	public String readFromFileToString(File file) {
		StringBuilder sb = new StringBuilder();
		try (FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			String curLine = null;
			while ((curLine = br.readLine()) != null) {
				sb.append(curLine);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public List<String> readFromFileToStringList(File file) {
		List<String> result = new ArrayList<String>();
		try (FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			String curLine = null;
			while ((curLine = br.readLine()) != null) {
				result.add(curLine);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
