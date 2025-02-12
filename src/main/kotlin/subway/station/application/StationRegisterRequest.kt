package subway.station.application

import subway.common.domain.Name
import subway.common.exception.INVALID_NAME_MESSAGE
import subway.station.domain.Station

data class StationRegisterRequest(val stationName: String) {
    init {
        require(stationName.length >= Name.MIN_LENGTH) { INVALID_NAME_MESSAGE }
    }

    val station get() = Station.valueOf(stationName)
}
