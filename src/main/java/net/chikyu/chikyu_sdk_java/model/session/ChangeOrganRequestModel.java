package net.chikyu.chikyu_sdk_java.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeOrganRequestModel {
    @JsonProperty(value = "target_organ_id")
    public Integer targetOrganId;
}
