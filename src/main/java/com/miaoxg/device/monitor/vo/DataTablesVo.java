package com.miaoxg.device.monitor.vo;

import java.util.List;

public class DataTablesVo<T> {
    private int total;  // 表中总记录数
    private int count;  // 符合条件的记录数
    private List<T> list;   
    
    public DataTablesVo(){
        
    }
    
    public DataTablesVo(int count, List<T> list){
        this.total = count;
        this.setCount(count);
        this.list = list;
    }
    
    public DataTablesVo(int total, int count, List<T> list){
        this.total = total;
        this.setCount(count);
        this.list = list;
    }
    
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
