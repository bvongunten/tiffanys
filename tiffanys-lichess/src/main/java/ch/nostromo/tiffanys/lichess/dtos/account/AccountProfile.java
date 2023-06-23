package ch.nostromo.tiffanys.lichess.dtos.account;

import lombok.Data;

@Data
public class AccountProfile {

    String firstName;
    String lastName;
    String country;
    String location;
    String links;
    String bio;

}
