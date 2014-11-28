package org.melodi.reader.larat.internal;

public class InstancesServiceImpl implements InstancesService {

	private String id;
	private String primer;
	private String item;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		id = this.id;
	}

	@Override
	public String getPrimer() {
		return primer;
	}

	@Override
	public void setPrimer(String primer) {
		primer = this.primer;

	}

	@Override
	public String getItem() {
		return item;
	}

	@Override
	public void setItem(String item) {
		item = this.item;
	}

}
