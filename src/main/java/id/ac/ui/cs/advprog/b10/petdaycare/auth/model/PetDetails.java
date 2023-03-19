package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PetDetails {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Pet pet;
    private Integer quantity;
    private Integer totalPrice;
}
