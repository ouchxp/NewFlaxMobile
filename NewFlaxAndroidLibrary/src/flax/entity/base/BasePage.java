package flax.entity.base;

import com.j256.ormlite.field.DatabaseField;


public class BasePage extends BaseEntity{
	
	@DatabaseField(canBeNull=true)
	int pageStatus;
	
	public BasePage(){}

	public int getPageStatus() {
		return pageStatus;
	}

	public void setPageStatus(int pageStatus) {
		this.pageStatus = pageStatus;
	}
	
	public void resetPage() {
		pageStatus = 0;
		this.clearExtra();
	}
}
