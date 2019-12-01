package br.com.pedrotfs.maestro.domain;

import org.springframework.data.annotation.Id;

public class Register {

    @Id
    private String _id;

    private Integer count;

    private Integer limit;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
