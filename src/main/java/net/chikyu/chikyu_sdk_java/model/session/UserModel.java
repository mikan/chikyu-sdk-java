package net.chikyu.chikyu_sdk_java.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu_sdk_java.model.ApiDataModel;

public class UserModel extends ApiDataModel {
    @JsonProperty(value = "user_id")
    public Integer userId;

    @JsonProperty(value = "email")
    public String email;

    @JsonProperty(value = "first_name")
    public String firstName;

    @JsonProperty(value = "last_name")
    public String lastName;

    @JsonProperty(value = "display_name")
    public String displayName;

    @JsonProperty(value = "organs")
    public OrganModel[] organs;
}
