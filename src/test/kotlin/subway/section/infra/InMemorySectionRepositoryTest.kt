package subway.section.infra

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import subway.line.domain.Line
import subway.section.domain.Section
import subway.section.domain.SectionRepository
import subway.station.domain.Station

@Suppress("NonAsciiCharacters")
internal class InMemorySectionRepositoryTest {
    private lateinit var sectionRepository: SectionRepository

    @BeforeEach
    internal fun setUp() {
        sectionRepository = InMemorySectionRepository().apply { saveAll(SECTION_FIXTURES) }
    }

    @Test
    internal fun `save() - 구간을 저장`() {
        // given
        val section =
            Section(Line.from("테스트노선2"), Station.valueOf("테스트역1"), Station.valueOf("테스트역3"), 2, 3)

        // when
        sectionRepository.save(section)

        // then
        assertThat(sectionRepository.findAll()).hasSize(SECTION_FIXTURES.size + 1)
    }

    @Test
    internal fun `saveAll() - vararg 타입의 여러 구간들을 저장`() {
        // given
        val section1 =
            Section(Line.from("테스트노선2"), Station.valueOf("테스트역1"), Station.valueOf("테스트역3"), 2, 3)
        val section2 =
            Section(Line.from("테스트노선2"), Station.valueOf("테스트역3"), Station.valueOf("테스트역4"), 2, 3)

        // when
        sectionRepository.saveAll(section1, section2)

        // then
        assertThat(sectionRepository.findAll()).hasSize(SECTION_FIXTURES.size + 2)
    }

    @Test
    internal fun `saveAll() - list 타입의 여러 구간들을 저장`() {
        // given
        val sections = listOf(
            Section(Line.from("테스트노선2"), Station.valueOf("테스트역1"), Station.valueOf("테스트역3"), 2, 3),
            Section(Line.from("테스트노선2"), Station.valueOf("테스트역3"), Station.valueOf("테스트역4"), 2, 3)
        )

        // when
        sectionRepository.saveAll(sections)

        // then
        assertThat(sectionRepository.findAll()).hasSize(SECTION_FIXTURES.size + 2)
    }

    @Test
    internal fun `find() - 해당하는 구간을 조회`() {
        // given
        val section = Section.of("테스트노선1", "테스트역1", "테스트역2", 0, 0)

        // when
        val actual = sectionRepository.find(section)

        // then
        assertThat(actual).isNotNull
    }

    @Test
    internal fun `findByLineAndPreStation() - 해당하는 노선,이전역이 포함된 구간을 조회`() {
        // given
        val line = Line.from("테스트노선1")
        val station = Station.valueOf("테스트역1")

        // when
        val actual = sectionRepository.findByLineAndPreStation(line, station)

        // then
        assertThat(actual).isNotNull
    }

    @Test
    internal fun `findByLineAndStation() - 해당하는 노선,현재역이 포함된 구간을 조회`() {
        // given
        val line = Line.from("테스트노선1")
        val station = Station.valueOf("테스트역3")

        // when
        val actual = sectionRepository.findByLineAndStation(line, station)

        // then
        assertThat(actual).isNotNull
    }

    @Test
    internal fun `findAll() - 모든 구간들을 조회`() {
        // when
        val sections = sectionRepository.findAll()

        // then
        assertThat(sections).hasSize(SECTION_FIXTURES.size)
    }

    @Test
    internal fun `findAllByLine() - 해당하는 노선의 모든 구간들을 조회`() {
        // when
        val sections = sectionRepository.findAllByLine(Line.from("테스트노선1"))

        // then
        assertThat(sections).hasSize(SECTION_FIXTURES.size)
    }

    @Test
    internal fun `countByLine() - 해당하는 노선에 해당하는 구간의 개수를 반환`() {
        // given
        val line = Line.from("테스트노선1")

        // when
        val countByLine = sectionRepository.countByLine(line)

        // then
        assertThat(countByLine).isEqualTo(3)
    }

    @Test
    internal fun `existsByLine() - 해당하는 노선의 구간이 존재하는지 여부`() {
        // given
        val line = Line.from("테스트노선1")

        // when
        val actual = sectionRepository.existsByLine(line)

        // then
        assertThat(actual).isTrue
    }

    @Test
    internal fun `existsByPreStation() - 해당하는 이전역이 포함된 구간이 존재하는지 여부`() {
        // given
        val station = Station.valueOf("테스트역1")

        // when
        val actual = sectionRepository.existsByPreStation(station)

        // then
        assertThat(actual).isTrue
    }

    @Test
    internal fun `existsByStation() - 해당하는 현재역이 포함된 구간이 존재하는지 여부`() {
        // given
        val station = Station.valueOf("테스트역3")

        // when
        val actual = sectionRepository.existsByStation(station)

        // then
        assertThat(actual).isTrue
    }

    @Test
    internal fun `existsByLineAndPreStation() - 해당하는 노선,이전역과 일치하는 구간이 존재하는지 여부`() {
        // given
        val line = Line.from("테스트노선1")
        val preStation = Station.valueOf("테스트역1")

        // when
        val actual = sectionRepository.existsByLineAndPreStation(line, preStation)

        // then
        assertThat(actual).isTrue
    }

    @Test
    internal fun `existsByLineAndStation() - 해당하는 노선,현재역과 일치하는 구간이 존재하는지 여부`() {
        // given
        val line = Line.from("테스트노선1")
        val station = Station.valueOf("테스트역2")

        // when
        val actual = sectionRepository.existsByLineAndStation(line, station)

        // then
        assertThat(actual).isTrue
    }

    @Test
    internal fun `existsByPreStationAndStation() - 해당하는 이전역,현재역과 일치하는 구간이 존재하는지 여부`() {
        // given
        val preStation = Station.valueOf("테스트역1")
        val station = Station.valueOf("테스트역2")

        // when
        val actual = sectionRepository.existsByPreStationAndStation(preStation, station)

        // then
        assertThat(actual).isTrue
    }

    @Test
    internal fun `existsByLineAndPreStationAndStation() - 해당하는 노선,이전역,현재역과 일치하는 구간이 존재하는지 여부`() {
        // given
        val line = Line.from("테스트노선1")
        val preStation = Station.valueOf("테스트역1")
        val station = Station.valueOf("테스트역2")

        // when
        val actual =
            sectionRepository.existsByLineAndPreStationAndStation(line, preStation, station)

        // then
        assertThat(actual).isTrue
    }

    @Test
    internal fun `delete() - 해당하는 구간을 삭제`() {
        // given
        val section =
            Section(Line.from("테스트노선1"), Station.valueOf("테스트역1"), Station.valueOf("테스트역2"), 2, 3)

        // when
        val actual = sectionRepository.delete(section)

        // then
        assertAll(
            { assertThat(actual).isTrue },
            { assertThat(sectionRepository.findAll()).hasSize(SECTION_FIXTURES.size - 1) }
        )
    }

    @Test
    internal fun `deleteByLine() - 해당하는 노선의 구간을 모두 삭제`() {
        // given
        val line = Line.from("테스트노선1")

        // when
        val actual = sectionRepository.deleteByLine(line)

        // then
        assertAll(
            { assertThat(actual).isTrue },
            { assertThat(sectionRepository.findAllByLine(line)).hasSize(0) }
        )
    }

    companion object {
        private val SECTION_FIXTURES = listOf(
            Section.ofUpwardEnd(Line.from("테스트노선1"), Station.valueOf("테스트역1")),
            Section(Line.from("테스트노선1"), Station.valueOf("테스트역1"), Station.valueOf("테스트역2"), 2, 3),
            Section(Line.from("테스트노선1"), Station.valueOf("테스트역2"), Station.valueOf("테스트역3"), 2, 3)
        )
    }
}
