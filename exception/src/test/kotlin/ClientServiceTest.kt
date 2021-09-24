import com.google.gson.Gson
import org.junit.Test
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@Disabled
class ClientServiceTest {

    private val gson = Gson()
    private val clientService = ClientService()

    @Test
    fun `success save client`() {
        val client = getClientFromJson("/success/user.json")
        val result = clientService.saveClient(client)
        assertNotNull(result)
    }

    @Test
    fun `success save client - 00 SNILS control number`() {
        val client = getClientFromJson("/success/user_00_snils.json")
        val result = clientService.saveClient(client)
        assertNotNull(result)
    }

    @Test
    fun `fail save client - validation errors`() {
        val client = getClientFromJson("/fail/user_data_corrupted.json")
        val exception = assertFailsWith<ValidationException>("Ожидаемая ошибка") {
            clientService.saveClient(client)
        }

        assertEquals(6, exception.errorCode.size)
        assertEquals(exception.errorCode[0], ErrorCode.NULL_OR_EMPTY_VALUE)
        assertEquals(exception.errorCode[1], ErrorCode.NAME_INVALID_SIZE)
        assertEquals(exception.errorCode[2], ErrorCode.NAME_INVALID_CHARACTER)
        assertEquals(exception.errorCode[3], ErrorCode.PHONE_INVALID_SIZE)
        assertEquals(exception.errorCode[4], ErrorCode.PHONE_INVALID_CHARACTER)
        assertEquals(exception.errorCode[5], ErrorCode.SNILS_INVALID_CONTROL_NUMBER)
    }

    @ParameterizedTest(name = "given user from file \"{0}\" raises error {1}")
    @MethodSource("badUserPathArguments")
    fun `fail save client - validation error`(
        fileName: String,
        errorCode: ErrorCode
    ) {
        val client = getClientFromJson("/fail/$fileName")
        val exception = assertThrows<ValidationException>("Ожидаемая ошибка") {
            clientService.saveClient(client)
        }

        assertEquals(errorCode, exception.errorCode[0])
    }

    private fun getClientFromJson(fileName: String): Client = this::class.java.getResource(fileName)
        .takeIf { it != null }
        ?.let { gson.fromJson(it.readText(), Client::class.java) }
        ?: throw Exception("Что-то пошло не так))")

    companion object {

        @JvmStatic
        fun badUserPathArguments(): Stream<Arguments> = Stream.of(
            Arguments.of("user_with_bad_email_too_long.json", ErrorCode.EMAIL_INVALID_SIZE),
            Arguments.of("user_with_bad_email_with_digit.json", ErrorCode.EMAIL_INVALID_CHARACTER),
            Arguments.of("user_with_bad_name_too_long.json", ErrorCode.NAME_INVALID_SIZE),
            Arguments.of("user_with_bad_name_with_digit.json", ErrorCode.NAME_INVALID_CHARACTER),
            Arguments.of("user_with_bad_phone_too_long.json", ErrorCode.PHONE_INVALID_SIZE),
            Arguments.of("user_with_bad_phone_too_short.json", ErrorCode.PHONE_INVALID_SIZE),
            Arguments.of("user_with_bad_phone_with_char.json", ErrorCode.PHONE_INVALID_CHARACTER),
            Arguments.of("user_with_bad_phone_wrong_code.json", ErrorCode.PHONE_INVALID_CHARACTER),
            Arguments.of("user_with_bad_snils_too_long.json", ErrorCode.SNILS_INVALID_SIZE),
            Arguments.of("user_with_bad_snils_too_short.json", ErrorCode.SNILS_INVALID_SIZE),
            Arguments.of("user_with_bad_snils_with_char.json", ErrorCode.SNILS_INVALID_CHARACTER),
            Arguments.of("user_with_bad_snils_wrong_check_number.json", ErrorCode.SNILS_INVALID_CONTROL_NUMBER)
        )
    }

}