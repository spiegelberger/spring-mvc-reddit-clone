package com.spiegelberger.springit.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.spiegelberger.springit.domain.validator.PasswordsMatch;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@PasswordsMatch
public class User implements UserDetails {

	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull
	@Size(min=8, max=30)
	@Email
	@Column(nullable = false, unique=true)
	private String email;
	
	@NonNull
	@Column(length=100)
	private String password;
	
	@NonNull
	@Column(nullable=false)
	private boolean enabled;
	
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
	private Set<Role>roles = new HashSet<>();
	
	@NonNull
	@NotEmpty(message="You must enter First Name")
	private String firstName;
	
	@NonNull
	@NotEmpty(message="You must enter Last Name")
	private String lastName;
	
	@Transient
	@Setter(AccessLevel.NONE)
	private String fullName;
	
	@NonNull
	@NotEmpty(message="You must enter alias")
	@Column(nullable=false, unique = true)
	private String alias;
	
	@Transient
	@NotEmpty(message = "Please enter Password Conformation")
	private String confirmPassword;
	
	private String activationCode;
	
	
	public String getFullName() {
		return firstName + " " + lastName;
	}

	public void addRole(Role role) {
		roles.add(role);
	}
	
	public void addRoles(Set<Role>newRoles) {
		newRoles.forEach(this::addRole);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return roles
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
