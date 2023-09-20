package ru.avalc.ordering.application.dto.track;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = TrackOrderQuery.TrackOrderQueryBuilder.class)
@Builder
@Getter
@AllArgsConstructor
public class TrackOrderQuery {

    @JsonProperty
    @NotNull
    private final UUID orderTrackingID;
}
