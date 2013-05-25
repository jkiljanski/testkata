package spockogroovy

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/*
Although Spock uses a different terminology, many of its concepts and features are inspired from JUnit.

    Spock                   JUnit
    -----                   -----
    Specification           Test class
    setup()                 @Before
    cleanup()               @After
    setupSpec()             @BeforeClass
    cleanupSpec()           @AfterClass
    Feature                 Test
    Parametrized feature    Theory
    Condition               Assertion
    Exception condition     @Test(expected=...)
    @FailsWith              @Test(expected=...)
    Interaction             Mock expectation (EasyMock, JMock, ...)
*/

class Chapter1SpockBasicsSpecification extends Specification {

    @Shared List aList = []

    def setup() {
        aList.clear()
        println 'setup() runs before every feature method'
    }

    def cleanup() { println 'cleanup() runs after every feature method' }

    def setupSpec() { println 'setupSpec() runs only once before the first feature method' }

    def cleanupSpec() { println 'cleanupSpec() run only once after the last feature method' }

    def 'dodać 2 + 2 równa się 4'() {
        expect:
        2 + 2 == 4
    }

    def 'pusta lista jest pusta'() {
        expect:
        aList.empty
    }

    def 'gdy dodamy coś do listy, to już nie jest pusta'() {
        when:
        aList << 'something'

        then:
        aList.size() == 1
    }

    def 'i ponownie lista pusta'() {
        expect:
        aList.empty
    }

    def 'a na mapie sobie działamy tak'() {
        given: 'mając zadeklarowaną mapę'
        Map aMap

        and: 'oraz drugą mapę z trzema wartościami'
        Map otherMap = [brand: 'Audi', model: 'S5', engine: '4.2L V8']

        expect: 'spodziewamy się, że zadeklarowana mapa będzie nullem'
        aMap == null

        and: 'natomiast mapa z wartościami będzie miała rozmiar równy trzy'
        otherMap.size() == 3

        when: 'kiedy zainicjalizujemy pierwszą mapę'
        aMap = [:]

        then: 'będzie ona pusta'
        aMap.isEmpty()

        when: 'a gdy wstawimy wszystkie wartości z drugiej mapy, do pierwszej'
        aMap.putAll(otherMap)

        then: 'to ta pierwsza mapa również będzie rozmiaru trzy'
        aMap.size() == 3

        and: 'oraz będzie mieć taką samą zawartość jak druga mapa'
        aMap == otherMap

        cleanup: 'zaś na koniec sobie wyczyścimy obie mapy'
        aMap.clear()
        otherMap.clear()
    }

    def 'sprawdź obiekt przy pomocy dodatkowej metody'() {
        given:
        def person = new Person('Stefan', 27)

        expect:
        matchesValidUser(person)
    }

    private void matchesValidUser(Person person) {
        with(person) {
            assert age >= 10
            assert name.charAt(0).isUpperCase()
        }
    }

    def 'jaki leci mi wyjątek?'() {
        when:
        throw new RuntimeException('TADAM!')

        then:
        def ex = thrown(RuntimeException)
        ex.message == 'TADAM!'
    }

    @Unroll('Z nicków "#player1" oraz "#player2" dłuższy ma #expected znaków')
    def 'test zparametryzowany'() {
        given:
        int p1 = player1.size()
        int p2 = player2.size()

        when:
        int max = Math.max(p1, p2)

        then:
        max == expected

        where:
        player1  | player2        | expected
        'Stefan' | 'Jan'          | 6
        'Bolo'   | 'Rozkminiator' | 12
        'Pryt'   | 'Hardkor!'     | 8
    }
}
