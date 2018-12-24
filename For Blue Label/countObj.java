import java.net.URL;
import java.util.Scanner;
import org.json.*;

public class countObj {

	public static void main(String[] args) throws Exception {
		String source = "http://173.249.42.182:30120/players.json";
		steamObj[] steamArr = steamObj.parseSteamFromJSON(source);

		steamObj.sort(steamArr, "ID");
		for (steamObj o : steamArr)
			System.out.println(o.toString());

		System.out.println("Total number of players in server: " + steamArr.length);

	}
}

class steamObj {
	int id, ping;
	String name, steam, license;

	public steamObj(int id, String steam, String license, String name, int ping) {
		this.id = id;
		this.steam = steam;
		this.license = license;
		this.name = name;
		this.ping = ping;
	}

	public String toString() {
		return "id:" + this.id + ", name:" + this.name + ", " + this.steam + ", " + this.license + ", ping:"
				+ this.ping;
	}

	public static steamObj[] parseSteamFromJSON(String fileName) throws Exception {
		URL url = new URL(fileName);

		Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();

		JSONArray arr = new JSONArray(str);
		steamObj[] steamArr = new steamObj[arr.length()];
		JSONObject[] objArr = new JSONObject[arr.length()];

		for (int i = 0; i < arr.length(); i++) {
			objArr[i] = arr.getJSONObject(i);
			int id = objArr[i].getInt("id");
			JSONArray identifers = objArr[i].getJSONArray("identifiers");
			String steam = ((JSONArray) identifers).getString(0);
			String license = ((JSONArray) identifers).getString(1);
			String name = objArr[i].getString("name");
			int ping = objArr[i].getInt("ping");
			steamArr[i] = new steamObj(id, steam, license, name, ping);

		}

		return steamArr;
	}

	public static void sort(steamObj[] steamArray, String key) {
		if (key.toLowerCase().equals("id"))
			sortByID(steamArray);
		else if (key.toLowerCase().equals("ping"))
			sortByPing(steamArray);
		else
			System.out.println("Invalid sort key (ID or Ping available)");
	}

	public static void sortByID(steamObj[] steamArray) {
		steamObj temp;
		for (int i = 0; i < steamArray.length - 1; i++)
			for (int j = i + 1; j < steamArray.length; j++)
				if (steamArray[i].id > steamArray[j].id) {
					temp = steamArray[i];
					steamArray[i] = steamArray[j];
					steamArray[j] = temp;
				}
	}

	public static void sortByPing(steamObj[] steamArray) {
		steamObj temp;
		for (int i = 0; i < steamArray.length - 1; i++)
			for (int j = i + 1; j < steamArray.length; j++)
				if (steamArray[i].ping > steamArray[j].ping) {
					temp = steamArray[i];
					steamArray[i] = steamArray[j];
					steamArray[j] = temp;
				}
	}
}
