package flax.entity.base;

import java.util.HashMap;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public abstract class BaseEntity {
	@DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE)
	private HashMap<String, String> extraMap;

	public String getExtra(String key) {
		return extraMap != null ? extraMap.get(key) : null;
	}

	public String putExtra(String key, String value) {
		if (extraMap == null) {
			extraMap = new HashMap<String, String>();
		}
		return extraMap.put(key, value);
	}
}
