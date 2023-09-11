package fullcare.backend.profile.dto.response;

import fullcare.backend.profile.domain.Contact;
import lombok.Data;

@Data
public class ProfileContactResponse {
    private Contact contact;
    private boolean myProfile;

    public ProfileContactResponse(Contact contact, boolean myProfile) {
        this.contact = contact;
        this.myProfile = myProfile;
    }
}
