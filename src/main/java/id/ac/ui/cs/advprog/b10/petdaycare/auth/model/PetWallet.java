package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "petwallet")
public class PetWallet {
    @Id
    @GeneratedValue
    private Integer id;
    private int balance;
}
