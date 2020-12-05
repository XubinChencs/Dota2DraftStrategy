import java.io.*;
import com.alibaba.fastjson.JSONObject;

// convert file content to jsonObject, provide heroes' counter, synergy, strength data.
public class HeroData {
	JSONObject anti = new JSONObject();
	JSONObject comb = new JSONObject();
	JSONObject winRate = new JSONObject();
	
	public HeroData(String antiFile, String combFile, String winFile) {
		anti = JSONObject.parseObject(IOHelper.getInstance().readFromFileToString(new File(antiFile)));
		comb = JSONObject.parseObject(IOHelper.getInstance().readFromFileToString(new File(combFile)));
		winRate = JSONObject.parseObject(IOHelper.getInstance().readFromFileToString(new File(winFile)));
	}
	
	// counterRate interface
	public double getCounterRate(String hero1, String hero2) {
		
		return anti.getJSONObject(hero1).getDoubleValue(hero2) * 100;
	}
	// synergy interface
	public double getSynergyRate(String hero1, String hero2) {
		return comb.getJSONObject(hero1).getDoubleValue(hero2) * 100;
	}
	// strength interface
	public double getStrength(String hero) {
		return winRate.getDoubleValue(hero) * 100 - 50;
	}
	
	public static void main(String[] args) {
		HeroData data = new HeroData("anti.txt", "comb.txt", "winRate.txt");
		System.out.println(data.getCounterRate("antimage", "chaos_knight"));
	}
	
}
