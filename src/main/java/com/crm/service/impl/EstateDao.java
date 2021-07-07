package com.crm.service.impl;

import com.crm.model.Client;
import com.crm.model.EstateProperty;
import com.crm.service.IEstateDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class EstateDao implements IEstateDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List searchUser(String eWorth, String textWorth, String eValue, String textValue, String choiceradio, String maritalStatus) {

        StringBuffer sql = new StringBuffer("");
        if (null != textWorth && !textWorth.equals("")) {
            if (eWorth.equals("gt"))
                sql.append(" and e.worth >= " + textWorth);
            else if (eWorth.equals("lt"))
                sql.append(" and e.worth <= " + textWorth);
            else if (eWorth.equals("eq"))
                sql.append(" and e.worth = " + textWorth);
        }

        if (null != textValue && !textValue.equals("")) {
            if (eValue.equals("gt"))
                sql.append(" and e.value >= " + textValue);
            else if (eValue.equals("lt"))
                sql.append(" and e.value <= " + textValue);
            else if (eValue.equals("eq"))
                sql.append(" and e.value = " + textValue);
        }

        if (null != choiceradio) {
            if (choiceradio.equals("true"))
                sql.append(" and e.own = '1'");
            else
                sql.append(" and (e.own = '0' or e.own is null)");
        }

        if (null != maritalStatus && !maritalStatus.equals(""))
            sql.append(" and c.marital_status = '" + maritalStatus + "'");


        String completeQuery = "SELECT e.id, c.name, e.worth, e.value, e.client_id FROM estate_property e, client c where c.id = e.client_id" + sql;
        System.out.println("completeQuery=== " + completeQuery);
        Query query = entityManager.createNativeQuery(completeQuery);

        return query.getResultList();
    }
}
