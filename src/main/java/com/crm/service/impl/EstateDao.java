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
                sql.append(" having sw > " + textWorth);
            else if (eWorth.equals("lt"))
                sql.append(" having sw < " + textWorth);
            else if (eWorth.equals("eq"))
                sql.append(" having sw = " + textWorth);
        }

        if (null != textValue && !textValue.equals("")) {
            String sql2 = sql.equals("") ? " having" : " and";
            if (eValue.equals("gt"))
                sql.append(sql2 + " sv > " + textValue);
            else if (eValue.equals("lt"))
                sql.append(sql2 + " sv < " + textValue);
            else if (eValue.equals("eq"))
                sql.append(sql2 + " sv = " + textValue);
        }
        StringBuffer sql3 = new StringBuffer("");
        if (null != choiceradio) {
            if (choiceradio.equals("true"))
                sql3.append(" and e.own = '1'");
            else
                sql3.append(" and (e.own = '0' or e.own is null)");
        }

        if (null != maritalStatus && !maritalStatus.equals(""))
            sql3.append(" and c.marital_status = '" + maritalStatus + "'");


        String completeQuery = "SELECT e.id, c.name, sum(e.worth) as sw, sum(e.value) as sv, e.client_id FROM estate_property e, client c where c.id = e.client_id " + sql3 + " group by e.client_id" + sql;
        System.out.println("completeQuery=== " + completeQuery);
        Query query = entityManager.createNativeQuery(completeQuery);

        return query.getResultList();
    }
}
