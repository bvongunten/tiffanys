package ch.nostromo.tiffanys.ui.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import ch.nostromo.tiffanys.ui.TiffanysFxGui;

public class TiffanysConfig {

	public static final String KEY_LANGUAGE = "Language";
	public static final String KEY_LICHESS_USER = "Lichess.User";
	public static final String KEY_LICHESS_APIKEY = "Lichess.ApiKey";
	public static final String KEY_LOG_FILE_LEVEL = "LogFileLevel";

	public static void setStringValue(String key, String value) {
		Preferences prefs = Preferences.userNodeForPackage(TiffanysFxGui.class);
		prefs.put(key, value);
	}

	public static String getStringValue(String key, String defaultValue) {
		Preferences prefs = Preferences.userNodeForPackage(TiffanysFxGui.class);
		return prefs.get(key, defaultValue);
	}

	public static void setTranslatedStringValue(String key, TiffanysConfigTranslation configTranslation) {
		setStringValue(key, configTranslation.getValue());
	}

	public static TiffanysConfigTranslation getTranslatedStringValue(String key, String defaultValue) {
		List<TiffanysConfigTranslation> allItems = getTranslationList(key);
		String value = getStringValue(key, defaultValue);
		
		for (TiffanysConfigTranslation configItem : allItems) {
			if (configItem.getValue().equals(value)) {
				return configItem;
			}
		}
		
		// Non found
		return allItems.get(0);

	}

	public static List<TiffanysConfigTranslation> getTranslationList(String key) {

		if (key.equals(KEY_LANGUAGE)) {
			List<TiffanysConfigTranslation> result = new ArrayList<>();
			result.add(new TiffanysConfigTranslation("de", "German"));
			result.add(new TiffanysConfigTranslation("en", "English"));
			return result;
		}

		throw new IllegalArgumentException("Unknown list: " + key);
	}

}
