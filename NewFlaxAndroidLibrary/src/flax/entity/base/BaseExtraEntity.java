package flax.entity.base;

import java.util.HashMap;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public abstract class BaseExtraEntity extends BaseEntity{
	@DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE)
	private HashMap<String, String> extraMap;

	public String getExtra(String key) {
		return extraMap != null ? extraMap.get(key) : null;
	}

	public String getExtra(String key, String defValue) {
		String value = getExtra(key);
		return value != null ? value : defValue;
	}

	public String putExtra(String key, String value) {
		if (extraMap == null) {
			extraMap = new HashMap<String, String>();
		}
		return extraMap.put(key, value);
	}

	public void putIntExtra(String key, int value) {
		putExtra(key, String.valueOf(value));
	}

	public int getIntExtra(String key) {
		return Integer.valueOf(getExtra(key, "0"));
	}

	public int getIntExtra(String key, int defValue) {
		return Integer.valueOf(getExtra(key, String.valueOf(defValue)));
	}

	public void clearExtra() {
		extraMap = null;
	}
}
