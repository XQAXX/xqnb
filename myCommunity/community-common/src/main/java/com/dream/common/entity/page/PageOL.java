package com.dream.common.entity.page;

import java.io.Serializable;

public class PageOL implements Serializable {

	private int offsetPara = 0;
	private int limitPara = 10;

	public int getOffsetPara() {
		return this.offsetPara;
	}

	public void setOffsetPara(int offsetPara) {
		this.offsetPara = offsetPara;
	}

	public int getLimitPara() {
		return this.limitPara;
	}

	public void setLimitPara(int limitPara) {
		this.limitPara = limitPara;
	}
}
