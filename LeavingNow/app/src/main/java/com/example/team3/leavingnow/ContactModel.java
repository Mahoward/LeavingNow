package com.example.team3.leavingnow;

import java.util.Comparator;

public class ContactModel implements Comparator<ContactModel> {
    private String name;
    private String pNumber;
    private boolean isSection;

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return pNumber;
    }
    public void setPhone(String phone) {
        this.pNumber = phone;
    }
    public boolean isSection() {
        return isSection;
    }
    public void setSection(boolean isSection) {
        this.isSection = isSection;
    }
    @Override
    public int compare(ContactModel lhs, ContactModel rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }



}
