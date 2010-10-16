package com.guzzservices.easydomain.logic.domain;

public class DomainProvider {
	
	private String keyword ;
	
	private String providerName ;
	
	private String description ;

    private int maxARecords ;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

    public int getMaxARecords() {
        return maxARecords;
    }

    public void setMaxARecords(int maxARecords) {
        this.maxARecords = maxARecords;
    }

    

}
