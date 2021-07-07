package com.crm.service;

import java.util.List;

public interface IEstateDao {

    List<Object[]> searchUser(String eWorth, String textWorth, String eValue, String string, String choiceradio, String maritalStatus);
}
