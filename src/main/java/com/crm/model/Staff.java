package com.crm.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(nullable = false, name = "staff_name")
    private String staffName;

  /*  @Column(nullable = false)
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CallList> callListSet;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

   /* public Set<CallList> getCallList() {
        return callListSet;
    }

    public void setCallList(CallList callList) {
        if (callListSet.size() == 0) {
            callListSet = new HashSet<>();
        }
        callListSet.add(callList);
    }*/
}
