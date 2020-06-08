package rs.ac.uns.ftn.findaroommate.FindARoommateServer.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resource_registry")
public class ResourceRegistry {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer entityId;

    @Column
    private int user;
    @Column
    private String uri;
    @Column
    private boolean profilePicture;
    @Column
    private int addId;

}