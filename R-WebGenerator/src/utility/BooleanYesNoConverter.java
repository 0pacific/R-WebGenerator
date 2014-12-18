package utility;

public class BooleanYesNoConverter {
	public static String convBoolToYesNo(boolean bool) {
		if(bool) {
			return "YES";
		}

		return "NO";
	}
}
