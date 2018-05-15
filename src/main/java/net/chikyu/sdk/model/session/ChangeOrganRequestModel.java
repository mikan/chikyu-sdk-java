package net.chikyu.sdk.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeOrganRequestModel {
    @JsonProperty(value = "target_organ_id")
    public Integer targetOrganId;
}
