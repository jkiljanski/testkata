package spockogroovy

import spock.lang.Specification

class Chapter2EasyMocking extends Specification {

    PersonDao personDaoMock

    def 'mock z groovy closure coercion'() {
        given:
        personDaoMock = {
            def person = new Person()
            person.id = 5 // id is private and has no setter!
            return person
        } as PersonDao

        when:
        def person = personDaoMock.findById(5)

        then:
        noExceptionThrown()
        person.id == 5
    }

    def 'mock z map coercion dla wielu metod'() {
        given:
        personDaoMock = [
                create: { String name, int age ->
                    return 5
                },
                findById: { long id ->
                    def per = new Person()
                    per.id = id
                    return per
                },
                update: { Person per ->
                    per.name = 'updated!'
                }
        ] as PersonDao

        when:
        int id = personDaoMock.create('whatever', 44)

        then:
        id == 5

        when:
        def person = personDaoMock.findById(8)

        then:
        person.id == 8

        when:
        personDaoMock.update(person)

        then:
        person.name == 'updated!'
    }

    def 'mock ze spockiem'() {
        given:
        personDaoMock = Mock(PersonDao)
        personDaoMock.create(*_) >> { String name, int age ->
            return age / 2
        }

        when:
        int resultId = personDaoMock.create('whatever', 50)

        then:
        resultId == 25

        when:
        resultId = personDaoMock.create('whatever', 80)

        then:
        resultId == 40
    }

    def 'mock static method'() {
        given:
        GroovyMock(StringGenerator, global: true)
        StringGenerator.multiplyString(*_) >> { String src, int times ->
            src * times
        }

        when:
        def result = StringGenerator.multiplyString('be', 5)

        then:
        noExceptionThrown()
        result == 'bebebebebe'
    }

}
