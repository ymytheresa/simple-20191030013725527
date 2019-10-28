package com.boc.cloud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base Entity for _id and _rev fields.
 */
public class BaseEntity{
    @JsonIgnore
    private String _id;
    
    @JsonIgnore
    private String _rev;
    
    @JsonIgnore
    private String type;

	public String get_id() {
        return _id;
    }

    public String get_rev() {
        return _rev;
    }

    public String getType() {
		return type;
	}

    public void set_id(String _id) {
        this._id = _id;
    }
    
    public void set_rev(String _rev) {
        this._rev = _rev;
    }

	public void setType(String type) {
		this.type = type;
	}
}
