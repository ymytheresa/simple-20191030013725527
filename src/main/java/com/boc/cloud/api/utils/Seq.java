package com.boc.cloud.api.utils;

/**
 * A Sequence class for cloudant db.
 */
public class Seq {
    private String _id;

    private String _rev;

    private String type = "SEQ";

    private String next_val;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNext_val() {
        return next_val;
    }

    public void setNext_val(String next_val) {
        this.next_val = next_val;
    }

}
