package subway.station.application

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import subway.line.domain.Line
import subway.section.domain.Section
import subway.section.domain.SectionRepository
import subway.section.infra.InMemorySectionRepository
import subway.station.domain.Station
import subway.station.domain.StationRepository
import subway.station.infra.InMemoryStationRepository

@Suppress("NonAsciiCharacters")
internal class StationServiceTest {
    private lateinit var stationRepository: StationRepository
    private lateinit var sectionRepository: SectionRepository
    private lateinit var stationService: StationService

    @BeforeEach
    internal fun setUp() {
        stationRepository = InMemoryStationRepository()
        sectionRepository = InMemorySectionRepository()
        stationService = StationService(stationRepository, sectionRepository)
    }

    @Test
    internal fun `register() - 해당하는 이름의 역을 생성`() {
        // given
        val request = StationRegisterRequest("테스트역1")

        // when
        stationService.register(request)

        // then
        assertThat(stationRepository.findAll()).hasSize(1)
    }

    @Test
    internal fun `register() - 이미 존재하는 역의 경우 예외 발생`() {
        // given
        val name = "테스트역1"
        stationRepository.save(Station.valueOf(name))

        // then
        assertThatIllegalArgumentException().isThrownBy {
            stationService.register(StationRegisterRequest(name))
        }
    }

    @Test
    internal fun `showAll() - 모든 역을 조회`() {
        // given
        stationRepository.save(Station.valueOf("테스트역1"))
        stationRepository.save(Station.valueOf("테스트역2"))

        // when
        val stations = stationService.showAll()

        // then
        assertThat(stations).hasSize(2)
    }

    @Test
    internal fun `remove() - 해당하는 이름의 역을 삭제`() {
        // given
        val name = "테스트역1"
        stationRepository.save(Station.valueOf(name))

        // when
        val actual = stationService.remove(StationRemoveRequest(name))

        // then
        assertAll(
            { assertThat(actual).isTrue },
            { assertThat(stationRepository.findAll()).hasSize(0) }
        )
    }

    @Test
    internal fun `remove() - 해당하는 역이 존재하지 않는 경우 예외 발생`() {
        // given
        val name = "테스트역1"

        // then
        assertThatIllegalArgumentException().isThrownBy {
            stationService.remove(StationRemoveRequest(name))
        }
    }

    @Test
    internal fun `remove() - 역이 구간에 등록되어 있는 경우 예외 발생`() {
        // given
        val name = "테스트역1"
        val section =
            Section(Line.from("테스트노선1"), Station.valueOf("테스트역1"), Station.valueOf("테스트역2"), 2, 3)
        stationRepository.save(Station.valueOf(name))
        sectionRepository.save(section)

        // then
        assertThatIllegalArgumentException().isThrownBy {
            stationService.remove(StationRemoveRequest(name))
        }
    }
}
