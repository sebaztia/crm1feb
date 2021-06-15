package com.crm.dto;

import com.crm.model.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class CompanyDto {

    private Long id;

    @NotEmpty
    private String name;
    private List<Movie> movies;
    private String address;
    @NotEmpty
    private String telephone;
    @NotEmpty
    @Email(message = "Invalid email ID")
    private String email;
    private String website;
    private Boolean inActive;
    private String isbn;

    private Set<Client> clientSet;

    public CompanyDto(Boolean aTrue, List aList) {
        this.inActive = aTrue;
        this.movies = aList;
    }

    public CompanyDto() { }
}
