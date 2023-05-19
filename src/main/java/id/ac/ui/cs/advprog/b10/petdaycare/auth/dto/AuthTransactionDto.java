package id.ac.ui.cs.advprog.b10.petdaycare.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthTransactionDto {
    Integer idCustomer;
    String username;
    String token;
}
