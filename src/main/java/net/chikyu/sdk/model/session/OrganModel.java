package net.chikyu.sdk.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.sdk.model.ApiDataModel;

public class OrganModel extends ApiDataModel {
    @JsonProperty(value = "organ_id")
    public Integer organId;

    @JsonProperty(value = "organ_name")
    public String organName;
}
