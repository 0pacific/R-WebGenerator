package property;

public class GeneratorProperty {
	// "Japanese" or "English"
	public static final String language = "English";

	public static boolean japanese() {
		return (GeneratorProperty.language.equals("Japanese"));
	}
}
