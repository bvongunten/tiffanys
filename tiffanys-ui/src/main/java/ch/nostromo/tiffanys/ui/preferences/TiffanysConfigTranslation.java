package ch.nostromo.tiffanys.ui.preferences;

public class TiffanysConfigTranslation {

	private final String value;
	private final String translation;

	public TiffanysConfigTranslation(String value, String translation) {
		this.value = value;
		this.translation = translation;
	}

	public String getValue() {
		return value;
	}

	public String getTranslation() {
		return translation;
	}

	public String toString() {
		return translation;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((translation == null) ? 0 : translation.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TiffanysConfigTranslation other = (TiffanysConfigTranslation) obj;
		if (translation == null) {
			if (other.translation != null)
				return false;
		} else if (!translation.equals(other.translation))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
