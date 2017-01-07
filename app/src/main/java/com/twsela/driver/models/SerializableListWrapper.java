package com.twsela.driver.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Shamyyoun on 3/5/2016.
 */
public class SerializableListWrapper<T> implements Serializable {
    private List<T> list;

    public SerializableListWrapper(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
