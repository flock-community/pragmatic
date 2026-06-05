package community.flock.pragmatic.app

import community.flock.pragmatic.app.environment.WithEnvironment
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.resttestclient.getForEntity
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AppTest : WithEnvironment {
    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun contextLoads() {
    }

    @Test
    fun `User Api should return 200`() {
        val result =
            // language=JSON
            """
                |[{"id":"cf8c1fe6-fb9e-436f-883f-cf5ffba90629","firstName":"Default","LastName":"User","birth-date":"2010-01-01"}]
            """.trimMargin()

        testRestTemplate.getForEntity<String>("/api/users").run {
            statusCode.value() shouldBe 200
            body.shouldNotBeNull() shouldBe result
        }
    }
}
